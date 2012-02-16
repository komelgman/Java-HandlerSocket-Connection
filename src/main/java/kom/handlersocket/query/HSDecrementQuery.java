package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.util.Util;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class HSDecrementQuery extends HSFindQuery {

	public HSDecrementQuery() {
		this(CompareOperator.GE, Arrays.asList(""), null, false);
	}

	public HSDecrementQuery(boolean returnData) {
		this(CompareOperator.GE, Arrays.asList(""), null, returnData);
	}

	public HSDecrementQuery(CompareOperator operator, List<String> conditions, List<String> values) {
		this(operator, conditions, values, false);
	}

	public HSDecrementQuery(CompareOperator operator, List<String> conditions, List<String> values, boolean returnData) {
		super(operator, conditions);
		returnData(returnData);

		if (values != null) {
			this.values = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(values));
		}
	}

	@Override
	protected void modify(StringBuilder buffer) {
		buffer.append(HS.TOKEN_DELIMITER_AS_STR);

		if (HS.ResultType.MOD_OPERATION == resultType) {
			buffer.append(HS.OPERATOR_DECREMENT);
		} else if (HS.ResultType.FIND_OPERATION == resultType) {
			buffer.append(HS.OPERATOR_GET_AND_DECREMENT);
		} else {
			throw new InvalidParameterException("invalid result type for DECREMENT operation");
		}

		buffer.append(HS.TOKEN_DELIMITER_AS_STR);
		buffer.append(values);
	}

	@Override
	public HSFindQuery values(List<String> values) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		this.values = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(values));

		return this;
	}
}