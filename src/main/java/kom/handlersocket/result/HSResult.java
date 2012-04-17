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

package kom.handlersocket.result;

import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.core.ResultType;
import kom.handlersocket.core.SafeByteStream;
import org.jboss.netty.buffer.ChannelBuffer;

import java.nio.charset.Charset;
import java.util.List;

public class HSResult {
	private List<ChannelBuffer> data;
	private Exception cause;

	private final HSIndexDescriptor indexDescriptor;
	private final ResultType resultType;
	private final Charset charset;

	public HSResult(HSIndexDescriptor indexDescriptor, ResultType resultType, Charset charset) {
		this.indexDescriptor = indexDescriptor;
		this.resultType = resultType;
		this.charset = charset;
	}

	void setData(List<ChannelBuffer> data) {
		this.data = data;
	}

	void setCause(Exception cause) {
		this.cause = cause;
	}




	public void debug() {
		SafeByteStream output = new SafeByteStream(1024, 65536, charset);
//		query.encode(output);
//
//		System.out.print("query  >> ");
//		System.out.print(new String(output.toByteArray(), charset));

		if (data != null) {
			System.out.print("result >> ");
			for (ChannelBuffer buffer : data) {
				System.out.print(buffer.toString(charset));
				System.out.print("-");
			}
		}

		if (cause != null) {
			System.out.print("cause  >> ");
			System.out.print(cause.getMessage());
		}

		System.out.println();
	}
}