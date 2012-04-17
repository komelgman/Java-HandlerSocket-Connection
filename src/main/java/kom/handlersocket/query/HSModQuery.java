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

import kom.handlersocket.core.HSProto;
import kom.handlersocket.core.ResultType;
import kom.handlersocket.core.SafeByteStream;

import java.security.InvalidParameterException;
import java.util.List;

public abstract class HSModQuery extends HSFindQuery {
	protected List<String> values;
	protected final byte[] modOperator;
	protected final byte[] findOperator;

	public HSModQuery(byte[] modOperator, byte[] findOperator, CompareOperator operator, List<String> conditions, List<String> values) {
		super(operator, conditions);

		this.values = values;
		this.modOperator = modOperator;
		this.findOperator = findOperator;

		returnData(false);
	}

	@Override
	protected void modify(SafeByteStream output) {
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

		if (ResultType.MOD_OPERATION == resultType) {
			output.writeBytes(modOperator, false);
		} else if (ResultType.FIND_OPERATION == resultType) {
			output.writeBytes(findOperator, false);
		} else {
			throw new InvalidParameterException("invalid result type for MOD operation");
		}

		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeStrings(values, HSProto.TOKEN_DELIMITER_AS_BYTES, true);
	}

	public HSFindQuery values(List<String> values) {
		if (null == values) {
			throw new InvalidParameterException("values can't be NULL");
		}

		this.values = values;
		return this;
	}

	public HSFindQuery returnData(boolean value) {
		resultType = value ? ResultType.FIND_OPERATION : ResultType.MOD_OPERATION;
		return this;
	}
}