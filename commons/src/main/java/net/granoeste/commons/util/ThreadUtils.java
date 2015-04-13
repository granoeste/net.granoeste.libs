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

public class ThreadUtils {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    /**
     * get called from any class
     *
     * @return ClassName
     */
    public static String calledFromClassName() {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        String clsName = null;

        // First, search back to a method in the ThreadUtils class.
        int ix = 0;
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            clsName = frame.getClassName();
            if (clsName.equals(ThreadUtils.class.getName())) {
                break;
            }
            ix++;
        }
        // Now search for the first frame before the "ThreadUtils" class.
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            clsName = frame.getClassName();
            if (!clsName.equals(ThreadUtils.class.getName())) {
                break;
            }
            ix++;
        }
        return clsName;
    }

    /**
     * get called from any class
     *
     * @return Class
     */
    public static Class<?> calledFromClass() {
        Class<?> cls = null;
        String clsName = calledFromClassName();
        try {
            if(clsName != null) {
                cls = Class.forName(clsName);
            }
        } catch (ClassNotFoundException e) {
            // NOP
        }
        return cls;
    }
}