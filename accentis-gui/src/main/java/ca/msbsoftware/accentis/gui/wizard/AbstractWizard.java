package ca.msbsoftware.accentis.gui.wizard;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWizard {

	private final WizardDialog wizardDialog;
	
	private List<AbstractWizardPage> pages;
	
	private int currentPageIndex;
	
	protected AbstractWizard(WizardDialog dialog) {
		wizardDialog = dialog;
		pages = new ArrayList<AbstractWizardPage>();
		createWizardPages(pages);
		wizardDialog.setWizardPage(pages.get(currentPageIndex));
		wizardDialog.updateButtons();
	}
	
	protected abstract void createWizardPages(List<AbstractWizardPage> pages);
	
	public boolean canWizardBack() {
		return 0 < currentPageIndex;
	}
	
	public boolean canWizardNext() {
		return pages.size() - 1 > currentPageIndex;
	}
	
	public abstract boolean canWizardFinish();

	public void doWizardBack() {
		wizardDialog.setWizardPage(pages.get(--currentPageIndex));
	}
	
	public void doWizardNext() {
		pages.get(currentPageIndex).recordInputs();
		wizardDialog.setWizardPage(pages.get(++currentPageIndex));
	}
	
	public void doWizardFinish() {
		pages.get(currentPageIndex).recordInputs();
		processInputs();
	}

	protected abstract void processInputs();
	
	protected WizardDialog getWizardDialog() {
		return wizardDialog;
	}
}
