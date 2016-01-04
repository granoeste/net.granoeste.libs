package net.granoeste.commons.util;

import java.util.Collection;
import java.util.Iterator;

public class ArrayUtils {

	/**
	 * Collection to CSV String
	 *
	 * @param collection collection
	 * @return CSV String
	 */
	public static String toCSVString(Collection<?> collection) {
		if (collection.isEmpty()) {
			return "";
		}

		StringBuilder buffer = new StringBuilder(collection.size() * 16);
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {

			Object next = it.next();
			buffer.append(next);

			if (it.hasNext()) {
				buffer.append(",");
			}
		}
		return buffer.toString();
	}
}
