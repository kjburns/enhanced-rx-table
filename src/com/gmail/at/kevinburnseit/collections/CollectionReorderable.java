package com.gmail.at.kevinburnseit.collections;

/**
 * Interface specifying the reordering of elements in a collection. It is recommended that this
 * be used to provide functionality to collections where it is logical to refer to the elements
 * by their index number, as all functions here do exactly that.
 * @author Kevin J. Burns
 *
 */
public interface CollectionReorderable {
	/**
	 * Moves an element to the bottom of the list (end of the collection).
	 * @param record Record number to move
	 * @return The new record index for this record
	 */
	int moveElementToBottom(int record);
	/**
	 * Moves an element one slot closer to the end of the collection.
	 * @param record Record number to move
	 * @return The new record index for this record
	 */
	int moveElementDown(int record);
	/**
	 * Moves an element one slot closer to the beginning of the collection.
	 * @param record Record number to move
	 * @return The new record index for this record
	 */
	int moveElementUp(int record);
	/**
	 * Moves an element to the top of the list (beginning of the collection).
	 * @param record Record number to move
	 * @return The new record index for this record
	 */
	int moveElementToTop(int record);
}
