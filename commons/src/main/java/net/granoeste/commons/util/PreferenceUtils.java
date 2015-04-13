/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.commons.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * PreferenceUtils
 */
public class PreferenceUtils {

    // ------------------------------------------------------------------------
    // GET
    // ------------------------------------------------------------------------
    /**
     * @param <T>     T
     * @param context context
     * @param key     key
     * @param value   value
     */
    public static <T> void put(Context context, String key, T value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        put(prefs, key, value);
    }

    /**
     * @param <T>   T
     * @param prefs prefs
     * @param key   key
     * @param value value
     */
    public static <T> void put(SharedPreferences prefs, String key, T value) {
        if (value instanceof Boolean) {
            prefs.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Integer) {
            prefs.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Long) {
            prefs.edit().putLong(key, (Long) value).apply();
        } else if (value instanceof Float) {
            prefs.edit().putFloat(key, (Float) value).apply();
        } else {
            prefs.edit().putString(key, (String) value).apply();
        }
    }

    /**
     * @param prefs     prefs
     * @param keyValues Object array of key and values.
     */
    public static void put(SharedPreferences prefs, Object[][] keyValues) {
        SharedPreferences.Editor edit = prefs.edit();
        for (Object[] obj : keyValues) {
            String key = (String) obj[0];
            Object value = obj[1];
            if (value instanceof Boolean) {
                edit.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                edit.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                edit.putFloat(key, (Float) value);
            } else {
                edit.putString(key, (String) value);
            }
        }
        edit.apply();
    }

    /**
     * @param prefs     prefs
     * @param keyValues Object array of KeyValue.
     */
    public static void put(SharedPreferences prefs, KeyValue<String, Object>[] keyValues) {
        SharedPreferences.Editor edit = prefs.edit();
        for (KeyValue<String, Object> kv : keyValues) {
            if (kv.value instanceof Boolean) {
                edit.putBoolean(kv.key, (Boolean) kv.value);
            } else if (kv.value instanceof Integer) {
                edit.putInt(kv.key, (Integer) kv.value);
            } else if (kv.value instanceof Long) {
                edit.putLong(kv.key, (Long) kv.value);
            } else if (kv.value instanceof Float) {
                edit.putFloat(kv.key, (Float) kv.value);
            } else {
                edit.putString(kv.key, (String) kv.value);
            }
            edit.apply();
        }
    }

    // ------------------------------------------------------------------------
    // PUT
    // ------------------------------------------------------------------------
    /**
     * @param <T>      T
     * @param context  context
     * @param key      key
     * @param defValue defValue
     * @return value
     */
    public static <T> T get(Context context, String key, T defValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return get(prefs, key, defValue);
    }

    /**
     * @param <T>      T
     * @param prefs    prefs
     * @param key      key
     * @param defValue defValue
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(SharedPreferences prefs, String key, T defValue) {
        if (defValue instanceof Boolean) {
            return (T) Boolean.valueOf(prefs
                    .getBoolean(key, (Boolean) defValue));
        } else if (defValue instanceof Integer) {
            return (T) Integer.valueOf(prefs.getInt(key, (Integer) defValue));
        } else if (defValue instanceof Long) {
            return (T) Long.valueOf(prefs.getLong(key, (Long) defValue));
        } else if (defValue instanceof Float) {
            return (T) Float.valueOf(prefs.getFloat(key, (Float) defValue));
        }
        return (T) prefs.getString(key, (String) defValue);
    }

    // ------------------------------------------------------------------------
    // REMOVE
    // ------------------------------------------------------------------------
    /**
     * @param context Context
     * @param key key
     */
    public static void remove(Context context, String key) {
        remove(PreferenceManager.getDefaultSharedPreferences(context), key);
    }

    /**
     * @param prefs prefs
     * @param key key
     */
    public static void remove(SharedPreferences prefs, String key) {
        prefs.edit().remove(key).apply();
    }

    // ------------------------------------------------------------------------
    // CLEAR
    // ------------------------------------------------------------------------
    /**
     * @param context Context
     * @param key key
     */
    public static void clear(Context context, String key) {
        clear(PreferenceManager.getDefaultSharedPreferences(context), key);
    }

    /**
     * @param prefs prefs
     * @param key key
     */
    public static void clear(SharedPreferences prefs, String key) {
        prefs.edit().clear().apply();
    }

}
