package ca.msbsoftware.accentis.ofxparser;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@SuppressWarnings("serial")
public class DownloadedTransactionData implements Serializable {

	public BigDecimal amount;
	
	public String description;
	
	public String bankAccountId;
	
	public Date postedDateTime;
	
	public Currency currency;
	
}
