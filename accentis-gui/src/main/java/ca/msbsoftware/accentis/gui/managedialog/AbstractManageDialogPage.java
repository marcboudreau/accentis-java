package ca.msbsoftware.accentis.gui.managedialog;

import javax.swing.JComponent;

import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.NamedObject;

public abstract class AbstractManageDialogPage<T extends NamedObject> {

	private ManagePojoDialog managePojoDialog;
	
	private T currentPojo;
	
	protected AbstractManageDialogPage(ManagePojoDialog dialog) {
		managePojoDialog = dialog;
	}
	
	abstract JComponent getComponent();
	
	protected abstract void doFinish();
	
	protected ManagePojoDialog getManagePojoDialog() {
		return managePojoDialog;
	}
	
	boolean canFinish() {
		return false;
	}
	
	String getTitleParameter() {
		return null;
	}

	protected T getCurrentPojo() {
		return currentPojo;
	}
	
	protected void setCurrentPojo(T object) {
		currentPojo = object;
		
		if (null == currentPojo)
			clearFields();
		else
			updateFields();
	}
	
	protected abstract void clearFields();
	
	protected abstract void updateFields();
	
	public void createPojo() {
		show(null);
	}
	
	public void editPojo(T object) {
		show(object);
	}
	
	private void show(T object) {
		getManagePojoDialog().setPage(this);
		setCurrentPojo(object);
		getManagePojoDialog().setVisible(true);
	}

	protected PersistenceManager getPersistenceManager() {
		return GUIApplication.getInstance().getPersistenceManager();
	}
}
