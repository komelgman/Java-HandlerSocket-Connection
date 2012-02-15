package kom.handlersocket.result;

import io.netty.buffer.ChannelBuffer;
import kom.handlersocket.query.HSQuery;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HSResult {
	private final HashMap<HSQuery, List<ChannelBuffer>> resultSet = new LinkedHashMap<HSQuery, List<ChannelBuffer>>();
	private final Charset charset;

	public HSResult(Charset charset) {
		this.charset = charset;
	}
	
	
	public void add(HSQuery query, List<ChannelBuffer> result) {
		resultSet.put(query, result);
	}

	public void debug() {
		for(Map.Entry<HSQuery, List<ChannelBuffer>> entry : resultSet.entrySet()) {
			System.out.print(entry.getKey().encode());

			for(ChannelBuffer buffer : entry.getValue()) {
				System.out.print(buffer.toString(charset));
				System.out.print("-");
			}

			System.out.println();
		}
	}
}
