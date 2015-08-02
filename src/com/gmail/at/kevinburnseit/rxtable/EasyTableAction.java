package com.gmail.at.kevinburnseit.rxtable;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

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
	 * Provides a way for external agents to provide feedback to a
	 * {@link RxTableWithMovableRecordControls} regarding whether an action should be
	 * enabled. The function {@link #shouldBeEnabled()} is checked, and all enabled checks
	 * for a particular action must return true in order for the action to be enabled.
	 * @author Kevin J. Burns
	 *
	 */
	public abstract class SupplementalEnabledCheck {
		/**
		 * The external agent should return <code>true</code> if, in its opinion, the
		 * affected action should be enabled, and <code>false</code> otherwise.
		 * @return
		 */
		public abstract boolean shouldBeEnabled();
		/**
		 * Fetches the table the action would be performed on.
		 * @return
		 */
		public final RxTableWithAddedRecords getTable() {
			return table;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8399788557224323982L;
	/**
	 * The table the action is performed on.
	 */
	protected RxTableWithAddedRecords table;
	/**
	 * The default text associated with this action.
	 */
	protected String defText;
	/**
	 * The default icon associated with this action.
	 */
	protected ImageIcon defaultIcon;
	private ArrayList<SupplementalEnabledCheck> supplementalEnabledChecks =
			new ArrayList<>();

	/**
	 * Constructor. Initializes the object with the table that the action would operate on.
	 * @param table Table that the object would operate on. Cannot be <code>null</code>.
	 */
	public EasyTableAction(RxTableWithAddedRecords table) {
		super();
		this.table = table;
	}

	/**
	 * Define here what happens when the action is performed.
	 */
	public abstract void actionPerformed(ActionEvent ev);
	
	/**
	 * Gets the text currently associated with this action.
	 * @return
	 */
	final String getText() {
		return (String)this.getValue(Action.NAME);
	}

	/**
	 * Sets the text associated with this action. If <code>null</code>, the default
	 * text stored in {@link #defText} is used instead.
	 * @param txt The proposed text for this action, or <code>null</code> to use the
	 * default text.
	 */
	final void setText(String txt) {
		String proposed = txt;
		if (proposed == null) proposed = this.getDefaultText();
		
		this.putValue(Action.NAME, proposed);
		this.putValue(Action.SHORT_DESCRIPTION, proposed);
	}
	
	/**
	 * Gets the icon currently associated with this action.
	 * @return
	 */
	final Icon getIcon() {
		return (Icon)this.getValue(Action.SMALL_ICON);
	}
	
	/**
	 * Sets the icon associated with this action. If <code>null</code> the default
	 * icon stored in {@link #defaultIcon} is used instead.
	 * @param ico The proposed icon to be used for this action, or <code>null</code>
	 * to use the default icon.
	 */
	final void setIcon(Icon ico) {
		if (ico == null) this.putValue(Action.SMALL_ICON, this.getDefaultIcon());
		this.putValue(Action.SMALL_ICON, ico);
	}

	/**
	 * Concrete implementations should return whether the action should be enabled,
	 * based on the information known to the implementation.
	 * @return
	 */
	protected abstract boolean shouldBeEnabled();

	/**
	 * Returns the selected index in the table.
	 * @return
	 */
	final protected int getSelectedRecord() {
		return this.table.getSelectionModel().getMinSelectionIndex();
	}
	
	/**
	 * Calls logic to determine whether the action should be enabled, and based on that
	 * result, enables or disables the action.
	 */
	public final void checkEnabled() {
		this.setEnabled(this.shouldBeEnabledGeneral());
	}

	private final boolean shouldBeEnabledGeneral() {
		boolean result = this.shouldBeEnabled();
		
		for (SupplementalEnabledCheck check : this.supplementalEnabledChecks) {
			result = result && check.shouldBeEnabled();
		}
		
		return result;
	}

	/**
	 * Returns the default text associated with this action.
	 * @return
	 */
	protected final String getDefaultText() {
		return this.defText;
	}

	/**
	 * Returns the default icon associated with this action.
	 * @return
	 */
	protected final Icon getDefaultIcon() {
		return this.defaultIcon;
	}
}