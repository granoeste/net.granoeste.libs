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

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple implementation of {@link net.granoeste.commons.util.MultiValueMap} that wraps a {@link java.util.LinkedHashMap}, storing multiple values in a
 * {@link java.util.LinkedList}.
 * <p>
 * This Map implementation is generally not thread-safe. It is primarily designed for data structures exposed from
 * request objects, for use in a single thread only.
 * 
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 1.0
 */
public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V>,
		Serializable {

	private static final long serialVersionUID = 3801124242820219131L;

	private final Map<K, List<V>> targetMap;

	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link java.util.LinkedHashMap}.
	 */
	public LinkedMultiValueMap() {
		this.targetMap = new LinkedHashMap<K, List<V>>();
	}

	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link java.util.LinkedHashMap} with
	 * the given initial capacity.
	 * 
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public LinkedMultiValueMap(final int initialCapacity) {
		this.targetMap = new LinkedHashMap<K, List<V>>(initialCapacity);
	}

	/**
	 * Copy constructor: Create a new LinkedMultiValueMap with the same mappings
	 * as the specified Map.
	 * 
	 * @param otherMap
	 *            the Map whose mappings are to be placed in this Map
	 */
	public LinkedMultiValueMap(final Map<K, List<V>> otherMap) {
		this.targetMap = new LinkedHashMap<K, List<V>>(otherMap);
	}

	// MultiValueMap implementation

	@Override
	public void add(final K key, final V value) {
		List<V> values = this.targetMap.get(key);
		if (values == null) {
			values = new LinkedList<V>();
			this.targetMap.put(key, values);
		}
		values.add(value);
	}

	@Override
	public V getFirst(final K key) {
		final List<V> values = this.targetMap.get(key);
		return values != null ? values.get(0) : null;
	}

	@Override
	public void set(final K key, final V value) {
		final List<V> values = new LinkedList<V>();
		values.add(value);
		this.targetMap.put(key, values);
	}

	@Override
	public void setAll(final Map<K, V> values) {
		for (final Entry<K, V> entry : values.entrySet()) {
			set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<K, V> toSingleValueMap() {
		final LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(
				this.targetMap.size());
		for (final Entry<K, List<V>> entry : targetMap.entrySet()) {
			singleValueMap.put(entry.getKey(), entry.getValue().get(0));
		}
		return singleValueMap;
	}

	// Map implementation

	@Override
	public int size() {
		return this.targetMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.targetMap.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.targetMap.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return this.targetMap.containsValue(value);
	}

	@Override
	public List<V> get(final Object key) {
		return this.targetMap.get(key);
	}

	@Override
	public List<V> put(final K key, final List<V> value) {
		return this.targetMap.put(key, value);
	}

	@Override
	public List<V> remove(final Object key) {
		return this.targetMap.remove(key);
	}

	@Override
	public void putAll(final Map<? extends K, ? extends List<V>> m) {
		this.targetMap.putAll(m);
	}

	@Override
	public void clear() {
		this.targetMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return this.targetMap.keySet();
	}

	@Override
	public Collection<List<V>> values() {
		return this.targetMap.values();
	}

	@Override
	public Set<Entry<K, List<V>>> entrySet() {
		return this.targetMap.entrySet();
	}

	@Override
	public boolean equals(final Object obj) {
		return this.targetMap.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.targetMap.hashCode();
	}

	@Override
	public String toString() {
		return this.targetMap.toString();
	}

}
