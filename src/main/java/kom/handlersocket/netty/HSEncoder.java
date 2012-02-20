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

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.oneone.OneToOneEncoder;
import kom.handlersocket.core.SafeByteStream;

import java.nio.charset.Charset;

import static io.netty.buffer.ChannelBuffers.copiedBuffer;
import static io.netty.buffer.ChannelBuffers.wrappedBuffer;

@Sharable
public class HSEncoder extends OneToOneEncoder {

	private final Charset charset;

	public HSEncoder() {
		this(Charset.defaultCharset());
	}

	public HSEncoder(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("charset");
		}
		this.charset = charset;
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (msg instanceof SafeByteStream) {
			return wrappedBuffer(((SafeByteStream) msg).toByteArray());
		} else if (msg instanceof String) {
			// String message
			return copiedBuffer((String) msg, charset);
		}

		return msg;
	}
}