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

import kom.handlersocket.core.SafeByteStream;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;
import static org.jboss.netty.channel.Channels.write;

@Sharable
public class HSEncoder implements ChannelDownstreamHandler {
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent evt) throws Exception {
		if (!(evt instanceof MessageEvent)) {
			ctx.sendDownstream(evt);
			return;
		}

		final MessageEvent e = (MessageEvent) evt;		
		final Object originalMessage = e.getMessage();
		
		if (originalMessage instanceof SafeByteStream) {
			write(ctx, e.getFuture(), wrappedBuffer(((SafeByteStream) originalMessage).toByteArray()), e.getRemoteAddress());
		} else {
			ctx.sendDownstream(evt);
		}
	}
}