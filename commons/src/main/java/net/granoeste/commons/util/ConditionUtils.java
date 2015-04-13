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

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Clean the if statement
 */
public class ConditionUtils {

    /**
     * Check if int is 1.
     *
     * @param i int
     * @return true if 1
     */
    public static boolean is(int i) {
        return i == 1;
    }

    /**
     * Null-safe check if the object is empty.
     * <p/>
     * Null returns true.
     *
     * @param o Object
     * @return true if null
     */
    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * Null-safe check if the object is not empty.
     * <p/>
     * Null returns false.
     *
     * @param o Object
     * @return true if non-null
     */
    public static boolean isNotNull(Object o) {
        return o != null;
    }

    /**
     * Null-safe check if the specified collection is empty.
     * <p/>
     * Null returns true.
     *
     * @param coll the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p/>
     * Null returns false.
     *
     * @param coll the collection to check, may be null
     * @return true if non-null and non-empty
     */
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * Null-safe check if the specified collection is empty.
     * <p/>
     * Null returns true.
     *
     * @param array the array to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p/>
     * Null returns false.
     *
     * @param array the array to check, may be null
     * @return true if non-null and non-empty
     */
    public static boolean isNotEmpty(final Object[] array) {
        return !isEmpty(array);
    }

    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     *
     * <pre>
     * isEmpty(null)      = true
     * isEmpty("")        = true
     * isEmpty(" ")       = false
     * isEmpty("bob")     = false
     * isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().</p>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @return true if empty or null
     */
    public static boolean isEmpty(final CharSequence cs) {
        return StringUtils.isEmpty(cs);
    }

    /**
     * <p>Checks if a CharSequence is not empty ("") and not null.</p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     * @since 3.0 Changed signature from isNotEmpty(String) to isNotEmpty(CharSequence)
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }
}