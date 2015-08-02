package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import com.gmail.at.kevinburnseit.rxtable.RxTableWithAddedRecords.AddRequestListener;

public class AddRecordAction extends EasyTableAction {
	/**
	 * An event generated when the user calls the popup menu for the table and selects the
	 * item which corresponds with adding a record. Query {@link #getRecord()} to determine
	 * which record was used to invoke the popup.
	 * @author Kevin J. Burns
	 *
	 */
	public static class AddRequestedEvent {
		RxTableWithAddedRecords table;
		int record;
		
		/**
		 * Fetches the table where the add was requested.
		 * @return the table in question
		 */
		public RxTableWithAddedRecords getTable() {
			return this.table;
		}
		/**
		 * Fetches the zero-based record (row) number where the adding was requested. 
		 * @return the record number, if the popup was invoked on the table itself. If the
		 * popup was invoked on the scroll pane, this event will still be generated if a
		 * scroll pane has been registered with
		 * {@link RxTableWithAddedRecords#setScrollPane(JScrollPane)} . In that case, the 
		 * record number returned will be -1.
		 */
		public int getRecord() {
			return record;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8858199986350262768L;
	AddRecordAction(RxTableWithAddedRecords table) {
		super(table);
		this.defText = "Add Record";
		this.setText(this.defText);
		this.defaultIcon = new ImageIcon(
				this.getClass().getClassLoader().getResource("res/add-generic.png"));
		this.setIcon(defaultIcon);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		AddRequestedEvent e = new AddRequestedEvent();
		e.table = this.table;
		e.record = this.table.getAffectedRow();
		
		for (AddRequestListener l : this.table.addListeners) {
			l.addRequested(e);
		}
	}
	
	@Override
	protected boolean shouldBeEnabled() {
		return true;
	}
}
