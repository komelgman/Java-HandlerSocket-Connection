package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.util.ByteStream;
import kom.handlersocket.util.Util;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

public class HSInsertQuery extends HSQuery {

	protected List<String> values;

	public HSInsertQuery() {
		this(null);
	}

	public HSInsertQuery(List<String> values) {
		super(HS.ResultType.INSERT_OPERATION);
		this.values = values;
	}

	@Override
	public void encode(ByteStream output) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}
		
		try {
			output.writeString(indexDescriptor.getIndexId(), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeBytes(HS.OPERATOR_INSERT, false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(String.valueOf(values.size()), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeStrings(values, HS.TOKEN_DELIMITER_AS_BYTES, true);
			output.writeBytes(HS.PACKET_DELIMITER_AS_BYTES, false);
		} catch (IOException e) {
			System.err.print(e.getMessage());
		}
	}

	public HSInsertQuery values(List<String> values) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		this.values = values;

		return this;
	}
}