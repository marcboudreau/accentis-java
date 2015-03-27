package ca.msbsoftware.accentis.persistence;

@SuppressWarnings("serial")
public class InvalidSINException extends Exception {

	public InvalidSINException(int number) {
		super(String.valueOf(number));
	}
}
