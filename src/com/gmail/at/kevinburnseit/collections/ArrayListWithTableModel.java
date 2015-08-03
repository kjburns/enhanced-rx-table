package com.gmail.at.kevinburnseit.collections;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * This abstract class extends the capability of an ArrayList by adding record reordering
 * functionality and providing the skeleton implementation of a TableModel. This class
 * fulfills the collection requirements for 
 * {@link com.gmail.at.kevinburnseit.collections.RxTableWithMovableRecordControls}.
 * Derived classes must provide the implementation-specific details for the TableModel.
 * 
 * @author Kevin J. Burns
 *
 * @param <T> The object type stored in the collection.
 */
public abstract class ArrayListWithTableModel<T> extends ArrayListReorderable<T> 
	implements TableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8418021814359679096L;
	private ArrayList<TableModelListener> listeners = new ArrayList<>();

	/**
	 * Constructor. The contents of this collection will be identical to an ArrayList
	 * instantiated with its empty constructor.
	 */
	public ArrayListWithTableModel() {
		super();
	}

	@Override
	public abstract void setValueAt(Object newValue, int row, int column);

	@Override
	public abstract boolean isCellEditable(int row, int column);

	@Override
	public abstract Object getValueAt(int row, int column);

	@Override
	public abstract String getColumnName(int column);

	@Override
	public abstract int getColumnCount();

	@Override
	public abstract Class<?> getColumnClass(int columnNumber);

	@Override
	public void addTableModelListener(TableModelListener l) {
		this.listeners.add(l);
	}

	@Override
	public int getRowCount() {
		return this.size();
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		this.listeners.remove(l);
	}

	@Override
	public void add(int index, T element) {
		super.add(index, element);
		TableModelEvent ev = new TableModelEvent(this, index, this.size() - 1, TableModelEvent.ALL_COLUMNS);
		this.dispatchEvent(ev);
	}

	/**
	 * Notifies all table model listeners of a TableModelEvent.
	 * @param ev The event to dispatch
	 */
	protected final void dispatchEvent(TableModelEvent ev) {
		for (TableModelListener l : this.listeners) {
			l.tableChanged(ev);
		}
	}

	@Override
	public boolean add(T e) {
		boolean ret = super.add(e);
		int newLastRow = this.size() - 1;
		TableModelEvent ev = new TableModelEvent(this, newLastRow, newLastRow, 
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
		this.dispatchEvent(ev);
		return ret;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean ret = super.addAll(c);
		int endRange = this.size() - 1;
		int beginRange = this.size() - c.size();
		this.dispatchEvent(new TableModelEvent(this, beginRange, endRange, 
				TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
		return ret;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean ret = super.addAll(index, c);
		this.dispatchEvent(new TableModelEvent(this, index, this.size() - 1, TableModelEvent.ALL_COLUMNS));
		return ret;
	}

	@Override
	public void clear() {
		super.clear();
		this.dispatchEvent(new TableModelEvent(this));
	}

	@Override
	public T remove(int index) {
		T ret = super.remove(index);
		this.dispatchEvent(new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
		return ret;
	}

	@Override
	public boolean remove(Object arg0) {
		int index = this.indexOf(arg0);
		boolean ret = super.remove(arg0);
		if (ret) {
			this.dispatchEvent(new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
		}
		return ret;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ret = super.removeAll(c);
		this.dispatchEvent(new TableModelEvent(this));
		return ret;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean ret = super.retainAll(c);
		this.dispatchEvent(new TableModelEvent(this));
		return ret;
	}

	@Override
	public T set(int index, T element) {
		T ret = super.set(index, element);
		requestTableUpdate(index);
		return ret;
	}

	/**
	 * This function should be called when a list element is modified externally; that is to
	 * say:
	 * <ol>
	 * <li>An existing item is fetched from the list using {@link #get(int)} </li>
	 * <li>The item is modified by calling functions or setting member variables on that item </li>
	 * <li>The item's changed state needs to be updated in a table </li>
	 * </ol>
	 * Calling this function requests the table to update its view of the item.
	 * @param index Index of the item to be updated
	 */
	public void requestTableUpdate(int index) {
		this.dispatchEvent(new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
	}

	@Override
	protected void removeRange(int begin, int end) {
		super.removeRange(begin, end);
		this.dispatchEvent(new TableModelEvent(this, begin, end, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	}

}