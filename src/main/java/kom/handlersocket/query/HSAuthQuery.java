package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.ByteStream;
import kom.handlersocket.util.Util;

import java.io.IOException;
import java.nio.charset.Charset;

public class HSAuthQuery extends HSQuery {

	private final String secret;
	private final byte[] type = new byte[] {'1'};

	public HSAuthQuery(String secret) {
		super(HS.ResultType.SIMPLE);
		this.secret = secret;
	}

	@Override
	public void encode(final ByteStream output) {
		try {
			output.writeBytes(HS.OPERATOR_AUTH, false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeBytes(this.type, false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(this.secret, true);
			output.writeBytes(HS.PACKET_DELIMITER_AS_BYTES, false);
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}

	@Override
	public void encode(HSIndexDescriptor indexDescriptor, final ByteStream output) {
		encode(output);
	}
}