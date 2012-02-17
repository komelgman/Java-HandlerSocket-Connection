package kom.handlersocket.result;

import io.netty.buffer.ChannelBuffer;
import kom.handlersocket.query.HSQuery;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HSResultFuture {

	private boolean isReady = false;
	private boolean isTimeout = false;

	private HSResult result = null;
	private LinkedList<HSQuery> queries;
	private Charset charset;


	public HSResultFuture(List<HSQuery> queries, Charset charset) {
		this.charset = charset;
		this.queries = new LinkedList<HSQuery>(queries);
	}

	public HSResult get() {
		return get(5000, TimeUnit.MILLISECONDS);
	}

	public HSResult get(long timeout) {
		return get(timeout, TimeUnit.MILLISECONDS);
	}

	public synchronized HSResult get(long timeout, TimeUnit unit) {
		if (!TimeUnit.MILLISECONDS.equals(unit)) {
			timeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
		}

		while (!isReady && !isTimeout) {
			try {
				wait(timeout);
				isTimeout = true;
			} catch (InterruptedException e) {
				// ignore
			}
		}

		if (!isReady) {
			if (result == null) {
				result = new HSResult(charset);
			}

			// result.setException(new TimeoutException());
		}

		return result;
	}

	public synchronized void addResult(List<ChannelBuffer> data) {
		if (!isTimeout) {
			if (result == null) {
				result = new HSResult(charset);
			}

			result.add(queries.removeFirst(), data);

			if (queries.size() == 0) {
				this.isReady = true;
				notifyAll();
			}
		}
	}

	public synchronized boolean isReady() {
		return isReady;
	}
}