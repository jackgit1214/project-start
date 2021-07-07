package org.mybatis.generator.myplugins;

import java.util.Properties;
import java.util.regex.Pattern;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author Maxim Kalina
 * @version $Id$
 */
public abstract class BasePluginConfig {

    private static final String excludeClassNamesRegexpKey = "excludeClassNamesRegexp";

    private Pattern excludeClassNamesRegexp;

    protected BasePluginConfig(Properties props) {
        String regexp = props.getProperty(excludeClassNamesRegexpKey, null);
        if (regexp != null)
            this.excludeClassNamesRegexp = Pattern.compile(regexp);
    }

    boolean shouldExclude(FullyQualifiedJavaType type) {
        return this.shouldExclude(type.getFullyQualifiedName());
    }

    boolean shouldExclude(String className) {
        return excludeClassNamesRegexp != null
                && excludeClassNamesRegexp.matcher(className).matches();
    }
}