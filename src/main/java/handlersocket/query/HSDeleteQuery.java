package kom.handlersocket.query;

import kom.handlersocket.HS;

import java.security.InvalidParameterException;
import java.util.List;

public class HSDeleteQuery extends HSFindQuery {

	public HSDeleteQuery() {
		this(null, null, false);
	}
		
	public HSDeleteQuery(boolean returnData) {
		this(null, null, returnData);
	}
	
	public HSDeleteQuery(CompareOperator operator, List<String> conditions) {
		this(operator, conditions, false);	
	}
	
	public HSDeleteQuery(CompareOperator operator, List<String> conditions, boolean returnData) {
		super(operator, conditions);
		returnData(returnData);
	}
	
	@Override
	protected void modify(StringBuilder buffer) {
		buffer.append(HS.TOKEN_DELIMITER_AS_STR);
		
		if (HS.ResultType.MOD_OPERATION == resultType) {			
			buffer.append(HS.OPERATOR_DELETE);
		} else if (HS.ResultType.FIND_OPERATION == resultType) {
			buffer.append(HS.OPERATOR_GET_AND_DELETE);
		} else {
			throw new InvalidParameterException("invalid result type for DELETE operation");
		}
	}
}
