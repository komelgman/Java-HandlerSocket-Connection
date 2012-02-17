package kom.handlersocket;

import kom.handlersocket.util.Util;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class HSIndexDescriptor {
	protected final String indexId;
	protected final String dbName;
	protected final String tableName;
	protected final String indexName;
	protected final List<String> columns;
	protected final List<String> filterColumns;
	protected final List<String> indexColumns;

	protected static AtomicLong generator = new AtomicLong(0L);

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, null, null);
	}

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns, List<String> filterColumns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, filterColumns, null);
	}

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns, List<String> filterColumns, List<String> indexColumns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, filterColumns, indexColumns);
	}

	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns) {
		this(indexId, dbName, tableName, indexName, columns, null, null);
	}

	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns, List<String> filterColumns) {
		this(indexId, dbName, tableName, indexName, columns, filterColumns, null);
	}
	
	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns, List<String> filterColumns, List<String> indexColumns) {
		this.indexId = Long.toString(indexId);

		this.dbName = dbName;
		this.tableName = tableName;
		this.indexName = indexName;

		this.columns = columns;
		this.filterColumns = filterColumns;
		this.indexColumns = indexColumns;
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

	public boolean isFilterColumns() {
		return filterColumns != null;
	}
}