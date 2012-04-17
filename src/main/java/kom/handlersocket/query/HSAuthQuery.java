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

import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.core.HSProto;
import kom.handlersocket.core.ResultType;
import kom.handlersocket.core.SafeByteStream;

public class HSAuthQuery extends HSQuery {

	private final String secret;
	private final byte[] type = new byte[]{'1'};

	public HSAuthQuery(String secret) {
		super(ResultType.SIMPLE);
		this.secret = secret;
	}

	@Override
	public void encode(final SafeByteStream output) {
		output.writeBytes(HSProto.OPERATOR_AUTH, false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeBytes(this.type, false);
		output.writeBytes(HSProto.TOKEN_DELIMITER_AS_BYTES, false);
		output.writeString(this.secret, true);
		output.writeBytes(HSProto.PACKET_DELIMITER_AS_BYTES, false);
	}

	@Override
	public void encode(HSIndexDescriptor indexDescriptor, final SafeByteStream output) {
		encode(output);
	}
}