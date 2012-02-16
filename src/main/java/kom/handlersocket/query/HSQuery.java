package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;

import java.security.InvalidParameterException;

abstract public class HSQuery {
	
	protected HS.ResultType resultType;
	protected HSIndexDescriptor indexDescriptor;

	protected HSQuery(HS.ResultType resultType) {
		this.resultType = resultType;
	}

	public String encode(HSIndexDescriptor indexDescriptor){
		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		this.indexDescriptor = indexDescriptor;

		return encode();
	}

	abstract public String encode();
	
	public HS.ResultType resultType() {
		return resultType;
	}
}