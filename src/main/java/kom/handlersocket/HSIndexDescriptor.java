/*
 * Copyright 2012 The Java HandlerSocket Connection Project
 *
 * https://github.com/komelgman/Java-HandlerSocket-Connection/
 *
 * The Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package kom.handlersocket;

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

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns,
	                         List<String> filterColumns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, filterColumns, null);
	}

	public HSIndexDescriptor(String dbName, String tableName, String indexName, List<String> columns,
	                         List<String> filterColumns, List<String> indexColumns) {
		this(generator.incrementAndGet(), dbName, tableName, indexName, columns, filterColumns, indexColumns);
	}

	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns) {
		this(indexId, dbName, tableName, indexName, columns, null, null);
	}

	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns,
	                         List<String> filterColumns) {
		this(indexId, dbName, tableName, indexName, columns, filterColumns, null);
	}

	public HSIndexDescriptor(long indexId, String dbName, String tableName, String indexName, List<String> columns,
	                         List<String> filterColumns, List<String> indexColumns) {
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