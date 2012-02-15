package kom.handlersocket;

import io.netty.buffer.ChannelBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import kom.handlersocket.query.HSAuthQuery;
import kom.handlersocket.query.HSQuery;
import kom.handlersocket.result.HSResultFuture;

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
		return execute(null, Arrays.asList((HSQuery)query));
	}

	public synchronized HSResultFuture execute(HSIndexDescriptor indexDescriptor, HSQuery query) {
		return execute(indexDescriptor, Arrays.asList(query));
	}

	public synchronized HSResultFuture execute(HSIndexDescriptor indexDescriptor, List<HSQuery> queries) {		
		StringBuilder packet = new StringBuilder(queries.size());

		for (HSQuery query : queries) {
			packet.append(query.encode(indexDescriptor));
		}

		channel.write(packet.toString());
		return addResultFuture(queries);
	}
	
	private HSResultFuture addResultFuture(List<HSQuery> queries) {
		HSResultFuture resultFuture = new HSResultFuture(charset, queries);
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