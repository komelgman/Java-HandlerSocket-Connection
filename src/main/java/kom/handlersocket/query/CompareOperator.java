package kom.handlersocket.query;


public enum CompareOperator {
	/**
	 * '=' operator
	 */
	EQ,
	/**
	 * '>' operator
	 */
	GT,
	/**
	 * '>=' operator
	 */
	GE,
	/**
	 * '<=' opeartor
	 */
	LE,
	/**
	 * '<' opeartor
	 */
	LT,
	/**
	 * '!=' opeartor
	 */
	NE;

	/**
	 * Returns operator string value
	 *
	 * @return
	 */
	public byte[] getValue() {
		switch (this) {
		case EQ:
			return OP_EQ;
		case GT:
			return OP_GT;
		case GE:
			return OP_GE;
		case LE:
			return OP_LE;
		case LT:
			return OP_LT;
		case NE:
			return OP_NE;
		default:
			throw new RuntimeException("Unknown find operator " + this);
		}
	}
	
	private static final byte[] OP_EQ = new byte[]{ '=' };
	private static final byte[] OP_GT = new byte[]{ '>' };
	private static final byte[] OP_GE = new byte[]{ '>', '=' };
	private static final byte[] OP_LE = new byte[]{ '<', '=' };
	private static final byte[] OP_LT = new byte[]{ '<' };
	private static final byte[] OP_NE = new byte[]{ '!', '=' };
}
