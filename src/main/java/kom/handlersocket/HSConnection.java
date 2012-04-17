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

import kom.handlersocket.core.SafeByteStream;
import kom.handlersocket.query.HSQuery;
import kom.handlersocket.result.HSResult;
import kom.handlersocket.result.HSResultFuture;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import java.nio.charset.Charset;
import java.util.*;

public class HSConnection {
	private Charset charset;	
	private Channel channel;

	private final LinkedList<HSResultFuture> pendingResults = new LinkedList<HSResultFuture>();
	private final LinkedList<PendingQueries> pendingQueries = new LinkedList<PendingQueries>();
	private HSResultFuture currentResultFuture = null;

	public HSConnection() {
		this(Charset.defaultCharset());
	}

	public HSConnection(Charset charset) {
		this.charset = charset;
	}

	public HSConnection addQuery(HSIndexDescriptor indexDescriptor, HSQuery query) {
		return addQueries(indexDescriptor, Arrays.asList(query));
	}

	public HSConnection addQueries(HSIndexDescriptor indexDescriptor, final HSQuery... queries) {
		return addQueries(indexDescriptor, Arrays.asList(queries));
	}

	public HSConnection addQueries(HSIndexDescriptor indexDescriptor, final List<? extends HSQuery> queries) {
		synchronized (pendingQueries) {
			pendingQueries.add(new PendingQueries(indexDescriptor, queries));
		}
		
		return this;
	}

	public HSResultFuture execute() {
		final LinkedList<PendingQueries> pendingQueries = getPendingQueries();
		final SafeByteStream packet = new SafeByteStream(pendingQueries.size() * 64, 65536, charset);
		final LinkedList<HSResult> resultSet = new LinkedList<HSResult>();

		for (PendingQueries entry : pendingQueries) {
			for (HSQuery query : entry.queries) {
				resultSet.add(new HSResult(entry.indexDescriptor, query.resultType(), charset));
				query.encode(entry.indexDescriptor, packet);
			}
		}

		final HSResultFuture resultFuture = new HSResultFuture(resultSet);

		synchronized (pendingResults) {
			pendingResults.addFirst(resultFuture);
			channel.write(packet);
		}

		return resultFuture;
	}

	private LinkedList<PendingQueries> getPendingQueries() {
		final LinkedList<PendingQueries> result;

		synchronized (pendingQueries) {
			result = (LinkedList<PendingQueries>)pendingQueries.clone();
			pendingQueries.clear();
		}

		return result;
	}

	public void response(List<List<ChannelBuffer>> data) {
		synchronized (pendingResults) {
			for (List<ChannelBuffer> entity : data) {
				if (currentResultFuture == null || currentResultFuture.isReady()) {				
					currentResultFuture = pendingResults.removeLast();
				}
	
				currentResultFuture.addResult(entity);
			}	
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




	private class PendingQueries {
		public final HSIndexDescriptor indexDescriptor;
		public final List<? extends HSQuery> queries;

		public PendingQueries(HSIndexDescriptor indexDescriptor, List<? extends HSQuery> queries) {
			this.indexDescriptor = indexDescriptor;
			this.queries = queries;
		}
	}
}