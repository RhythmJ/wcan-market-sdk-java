package com.wcan.market.api.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 断言
 *
 * @author majunjie
 * @date 2019/4/17 10:41
 */
public class Validator {

    public static void isTrue(boolean expression, RuntimeException re) {
        if (!expression) {
            throw re;
        }
    }

    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * @param obj null 或者 ""
     */
    public static void notEmpty(String obj, RuntimeException re) {
        if (StringUtils.isEmpty(obj)) {
            throw re;
        }
    }

    public static void notEmpty(String obj, String msg) {
        if (StringUtils.isEmpty(obj)) {
            throw new RuntimeException(msg);
        }
    }

    public static void notBlank(String obj, String msg) {
        if (StringUtils.isNotBlank(obj)) {
            throw new RuntimeException(msg);
        }
    }

    public static void notNull(Object obj, RuntimeException re) {
        if (obj == null) {
            throw re;
        }
    }

    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new RuntimeException(msg);
        }
    }

    public static void isNull(Object obj, RuntimeException re) {
        if (obj != null) {
            throw re;
        }
    }

    public static void isNull(Object obj, String msg) {
        if (obj != null) {
            throw new RuntimeException(msg);
        }
    }

    public static void isSize(String str, int len, RuntimeException re) {
        if (str == null || str.length() != len) {
            throw re;
        }
    }

    public static void isSize(String str, int len, String msg) {
        if (str == null || str.length() != len) {
            throw new RuntimeException(msg);
        }
    }

    public static void isSize(String str, int min, int max, RuntimeException re) {
        if (str == null || str.length() < min || str.length() > max) {
            throw re;
        }
    }

    public static void isSize(String str, int min, int max, String msg) {
        if (str == null || str.length() < min || str.length() > max) {
            throw new RuntimeException(msg);
        }
    }

}
