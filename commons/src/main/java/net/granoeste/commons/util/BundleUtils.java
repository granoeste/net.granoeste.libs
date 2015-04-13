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

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import java.io.Serializable;

public class BundleUtils {

    public static BundleBuilder builder() {
        return new BundleBuilder();
    }

    public static class BundleBuilder {

        private Bundle args;

        private BundleBuilder() {
            args = new Bundle();
        }

        public Bundle build() {
            return args;
        }

        public BundleBuilder putAll(Bundle value) {
            args.putAll(value);
            return this;
        }

        public BundleBuilder put(String key, boolean value) {
            args.putBoolean(key, value);
            return this;
        }

        public BundleBuilder put(String key, byte value) {
            args.putByte(key, value);
            return this;
        }

        public BundleBuilder put(String key, char value) {
            args.putChar(key, value);
            return this;
        }

        public BundleBuilder put(String key, double value) {
            args.putDouble(key, value);
            return this;
        }

        public BundleBuilder put(String key, float value) {
            args.putFloat(key, value);
            return this;
        }

        public BundleBuilder put(String key, int value) {
            args.putInt(key, value);
            return this;
        }

        public BundleBuilder put(String key, long value) {
            args.putLong(key, value);
            return this;
        }

        public BundleBuilder put(String key, short value) {
            args.putShort(key, value);
            return this;
        }

        public BundleBuilder put(String key, Bundle value) {
            args.putBundle(key, value);
            return this;
        }


        public BundleBuilder put(String key, CharSequence value) {
            args.putCharSequence(key, value);
            return this;
        }

        public BundleBuilder put(String key, String value) {
            args.putString(key, value);
            return this;
        }


        public BundleBuilder put(String key, Parcelable value) {
            args.putParcelable(key, value);
            return this;
        }

        public BundleBuilder put(String key, Serializable value) {
            args.putSerializable(key, value);
            return this;
        }

        public BundleBuilder put(String key, String[] value) {
            args.putStringArray(key, value);
            return this;
        }

        // TODO: putXxxxArray did not implements.

    }
}
