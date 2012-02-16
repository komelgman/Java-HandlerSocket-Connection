package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.Util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HSFindQuery extends HSQuery {

	protected CompareOperator operator = null;
	protected int conditionsCount = 0;
	protected String conditions = null;

	// extra params
	// LIM
	protected int limit = 1;
	protected int offset = 0;

	// [IN]
	protected int INIndexNumber = 0;
	protected int INIndexValuesCount = 0;
	protected String INIndexValues = null;

	// [FILTER]
	protected List<Filter> filters = new ArrayList<Filter>();

	// [MOD]
	protected String values;

	public HSFindQuery() {
		this(CompareOperator.GE, Arrays.asList(""));
	}

	public HSFindQuery(CompareOperator operator, List<String> conditions) {
		super(HS.ResultType.FIND_OPERATION);
			
		if (conditions != null) {			
			this.operator = operator;
			this.conditionsCount = conditions.size();
			this.conditions = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(conditions));
		}
	}

	@Override
	public String encode() {
		if (this.conditionsCount == 0) {
			throw new InvalidParameterException("invalid conditions");
		}

		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		StringBuilder result = new StringBuilder();

		result.append(indexDescriptor.getIndexId());
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(operator.getValue());
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(conditionsCount);
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(conditions);
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(limit);
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(offset);

		if (INIndexValuesCount > 0) {
			result.append(HS.TOKEN_DELIMITER_AS_STR);
			result.append(HS.OPERATOR_IN);
			result.append(HS.TOKEN_DELIMITER_AS_STR);

			result.append(INIndexNumber);
			result.append(HS.TOKEN_DELIMITER_AS_STR);

			result.append(INIndexValuesCount);
			result.append(HS.TOKEN_DELIMITER_AS_STR);

			result.append(INIndexValues);
		}

		if (filters.size() > 0) {
			for (Filter filter : filters) {
				filter.encode(result);
			}
		}

		// [MOD]
		modify(result);

		result.append(HS.PACKET_DELIMITER_AS_STR);

		return result.toString();
	}

	public HSFindQuery values(List<String> values) {
		throw new UnsupportedOperationException("can't perform this operation");
	}

	protected void modify(StringBuilder buffer) {
		// nothing, override in modify operations
	}

	protected void returnData(boolean value) {
		resultType = value
			? HS.ResultType.FIND_OPERATION
			: HS.ResultType.MOD_OPERATION;
	}

	public HSFindQuery where(CompareOperator operator, List<String> conditions) {
		if (conditions == null) {
			throw new IllegalArgumentException("conditions can't be NULL");
		}

		if (operator == null) {
			throw new IllegalArgumentException("operator can't be NULL");
		}

		if (conditions.size() == 0) {
			throw new IllegalArgumentException("conditions can't be empty");
		}

		this.operator = operator;
		this.conditionsCount = conditions.size();
		this.conditions = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(conditions));

		return this;
	}


	public HSFindQuery limit(int limit) {
		if (limit < 1) {
			throw new IllegalArgumentException("limit must be ONE ore greater");
		}
		
		this.limit = limit;
		return this;
	}
	
	public HSFindQuery offset(int offset) {
		if (limit < 0) {
			throw new IllegalArgumentException("limit must be ZERO ore greater");
		}
		
		this.offset = offset;
		return this;
	}
	
	public HSFindQuery in(int indexFieldNumber, List<String> conditions) {
		INIndexNumber = indexFieldNumber;
		INIndexValuesCount = conditions.size();
		INIndexValues = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(conditions));
		return this;
	}
	
	public HSFindQuery filter(String column, CompareOperator operator, String value) {
		filters.add(new Filter(HS.FilterType.FILTER, operator, column, value));
		return this;
	}

	public HSFindQuery till(String column, CompareOperator operator, String value) {
		filters.add(new Filter(HS.FilterType.WHILE, operator, column, value));
		return this;
	}
	
	public void reset() {
		filters.clear();
		
		INIndexNumber = 0;
		INIndexValues = null;
		INIndexValuesCount = 0;
		
		limit = 1;
		offset = 0;

		conditions = null;
		conditionsCount = 0;
		operator = null;
	}


	class Filter {
		public final HS.FilterType type;
		public final CompareOperator operator;
		public final String column;
		public final String value;
		
		public Filter(HS.FilterType type, CompareOperator operator, String column, String value) {
			this.value = Util.safe(value);
			this.operator = operator;
			this.type = type;
			this.column = column;
		}
		
		public void encode(StringBuilder builder) {			
			validate();

			builder.append(HS.TOKEN_DELIMITER_AS_STR);
			builder.append(type.getValue());
			builder.append(HS.TOKEN_DELIMITER_AS_STR);
			builder.append(operator.getValue());
			builder.append(HS.TOKEN_DELIMITER_AS_STR);
			builder.append(indexDescriptor.getFilterColumnIndex(column));
			builder.append(HS.TOKEN_DELIMITER_AS_STR);
			builder.append(value);
		}
		
		private void validate() {
			if (value == null || operator == null || type == null || indexDescriptor.getColumnIndex(column) == -1) {
				throw new IllegalArgumentException("invalid filter");
			}
		}
	}
}