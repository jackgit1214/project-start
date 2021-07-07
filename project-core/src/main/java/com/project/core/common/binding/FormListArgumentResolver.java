/**
 *
 */
package com.project.core.common.binding;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import com.project.core.common.binding.anaotation.FormList;
import com.project.core.common.binding.anaotation.MapWapper;

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
public class FormListArgumentResolver implements HandlerMethodArgumentResolver,
        HandlerMethodReturnValueHandler {

    protected Log logger = LogFactory.getLog(this.getClass());

    private static final String DEFAULT_SEPARATOR = ".";
    private String separator = DEFAULT_SEPARATOR;

    public FormListArgumentResolver() {

    }

    /**
     * @return true if the parameter is annotated with {@link ModelAttribute} or
     *         in default resolution mode also if it is not a simple type.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(FormList.class);
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
                                        ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                        WebDataBinderFactory binderFactory) throws Exception {

        String prefix = this.getPrefix(parameter);
        Class<?> paramType = parameter.getParameterType();

        WebDataBinder binder = new WebDataBinder(null);
        Object bindObject = null;
        List<BindingResult> bindingResults = new ArrayList<BindingResult>();
        if (Collection.class.isAssignableFrom(paramType) || paramType.isArray()) {
            Class<?> genericClass = this.getGenericClass(parameter, 0);
            if (genericClass == null)
                return null;
            Map<String, Object> mappedValues = createMappedValues(genericClass,
                    webRequest, parameter, prefix, binderFactory,
                    bindingResults);
            if (!mappedValues.isEmpty()) {
                List<Object> targetObject = new ArrayList<Object>(
                        mappedValues.values());
                bindObject = binder.convertIfNecessary(targetObject, paramType);
            }
        } else if (MapWapper.class.isAssignableFrom(paramType)) {
            Class<?> genericClass = this.getGenericClass(parameter, 1);
            if (genericClass == null)
                return null;
            Map<String, Object> mappedValues = createMappedValues(genericClass,
                    webRequest, parameter, prefix, binderFactory,
                    bindingResults);
            if (!mappedValues.isEmpty()) {
                Map<String, Object> targetObject = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : mappedValues.entrySet()) {
                    String key = entry.getKey();
                    key = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                    targetObject.put(key, entry.getValue());
                }
                MapWapper<String, Object> mw = new MapWapper<String, Object>();
                mw.setInnerMap(targetObject);
                bindObject = binder.convertIfNecessary(mw, paramType);
            }

        }
        Map<String, Object> bindingResultModel = new LinkedHashMap<String, Object>(
                2);
        BindingResult bindingResultTotal = new BeanPropertyBindingResult(
                bindObject, prefix);
        if (bindingResults.size() > 0) {
            for (BindingResult bindingResult : bindingResults) {
                if (bindingResult.hasErrors())
                    throw new BindException(bindingResult);
            }
        }

        bindingResultModel.put(prefix, bindObject);
        bindingResultModel.put(BindingResult.MODEL_KEY_PREFIX + prefix,
                bindingResultTotal);

        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);
        return bindObject;
    }

    private Class<?> getGenericClass(MethodParameter parameter, int paramOrder) {
        Class<?> genericClass = null;
        Class<?> paramType = parameter.getParameterType();
        if (paramType.isArray()) {
            genericClass = paramType.getComponentType();
        } else {
            Type type = parameter.getGenericParameterType();
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                genericClass = (Class<?>) pt.getActualTypeArguments()[paramOrder];
            }
        }
        return genericClass;
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
     * Return {@code true} if there is a method-level {@code @ModelAttribute} or
     * if it is a non-simple type when {@code annotationNotRequired=true}.
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(FormList.class) != null;
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
     * possible {@link FormList} if available or return the configured prefix
     * otherwise.
     *
     * @param parameter
     */
    private String getPrefix(MethodParameter parameter) {
        for (Annotation annotation : parameter.getParameterAnnotations()) {
            if (annotation instanceof FormList) {
                return ((FormList) annotation).value();
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

    /*
     * 获取指定前缀的KEY值
     */
    private Set<String> getSortedKeySet(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(prefix, "Prefix must not be null");
        Enumeration<String> paramNames = request.getParameterNames();
        Set<String> keySet = new TreeSet<String>(ComparatorImpl.INSTANCE);
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith(prefix)) {
                String key = paramName;
                int lastScopeIndex = paramName.indexOf(']');
                int firstSeparator = paramName.indexOf(separator);
                if (firstSeparator > lastScopeIndex) {
                    // 这里把separator也加上，用来判断是简单数据类型还是复杂类型
                    key = paramName.substring(0, firstSeparator + 1);
                }
                keySet.add(key);
            }
        }
        return keySet;
    }

    private static final class ComparatorImpl implements Comparator<String> {
        public static final ComparatorImpl INSTANCE = new ComparatorImpl();

        @Override
        public int compare(String left, String right) {
            int lengthCompare = left.length() - right.length();
            return lengthCompare != 0 ? lengthCompare : left.compareTo(right);
        }
    }

    /*
     * 如果是Domain Class，则根据是否有ID属性来自动查询实体数据，再行绑定
     */
    private Object convertIfDomainClass(WebRequest webRequest,
                                        PropertyValues pvs, Class<?> paramType, String prefix) {
        // 如果参数是Domain Class，则看看是否有ID，有就根据ID读取数据
        if (Persistable.class.isAssignableFrom(paramType)) {
            PropertyValue idValue = pvs.getPropertyValue("id");
            if (null != idValue) {
                String idString = (String) idValue.getValue();
                if (!StringUtils.isEmpty(idString)) {
                    WebDataBinder binder = new WebDataBinder(null, prefix
                            + separator + "id");
                    return binder.convertIfNecessary(idString, paramType);
                }
            }
        }
        return null;
    }

    /*
     * 生成指定前缀的数据Map
     */
    private Map<String, Object> createMappedValues(Class<?> genericClass,
                                                   NativeWebRequest webRequest, MethodParameter parameter,
                                                   String prefix, WebDataBinderFactory binderFactory,
                                                   List<BindingResult> bindingResult) throws Exception {
        ServletRequest servletRequest = (ServletRequest) webRequest
                .getNativeRequest();
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        // 将数组提取为一个一个的KEY，这里是集合必须要有prefix + '['
        Set<String> keySet = getSortedKeySet(servletRequest, prefix + '[');

        for (String key : keySet) {
            Object genericObj = null;
            if (key.endsWith(separator)) {
                ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(
                        servletRequest, key, "");

                String realKey = key.substring(0, key.length() - 1);
                genericObj = convertIfDomainClass(webRequest, pvs,
                        genericClass, realKey);
                if (null == genericObj) {
                    genericObj = BeanUtils.instantiateClass(genericClass);
                }
                WebDataBinder objectBinder = binderFactory.createBinder(
                        webRequest, genericObj, realKey);
                objectBinder.bind(pvs);
                bindingResult.add(objectBinder.getBindingResult());
                this.validateIfApplicable(objectBinder, parameter);
            } else {
                WebDataBinder binderHelper = new WebDataBinder(null);
                Map<String, Object> paramValues = WebUtils
                        .getParametersStartingWith(servletRequest, key);
                if (!paramValues.isEmpty()) {
                    if (Collection.class.isAssignableFrom(genericClass)) {
                        genericObj = binderHelper.convertIfNecessary(
                                paramValues.values(), genericClass);
                    } else {
                        genericObj = binderHelper.convertIfNecessary(
                                paramValues.values().iterator().next(),
                                genericClass);
                    }
                    bindingResult.add(binderHelper.getBindingResult());
                }
            }
            if (genericObj != null) {
                resultMap.put(key, genericObj);
            }
        }
        return resultMap;
    }

}