package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import com.gmail.at.kevinburnseit.rxtable.RxTableWithAddedRecords.RemoveRequestListener;

/**
 * Action which, when executed, alerts an external listener that the user has requested that
 * the selected record be deleted.
 * @author Kevin J. Burns
 *
 */
public class RemoveRecordAction extends EasyTableAction {
	/**
	 * An event generated when the user calls the popup menu for the table and selects the
	 * item which corresponds with removing a record. Query {@link #getRecord()} to determine
	 * which record was used to invoke the popup.
	 * @author Kevin J. Burns
	 *
	 */
	public static class RemoveRequestedEvent {
		RxTableWithAddedRecords table;
		int record;
		
		/**
		 * Fetches the table where the removal was requested.
		 * @return the table in question
		 */
		public RxTableWithAddedRecords getTable() {
			return table;
		}
		/**
		 * Fetches the zero-based record (row) number where the removal was requested. 
		 * @return the record number, if the popup was invoked on the table itself. If the
		 * popup was invoked on the scroll pane, this event should never be generated.
		 */
		public int getRecord() {
			return record;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1378791668066727554L;

	RemoveRecordAction(RxTableWithAddedRecords table) {
		super(table);
		this.defText = "Remove Record";
		this.setText(this.defText);
		this.defaultIcon = new ImageIcon(
				this.getClass().getClassLoader().getResource("res/delete-generic.png"));
		this.setIcon(this.defaultIcon);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (this.table.isEditing()) {
			this.table.getCellEditor().stopCellEditing();
		}
		RemoveRecordAction.RemoveRequestedEvent e = 
				new RemoveRecordAction.RemoveRequestedEvent();
		e.table = this.table;
		e.record = this.table.getAffectedRow();
		
		for (RemoveRequestListener l : this.table.removeListeners) {
			l.removeRequested(e);
		}
	}

	protected boolean shouldBeEnabled() {
		return (this.table.getAffectedRow() != -1);
//		int clickRecord = this.table.getRowClicked();
//		int selectRecord = this.getSelectedRecord();
//		
//		return ((clickRecord != -1) || (selectRecord != -1));
	}
}
