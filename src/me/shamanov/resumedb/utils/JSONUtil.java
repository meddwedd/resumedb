package me.shamanov.resumedb.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Author: Mike
 * Date: 10.04.2019
 */

public class JSONUtil {
    private static final Gson gson = new Gson();

    public static <T> String serialize(T object, Type typeOfSrc) {
        return gson.toJson(object, typeOfSrc);
    }

    public static <T> T deserialize(String src, Type typeOfSrc) {
        return gson.fromJson(src, typeOfSrc);
    }
}
