package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.util.ByteStream;
import kom.handlersocket.util.Util;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class HSUpdateQuery extends HSFindQuery {

	public HSUpdateQuery() {
		this(CompareOperator.GE, Arrays.asList(""), null, false);
	}

	public HSUpdateQuery(boolean returnData) {
		this(CompareOperator.GE, Arrays.asList(""), null, returnData);
	}

	public HSUpdateQuery(CompareOperator operator, List<String> conditions, List<String> values) {
		this(operator, conditions, values, false);
	}

	public HSUpdateQuery(CompareOperator operator, List<String> conditions, List<String> values, boolean returnData) {
		super(operator, conditions);
		returnData(returnData);

		this.values = values;
	}

	@Override
	protected void modify(ByteStream output) {
		try {
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			if (HS.ResultType.MOD_OPERATION == resultType) {
				output.writeBytes(HS.OPERATOR_UPDATE, false);
			} else if (HS.ResultType.FIND_OPERATION == resultType) {
				output.writeBytes(HS.OPERATOR_GET_AND_UPDATE, false);
			} else {
				throw new InvalidParameterException("invalid result type for UPDATE operation");
			}

			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeStrings(values, HS.TOKEN_DELIMITER_AS_BYTES, true);
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}

	@Override
	public HSFindQuery values(List<String> values) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		this.values = values;

		return this;
	}
}