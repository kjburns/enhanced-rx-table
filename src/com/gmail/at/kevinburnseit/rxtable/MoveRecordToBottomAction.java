package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

/**
 * Action which moves a record to the bottom of the list.
 * @author Kevin J. Burns
 *
 */
public class MoveRecordToBottomAction extends EasyTableWidgetAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6504933887886011644L;

	public MoveRecordToBottomAction(RxTableWithMovableRecordControls<?> widget) {
		super(widget);
		this.defText = "Move Record to Bottom";
		this.setText(this.defText);
		this.defaultIcon = new ImageIcon(
				this.getClass().getClassLoader().getResource("res/move-to-bottom.png"));
		this.setIcon(this.defaultIcon);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		int record = this.table.getSelectedRow();
		int count = this.table.getModel().getRowCount();
		if (record >= count - 1) return;
		
		this.widget.getModel().moveElementToBottom(record);
		this.table.setSelection(count - 1);
	}

	@Override
	protected boolean shouldBeEnabled() {
		int record = this.table.getSelectedRow();
		int count = this.table.getModel().getRowCount();

		return (record < count - 1);
	}
}
