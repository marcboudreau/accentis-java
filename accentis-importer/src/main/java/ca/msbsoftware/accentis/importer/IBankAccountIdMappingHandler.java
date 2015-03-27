package ca.msbsoftware.accentis.importer;

import ca.msbsoftware.accentis.persistence.pojos.Account;

/**
 * 
 * 
 * @author Marc Boudreau
 *
 * @since 0.0.1
 */
public interface IBankAccountIdMappingHandler {

	/**
	 * Provides an appropriate {@link Account} object based on the given bank account ID.
	 * 
	 * @param bankAccountId
	 *            A {@link String} object representing the bank account ID.
	 * 
	 * @return An {@link Account} object.
	 */
	Account handleMapping(String bankAccountId);
}
