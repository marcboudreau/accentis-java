package ca.msbsoftware.accentis.persistence.utils;

import java.math.BigDecimal;

import ca.msbsoftware.accentis.persistence.pojos.Account;
import ca.msbsoftware.accentis.persistence.pojos.Category;
import ca.msbsoftware.accentis.persistence.pojos.Transaction;
import ca.msbsoftware.accentis.persistence.pojos.TransactionDetail;

public class TransactionHelper {

	public static BigDecimal getTransactionValueForAccount(Transaction transaction, Account account) {
		BigDecimal amount = BigDecimal.ZERO;
		
		for (TransactionDetail detail : transaction.getTransactionDetails())
			if (detail.getAccount().equals(account))
				amount = amount.add(detail.getValue());
		
		return amount;
	}
	
	public static Category getActualCategory(Category parentCategory, Category subCategory) {
		if (null == subCategory)
			return parentCategory;
		
		return subCategory;
	}
	
	public static boolean isExistingTransaction(Transaction transaction) {
		if (null == transaction)
			return false;
		
		return 0 != transaction.getId();
	}
}
