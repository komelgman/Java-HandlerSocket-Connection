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

package kom.handlersocket.core;

public final class HSProto {

	private HSProto() { throw new IllegalAccessError(); }

	public static final byte PACKET_DELIMITER = '\n';
	public static final byte[] PACKET_DELIMITER_AS_BYTES = new byte[]{'\n'};
	public static final byte TOKEN_DELIMITER = '\t';
	public static final byte[] TOKEN_DELIMITER_AS_BYTES = new byte[]{'\t'};

	public static final byte UNSAFE_BYTE_MARKER = 0x01;
	public static final byte UNSAFE_BYTE_MASK = 0x40;

	public static final byte[] OPERATOR_AUTH = new byte[]{'A'};
	public static final byte[] OPERATOR_OPEN_INDEX = new byte[]{'P'};
	public static final byte[] OPERATOR_INSERT = new byte[]{'+'};
	public static final byte[] OPERATOR_UPDATE = new byte[]{'U'};
	public static final byte[] OPERATOR_GET_AND_UPDATE = new byte[]{'U', '?'};
	public static final byte[] OPERATOR_DELETE = new byte[]{'D'};
	public static final byte[] OPERATOR_GET_AND_DELETE = new byte[]{'D', '?'};
	public static final byte[] OPERATOR_INCREMENT = new byte[]{'+'};
	public static final byte[] OPERATOR_GET_AND_INCREMENT = new byte[]{'+', '?'};
	public static final byte[] OPERATOR_DECREMENT = new byte[]{'-'};
	public static final byte[] OPERATOR_GET_AND_DECREMENT = new byte[]{'-', '?'};
	public static final byte[] OPERATOR_IN = new byte[]{'@'};
	public static final byte[] OPERATOR_FILTER = new byte[]{'F'};
	public static final byte[] OPERATOR_WHILE = new byte[]{'W'};
}