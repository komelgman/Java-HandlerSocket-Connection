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
package kom.handlersocket.netty;

import kom.handlersocket.HSConnection;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a client-side channel.
 */
public class HSHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = Logger.getLogger(HSHandler.class.getName());
	private final HSConnection connection;

	public HSHandler(HSConnection connection) {
		this.connection = connection;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		connection.response((List<List<ChannelBuffer>>) e.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}