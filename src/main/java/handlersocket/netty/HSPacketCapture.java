package kom.handlersocket.netty;

import io.netty.buffer.ChannelBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.frame.FrameDecoder;
import kom.handlersocket.HS;

import java.nio.charset.Charset;

public class HSPacketCapture extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		int packetLength = getPacketLength(buffer);

		if (packetLength < 0) {
			buffer.skipBytes(buffer.readableBytes());
			return null;
		}

		ChannelBuffer packet = buffer.readBytes(packetLength);
		buffer.skipBytes(1);

		return packet;
	}

	private int getPacketLength(ChannelBuffer buffer) {
		for(int i = buffer.writerIndex() - 1; i >= buffer.readerIndex(); --i) {
			if (buffer.getByte(i) == HS.PACKET_DELIMITER) {
				return i;
			}
		}

		return -1;
	}
}
