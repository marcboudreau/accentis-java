package ca.msbsoftware.accentis.utils.resources;

import java.util.ArrayList;
import java.util.List;

public class ResourcesUtils {

	public static String[] splitList(String resourceString) {
		List<String> tokens = new ArrayList<String>();

		StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < resourceString.length(); ++i) {
			char c  = resourceString.charAt(i);
			if ('\\' == c) {
				if (i < resourceString.length() - 1)
					buffer.append(resourceString.charAt(i + 1));
				else
					buffer.append('\\');
				
				i++;
			} else if (',' == c) {
				tokens.add(buffer.toString());
				buffer.delete(0, buffer.length());
			} else
				buffer.append(c);
		}
		
		tokens.add(buffer.toString());

		return tokens.toArray(new String[0]);
	}

}
