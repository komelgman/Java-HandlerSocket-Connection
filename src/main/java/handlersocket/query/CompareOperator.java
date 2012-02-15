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
	public String getValue() {
		switch (this) {
		case EQ:
			return "=";
		case GT:
			return ">";
		case GE:
			return ">=";
		case LE:
			return "<=";
		case LT:
			return "<";
		case NE:
			return "!=";
		default:
			throw new RuntimeException("Unknown find operator " + this);
		}
	}
}
