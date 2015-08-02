package com.gmail.at.kevinburnseit.rxtable;

public abstract class EasyTableWidgetAction extends EasyTableAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1785722934677697535L;
	protected RxTableWithMovableRecordControls<?> widget;
	
	public EasyTableWidgetAction(RxTableWithMovableRecordControls<?> widget) {
		super(widget.getTable());
		this.widget = widget;
	}
}
