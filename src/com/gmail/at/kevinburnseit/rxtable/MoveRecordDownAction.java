package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

/**
 * Action for moving a record down in a table.
 * @author Kevin J. Burns
 *
 */
public class MoveRecordDownAction extends EasyTableWidgetAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6504933887886011644L;

	public MoveRecordDownAction(RxTableWithMovableRecordControls<?> widget) {
		super(widget);
		this.defText = "Move Record Down";
		this.setText(this.defText);
		this.defaultIcon = new ImageIcon(
				this.getClass().getClassLoader().getResource("res/move-down.png"));
		this.setIcon(this.defaultIcon);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		int record = this.table.getSelectedRow();
		int count = this.table.getModel().getRowCount();
		if (record >= count - 1) return;
		
		this.widget.getModel().moveElementDown(record);
		this.table.setSelection(record + 1);
	}

	@Override
	protected boolean shouldBeEnabled() {
		int record = this.table.getSelectedRow();
		if (record == -1) return false;
		
		int count = this.table.getModel().getRowCount();

		return (record < count - 1);
	}
}
