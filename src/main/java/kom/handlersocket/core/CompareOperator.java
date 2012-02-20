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


public enum CompareOperator {
	/**
	 * '=' operator
	 */
	EQ,
	/**
	 * '>' operator
	 */
	GT,
	/**
	 * '>=' operator
	 */
	GE,
	/**
	 * '<=' opeartor
	 */
	LE,
	/**
	 * '<' opeartor
	 */
	LT,
	/**
	 * '!=' opeartor
	 */
	NE;

	/**
	 * Returns operator string value
	 *
	 * @return
	 */
	public byte[] getValue() {
		switch (this) {
			case EQ:
				return OP_EQ;
			case GT:
				return OP_GT;
			case GE:
				return OP_GE;
			case LE:
				return OP_LE;
			case LT:
				return OP_LT;
			case NE:
				return OP_NE;
			default:
				throw new RuntimeException("Unknown find operator " + this);
		}
	}

	private static final byte[] OP_EQ = new byte[]{'='};
	private static final byte[] OP_GT = new byte[]{'>'};
	private static final byte[] OP_GE = new byte[]{'>', '='};
	private static final byte[] OP_LE = new byte[]{'<', '='};
	private static final byte[] OP_LT = new byte[]{'<'};
	private static final byte[] OP_NE = new byte[]{'!', '='};
}
