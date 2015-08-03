package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

/**
 * Action which move a record to the top of its list.
 * @author Kevin J. Burns
 *
 */
public class MoveRecordToTopAction extends EasyTableWidgetAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6504933887886011644L;

	public MoveRecordToTopAction(RxTableWithMovableRecordControls<?> widget) {
		super(widget);
		this.defText = "Move Record to Top";
		this.setText(this.defText);
		this.defaultIcon = new ImageIcon(
				this.getClass().getClassLoader().getResource("res/move-to-top.png"));
		this.setIcon(this.defaultIcon);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		int record = this.table.getSelectedRow();
		if (record <= 0) return;
		
		this.widget.getModel().moveElementToTop(record);
		this.table.setSelection(0);
	}

	@Override
	protected boolean shouldBeEnabled() {
		return (this.table.getSelectedRow() > 0);
	}
}
