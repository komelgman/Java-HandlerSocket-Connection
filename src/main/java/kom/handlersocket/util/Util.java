package kom.handlersocket.util;

import kom.handlersocket.HS;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static String implode(String delimiter, List<String> values) {
		StringBuilder result = new StringBuilder();

		result.append(values.get(0));

		for(int i = 1; i < values.size(); ++i) {
			result.append(delimiter);
			result.append(values.get(i));
		}

		return result.toString();
	}
	
	public static String safe(String value) {
		byte[] input = value.getBytes();
		byte[] output = new byte[input.length];
		int count = 0;
		
		for(byte b : input) {
			if (b <= 0x0f) {
				output[count++] = HS.UNSAFE_BYTE_MARKER;
				output[count++] = (byte) (b ^ HS.UNSAFE_BYTE_MASK);
			} else {
				output[count++] = b;
			}
		}

		return new String(output);
	}
	
	public static List<String> safe(List<String> values) {
		if ((values == null) || (values.size() == 0)) {
			return values;
		}

		List<String> result = new ArrayList<String>(values.size());
		for(int i = 0; i < values.size(); ++i) {
			result.add(i, Util.safe(values.get(i)));
		}

		return result;
	}
}