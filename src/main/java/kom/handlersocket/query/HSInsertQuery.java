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
import kom.handlersocket.core.ResultType;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

public class HSInsertQuery extends HSQuery {

	protected List<String> values;

	public HSInsertQuery() {
		this(null);
	}

	public HSInsertQuery(List<String> values) {
		super(ResultType.INSERT_OPERATION);
		this.values = values;
	}

	@Override
	public void encode(SafeByteStream output) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		try {
			output.writeString(indexDescriptor.getIndexId(), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeBytes(HSProto.OPERATOR_INSERT, false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(String.valueOf(values.size()), false);
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeStrings(values, HSProto.TOKEN_DELIMITER_AS_BYTES, true);
			output.writeBytes(HSProto.PACKET_DELIMITER_AS_BYTES, false);
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}

	public HSInsertQuery values(List<String> values) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		this.values = values;

		return this;
	}
}