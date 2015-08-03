package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

/**
 * Action which moves a record one slot up in its list.
 * @author Kevin J. Burns
 *
 */
public class MoveRecordUpAction extends EasyTableWidgetAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6504933887886011644L;

	public MoveRecordUpAction(RxTableWithMovableRecordControls<?> widget) {
		super(widget);
		this.defText = "Move Record Up";
		this.setText(this.defText);
		this.defaultIcon = new ImageIcon(
				this.getClass().getClassLoader().getResource("res/move-up.png"));
		this.setIcon(this.defaultIcon);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		int record = this.table.getSelectedRow();
		if (record <= 0) return;
		
		this.widget.getModel().moveElementUp(record);
		this.table.setSelection(record - 1);
	}

	@Override
	protected boolean shouldBeEnabled() {
		return (this.table.getSelectedRow() > 0);
	}
}
