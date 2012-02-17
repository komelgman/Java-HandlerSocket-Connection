package kom.handlersocket.query;

import kom.handlersocket.HS;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.util.ByteStream;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;

abstract public class HSQuery {
	
	protected HS.ResultType resultType;
	protected HSIndexDescriptor indexDescriptor;

	protected HSQuery(HS.ResultType resultType) {
		this.resultType = resultType;
	}

	public void encode(final HSIndexDescriptor indexDescriptor, final ByteStream output){
		if (null == indexDescriptor) {
			throw new InvalidParameterException("indexDescriptor can't be null");
		}

		this.indexDescriptor = indexDescriptor;

		encode(output);
	}

	abstract public void encode(final ByteStream output);
	
	public HS.ResultType resultType() {
		return resultType;
	}
}