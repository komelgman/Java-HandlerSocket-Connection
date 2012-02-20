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

import org.jboss.netty.buffer.ChannelBuffer;
import kom.handlersocket.query.HSQuery;
import kom.handlersocket.core.SafeByteStream;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HSResult {
	private final HashMap<HSQuery, List<ChannelBuffer>> resultSet = new LinkedHashMap<HSQuery, List<ChannelBuffer>>();
	private final Charset charset;

	public HSResult(Charset charset) {
		this.charset = charset;
	}

	public void add(HSQuery query, List<ChannelBuffer> result) {
		resultSet.put(query, result);
	}

	public void debug() {
		SafeByteStream output = new SafeByteStream(1024, 65536, charset);

		for (Map.Entry<HSQuery, List<ChannelBuffer>> entry : resultSet.entrySet()) {
			entry.getKey().encode(output);
			System.out.print(new String(output.toByteArray(), charset));
			output.reset();

			for (ChannelBuffer buffer : entry.getValue()) {
				System.out.print(buffer.toString(charset));
				System.out.print("-");
			}

			System.out.println();
		}
	}
}
