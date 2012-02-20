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

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import kom.handlersocket.netty.HSPipelineFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class HSConnectionFactory {
	protected final List<HSConnectionPoint> readWriteHosts = new ArrayList<HSConnectionPoint>();
	protected final List<HSConnectionPoint> allHosts = new ArrayList<HSConnectionPoint>();

	protected final ClientBootstrap bootstrap;
	protected final ChannelGroup channelGroup;
	protected int connectionTimeout = 10000;
	protected Charset charset = Charset.defaultCharset();

	public HSConnectionFactory(String host) {
		this(new HSConnectionPoint(host));
	}

	public HSConnectionFactory(HSConnectionPoint point) {
		this(Arrays.asList(point));
	}

	public HSConnectionFactory(List<HSConnectionPoint> connectionPoints) {
		for (HSConnectionPoint point : connectionPoints) {
			addConnectionPoint(point);
		}

		channelGroup = new DefaultChannelGroup(this.getClass().getName());

		bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
	}

	public synchronized void addConnectionPoint(HSConnectionPoint point) {
		if (point.getSupportedMode() == HSConnectionMode.READ_WRITE) {
			readWriteHosts.add(point);
		}

		allHosts.add(point);
	}

	public synchronized void removeConnectionPoint(HSConnectionPoint point) {
		readWriteHosts.remove(point);
		allHosts.remove(point);
	}

	public synchronized HSConnection connect(HSConnectionMode mode) {
		HSConnection connection = new HSConnection(charset);

		bootstrap.setPipelineFactory(new HSPipelineFactory(connection));

		Channel channel = getChannel(mode);
		channelGroup.add(channel);
		connection.setChannel(channel);

		return connection;
	}

	public synchronized ChannelGroupFuture release() {
		ChannelGroupFuture result = channelGroup.close().awaitUninterruptibly();
		bootstrap.releaseExternalResources();

		return result;
	}

	private Channel getChannel(HSConnectionMode mode) {
		HSConnectionPoint connectionPoint = getConnectionPoint(mode);
		ChannelFuture channelFuture = bootstrap.connect(
				new InetSocketAddress(
						connectionPoint.getHost(),
						connectionPoint.getPort(mode)));

		if (!channelFuture.awaitUninterruptibly(connectionTimeout)) {
			// todo: throw TimeOutException
		}

		if (!channelFuture.isSuccess()) {
			// todo: throw 'connection error'
		}

		return channelFuture.getChannel();
	}

	// simple load balancing
	protected HSConnectionPoint getConnectionPoint(HSConnectionMode mode) {
		if (HSConnectionMode.READ_ONLY == mode) {
			return getRandomHost(allHosts);
		} else {
			return getRandomHost(readWriteHosts);
		}
	}

	protected HSConnectionPoint getRandomHost(List<HSConnectionPoint> collection) {
		return collection.get((int) (Math.random() * (collection.size() - 1)));
	}

	public void setConnectionTimeout(int timeout) {
		connectionTimeout = timeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public Charset getCharset() {
		return charset;
	}
}