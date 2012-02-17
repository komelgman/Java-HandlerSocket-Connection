package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.ByteStream;
import kom.handlersocket.util.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HSFindQuery extends HSQuery {

	protected CompareOperator operator = null;
	protected List<String> conditions = null;

	// extra params
	// LIM
	protected int limit = 1;
	protected int offset = 0;

	// [IN]
	protected int INIndexNumber = 0;
	protected List<String> INIndexValues = null;

	// [FILTER]
	protected List<Filter> filters = new ArrayList<Filter>();

	// [MOD]
	protected List<String> values;

	public HSFindQuery() {
		this(CompareOperator.GE, Arrays.asList(""));
	}

	public HSFindQuery(CompareOperator operator, List<String> conditions) {
		super(HS.ResultType.FIND_OPERATION);
			
		if (conditions != null) {			
			this.operator = operator;
			this.conditions = conditions;
		}
	}

	@Override
	public void encode(final ByteStream output) {
		if (this.conditions == null) {
			throw new InvalidParameterException("invalid conditions");
		}

		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		try {
			output.writeString(indexDescriptor.getIndexId(), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeBytes(operator.getValue(), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeString(String.valueOf(conditions.size()), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeStrings(conditions, HS.TOKEN_DELIMITER_AS_BYTES, true);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeString(String.valueOf(limit), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeString(String.valueOf(offset), false);

			if (INIndexValues != null) {
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
				output.writeBytes(HS.OPERATOR_IN, false);
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
	
				output.writeString(String.valueOf(INIndexNumber), false);
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
					
				output.writeString(String.valueOf(INIndexValues.size()), false);				
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
	
				output.writeStrings(INIndexValues, HS.TOKEN_DELIMITER_AS_BYTES, true);
			}
	
			if (filters.size() > 0) {
				for (Filter filter : filters) {
					filter.encode(output);
				}
			}
	
			// [MOD]
			modify(output);
	
			output.writeBytes(HS.PACKET_DELIMITER_AS_BYTES, false);
			
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}

	public HSFindQuery values(List<String> values) {
		throw new UnsupportedOperationException("can't perform this operation");
	}

	protected void modify(ByteStream output) {
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
		this.conditions = conditions;

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
		INIndexValues = conditions;
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
		
		limit = 1;
		offset = 0;

		conditions = null;
		operator = null;
	}


	class Filter {
		public final HS.FilterType type;
		public final CompareOperator operator;
		public final String column;
		public final String value;
		
		public Filter(HS.FilterType type, CompareOperator operator, String column, String value) {
			this.value = value;
			this.operator = operator;
			this.type = type;
			this.column = column;
		}
		
		public void encode(ByteStream output) {			
			validate();

			try {
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
				output.writeBytes(type.getValue(), false);
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
				output.writeBytes(operator.getValue(), false);
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
				output.writeString(String.valueOf(indexDescriptor.getFilterColumnIndex(column)), false);
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
				output.writeString(value, true);			
			} catch (IOException e) {
				System.err.print(e.getMessage());
			}			
		}
		
		private void validate() {
			if (value == null || operator == null || type == null || indexDescriptor.getColumnIndex(column) == -1) {
				throw new IllegalArgumentException("invalid filter");
			}
		}
	}
}