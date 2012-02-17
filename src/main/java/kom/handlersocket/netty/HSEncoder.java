package kom.handlersocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.oneone.OneToOneEncoder;
import kom.handlersocket.HS;
import kom.handlersocket.util.ByteStream;
import kom.handlersocket.util.Util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
	    if (msg instanceof ByteStream) {
		    return wrappedBuffer(((ByteStream) msg).toByteArray());
	    } else if (msg instanceof String) {
		    // String message
		    return copiedBuffer((String) msg, charset);
	    }

	    return msg;
    }
}