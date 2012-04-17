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

import kom.handlersocket.core.HSProto;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.util.ArrayList;
import java.util.List;

public class HSDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		final int packetLength = getPacketLength(buffer);

		if (packetLength < 0) {
			buffer.skipBytes(buffer.readableBytes());
			return null;
		}

		final ChannelBuffer packet = buffer.readBytes(packetLength);
		buffer.skipBytes(1);

		return decodePacket(packet);
	}

	private int getPacketLength(ChannelBuffer buffer) {
		for (int i = buffer.writerIndex() - 1; i >= buffer.readerIndex(); --i) {
			if (buffer.getByte(i) == HSProto.PACKET_DELIMITER) {
				return i;
			}
		}

		return -1;
	}

	private List<List<ChannelBuffer>> decodePacket(ChannelBuffer buffer) {
		final List<List<ChannelBuffer>> result = new ArrayList<List<ChannelBuffer>>();
		List<ChannelBuffer> message = new ArrayList<ChannelBuffer>();

		byte symbol;
		boolean unsafe_byte = false;

		int prevIndex = 0;
		int curIndex = 0;
		final int endIndex = buffer.capacity() - 1;

		while (curIndex <= endIndex) {
			symbol = buffer.getByte(curIndex++);

			if (symbol == HSProto.TOKEN_DELIMITER) {
				message.add(copy(buffer, prevIndex, curIndex, unsafe_byte));
				prevIndex = curIndex;
				unsafe_byte = false;
			} else if (symbol == HSProto.PACKET_DELIMITER) {
				message.add(copy(buffer, prevIndex, curIndex, unsafe_byte));
				prevIndex = curIndex;
				unsafe_byte = false;

				result.add(message);
				message = new ArrayList<ChannelBuffer>();
			} else if (symbol == HSProto.UNSAFE_BYTE_MARKER) {
				unsafe_byte = true;
			} else if (curIndex > endIndex) {
				message.add(copy(buffer, prevIndex, curIndex + 1, unsafe_byte));
				result.add(message);
			}
		}

		return result;
	}

	private ChannelBuffer copy(final ChannelBuffer buffer, final int prevIndex, final int curIndex,
	                           final boolean unsafe) {
		byte symbol;
		boolean flag = false;

		if (unsafe) {
			final byte[] bytes = new byte[curIndex - prevIndex - 1];
			int count = 0;

			for (int i = prevIndex; i < curIndex - 1; ++i) {
				symbol = buffer.getByte(i);

				if (symbol == HSProto.UNSAFE_BYTE_MARKER) {
					flag = true;
					continue;
				}

				bytes[count++] = (flag ? (byte) (symbol ^ HSProto.UNSAFE_BYTE_MASK) : symbol);
				flag = false;
			}

			return ChannelBuffers.wrappedBuffer(bytes, 0, count);
		} else {
			return buffer.copy(prevIndex, curIndex - prevIndex - 1);
		}
	}
}