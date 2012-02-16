package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;

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
	public String encode() {
		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		StringBuilder result = new StringBuilder();

		result.append(HS.OPERATOR_OPEN_INDEX);
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(indexDescriptor.getIndexId());
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(indexDescriptor.getDbName());
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(indexDescriptor.getTableName());
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(indexDescriptor.getIndexName());
		result.append(HS.TOKEN_DELIMITER_AS_STR);
		result.append(indexDescriptor.getColumnsAsString());
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		if (indexDescriptor.isFilterColumns()) {
			result.append(indexDescriptor.getFilterColumnsAsString());
			result.append(HS.TOKEN_DELIMITER_AS_STR);
		}

		result.append(HS.PACKET_DELIMITER_AS_STR);

		return result.toString();
	}
}