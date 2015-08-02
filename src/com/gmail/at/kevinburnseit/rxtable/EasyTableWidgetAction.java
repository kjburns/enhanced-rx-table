package com.gmail.at.kevinburnseit.rxtable;

/**
 * An abstract base class for table widget actions which move records within the model.
 * @author Kevin J. Burns
 *
 */
public abstract class EasyTableWidgetAction extends EasyTableAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1785722934677697535L;
	protected RxTableWithMovableRecordControls<?> widget;
	
	/**
	 * Only constructor. 
	 * @param widget
	 */
	public EasyTableWidgetAction(RxTableWithMovableRecordControls<?> widget) {
		super(widget.getTable());
		this.widget = widget;
	}
}
