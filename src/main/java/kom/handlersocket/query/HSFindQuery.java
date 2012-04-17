/*
 * Copyright 2012 The Java HandlerSocket Connection Project
 *
 * https://github.com/komelgman/Java-HandlerSocket-Connection/
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package kom.handlersocket.query;

import kom.handlersocket.core.FilterType;
import kom.handlersocket.core.HSProto;
import kom.handlersocket.core.ResultType;
import kom.handlersocket.core.SafeByteStream;

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

	public HSFindQuery() {
		this(CompareOperator.GE, Arrays.asList(""));
	}

	public HSFindQuery(CompareOperator operator, List<String> conditions) {
		super(ResultType.FIND_OPERATION);

		if (conditions != null) {
			this.operator = operator;
			this.conditions = conditions;
		}
	}

	@Override
	protected void encode(final SafeByteStream output) {
		if (this.conditions == null) {
			throw new InvalidParameterException("invalid conditions");
		}

		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		output.writeString(indexDescriptor.getIndexId(), false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

		output.writeBytes(operator.getValue(), false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

		output.writeString(String.valueOf(conditions.size()), false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

		output.writeStrings(conditions, HSProto.TOKEN_DELIMITER_AS_BYTES, true);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

		output.writeString(String.valueOf(limit), false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

		output.writeString(String.valueOf(offset), false);

		if (INIndexValues != null) {
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeBytes(HSProto.OPERATOR_IN, false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeString(String.valueOf(INIndexNumber), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeString(String.valueOf(INIndexValues.size()), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

			output.writeStrings(INIndexValues, HSProto.TOKEN_DELIMITER_AS_BYTES, true);
		}

		if (filters.size() > 0) {
			for (Filter filter : filters) {
				filter.encode(output);
			}
		}

		// [MOD]
		modify(output);

		output.writeBytes(HSProto.PACKET_DELIMITER_AS_BYTES, false);
	}

	protected void modify(SafeByteStream output) {
		// nothing, override in modify operations
	}
	
	public HSFindQuery where(CompareOperator operator, List<String> conditions) {
		if (conditions == null) {
			throw new IllegalArgumentException("conditions can't be NULL");
		}

		if (operator == null) {
			throw new IllegalArgumentException("operator can't be NULL");
		}

		if (conditions.size() == 0) {
			throw new IllegalArgumentException("conditions can't be EMPTY");
		}

		this.operator = operator;
		this.conditions = conditions;

		return this;
	}

	public HSFindQuery limit(int limit) {
		if (limit < 1) {
			throw new IllegalArgumentException("limit must be ONE or greater");
		}

		this.limit = limit;
		return this;
	}

	public HSFindQuery offset(int offset) {
		if (limit < 0) {
			throw new IllegalArgumentException("limit must be ZERO or greater");
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
		filters.add(new Filter(FilterType.FILTER, operator, column, value));
		return this;
	}

	public HSFindQuery till(String column, CompareOperator operator, String value) {
		filters.add(new Filter(FilterType.WHILE, operator, column, value));
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
		public final FilterType type;
		public final CompareOperator operator;
		public final String column;
		public final String value;

		public Filter(FilterType type, CompareOperator operator, String column, String value) {
			this.value = value;
			this.operator = operator;
			this.type = type;
			this.column = column;
		}

		public void encode(SafeByteStream output) {
			validate();

			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeBytes(type.getValue(), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeBytes(operator.getValue(), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(String.valueOf(indexDescriptor.getFilterColumnPos(column)), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(value, true);
		}

		private void validate() {
			if (value == null || operator == null || type == null || !indexDescriptor.hasFilterColumn(column)) {
				throw new IllegalArgumentException("invalid filter");
			}
		}
	}
}