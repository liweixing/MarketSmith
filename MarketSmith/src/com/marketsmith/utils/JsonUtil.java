package com.marketsmith.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Class Name:      JsonUtil
 * Description:   TODO
 * @author  Wendy
 *
 */
public class JsonUtil {
    /**
     * json2Map
     * @param json
     * @return
     */
    public static Map<String, Map<String, String[]>> json2Map(String json) {
        if (StringUtil.isTrimBlank(json)) {
            return null;
        }
        Type type = new TypeToken<Map<String, Map<String, String[]>>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * json2Any
     * @param json
     * @return
     */
    public static <T> T json2Any(String json, Type type) {
        if (StringUtil.isTrimBlank(json)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * json2Obj
     */
    public static <T> T json2Obj(String json, Class<T> clazz) {
        if (StringUtil.isTrimBlank(json)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    /**
     * Object to Json
     * 
     * @param object 
     * @return json
     */
    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
