package kom.handlersocket;

import io.netty.bootstrap.ClientBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioClientSocketChannelFactory;
import kom.handlersocket.netty.HSPipelineFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

public class HSConnectionFactory {
	protected final ArrayList<HSConnectionPoint> readWriteHosts = new ArrayList<HSConnectionPoint>();
	protected final ArrayList<HSConnectionPoint> allHosts = new ArrayList<HSConnectionPoint>();
	
	protected final ClientBootstrap bootstrap;
	protected final ChannelGroup channelGroup;
	protected int connectionTimeout = 10000;
	protected Charset charset = Charset.defaultCharset();

	public HSConnectionFactory(String host) {		
		this(new HSConnectionPoint(host));
	}
	
	public HSConnectionFactory(HSConnectionPoint point) {
		this(new ArrayList<HSConnectionPoint>(Arrays.asList(point)));
	}

	public HSConnectionFactory(ArrayList<HSConnectionPoint> connectionPoints) {
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
	 	
		if ( ! channelFuture.awaitUninterruptibly(connectionTimeout)) {
			// todo: throw TimeOutException
		}

		if ( ! channelFuture.isSuccess()) {
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
	
	protected HSConnectionPoint getRandomHost(ArrayList<HSConnectionPoint> collection) {
		return collection.get((int)(Math.random() * (collection.size() - 1)));
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