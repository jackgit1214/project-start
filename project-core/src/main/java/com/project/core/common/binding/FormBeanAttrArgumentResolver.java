/**
 *
 */
package com.project.core.common.binding;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.project.core.common.binding.anaotation.FormBean;

/**
 * Resolves method arguments annotated with {@code @ModelAttribute} and handles
 * return values from methods annotated with {@code @ModelAttribute}.
 *
 * <p>
 * Model attributes are obtained from the model or if not found possibly created
 * with a default constructor if it is available. Once created, the attributed
 * is populated with request data via data binding and also validation may be
 * applied if the argument is annotated with {@code @javax.validation.Valid}.
 *
 * <p>
 * When this handler is created with {@code annotationNotRequired=true}, any
 * non-simple type argument and return value is regarded as a model attribute
 * with or without the presence of an {@code @ModelAttribute}.
 *
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class FormBeanAttrArgumentResolver implements
        HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

    protected Log logger = LogFactory.getLog(this.getClass());

    private static final String DEFAULT_SEPARATOR = ".";
    private String separator = DEFAULT_SEPARATOR;

    public FormBeanAttrArgumentResolver() {

    }

    /**
     * @return true if the parameter is annotated with {@link ModelAttribute} or
     *         in default resolution mode also if it is not a simple type.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FormBean.class);
    }

    /**
     * Resolve the argument from the model or if not found instantiate it with
     * its default if it is available. The model attribute is then populated
     * with request values via data binding and optionally validated if
     * {@code @java.validation.Valid} is present on the argument.
     *
     * @throws BindException
     *             if data binding and validation result in an error and the
     *             next method parameter is not of type {@link Errors}.
     * @throws Exception
     *             if WebDataBinder initialization fails.
     */
    @Override
    public final Object resolveArgument(MethodParameter parameter,
                                        ModelAndViewContainer mavContainer, NativeWebRequest request,
                                        WebDataBinderFactory binderFactory) throws Exception {

        String name = ModelFactory.getNameForParameter(parameter);
        String prefix = this.getPrefix(parameter);
        Object attribute = (mavContainer.containsAttribute(name)) ? mavContainer
                .getModel().get(name) : createAttribute(name, parameter,
                binderFactory, request);
        WebDataBinder binder = binderFactory.createBinder(request, attribute, name);

        binder.setFieldDefaultPrefix(prefix + getSeparator());
        if (binder.getTarget() != null) {
            bindRequestParameters(binder, request);
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors()) {
                if (isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
        }
        // 方法内绑定结果bindingResult
        Map<String, Object> bindingResultModel = binder.getBindingResult()
                .getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);
        // mavContainer.addAttribute(binder.getBindingResult());
        return binder.getTarget();
    }

    /**
     * Extension point to create the model attribute if not found in the model.
     * The default implementation uses the default constructor.
     *
     * @param attributeName
     *            the name of the attribute, never {@code null}
     * @param parameter
     *            the method parameter
     * @param binderFactory
     *            for creating WebDataBinder instance
     * @param request
     *            the current request
     * @return the created model attribute, never {@code null}
     */
    protected Object createAttribute(String attributeName,
                                     MethodParameter parameter, WebDataBinderFactory binderFactory,
                                     NativeWebRequest request) throws Exception {
        String value = getRequestValueForAttribute(attributeName, request);
        if (value != null) {
            Object attribute = createAttributeFromRequestValue(value,
                    attributeName, parameter, binderFactory, request);
            if (attribute != null) {
                return attribute;
            }
        }
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    /**
     * Create a model attribute from a String request value (e.g. URI template
     * variable, request parameter) using type conversion.
     * <p>
     * The default implementation converts only if there a registered
     * {@link Converter} that can perform the conversion.
     *
     * @param sourceValue
     *            the source value to create the model attribute from
     * @param attributeName
     *            the name of the attribute, never {@code null}
     * @param parameter
     *            the method parameter
     * @param binderFactory
     *            for creating WebDataBinder instance
     * @param request
     *            the current request
     * @return the created model attribute, or {@code null}
     * @throws Exception
     */
    protected Object createAttributeFromRequestValue(String sourceValue,
                                                     String attributeName, MethodParameter parameter,
                                                     WebDataBinderFactory binderFactory, NativeWebRequest request)
            throws Exception {
        DataBinder binder = binderFactory.createBinder(request, null,
                attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(parameter);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue,
                        parameter.getParameterType(), parameter);
            }
        }
        return null;
    }

    /**
     * Obtain a value from the request that may be used to instantiate the model
     * attribute through type conversion from String to the target type.
     * <p>
     * The default implementation looks for the attribute name to match a URI
     * variable first and then a request parameter.
     *
     * @param attributeName
     *            the model attribute name
     * @param request
     *            the current request
     * @return the request value to try to convert or {@code null}
     */
    protected String getRequestValueForAttribute(String attributeName,
                                                 NativeWebRequest request) {
        Map<String, String> variables = getUriTemplateVariables(request);
        if (StringUtils.hasText(variables.get(attributeName))) {
            return variables.get(attributeName);
        } else if (StringUtils.hasText(request.getParameter(attributeName))) {
            return request.getParameter(attributeName);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, String> getUriTemplateVariables(
            NativeWebRequest request) {
        Map<String, String> variables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                        RequestAttributes.SCOPE_REQUEST);
        return (variables != null) ? variables : Collections
                .<String, String>emptyMap();
    }

    /**
     * Extension point to bind the request to the target object.
     *
     * @param binder
     *            the data binder instance to use for the binding
     * @param request
     *            the current request
     */
    protected void bindRequestParameters(WebDataBinder binder,
                                         NativeWebRequest request) {
        ServletRequest servletRequest = request
                .getNativeRequest(ServletRequest.class);
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
        // ((WebRequestDataBinder) binder).bind(request);
    }

    /**
     * Validate the model attribute if applicable.
     * <p>
     * The default implementation checks for {@code @javax.validation.Valid}.
     *
     * @param binder
     *            the DataBinder to be used
     * @param parameter
     *            the method parameter
     */
    protected void validateIfApplicable(WebDataBinder binder,
                                        MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints
                        : new Object[]{hints});

                break;
            }
        }
    }

    /**
     * Whether to raise a {@link BindException} on validation errors.
     *
     * @param binder
     *            the data binder used to perform data binding
     * @param parameter
     *            the method argument
     * @return {@code true} if the next method argument is not of type
     *         {@link Errors}.
     */
    protected boolean isBindExceptionRequired(WebDataBinder binder,
                                              MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class
                .isAssignableFrom(paramTypes[i + 1]));

        return !hasBindingResult;
    }

    /**
     * Return {@code true} if there is a method-level {@code @ModelAttribute} or
     * if it is a non-simple type when {@code annotationNotRequired=true}.
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(FormBean.class) != null;
    }

    /**
     * Add non-null return values to the {@link ModelAndViewContainer}.
     */
    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {

        if (returnValue != null) {
            String name = ModelFactory.getNameForReturnValue(returnValue,
                    returnType);
            mavContainer.addAttribute(name, returnValue);
        }
    }

    /**
     * Resolves the prefix to use to bind properties from. Will prepend a
     * possible {@link FormBean} if available or return the configured prefix
     * otherwise.
     *
     * @param parameter
     */
    private String getPrefix(MethodParameter parameter) {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation instanceof FormBean) {
                return ((FormBean) annotation).value();
            }
        }
        return null;
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @param separator
     *            the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = null == separator ? DEFAULT_SEPARATOR : separator;
    }

}