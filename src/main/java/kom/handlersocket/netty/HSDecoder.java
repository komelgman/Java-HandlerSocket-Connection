package kom.handlersocket.netty;

import io.netty.buffer.ChannelBuffer;
import io.netty.buffer.ChannelBuffers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.frame.FrameDecoder;
import kom.handlersocket.HS;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HSDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		int packetLength = getPacketLength(buffer);

		if (packetLength < 0) {
			buffer.skipBytes(buffer.readableBytes());
			return null;
		}

		ChannelBuffer packet = buffer.readBytes(packetLength);
		buffer.skipBytes(1);

		return decodePacket(packet);
	}

	private int getPacketLength(ChannelBuffer buffer) {
		for(int i = buffer.writerIndex() - 1; i >= buffer.readerIndex(); --i) {
			if (buffer.getByte(i) == HS.PACKET_DELIMITER) {
				return i;
			}
		}

		return -1;
	}

	protected List<List<ChannelBuffer>> decodePacket(ChannelBuffer buffer) {
		List<List<ChannelBuffer>> result = new ArrayList<List<ChannelBuffer>>();
		List<ChannelBuffer> message = new ArrayList<ChannelBuffer>();

		byte symbol;
		boolean unsafe_byte = false;

		int prevIndex = 0;
		int curIndex = 0;
		int endIndex = buffer.capacity() - 1;

		while (curIndex <= endIndex) {
			symbol = buffer.getByte(curIndex++);

			if (symbol == HS.TOKEN_DELIMITER) {
				message.add(copy(buffer, prevIndex, curIndex, unsafe_byte));
				prevIndex = curIndex;
				unsafe_byte = false;
			} else if (symbol == HS.PACKET_DELIMITER) {
				message.add(copy(buffer, prevIndex, curIndex, unsafe_byte));
				prevIndex = curIndex;
				unsafe_byte = false;

				result.add(message);
				message = new ArrayList<ChannelBuffer>();
			} else if (symbol == HS.UNSAFE_BYTE_MARKER) {
				unsafe_byte = true;
			} else if (curIndex > endIndex) {
				message.add(copy(buffer, prevIndex, curIndex + 1, unsafe_byte));
				result.add(message);
			}
		}

		return result;
	}

	private ChannelBuffer copy(final ChannelBuffer buffer, final int prevIndex, final int curIndex, final boolean unsafe) {
		byte symbol;
		boolean flag = false;

		if (unsafe) {
			final byte[] bytes = new byte[curIndex - prevIndex - 1];
			int count = 0;

			for(int i = prevIndex; i < curIndex - 1; ++i) {
				symbol = buffer.getByte(i);

				if (symbol == HS.UNSAFE_BYTE_MARKER) {
					flag = true;
					continue;
				}

				bytes[count++] = (flag ? (byte)(symbol ^ HS.UNSAFE_BYTE_MASK) : symbol);
				flag = false;
			}

			return ChannelBuffers.wrappedBuffer(bytes, 0, count);
		} else {
			return buffer.copy(prevIndex, curIndex - prevIndex - 1);
		}
	}
}
