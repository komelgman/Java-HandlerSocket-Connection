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

public class HSOpenIndexQuery extends HSQuery {

	private static final byte[] COMMA_DELIMITER = new byte[]{','};

	public HSOpenIndexQuery() {
		super(ResultType.SIMPLE);
	}

	@Override
	public void encode(SafeByteStream output) {
		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		output.writeBytes(HSProto.OPERATOR_OPEN_INDEX, false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeString(indexDescriptor.getIndexId(), false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeString(indexDescriptor.getDbName(), true);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeString(indexDescriptor.getTableName(), true);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeString(indexDescriptor.getIndexName(), true);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeStrings(indexDescriptor.getColumns(), new byte[]{','}, true);

		if (indexDescriptor.hasFilterColumns()) {
			output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeStrings(indexDescriptor.getFilterColumns(), COMMA_DELIMITER, true);
		}

		output.writeBytes(HSProto.PACKET_DELIMITER_AS_BYTES, false);
	}
}