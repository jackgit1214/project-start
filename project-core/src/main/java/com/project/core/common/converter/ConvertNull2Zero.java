/**
 *
 */
package com.project.core.common.converter;

import org.springframework.core.convert.converter.Converter;


public class ConvertNull2Zero implements Converter<String, Integer> {

    /* (non-Javadoc)
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public Integer convert(String source) {

        if (source == null || "".equals(source.trim()))
            return 0;

        return Integer.parseInt(source);

    }

}
