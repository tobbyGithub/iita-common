package org.iita.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.iita.security.model.User;
import org.iita.util.collections.Match;

/**
 * Collection utilities
 * 
 * @author mobreza
 * 
 */
public class Collections {

	/**
	 * Convert an array of objects to a collection
	 * 
	 * @param tt
	 * @return instance of {@link ArrayList}
	 */
	public static Collection<Object> fromArray(Object[] tt) {
		if (tt == null)
			return null;

		Collection<Object> collection = new ArrayList<Object>();
		for (Object t : tt)
			collection.add(t);
		return collection;
	}

	/**
	 * Find an object in collection using comparator
	 * 
	 * @param list
	 * @param toFind
	 * @param comparator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object match(List<?> list, Object toFind, Comparator comparator) {
		for (Object x : list) {
			if (comparator.compare(x, toFind) == 0)
				return x;
		}
		return null;
	}

	/**
	 * @param staff
	 * @param match
	 */
	public <T> List<T> subList(List<T> list, Match<T> match) {
		List<T> newList=new ArrayList<T>();
		for (T element : list) {
			if (match.isMatch(element))
				newList.add(element);
		}
		return newList;
	}

	/**
	 * @param staff
	 * @param match
	 * @return
	 */
	public <T> T findFirst(List<T> list, Match<T> match) {
		for (T element : list) {
			if (match.isMatch(element))
				return element;
		}
		return null;
	}

	public User findFirst(User element, Match<User> match) {
		if (match.isMatch(element))
			return element;
		return null;
	}
}
