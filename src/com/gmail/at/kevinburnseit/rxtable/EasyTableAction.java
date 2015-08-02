package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Base class for actions on a {@link RxTableWithAddedRecords}.
 * @author Kevin J. Burns
 *
 */
public abstract class EasyTableAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8399788557224323982L;
	protected RxTableWithAddedRecords table;
	protected String defText;
	protected ImageIcon defaultIcon;

	/**
	 * Constructor. Initializes the object with the table that the action would operate on.
	 * @param table Table that the object would operate on. Cannot be <code>null</code>.
	 */
	public EasyTableAction(RxTableWithAddedRecords table) {
		super();
		this.table = table;
	}

	public abstract void actionPerformed(ActionEvent ev);
	
	final String getText() {
		return (String)this.getValue(Action.NAME);
	}

	final void setText(String txt) {
		String proposed = txt;
		if (proposed == null) proposed = this.getDefaultText();
		
		this.putValue(Action.NAME, proposed);
		this.putValue(Action.SHORT_DESCRIPTION, proposed);
	}
	
	final Icon getIcon() {
		return (Icon)this.getValue(Action.SMALL_ICON);
	}
	
	final void setIcon(Icon ico) {
		if (ico == null) this.putValue(Action.SMALL_ICON, this.getDefaultIcon());
		this.putValue(Action.SMALL_ICON, ico);
	}

	protected abstract boolean shouldBeEnabled();

	final protected int getSelectedRecord() {
		return this.table.getSelectionModel().getMinSelectionIndex();
	}
	
	/**
	 * Calls logic to determine whether the action should be enabled, and based on that
	 * result, enables or disables the action.
	 */
	public final void checkEnabled() {
		this.setEnabled(this.shouldBeEnabled());
	}

	protected final String getDefaultText() {
		return this.defText;
	}

	protected final Icon getDefaultIcon() {
		return this.defaultIcon;
	}
}