package ca.msbsoftware.accentis.gui.views;

import ca.msbsoftware.accentis.gui.Resources;

public enum DownloadedTransactionAction {

	DISCARD, CREATE, MATCH;

	@Override
	public String toString() {
		return Resources.getInstance().getString("downloadedtransactionactionenum." + name().toLowerCase()); //$NON-NLS-1$
	}
}
