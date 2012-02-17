package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.util.ByteStream;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class HSDeleteQuery extends HSFindQuery {

	public HSDeleteQuery() {
		this(CompareOperator.GE, Arrays.asList(""), false);
	}
		
	public HSDeleteQuery(boolean returnData) {
		this(CompareOperator.GE, Arrays.asList(""), returnData);
	}
	
	public HSDeleteQuery(CompareOperator operator, List<String> conditions) {
		this(operator, conditions, false);	
	}
	
	public HSDeleteQuery(CompareOperator operator, List<String> conditions, boolean returnData) {
		super(operator, conditions);
		returnData(returnData);
	}
	
	@Override
	protected void modify(ByteStream output) {
		try {
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			if (HS.ResultType.MOD_OPERATION == resultType) {
				output.writeBytes(HS.OPERATOR_DELETE, false);
			} else if (HS.ResultType.FIND_OPERATION == resultType) {
				output.writeBytes(HS.OPERATOR_GET_AND_DELETE, false);
			} else {
				throw new InvalidParameterException("invalid result type for DELETE operation");
			}

		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}
}