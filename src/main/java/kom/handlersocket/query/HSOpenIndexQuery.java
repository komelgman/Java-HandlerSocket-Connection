package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.ByteStream;

import java.io.IOException;
import java.security.InvalidParameterException;

public class HSOpenIndexQuery extends HSQuery {

	public HSOpenIndexQuery() {
		this(null);
	}

	public HSOpenIndexQuery(HSIndexDescriptor indexDescriptor) {
		super(HS.ResultType.SIMPLE);
		this.indexDescriptor = indexDescriptor;
	}

	@Override
	public void encode(ByteStream output) {
		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		try {
			output.writeBytes(HS.OPERATOR_OPEN_INDEX, false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(indexDescriptor.getIndexId(), false);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(indexDescriptor.getDbName(), true);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(indexDescriptor.getTableName(), true);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeString(indexDescriptor.getIndexName(), true);
			output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
			output.writeStrings(indexDescriptor.getColumns(), new byte[]{','}, true);

			if (indexDescriptor.isFilterColumns()) {
				output.writeBytes(HS.TOKEN_DELIMITER_AS_BYTES, false);
				output.writeStrings(indexDescriptor.getFilterColumns(), new byte[]{','}, true);
			}

			output.writeBytes(HS.PACKET_DELIMITER_AS_BYTES, false);

		} catch (IOException e) {
			System.err.print(e.getMessage());
		}

	}
}