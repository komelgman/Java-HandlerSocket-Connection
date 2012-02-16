package example;

import kom.handlersocket.HSConnection;
import kom.handlersocket.HSConnectionFactory;
import kom.handlersocket.HSConnectionMode;
import kom.handlersocket.HSIndexDescriptor;
import kom.handlersocket.query.*;
import kom.handlersocket.result.HSResult;
import kom.handlersocket.result.HSResultFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HSExample3 {
	public static void main(String[] args) throws Exception {
		HSConnectionFactory connectionFactory = new HSConnectionFactory("192.168.56.101");

		HSConnection connection = connectionFactory.connect(HSConnectionMode.READ_WRITE);
		HSIndexDescriptor indexDescriptor = new HSIndexDescriptor("test", "test", "PRIMARY", Arrays.asList("v1", "v2"), Arrays.asList("v1", "v2"));
		HSIndexDescriptor indexDescriptor1 = new HSIndexDescriptor("test", "test", "PRIMARY", Arrays.asList("v1", "v2"));
		HSResultFuture resultFuture;
		HSResult result;
		
		resultFuture = connection.execute(indexDescriptor1, new HSOpenIndexQuery());
		result = resultFuture.get();
		result.debug();

		long start = System.currentTimeMillis();
		System.out.println("start");
		HSFindQuery findQuery = new HSFindQuery();

		for (int i = 0; i < 2000; ++i) {
			resultFuture = connection.execute(indexDescriptor1, findQuery.where(CompareOperator.EQ, Arrays.asList(String.valueOf(i))));
			result = resultFuture.get();
			//result.debug();
		}

		System.out.println("end: " + (System.currentTimeMillis() - start));


		start = System.currentTimeMillis();
		System.out.println("start");

		List<HSQuery> queries = new ArrayList<HSQuery>();
		for (int i = 0; i < 2000; ++i) {
			queries.add(new HSFindQuery(CompareOperator.EQ, Arrays.asList(String.valueOf(i))));
		}

		resultFuture = connection.execute(indexDescriptor1, queries);
		result = resultFuture.get();
		//result.debug();

		System.out.println("end: " + (System.currentTimeMillis() - start));


		
		
		resultFuture = connection.execute(
				indexDescriptor,
				Arrays.asList(
						(HSQuery) new HSOpenIndexQuery(),
						(HSQuery) new HSFindQuery().in(0, Arrays.asList("1", "2")).limit(10),
//						(HSQuery) new HSInsertQuery(Arrays.asList("3 v1 value", "3 v2 text")),
//						(HSQuery) new HSInsertQuery().values(Arrays.asList("4 v1 value", "4 v2 text"))
						(HSQuery) new HSDeleteQuery(CompareOperator.EQ, Arrays.asList("3"), true),
						(HSQuery) new HSUpdateQuery().filter("v1", CompareOperator.EQ, "1 v1 value").values(Arrays.asList("1 v1 value updated", "1 v2 text updated")),
						(HSQuery) new HSFindQuery().limit(10)
				));

		result = resultFuture.get();
		result.debug();
		

		connectionFactory.release();
	}
}