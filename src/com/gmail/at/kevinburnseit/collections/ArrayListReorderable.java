package com.gmail.at.kevinburnseit.collections;

import java.util.ArrayList;

/**
 * A subclass of ArrayList which provides for the reordering of records by implementing
 * {@link CollectionReorderable}.
 * @author Kevin J. Burns
 *
 * @param <T> the type of object stored in the list.
 */
public class ArrayListReorderable<T> extends ArrayList<T>
		implements CollectionReorderable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8279575692545888710L;

	@Override
	public int moveElementToTop(int record) {
		T data = this.get(record);
		this.remove(record);
		this.add(0, data);
		
		return 0;
	}
	
	@Override
	public int moveElementUp(int record) {
		T data = this.get(record);
		this.remove(record);
		this.add(record - 1, data);
		
		return record - 1;
	}
	
	@Override
	public int moveElementDown(int record) {
		T data = this.get(record);
		this.remove(record);
		this.add(record + 1, data);
		
		return record + 1;
	}
	
	@Override
	public int moveElementToBottom(int record) {
		T data = this.get(record);
		this.remove(record);
		this.add(data);
		
		return this.size() - 1;
	}
}
