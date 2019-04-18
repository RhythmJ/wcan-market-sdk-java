package com.wcan.market.api.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * map工具类
 *
 * @author majunjie
 * @date 2018/2/7 9:41
 * @since 1.0
 */
@Slf4j
public class MapUtil {

    public static final String defaultEncode = "UTF-8";

    /**
     * 格式化成签名原串（按key自然排序，取value进行URLEncode后拼接）
     *
     * @param map
     * @return
     */
    public static String map2SignString(Map map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> paramMap = sortByKey(map);
        StringBuilder content = new StringBuilder();
        for (String key : paramMap.keySet()) {
            if (null != paramMap.get(key)) {
                String value = paramMap.get(key).toString();
                if (StringUtils.isNotBlank(value)) {
                    try {
                        content.append(URLEncoder.encode(value, defaultEncode));
                    } catch (UnsupportedEncodingException e) {
                        log.error("URLEncoder.encode异常", e);
                        return null;
                    }
                }
            }
        }
        return content.toString();
    }


    /**
     * map集合，组装成queryString，value值默认不urlEncode
     *
     * @param map
     * @return
     * @throws Exception
     */
    public static String map2QueryString(Map map) {
        return map2QueryString(map, false);
    }

    /**
     * map集合，组装成queryString
     *
     * @param map
     * @param urlEncode 是否urlEncode
     * @return
     * @throws Exception
     */
    public static String map2QueryString(Map map, boolean urlEncode) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        for (Object key : map.keySet()) {
            Object valueObj = map.get(key.toString());
            if (valueObj == null) {
                continue;
            }
            String value = valueObj.toString();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            content.append(key.toString());
            content.append("=");
            try {
                content.append(urlEncode ? URLEncoder.encode(value, defaultEncode) : value);
            } catch (UnsupportedEncodingException e) {
                log.error("URLEncoder.encode异常", e);
                return null;
            }
            content.append("&");
        }
        content.deleteCharAt(content.length() - 1);
        return content.toString();
    }

    /**
     * 按照key排序(字典排序)
     *
     * @param map
     * @return
     */
    public static Map<String, Object> sortByKey(Map map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<>(Comparator.naturalOrder());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 按照value排序(字典排序)
     *
     * @param oriMap
     * @return
     */
    public static Map<String, String> sortByValue(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new LinkedHashMap<>();
        List<Entry<String, String>> entryList = new ArrayList<>(oriMap.entrySet());
        Collections.sort(entryList, Comparator.comparing(Entry::getValue));

        Iterator<Entry<String, String>> iter = entryList.iterator();
        Entry<String, String> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    /**
     * 快速创建map集合
     *
     * @param names  key的集合，逗号分隔，例：key1,key2,.....,keyn
     * @param values value的数组
     * @return
     */
    public static HashMap<String, String> newHashMap(String names, String... values) {
        if (values == null || values.length == 0) {
            return null;
        }
        String[] nameArr = names.split(",");
        if (nameArr.length != values.length) {
            throw new RuntimeException("map键值length不符");
        }

        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < nameArr.length; i++) {
            map.put(nameArr[i], values[i]);
        }
        return map;
    }

    /**
     * 获取请求参数map集合
     *
     * @param queryString 请求参数
     * @return
     */
    public static Map<String, String> queryString2Map(String queryString) throws Exception {
        if (StringUtils.isBlank(queryString)) {
            return null;
        }

        String[] pairs = queryString.split("&");
        Map<String, String> params = new HashMap<>(pairs.length);
        for (String pair : pairs) {
            String[] pairArr = pair.split("=");
            params.put(pairArr[0], URLDecoder.decode(pairArr.length == 1 ? "" : pairArr[1], defaultEncode));
        }
        return params;
    }

    /**
     * Object to map 将所有字段及字段值封装至MAP  值为NULL 传 “”
     *
     * @param obj
     * @return
     */
    public static Map<String, String> obj2Map(Object obj) {
        Map<String, String> map = new HashMap<>();
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field filed : fields) {
                filed.setAccessible(true);
                String filedName = filed.getName();
                String filedValue = filed.get(obj) == null ? "" : filed.get(obj).toString();
                map.put(filedName, filedValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }

    /**
     * Map 转实体
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {

        Object obj = null;
        try {
            obj = clazz.newInstance();
            Iterator<String> iterator = map.keySet().iterator();

            while (iterator.hasNext()) {
                String key = iterator.next();
                String methodName = "";
                try {
                    Field field = /*clazz.getField(key);*/clazz.getDeclaredField(key);
                    Class type = field.getType();
                    String indexName = key.substring(0, 1).toUpperCase();
                    methodName = "set" + indexName + key.substring(1);
                    Method method = clazz.getMethod(methodName, type);
                    Object valueObj = castType(map.get(key), type);
                    method.invoke(obj, valueObj);
                } catch (Exception e) {
                    log.error("出现异常" + e);
                }
            }
        } catch (Exception e) {
            log.error("出现异常" + e);
        }
        return (T) obj;
    }

    /**
     * 数据类型转换
     *
     * @param obj
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NumberFormatException
     */
    public static Object castType(Object obj, Class<?> clazz) {
        try {
            if (null == obj) {
                return null;
            }

            if (clazz.equals(int.class)) {
                return Integer.parseInt(obj.toString());
            } else if (clazz.equals(float.class)) {
                return Float.parseFloat(obj.toString());
            } else if (clazz.equals(char.class)) {
                return (Character) obj;
            } else if (clazz.equals(short.class)) {
                return Short.valueOf(obj.toString());
            } else if (clazz.equals(byte.class)) {
                return Byte.valueOf(obj.toString());
            } else if (clazz.equals(long.class)) {
                return Long.valueOf(obj.toString());
            } else if (clazz.equals(double.class)) {
                return Double.valueOf(obj.toString());
            } else if (clazz.equals(Integer.class)) {
                return Integer.parseInt(obj.toString());
            }


            Object newObj = clazz.newInstance();
            if (newObj instanceof String) {
                return obj + "";
            } else if (newObj instanceof Boolean) {
                return Boolean.valueOf(obj.toString());
            } else if (newObj instanceof Long) {
                return Long.getLong(obj.toString());
            } else if (newObj instanceof Double) {
                return Double.valueOf(obj.toString());
            } else {
                return obj;
            }
        } catch (Exception e) {
            log.error(clazz + "是基本数据类型无法强转，【" + e.getMessage() + "】");
            return obj;
        }
    }

    /**
     * @描述 Object to map 将所有字段及字段值封装至MAP  KEY转小写 值为NULL 传 “”
     * @return
     */
    public static Map<String,String> obj2MapKeytoLowerCase(Object obj){
        Map<String,String> map = new HashMap<String, String>();
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field filed :fields){
                filed.setAccessible(true);
                String filedName = filed.getName();
                String filedValue = filed.get(obj)==null?"":filed.get(obj).toString();
                map.put(filedName.toLowerCase(), filedValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }
}
