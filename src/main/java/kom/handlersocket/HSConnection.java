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

package kom.handlersocket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import kom.handlersocket.query.HSAuthQuery;
import kom.handlersocket.query.HSQuery;
import kom.handlersocket.result.HSResultFuture;
import kom.handlersocket.core.SafeByteStream;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HSConnection {
	private Channel channel;
	private Charset charset;

	private LinkedList<HSResultFuture> pendingResults = new LinkedList<HSResultFuture>();
	private HSResultFuture currentResultFuture = null;

	public HSConnection() {
		this(Charset.defaultCharset());
	}

	public HSConnection(Charset charset) {
		this.charset = charset;
	}

	public synchronized HSResultFuture execute(HSAuthQuery query) {
		return execute(null, Arrays.asList((HSQuery) query));
	}

	public synchronized HSResultFuture execute(HSIndexDescriptor indexDescriptor, HSQuery query) {
		return execute(indexDescriptor, Arrays.asList(query));
	}

	public synchronized HSResultFuture execute(HSIndexDescriptor indexDescriptor, List<HSQuery> queries) {
		final SafeByteStream packet = new SafeByteStream(queries.size() * 128, 65536, charset);

		for (HSQuery query : queries) {
			query.encode(indexDescriptor, packet);
		}

		channel.write(packet);

		return addResultFuture(queries);
	}

	private HSResultFuture addResultFuture(List<HSQuery> queries) {
		HSResultFuture resultFuture = new HSResultFuture(queries, charset);
		pendingResults.addFirst(resultFuture);
		return resultFuture;
	}

	public synchronized void response(List<List<ChannelBuffer>> responses) {
		for (List<ChannelBuffer> response : responses) {
			if (currentResultFuture == null || currentResultFuture.isReady()) {
				currentResultFuture = pendingResults.removeLast();
			}

			currentResultFuture.addResult(response);
		}
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public ChannelFuture close() {
		return channel.close();
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}