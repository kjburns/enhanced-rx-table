package com.gmail.at.kevinburnseit.rxtable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.gmail.at.kevinburnseit.collections.CollectionReorderable;
import com.gmail.at.kevinburnseit.rxtable.RxTableWithAddedRecords.MenuItemEnableListener;

/**
 * <p>
 * A widget that provides a table within a scroll pane, along with actions to
 * add, remove, and reorder records in the table.
 * </p>
 * <p>
 * The actions are implemented both by push buttons, which are by default below the
 * table, and by popup menu items, which can be retrieved by right-clicking on either
 * the table or its scroll pane (if the table doesn't fully fill the scroll pane).
 * </p>
 * <p>
 * Because the table supports reordering records, the records must themselves be
 * orderable--that is to say, each record must be have a one-to-one relationship with
 * its index in its collection. In order to facilitate record reordering, the table's
 * model is required to also implement {@link CollectionReorderable}. As a convenience,
 * two collection classes are provided which may well work for your needs: 
 * </p>
 * <ul>
 * <li>{@link ArrayListReorderable} is just an ArrayList which implements
 * {@link CollectionReorderable}. </li>
 * <li>{@link ArrayListWithTableModel} provides a TableModel on ArrayListReorderable.
 * That abstract class meets the parameterization requirements of this widget.- </li>
 * </ul>
 * <p>
 * This class provides several constructors which are similar to those of JTable:
 * </p>
 * <ul>
 * <li>{@link #RxTableWithMovableRecordControls()} </li>
 * <li>{@link #RxTableWithMovableRecordControls(TableModelType)} </li>
 * <li>{@link #RxTableWithMovableRecordControls(TableModelType, TableColumnModel)} </li>
 * <li>{@link #RxTableWithMovableRecordControls(TableModelType, TableColumnModel, ListSelectionModel)} </li>
 * </ul>
 * <p>
 * However, due to the parameterization requirements, other JTable-like constructors,
 * such as those that provide a table with a fixed number of rows or columns, cannot be
 * replicated here. In any event, those constructors bypass the model-view-controller
 * paradigm and probably shouldn't be used anyway.
 * </p>
 * <p>
 * Direct manipulation of the enclosed JTable is beyond the scope of this class and should
 * be done directly, with one exception. Changing the model associated with the table
 * must be done using {@link #setTableModel(TableModelType)}. Otherwise, the table can
 * be manipulated by fetching the table using {@link #getTable()} and operating directly
 * on the returned object. If you need access to the table's scroll pane, such as for
 * setting its size, call {@link #getScrollPane()} and operate on it directly.
 * </p>
 * <p>
 * This widget performs or facilitates six actions on the underlying data; these actions
 * are enumerated in {@link TableActionEnum}. Detailed information on these actions is
 * provided in the javadoc for the enum constants.
 * </p>
 * <p>
 * If you want to have additional listeners notified for an action, register or
 * unregister the listeners using
 * {@link #addActionListener(TableActionEnum, ActionListener)} or
 * {@link #removeActionListener(TableActionEnum, ActionListener)}. 
 * </p>
 * <p>
 * This widget provides default text and a default icon for each action. Due to
 * localization concerns, you may want to change the text or icon. To do so, call
 * {@link #setTextForAction(TableActionEnum, String)} or
 * {@link #setIconForAction(TableActionEnum, Icon)}.
 * </p>
 * <p>
 * By default, the action buttons are located below the table. You can change this
 * behavior by calling {@link #setButtonsLocation(String)}.
 * </p>
 * @author Kevin J. Burns 
 *
 * @param <TableModelType> This is a virtual (anded) interface for TableModel and
 * {@link CollectionReorderable}. For convenience, {@link ArrayListWithTableModel} is
 * provided should it meet your needs; otherwise, you must parameterize any instances
 * of this class with a class which extends both TableModel and CollectionReorderable.
 */
public class RxTableWithMovableRecordControls
		<TableModelType extends TableModel & CollectionReorderable>	extends JPanel {
	/**
	 * An enumeration of the available record manipulation actions on a
	 * {@link RxTableWithMovableRecordControls}. Each action either performs or
	 * facilitates an activity; see individual enum constants for more information:
	 * {@link #ADD_RECORD}; {@link #REMOVE_RECORD}; {@link #MOVE_RECORD_TO_TOP};
	 * {@link #MOVE_RECORD_UP}; {@link #MOVE_RECORD_DOWN}; {@link #MOVE_RECORD_TO_BOTTOM}
	 * @author Kevin J. Burns
	 * 
	 */
	public enum TableActionEnum {
		/**
		 * This action <u>facilitates</u> the adding of a record.
		 * When invoked, tells an external listener that the user has called for a
		 * record to be added to the list. To place an add request listener on a table
		 * widget, use the following steps:
		 * <ol>
		 * <li>Create an {@link AddRequestListener}. You will pass this object in step
		 * 3. </li>
		 * <li>Call {@link RxTableWithMovableRecordControls#getTable()}. </li>
		 * <li>On the resultant object, call 
		 * {@link RxTableWithAddedRecords#addAddRecordListener(com.gmail.at.kevinburnseit.rxtable.RxTableWithAddedRecords.AddRequestListener)}
		 * with the listener created in step 1. </li>
		 * <li>When the user requests an add, your listener will be notified. It is your
		 * listener's responsibility to actually add the record. </li>
		 * </ol>
		 */
		ADD_RECORD,
		/**
		 * This action <u>facilitates</u> the deletion of a record.
		 * When invoked, tells an external listener that the user has called for a
		 * record to be deleted from the list. To place a remove request listener on a table
		 * widget, use the following steps:
		 * <ol>
		 * <li>Create an {@link RemoveRequestListener}. You will pass this object in step
		 * 3. </li>
		 * <li>Call {@link RxTableWithMovableRecordControls#getTable()}. </li>
		 * <li>On the resultant object, call 
		 * {@link RxTableWithAddedRecords#addRemoveListener(com.gmail.at.kevinburnseit.rxtable.RxTableWithAddedRecords.RemoveRequestListener)}
		 * with the listener created in step 1. </li>
		 * <li>When the user requests a delete, your listener will be notified. It is your
		 * listener's responsibility to actually delete the record. </li>
		 * </ol>
		 */
		REMOVE_RECORD,
		/**
		 * This action <u>performs</u> a change on the data model. No external code is
		 * necessary to complete the change.
		 * When invoked, the selected record is moved to the top (beginning) of the list.
		 */
		MOVE_RECORD_TO_TOP,
		MOVE_RECORD_UP,
		MOVE_RECORD_DOWN,
		MOVE_RECORD_TO_BOTTOM;
	}
	
	private class ActionTextFixer implements PropertyChangeListener {
		private TableActionEnum act;
		
		public ActionTextFixer(TableActionEnum action) {
			this.act = action;
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (e.getPropertyName().equals(Action.NAME)) {
				actionButtons.get(this.act).setText(null);
			}
			if (e.getPropertyName().equals(Action.SHORT_DESCRIPTION)) {
				actionMenus.get(this.act).setToolTipText(null);
			}
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5984725319045224608L;

	private RxTableWithAddedRecords table;
	private JScrollPane scrollPane;
	private String buttonsLocation = BorderLayout.SOUTH;
	private TableModelType model;
	protected int componentGap = 5;
	private JPanel buttonsPanel;
	
	private HashMap<TableActionEnum, JButton> actionButtons = new HashMap<>();
	private HashMap<TableActionEnum, JMenuItem> actionMenus = new HashMap<>();
	private HashMap<TableActionEnum, EasyTableAction> actions = new HashMap<>();
	private TableModelListener currentModelListener = null;
	
	/**
	 * See constructors for JTable.
	 */
	public RxTableWithMovableRecordControls() {
		super();
		this.table = new RxTableWithAddedRecords();
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithMovableRecordControls(TableModelType dm, TableColumnModel cm,
			ListSelectionModel sm) {
		super();
		this.table = new RxTableWithAddedRecords(dm, cm, sm);
		this.model = dm;
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithMovableRecordControls(TableModelType dm, TableColumnModel cm) {
		super();
		this.table = new RxTableWithAddedRecords(dm, cm);
		this.model = dm;
		this.finishConstructing();
	}

	/**
	 * See constructors for JTable.
	 */
	public RxTableWithMovableRecordControls(TableModelType dm) {
		super();
		this.table = new RxTableWithAddedRecords(dm);
		this.model = dm;
		this.finishConstructing();
	}
	
	private void finishConstructing() {
		BorderLayout layout = new BorderLayout(this.componentGap, this.componentGap);
		this.setLayout(layout);
		
		this.scrollPane = new JScrollPane(this.table);
		this.table.setScrollPane(this.scrollPane);
		
		this.add(this.scrollPane, BorderLayout.CENTER);
		
		this.buttonsPanel = new JPanel();
		FlowLayout btnLayout = new FlowLayout(FlowLayout.LEADING, 0, 0);
		this.buttonsPanel.setLayout(btnLayout);
		
		this.add(this.buttonsPanel, this.buttonsLocation);
		
		this.setupActions();
		
		this.createButtonsAndMenus();
		this.createListeners();
	}
	
	private void createListeners() {
		ListSelectionListener lsl = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent ev) {
				if (ev.getValueIsAdjusting()) return;
				
				enableManipulationButtonsFromTableState();
			}
		};
		
		this.table.getSelectionModel().addListSelectionListener(lsl);
		
		this.currentModelListener = new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				enableManipulationButtonsFromTableState();
			}
		};
		
		if (this.model != null) {
			this.model.addTableModelListener(this.currentModelListener);
		}

		table.registerMenuItemListener(
				this.actionMenus.get(TableActionEnum.MOVE_RECORD_TO_TOP), 
				new MenuItemEnableListener() {
			@Override
			public boolean popupInvoked(RxTableWithAddedRecords table,
					int recordNumber, JMenuItem item) {
				return (recordNumber > 0);
			}
		});
		
		for (TableActionEnum act : TableActionEnum.values()) {
			this.actions.get(act).addPropertyChangeListener(
					this.new ActionTextFixer(act));
		}
	}

	private void createButtonsAndMenus() {
		this.actionMenus.put(TableActionEnum.ADD_RECORD, table.getAddMenu());
		this.actionMenus.put(TableActionEnum.REMOVE_RECORD, table.getRemoveMenu());
		
		JPopupMenu popup = this.table.getPopup();
		popup.addSeparator();
		
		JButton btn;
		JMenuItem menu;

		for (TableActionEnum act : TableActionEnum.values()) {
			if (act.ordinal() == 2) {
				// create rigid area between add/remove buttons and move buttons
				Component rigidArea = Box.createHorizontalStrut(this.componentGap);
				this.buttonsPanel.add(rigidArea);
			}

			EasyTableAction a = this.actions.get(act);
			a.setEnabled(act == TableActionEnum.ADD_RECORD);
			
			btn = new JButton(a);
			btn.setText(null);
			this.actionButtons.put(act, btn);
			this.buttonsPanel.add(btn);
			
			if (act.ordinal() < 2) continue;
			
			menu = new JMenuItem(a);
			menu.setToolTipText(null);
			this.actionMenus.put(act, menu);
			popup.add(menu);
		}		
	}
	
	private void setupActions() {
		this.actions.put(TableActionEnum.ADD_RECORD, this.table.getAddAction());
		this.actions.put(TableActionEnum.REMOVE_RECORD, this.table.getRemoveAction());
		this.actions.put(TableActionEnum.MOVE_RECORD_TO_TOP, 
				new MoveRecordToTopAction(this));
		this.actions.put(TableActionEnum.MOVE_RECORD_UP, new MoveRecordUpAction(this));
		this.actions.put(TableActionEnum.MOVE_RECORD_DOWN, new MoveRecordDownAction(this));
		this.actions.put(TableActionEnum.MOVE_RECORD_TO_BOTTOM, 
				new MoveRecordToBottomAction(this));
	}

	/**
	 * Changes the model associated with this table. This function should be used rather than
	 * calling getTable().setModel(...) because in order for the rearrangement functionality
	 * to work the model must also extend {@link CollectionReorderable}. 
	 * @param model Model to assign to table
	 */
	public void setTableModel(TableModelType model) {
		TableModel oldModel = this.table.getModel();
		if (oldModel != null) {
			if (this.currentModelListener != null) {
				oldModel.removeTableModelListener(this.currentModelListener);
			}
		}
		
		this.model = model;
		this.table.setModel(model);
		
		if (this.currentModelListener != null) {
			this.model.addTableModelListener(this.currentModelListener);
		}
	}

	/**
	 * Fetches the current location of the table manipulation buttons relative to the table.
	 * @return Will return one of the following values:
	 * <ul>
	 * <li>{@link BorderLayout#NORTH} </li>
	 * <li>{@link BorderLayout#SOUTH} </li>
	 * <li>{@link BorderLayout#EAST}</li>
	 * <li>{@link BorderLayout#WEST} </li>
	 * </ul>
	 */
	public String getButtonsLocation() {
		return buttonsLocation;
	}

	/**
	 * Sets the location of the table manipulation buttons relative to the table.
	 * @param loc the proposed location of the buttons. Must be one of the following:
	 * <ul>
	 * <li>{@link BorderLayout#NORTH} </li>
	 * <li>{@link BorderLayout#SOUTH} </li>
	 * <li>{@link BorderLayout#EAST}</li>
	 * <li>{@link BorderLayout#WEST} </li>
	 * </ul>
	 * If <code>loc</code> is <code>null</code>, nothing happens. Additionally, if a
	 * disallowed value is passed, nothing happens.
	 */
	public void setButtonsLocation(String loc) {
		if (loc == null) return;
		
		String[] locs = {BorderLayout.NORTH, BorderLayout.SOUTH, 
				BorderLayout.WEST, BorderLayout.EAST};
		boolean found = false;
		
		for (String l : locs) {
			found = found || l.equals(loc);
		}
		
		if (!found) return;
		
		this.remove(this.buttonsPanel);
		
		this.buttonsLocation = loc;
		this.add(this.buttonsPanel, this.buttonsLocation);
	}

	/**
	 * Fetches the table associated with this widget.
	 * <b>Do not call setModel(...) on the result of this function.</b> Use
	 * {@link #setTableModel(TableModelType)} instead, because the data reordering
	 * functionality will not work properly if the table model does not implement
	 * {@link CollectionReorderable}.
	 * @return
	 */
	public RxTableWithAddedRecords getTable() {
		return table;
	}

	/**
	 * Fetches the scroll pane associated with this widget. Having reference to the
	 * scroll pane can be handy in the event that it is desired to modify its size
	 * constraints. 
	 * @return
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	/**
	 * Changes the text on the table popup menu and the tooltip text on the manipulation
	 * buttons.
	 * @param action Action enum value for the associated button/menu item.
	 * If <code>null</code>, nothing happens.
	 * @param txt The new text. If <code>null</code>, the text is reverted to its
	 * default value.
	 */
	public void setTextForAction(TableActionEnum action, String txt) {
		if (action == null) return;

		this.actions.get(action).setText(txt);
	}
	
	/**
	 * Changes the icon on the table popup menu and on the manipulation
	 * buttons.
	 * @param action Action enum value for the associated button/menu item.
	 * If <code>null</code>, nothing happens.
	 * @param ico The new icon. If <code>null</code>, the icon is reverted to its
	 * default value.
	 */
	public void setIconForAction(TableActionEnum action, Icon ico) {
		if (action == null) return;

		this.actions.get(action).setIcon(ico);
	}
	
	/**
	 * Adds an action listener to both the button and popup menu item associated with
	 * a table manipulation action.
	 * @param action The table manipulation action to trap. If <code>null</code>, the
	 * listener is not registered on any widget.
	 * @param l the listener to register. If <code>null</code>, nothing happens.
	 */
	public void addActionListener(TableActionEnum action, ActionListener l) {
		if (action == null) return;
		if (l == null) return;
		
		this.actionButtons.get(action).addActionListener(l);
		this.actionMenus.get(action).addActionListener(l);
	}
	
	/**
	 * Removes an action listener from both the button and popup menu item associated with
	 * a table manipulation action, if in fact that listener has been previously registered.
	 * @param action The table manipulation action to trap. If <code>null</code>, nothing
	 * happens.
	 * @param l the listener to unregister. If <code>null</code>, nothing happens.
	 */
	public void removeActionListener(TableActionEnum action, ActionListener l) {
		if (action == null) return;
		if (l == null) return;
		
		this.actionButtons.get(action).removeActionListener(l);
		this.actionMenus.get(action).removeActionListener(l);
	}

	private void enableManipulationButtonsFromTableState() {
		for (TableActionEnum action : TableActionEnum.values()) {
			this.actions.get(action).checkEnabled();
		}
//		int record = getSelectedRecord();
//		int recordCount = model.getRowCount();
//		
//		boolean canMoveUp = (record >= 1);
//		TableActionEnum[] upActions = 
//			{TableActionEnum.MOVE_RECORD_UP, TableActionEnum.MOVE_RECORD_TO_TOP};
//		for (TableActionEnum ua : upActions) {
//			actionButtons.get(ua).setEnabled(canMoveUp);
//			actionMenus.get(ua).setEnabled(canMoveUp);
//		}
//		
//		boolean canMoveDown = (record >= 0) && (record != (recordCount - 1));
//		TableActionEnum[] downActions =
//			{TableActionEnum.MOVE_RECORD_DOWN, 
//			 TableActionEnum.MOVE_RECORD_TO_BOTTOM};
//		for (TableActionEnum da : downActions) {
//			actionButtons.get(da).setEnabled(canMoveDown);
//			actionMenus.get(da).setEnabled(canMoveDown);
//		}
	}

	public TableModelType getModel() {
		return model;
	}
}
