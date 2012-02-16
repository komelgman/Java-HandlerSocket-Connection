package kom.handlersocket;

public interface HS {
	public static final byte PACKET_DELIMITER = '\n';
	public static final String PACKET_DELIMITER_AS_STR = "\n";
	public static final byte TOKEN_DELIMITER = '\t';
	public static final String TOKEN_DELIMITER_AS_STR = "\t";

	public static final byte UNSAFE_BYTE_MARKER = 0x01;
	public static final byte UNSAFE_BYTE_MASK = 0x40;

	public static final String OPERATOR_AUTH = "A";
	public static final String OPERATOR_OPEN_INDEX = "P";
	public static final String OPERATOR_INSERT = "+";
	public static final String OPERATOR_UPDATE = "U";
	public static final String OPERATOR_GET_AND_UPDATE = "U?";
	public static final String OPERATOR_DELETE = "D";
	public static final String OPERATOR_GET_AND_DELETE = "D?";
	public static final String OPERATOR_INCREMENT = "+";
	public static final String OPERATOR_GET_AND_INCREMENT = "+?";
	public static final String OPERATOR_DECREMENT = "-";
	public static final String OPERATOR_GET_AND_DECREMENT = "-?";
	public static final String OPERATOR_IN = "@";

	public enum ResultType {
		SIMPLE,
		MOD_OPERATION,
		FIND_OPERATION,
		INSERT_OPERATION
	}

	public enum FilterType {
		FILTER,
		WHILE;

		public String getValue() {
			switch (this) {
			case FILTER:
				return "F";
			case WHILE:
				return "W";
			default:
				throw new RuntimeException("Unknown find operator " + this);
			}
		}
	}
}
