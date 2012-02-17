package kom.handlersocket;

public interface HS {
	public static final byte PACKET_DELIMITER = '\n';
	public static final byte[] PACKET_DELIMITER_AS_BYTES = new byte[] {'\n'};
	public static final byte TOKEN_DELIMITER = '\t';
	public static final byte[] TOKEN_DELIMITER_AS_BYTES = new byte[] {'\t'};

	public static final byte UNSAFE_BYTE_MARKER = 0x01;
	public static final byte UNSAFE_BYTE_MASK = 0x40;

	public static final byte[] OPERATOR_AUTH = new byte[] {'A'};
	public static final byte[] OPERATOR_OPEN_INDEX = new byte[] {'P'};
	public static final byte[] OPERATOR_INSERT = new byte[] {'+'};
	public static final byte[] OPERATOR_UPDATE = new byte[] {'U'};
	public static final byte[] OPERATOR_GET_AND_UPDATE = new byte[] {'U', '?'};
	public static final byte[] OPERATOR_DELETE = new byte[] {'D'};
	public static final byte[] OPERATOR_GET_AND_DELETE = new byte[] {'D', '?'};
	public static final byte[] OPERATOR_INCREMENT = new byte[] {'+'};
	public static final byte[] OPERATOR_GET_AND_INCREMENT = new byte[] {'+', '?'};
	public static final byte[] OPERATOR_DECREMENT = new byte[] {'-'};
	public static final byte[] OPERATOR_GET_AND_DECREMENT = new byte[] {'-', '?'};
	public static final byte[] OPERATOR_IN = new byte[] {'@'};
	public static final byte[] OPERATOR_FILTER = new byte[] {'F'};
	public static final byte[] OPERATOR_WHILE = new byte[] {'W'};

	public enum ResultType {
		SIMPLE,
		MOD_OPERATION,
		FIND_OPERATION,
		INSERT_OPERATION
	}

	public enum FilterType {
		FILTER,
		WHILE;

		public byte[] getValue() {
			switch (this) {
			case FILTER:
				return OPERATOR_FILTER;
			case WHILE:
				return OPERATOR_WHILE;
			default:
				throw new RuntimeException("Unknown find operator " + this);
			}
		}
	}
}
