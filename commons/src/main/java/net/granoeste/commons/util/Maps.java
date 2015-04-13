/*
 * Copyright 2012 Google Inc.
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Provides static methods for creating mutable {@code Maps} instances easily.
 */
public class Maps {
	/**
	 * Creates a {@code HashMap} instance.
	 *
	 * @return a newly-created, initially-empty {@code HashMap}
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> Builder<K, V> builder() {
		return new Builder<K, V>();
	}

	public static final class Builder<K, V> {
		private HashMap<K, V> map = newHashMap();

		public Builder() {
		}

		public Builder<K, V> put(K key, V value) {
			this.map.put(key, value);
			return this;
		}

		public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
			for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
				this.map.put(entry.getKey(), entry.getValue());
			}
			return this;
		}

		public HashMap<K, V> build() {
			return this.map;
		}
	}
}
