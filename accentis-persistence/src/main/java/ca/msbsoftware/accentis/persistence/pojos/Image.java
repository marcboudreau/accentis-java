package ca.msbsoftware.accentis.persistence.pojos;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Image extends BaseObject {

	@Lob
	private byte[] data;
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] value) {
		if (null == value)
			data = null;
		else {
			data = new byte[value.length];
			System.arraycopy(value, 0, data, 0, value.length);
		}
	}
}
