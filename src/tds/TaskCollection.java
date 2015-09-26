package tds;

import java.util.List;

import tds.Task.FLAG_TYPE;
import tds.Task.PRIORITY_TYPE;

/**
 * The {@code TaskCollection} class is an interface that provides methods for
 * storing and manipulating {@code Task}.
 * 
 * @see TaskTree
 * @see Task
 * @see TaskAttributeConstants
 * 
 * @author amoshydra
 */
interface TaskCollection<E> {

	/**
	 * Adds the specified element to this collection
	 * 
	 * @param element
	 *            to be added to this collection
	 * @return true if this collection did not already contain the specified
	 *         element
	 */
	public boolean add(E element);

	/**
	 * Removes the specified element to this collection
	 * 
	 * @param element
	 *            to be removed from this collection
	 * @return true if this collection contained the specified element
	 */
	public boolean remove(E element);

	/**
	 * Replace an old element from this collection with a new element. The new
	 * element will be treated as a newly created task.
	 * 
	 * @param oldE
	 *            to be replaced from this collection
	 * @param newE
	 *            to replace the old element
	 * 
	 * @return true if this collection contained the old element and can be
	 *         replaced by the new element
	 */
	public boolean replace(E oldE, E newE);

	/**
	 * Returns a view of the portion of this collection whose elements contain
	 * the {@code searchTerm}. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param str
	 *            the sequence to search for
	 * @return a view of the portion of this collection whose element contain
	 *         the {@code searchTerm}
	 */
	public List<E> searchName(String str);

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from {@code fromStartTime}, inclusive, to {@code toStartTime}, inclusive.
	 * (If {@code fromStartTime} and {@code toStartTime} are equal, the returned
	 * {@code List} contains element that matches {@code fromEndTime}.) The
	 * returned {@code List} is backed by this collection, so changes in the
	 * returned {@code List} are reflected in this collection, and vice-versa.
	 * 
	 * @param fromStartTime
	 *            low endpoint (inclusive) of the returned list
	 * @param toStartTime
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from {@code fromStartTime}, inclusive, to {@code toStartTime},
	 *         inclusive
	 */
	public List<E> queryStartTime(long fromStartTime, long toStartTime);

	/**
	 * Returns a view of the portion of this collection whose elements range
	 * from {@code fromEndTime}, inclusive, to {@code toEnd}Time, inclusive. (If
	 * {@code fromEndTime} and {@code toEndTime} are equal, the returned
	 * {@code List} contains element that matches {@code fromEndTime} only.) The
	 * returned {@code List} is backed by this collection, so changes in the
	 * returned {@code List} are reflected in this collection, and vice-versa.
	 * 
	 * @param fromEndTime
	 *            low endpoint (inclusive) of the returned list
	 * @param toEndTime
	 *            high endpoint (inclusive) of the returned list
	 * @return a view of the portion of this collection whose elements range
	 *         from {@code fromEndTime}, inclusive, to {@code toEndTime},
	 *         inclusive
	 */
	public List<E> queryEndTime(long fromEndTime, long toEndTime);

	/**
	 * Returns a view of the portion of this collection whose elements matches
	 * the {@code flagSearch}. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param flagIndex
	 *            to search for
	 * @return a view of the portion of this collection whose elements match the
	 *         {@code flagSearch}
	 * @see tds.Task
	 * 
	 */
	public List<E> searchFlag(FLAG_TYPE flagIndex);

	/**
	 * Returns a view of the portion of this collection whose elements matches
	 * the {@code prioritySearch}. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param priorityIndex
	 *            to search for
	 * @return a view of the portion of this collection whose elements match the
	 *         {@code prioritySearch}
	 * @see tds.Task
	 * 
	 */
	public List<E> searchPriority(PRIORITY_TYPE priorityIndex);

	/**
	 * Returns a view of this collection whose elements are sorted according to
	 * order it is created. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @return a view of this collection.
	 */
	public List<E> getList();

	/**
	 * Returns a view of this collection whose elements are sorted according to
	 * its specified attribute type. The returned {@code List} is backed by this
	 * collection, so changes in the returned {@code List} are reflected in this
	 * collection, and vice-versa.
	 *
	 * @param taskAttributeType
	 *            the attribute type to be sorted with.
	 * @return a view of this collection whose elements are sorted according to
	 *         its specified attribute type.
	 * @see tds.TaskAttributeConstants
	 */
	public List<E> getSortedList(int taskAttributeType);

	/**
	 * Returns the number of elements in this collection (its cardinality).
	 * 
	 * @return the number of elements in this collection (its cardinality)
	 */
	public int size();
}
