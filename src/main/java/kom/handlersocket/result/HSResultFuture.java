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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HSResultFuture {

	private boolean isReady = false;

	private final LinkedList<HSResult> resultSet;
	private final Iterator<HSResult> resultSetIterator;
	private Exception cause = null;

	private final long defaultTimeout = 5000;


	public HSResultFuture(LinkedList<HSResult> resultSet) {
		this.resultSet = resultSet;
		this.resultSetIterator = resultSet.iterator();
	}

	public LinkedList<HSResult> get() {
		return get(defaultTimeout, TimeUnit.MILLISECONDS);
	}

	public LinkedList<HSResult> get(long timeout) {
		return get(timeout, TimeUnit.MILLISECONDS);
	}

	public synchronized LinkedList<HSResult> get(long timeout, TimeUnit unit) {
		if (!TimeUnit.MILLISECONDS.equals(unit)) {
			timeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
		}

		while (!isReady && (cause == null)) {
			try {
				wait(timeout);
				
				if (cause == null) {
					cause = new TimeoutException("request reset by timeout");
				}
			} catch (InterruptedException e) {
				// ignore
			}
		}

		if (!isReady) {
			while (resultSetIterator.hasNext()) {
				resultSetIterator.next().setCause(cause);
			}

			isReady = true;
		}

		return resultSet;
	}

	public synchronized void addResult(List<ChannelBuffer> data) {
		if (!isReady) {
			resultSetIterator.next().setData(data);

			if (!resultSetIterator.hasNext()) {
				isReady = true;
				notifyAll();
			}
		}
	}

	public synchronized void setCause(Exception cause) {
		if (this.cause == null) {
			this.cause = cause;
			notifyAll();
		}
	}

	public synchronized boolean isReady() {
		return isReady;
	}
	
	public synchronized boolean isSuccess() {
		return isReady && (cause == null);
	}

	public synchronized Exception getCause() {
		return cause;
	}
}