package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.Util;

import java.security.InvalidParameterException;
import java.util.List;

public class HSInsertQuery extends HSQuery {

	protected String values;
	protected int valuesCount = 0;
	

	public HSInsertQuery() {
		this(null);
	}

	public HSInsertQuery(List<String> values) {
		super(HS.ResultType.INSERT_OPERATION);
		
		if (values != null) {
			this.valuesCount = values.size();
			this.values = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(values));
		}
	}

	@Override
	public String encode() {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		StringBuilder result = new StringBuilder();

		result.append(indexDescriptor.getIndexId());
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(HS.OPERATOR_INSERT);
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(valuesCount);
		result.append(HS.TOKEN_DELIMITER_AS_STR);

		result.append(values);

		result.append(HS.PACKET_DELIMITER_AS_STR);

		return result.toString();
	}

	public HSInsertQuery values(List<String> values) {
		if (null == values) {
			throw new InvalidParameterException("values can't be null");
		}

		this.valuesCount = values.size();
		this.values = Util.implode(HS.TOKEN_DELIMITER_AS_STR, Util.safe(values));

		return this;
	}
}