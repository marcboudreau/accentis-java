package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 
 * 
 * @author Marc Boudreau
 *
 * @since 1.0
 */
@Entity
@NamedQueries({@NamedQuery(name="PayeeMapping.findPayeeMapping", query="SELECT pm.payee FROM PayeeMapping pm WHERE LOWER(pm.payeeText) = LOWER(:text)")})
public class PayeeMapping extends BaseObject {

	@Basic
	private String payeeText;
	
	@ManyToOne
	private Payee payee;

	public String getPayeeText() {
		return payeeText;
	}

	public void setPayeeText(String value) {
		payeeText = value;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee value) {
		payee = value;
	}
}
