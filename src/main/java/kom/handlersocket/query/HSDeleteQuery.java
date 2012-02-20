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
import kom.handlersocket.core.SafeByteStream;
import kom.handlersocket.core.CompareOperator;
import kom.handlersocket.core.ResultType;

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
	protected void modify(SafeByteStream output) {
		try {
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);

			if (ResultType.MOD_OPERATION == resultType) {
				output.writeBytes(HSProto.OPERATOR_DELETE, false);
			} else if (ResultType.FIND_OPERATION == resultType) {
				output.writeBytes(HSProto.OPERATOR_GET_AND_DELETE, false);
			} else {
				throw new InvalidParameterException("invalid result type for DELETE operation");
			}

		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}
}