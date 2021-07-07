package com.project.core.common.util;

import java.util.UUID;

/**
 * UUID生成类
 *
 * @version 1.0
 */
public class UUIDUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
