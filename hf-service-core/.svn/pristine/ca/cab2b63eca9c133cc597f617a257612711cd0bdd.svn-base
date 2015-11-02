package com.homefellas.dao.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Encapsulates the Query Paramaters to be passed to {@link ContactDao#find}
 * method.
 *
 */
public class QueryParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	private int first;
	private int count;
	private String sort;
	private boolean sortAsc;
	
	private Map<String, Object> restrictions = new HashMap<String, Object>();
	/**
	 * Set to return <tt>count</tt> elements, starting at the <tt>first</tt>
	 * element.
	 *
	 * @param first
	 *            First element to return.
	 * @param count
	 *            Number of elements to return.
	 */
	public QueryParameter(int first, int count) {
		this(first, count, null, true);
	}

	/**
	 * Set to return <tt>count</tt> sorted elements, starting at the
	 * <tt>first</tt> element.
	 *
	 * @param first
	 *            First element to return.
	 * @param count
	 *            Number of elements to return.
	 * @param sort
	 *            Column to sort on.
	 * @param sortAsc
	 *            Sort ascending or descending.
	 */
	public QueryParameter(int first, int count, String sort, boolean sortAsc) {
		this.first = first;
		this.count = count;
		this.sort = sort;
		this.sortAsc = sortAsc;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setSortAsc(boolean sortAsc) {
		this.sortAsc = sortAsc;
	}

	public int getCount() {
		return count;
	}

	public int getFirst() {
		return first;
	}

	public String getSort() {
		return sort;
	}

	public boolean isSortAsc() {
		return sortAsc;
	}

	public boolean hasSort() {
		return sort != null;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public boolean isDescending()
	{
		return !sortAsc;
	}
	
	public void addRestriction(String column, Object restriction)
	{
		restrictions.put(column, restriction);
	}
	
	public int getNumnrtRestrictions()
	{
		return restrictions.size();
	}
	
	public Iterator<String> iterate()
	{
		return restrictions.keySet().iterator();
	}
	
	public Object getRestiction(String column)
	{
		return restrictions.get(column);
	}
}
