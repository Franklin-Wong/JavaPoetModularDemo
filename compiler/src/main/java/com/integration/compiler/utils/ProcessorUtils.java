package com.integration.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/8/11 - 21:48
 * @Description com.integration.compiler.utils
 */
public final class ProcessorUtils {
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }



}
