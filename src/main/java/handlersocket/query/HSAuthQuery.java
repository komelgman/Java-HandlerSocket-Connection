package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.Util;

public class HSAuthQuery extends HSQuery {

	private final String secret;
	private final String type = "1";

	public HSAuthQuery(String secret) {
		super(HS.ResultType.SIMPLE);

		this.secret = Util.safe(secret);
	}

	@Override
	public String encode() {
		StringBuilder result = new StringBuilder();

		result.append(HS.OPERATOR_AUTH);
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(this.type);
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(this.secret);
		result.append(HS.PACKET_DELIMITER_AS_STR);

		return result.toString();
	}

	@Override
	public String encode(HSIndexDescriptor indexDescriptor) {
		return encode();
	}
}