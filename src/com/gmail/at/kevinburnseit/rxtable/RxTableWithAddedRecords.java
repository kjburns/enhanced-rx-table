package com.gmail.at.kevinburnseit.rxtable;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * A further extension of Rob Camick's {@link RXTable}, the source of which is available
 * <a href="http://www.camick.com/java/source/RXTable.java">here.</a> This class includes Mr.
 * Camick's modifications, as well as the following additional features:
 * <ul>
 * <li>Provides a context menu for the user to add a new record or to delete an existing record </li>
 * <li>The context menu provided for these purposes can be fetched for adding more menu items,
 * if desired. </li>
 * <li>Allows the user to register a menu item listener which will be notified with every popup
 * menu invoke event. This listener will determine whether the menu item should be enabled.</li>
 * <li>Allows other code to register a scroll pane which contains this table, so that the popup
 * menu will appear when the user right-clicks outside the table. </li>
 * <li>Adds accessibility support </li>
 * </ul>
 * @author Kevin J. Burns
 * @version 1.0
 *
 */
public class RxTableWithAddedRecords extends RXTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5004232123995633490L;
	
	public class AccessibleRxTableWithAddedRecords extends AccessibleJTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 9120956285899504289L;
	}
	/**
	 * A listener which is notified when the popup menu is invoked.
	 * @author Kevin J. Burns
	 *
	 */
	public static interface MenuItemEnableListener {
		/**
		 * The function called when the popup menu has been invoked. The parameters give the
		 * relevant information for the listener to determine whether the menu item should be
		 * enabled or disabled.
		 * @param table Table upon which the popup menu was invoked
		 * @param recordNumber Record number upon which the popup menu was invoked
		 * @param item The menu item to be enabled or disabled
		 * @return <code>true</code> if the menu item is to be enabled; <code>false</code>
		 * otherwise.
		 */
		boolean popupInvoked(RxTableWithAddedRecords table, int recordNumber, JMenuItem item);
	}
	/**
	 * A listener for record removal requests. The record has not actually been removed; this 
	 * action is left to the listener code. Therefore, veto capability is left to the user.
	 * @author Kevin J. Burns
	 *
	 */
	public static interface RemoveRequestListener {
		void removeRequested(RemoveRecordAction.RemoveRequestedEvent e);
	}
	/**
	 * A listener for record add requests. The record is not actually added; adding a
	 * record is left to the listener code. Therefore, veto capability is left to the user.
	 * @author Kevin J. Burns
	 *
	 */
	public static interface AddRequestListener {
		void addRequested(AddRecordAction.AddRequestedEvent e);
	}
	
	private JPopupMenu popup;
	private JMenuItem addItem;
	private JMenuItem removeItem;
	/**
	 * List which contains remove request listeners.
	 */
	ArrayList<RemoveRequestListener> removeListeners = new ArrayList<>();
	/**
	 * List which contains add request listeners.
	 */
	ArrayList<AddRequestListener> addListeners = new ArrayList<>();
	private HashMap<JMenuItem, MenuItemEnableListener> menuItemMap = new HashMap<>();
	private JScrollPane scrollPane = null;
	private int rowClicked;
	private MouseAdapter scrollPaneMouseListener;
	private AccessibleRxTableWithAddedRecords accessibleContext = 
			new AccessibleRxTableWithAddedRecords();
	private AddRecordAction addAction;
	private RemoveRecordAction removeAction;
	
	/**
	 * Fetches the action which, when executed, requests that a record be added to the
	 * table.
	 * @return
	 */
	AddRecordAction getAddAction() {
		return addAction;
	}

	/**
	 * Fetches the action which, when executed, requests that a record be removed from
	 * the table.
	 * @return
	 */
	RemoveRecordAction getRemoveAction() {
		return removeAction;
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords() {
		super();
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords(int numRows, int numColumns) {
		super(numRows, numColumns);
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords(TableModel dm, TableColumnModel cm,
			ListSelectionModel sm) {
		super(dm, cm, sm);
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords(TableModel dm) {
		super(dm);
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithAddedRecords(Vector<?> rowData, Vector<?> columnNames) {
		super(rowData, columnNames);
		this.finishConstructing();
	}

	private void finishConstructing() {
		this.popup = new JPopupMenu();
		
		this.popup.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
				rowClicked = -1;
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				// nothing to do here
			}
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				// nothing to do here
			}
		});
		
		this.addAction = new AddRecordAction(this);
		this.addAction.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(Action.SHORT_DESCRIPTION)) {
					addItem.setToolTipText(null);
				}
			}
		});
		this.addItem = new JMenuItem(this.addAction);
		this.addItem.setToolTipText(null);
		this.popup.add(this.addItem);
		
		this.removeAction = new RemoveRecordAction(this);
		this.removeAction.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals(Action.SHORT_DESCRIPTION)) {
					removeItem.setToolTipText(null);
				}
			}
		});
		this.removeItem = new JMenuItem(this.removeAction);
		this.removeItem.setToolTipText(null);
		this.popup.add(this.removeItem);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					rowClicked = queryRowByXY(e.getPoint());
					if (rowClicked != -1) {
						setSelection(rowClicked);
					}
					removeAction.checkEnabled();
					notifyExternalMenuItemsOfPopup();
					popup.show(RxTableWithAddedRecords.this, e.getX(), e.getY());
				}
			}
		});
	}
	
	private int queryRowByXY(Point xy) {
		for (int i = 0; i < this.getModel().getRowCount(); i++) {
			for (int j = 0; j < this.getModel().getColumnCount(); j++) {
				if (this.getCellRect(i, j, true).contains(xy)) return i;
			}
		}
		return -1;
	}
	
	/**
	 * Sets the text displayed on the "add record" menu item. This function is useful for
	 * localization.
	 * @param addText the text to be displayed. If <code>null</code>, reverts to the
	 * default text.
	 */
	public void setAddText(String addText) {
		this.addAction.setText(addText);
	}
	
	/**
	 * Sets the icon associated with the "add record" menu item. By default, there is no
	 * icon.
	 * @param icon
	 */
	public void setAddIcon(Icon icon) {
		this.addAction.setIcon(icon);
	}

	/**
	 * Sets the text displayed on the "remove this record" menu item. This function is useful
	 * for localization.
	 * @param removeText the text to be displayed. If <code>null</code>, reverts to the
	 * default text.
	 */
	public void setRemoveText(String removeText) {
		this.removeAction.setText(removeText);
	}
	
	/**
	 * Sets the icon associated with the "remove this record" menu item. By default, there is
	 * no icon.
	 * @param icon
	 */
	public void setRemoveIcon(Icon icon) {
		this.removeAction.setIcon(icon);
	}

	/**
	 * Fetches the popup menu which appears when the user right clicks on the table, or the
	 * scroll pane if one has been registered with {@link #setScrollPane(JScrollPane)}. Other
	 * code can then add other menu items to this popup.
	 * @return
	 */
	public JPopupMenu getPopup() {
		return popup;
	}
	
	/**
	 * Adds a {@link RemoveRequestListener} which is notified of an attempt to remove a record
	 * from the table.
	 * @param l
	 */
	public void addRemoveListener(RemoveRequestListener l) {
		this.removeListeners.add(l);
	}
	
	/**
	 * Removes a previously-added {@link RemoveRequestListener}.
	 * @param l
	 */
	public void removeRemoveListener(RemoveRequestListener l) {
		this.removeListeners.remove(l);
	}
	
	/**
	 * Adds a {@link AddRequestListener} which is notified of an attempt to add a record to
	 * the table.
	 * @param l
	 */
	public void addAddRecordListener(AddRequestListener l) {
		this.addListeners.add(l);
	}
	
	/**
	 * Removes a previously-added {@link AddRequestListener}.
	 * @param l
	 */
	public void removeAddRecordListener(AddRequestListener l) {
		this.addListeners.remove(l);
	}

	/**
	 * Registers a scroll pane, which presumably contains this table, so that if the user
	 * right-clicks within the scroll pane the popup menu will still appear. To unregister
	 * a scroll pane, pass <code>null</code>.
	 * @param scrollPane
	 */
	public void setScrollPane(JScrollPane scrollPane) {
		this.unregisterListenersOnExistingScrollPane();
		this.scrollPane = scrollPane;
		this.registerListenersOnNewScrollPane();
	}

	private void registerListenersOnNewScrollPane() {
		if (this.scrollPane == null) return;
		
		this.scrollPaneMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					rowClicked = -1;
					removeAction.checkEnabled();
					notifyExternalMenuItemsOfPopup();
					popup.show(RxTableWithAddedRecords.this.scrollPane, e.getX(), e.getY());
				}
			}
		};
		
		this.scrollPane.addMouseListener(this.scrollPaneMouseListener);
	}

	private void unregisterListenersOnExistingScrollPane() {
		if (this.scrollPane == null) return;
		
		this.scrollPane.removeMouseListener(this.scrollPaneMouseListener);
	}

	@Override
	public AccessibleContext getAccessibleContext() {
		return this.accessibleContext;
	}
	
	/**
	 * Returns the row that was clicked last time the user invoked the popup menu. If the user
	 * clicked in the table's scroll pane, if one was registered using
	 * {@link #setScrollPane(JScrollPane)}, this function will return -1.
	 * @return
	 */
	public int getRecordInvokingLastPopup() {
		return this.getRowClicked();
	}
	
	/**
	 * Notifies this table that notice is required to an external popup menu item when the
	 * popup is invoked. The listener will determine whether the item should be enabled when
	 * displayed to the user
	 * @param item the menu item in question
	 * @param l the listener to be notified
	 */
	public void registerMenuItemListener(JMenuItem item, MenuItemEnableListener l) {
		this.menuItemMap.put(item, l);
	}

	/**
	 * 
	 */
	private void notifyExternalMenuItemsOfPopup() {
		for (JMenuItem mi : menuItemMap.keySet()) {
			MenuItemEnableListener listener = menuItemMap.get(mi);
			if (listener != null) {
				boolean en = listener.popupInvoked(
						RxTableWithAddedRecords.this, getRowClicked(), mi);
				mi.setEnabled(en);
			}
		}
	}

	/**
	 * Returns the menu item for adding a record.
	 * @return
	 */
	JMenuItem getAddMenu() {
		return this.addItem;
	}

	/**
	 * Returns the menu item for removing a record.
	 * @return
	 */
	JMenuItem getRemoveMenu() {
		return this.removeItem;
	}

	/**
	 * Returns the row which was clicked last time the user invoked the popup menu, if
	 * known. If the right-click occurred somewhere other than a table record (in the
	 * scroll pane, for example), or if the table has intentionally forgotten which row
	 * was clicked last time, -1 is returned.
	 * @return
	 */
	public int getRowClicked() {
		return rowClicked;
	}
	
	/**
	 * Sets the table's selection interval to be equal to the specified record.
	 * @param record The record to be selected, or -1 to select none.
	 */
	void setSelection(int record) {
		if (record < 0) record = -1;
		if (record >= this.getModel().getRowCount()) record = -1;
		
		ListSelectionModel lsm = this.getSelectionModel();
		if (record == -1) {
			lsm.clearSelection();
		}
		else lsm.setSelectionInterval(record, record);
	}

	/**
	 * For a record manipulation action which could be performed on this table, this
	 * function fetches the record number which would be affected by such an action.
	 * First, it looks to see if a popup invocation is remembered; if so, it returns
	 * that record number. Otherwise, it returns the record number which is currently
	 * selected. If there is no record selected, the function returns -1.
	 * @return
	 */
	public int getAffectedRow() {
		if (this.rowClicked != -1) return this.rowClicked;
		return this.selectionModel.getMinSelectionIndex();
	}
}
