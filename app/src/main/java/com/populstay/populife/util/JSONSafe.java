package com.populstay.populife.util;
import com.alibaba.fastjson.JSONObject;

/**
 * fastjson JSONObject 安全取值工具类
 */
public class JSONSafe {

    /**
     * 安全获取 int 值，key 不存在或值为 null 时返回默认值
     */
    public static int getInt(JSONObject json, String key, int defaultValue) {
        if (json == null || !json.containsKey(key)) {
            return defaultValue;
        }
        try {
            // fastjson 的 getIntValue 在值为 null 时返回 0，但 key 不存在会抛异常
            // 所以上面已经判断了 containsKey
            return json.getIntValue(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全获取 long 值，key 不存在或值为 null 时返回默认值
     */
    public static long getLong(JSONObject json, String key, long defaultValue) {
        if (json == null || !json.containsKey(key)) {
            return defaultValue;
        }
        try {
            return json.getLongValue(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全获取 String 值，key 不存在或值为 null 时返回默认值
     */
    public static String getString(JSONObject json, String key, String defaultValue) {
        if (json == null || !json.containsKey(key) || json.get(key) == null) {
            return defaultValue;
        }
        try {
            return json.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全获取 boolean 值，key 不存在或值为 null 时返回默认值
     */
    public static boolean getBoolean(JSONObject json, String key, boolean defaultValue) {
        if (json == null || !json.containsKey(key) || json.get(key) == null) {
            return defaultValue;
        }
        try {
            return json.getBooleanValue(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全获取 Integer 对象，key 不存在或值为 null 时返回 null
     */
    public static Integer getInteger(JSONObject json, String key) {
        if (json == null || !json.containsKey(key) || json.get(key) == null) {
            return null;
        }
        try {
            return json.getInteger(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全获取 JSONObject，key 不存在或值为 null 时返回 null
     */
    public static JSONObject getJSONObject(JSONObject json, String key) {
        if (json == null || !json.containsKey(key) || json.get(key) == null) {
            return null;
        }
        try {
            return json.getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全获取 Object，key 不存在或值为 null 时返回 null
     */
    public static Object get(JSONObject json, String key) {
        if (json == null || !json.containsKey(key)) {
            return null;
        }
        return json.get(key);
    }
}