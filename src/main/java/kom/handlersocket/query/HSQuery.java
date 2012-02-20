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
import kom.handlersocket.core.SafeByteStream;
import kom.handlersocket.core.ResultType;

import java.security.InvalidParameterException;

abstract public class HSQuery {

	protected ResultType resultType;
	protected HSIndexDescriptor indexDescriptor;

	protected HSQuery(ResultType resultType) {
		this.resultType = resultType;
	}

	public void encode(final HSIndexDescriptor indexDescriptor, final SafeByteStream output) {
		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		this.indexDescriptor = indexDescriptor;

		encode(output);
	}

	abstract public void encode(final SafeByteStream output);

	public ResultType resultType() {
		return resultType;
	}
}