package kom.handlersocket;

import kom.handlersocket.util.Util;

import java.util.List;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class HSIndexDescriptor {
	protected final String indexId;
	protected final String dbName;
	protected final String tableName;
	protected final String indexName;
	protected final List<String> columns;
	protected final List<String> filterColumns;

	private static AtomicLong generator = new AtomicLong(0L);
	private String columnsImploded;
	private String filterColumnsImploded;

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, null);
	}

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns, List<String> filterColumns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, filterColumns);
	}

	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns) {
		this(indexId, dbName, tableName, indexName, columns, null);
	}
	
	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns, List<String> filterColumns) {
		this.indexId = Long.toString(indexId);

		this.dbName = Util.safe(dbName);
		this.tableName = Util.safe(tableName);
		this.indexName = Util.safe(indexName);

		this.columns = columns;
		this.columnsImploded = Util.safe(Util.implode(",", columns));
		this.filterColumns = filterColumns;

		if (isFilterColumns()) {
			this.filterColumnsImploded = Util.safe(Util.implode(",", filterColumns));
		}
	}



	public String getIndexId() {
		return indexId;
	}

	public String getDbName() {
		return dbName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getIndexName() {
		return indexName;
	}

	public List<String> getColumns() {
		return columns;
	}
	
	public int getColumnIndex(String name) {
		return columns.indexOf(name);
	}

	public List<String> getFilterColumns() {
		return filterColumns;
	}

	public int getFilterColumnIndex(String name) {
		return filterColumns.indexOf(name);
	}

	public String getColumnsAsString() {
		return columnsImploded;
	}

	public String getFilterColumnsAsString() {
		return filterColumnsImploded;
	}
	
	public boolean isFilterColumns() {
		return filterColumns != null;
	}
}