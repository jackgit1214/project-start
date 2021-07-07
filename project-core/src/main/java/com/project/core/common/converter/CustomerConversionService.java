/**
 *
 */
package com.project.core.common.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

/**
 * 需要处理。。。。。
 * @author lilj
 * @date 2021/6/16 15:10
 * @return
 */

public class CustomerConversionService implements ConversionService {

    /* (non-Javadoc)
     * @see org.springframework.core.convert.ConversionService#canConvert(java.lang.Class, java.lang.Class)
     */
    @Override
    public boolean canConvert(Class<?> _sourceType, Class<?> _targetType) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.springframework.core.convert.ConversionService#canConvert(org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
     */
    @Override
    public boolean canConvert(TypeDescriptor _sourceType,
                              TypeDescriptor _targetType) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.springframework.core.convert.ConversionService#convert(java.lang.Object, java.lang.Class)
     */
    @Override
    public <T> T convert(Object _source, Class<T> _targetType) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.springframework.core.convert.ConversionService#convert(java.lang.Object, org.springframework.core.convert.TypeDescriptor, org.springframework.core.convert.TypeDescriptor)
     */
    @Override
    public Object convert(Object _source, TypeDescriptor _sourceType,
                          TypeDescriptor _targetType) {
        // TODO Auto-generated method stub
        return null;
    }

}
