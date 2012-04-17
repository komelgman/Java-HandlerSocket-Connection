package example;

import kom.handlersocket.*;
import kom.handlersocket.query.*;
import kom.handlersocket.result.HSResult;
import kom.handlersocket.result.HSResultFuture;
import kom.handlersocket.query.CompareOperator;

import java.nio.charset.Charset;
import static java.util.Arrays.asList;
import java.util.LinkedList;

public class HSExample2 {
	public static void main(String[] args) throws Exception {
		HSConnectionFactory connectionFactory = new HSConnectionFactory("192.168.56.101");

		HSConnection connection = connectionFactory.connect(HSConnectionMode.READ_WRITE);
		HSIndexDescriptor indexDescriptor = new HSIndexDescriptor("test", "test", "PRIMARY", asList("v1", "v2"), asList("v1", "v2"));
		HSResultFuture resultFuture;
		LinkedList<HSResult> results;

		resultFuture = connection.addQueries(indexDescriptor,
							new HSOpenIndexQuery(),
							new HSUpdateQuery(CompareOperator.EQ, asList("5"), asList("русск8ий", "09")),
							new HSUpdateQuery(CompareOperator.EQ, asList("5"), asList("русский", "9")).returnData(true),
							new HSFindQuery(CompareOperator.EQ, asList("5")).limit(10)
						).execute();

		results = resultFuture.get();

		for (HSResult result : results) {
			result.debug();
		}

		connectionFactory.release();
	}
}