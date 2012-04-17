/**
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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * From HS protocol description
 *
 * ----------------------------------------------------------------------------
 * Opening index
 *
 * The 'open_index' request has the following syntax.
 *
 *    P <indexid> <dbname> <tablename> <indexname> <columns> [<fcolumns>]
 *
 * - <indexid> is a number in decimal.
 * - <dbname>, <tablename>, and <indexname> are strings. To open the primary
 *   key, use PRIMARY as <indexname>.
 * - <columns> is a comma-separated list of column names.
 * - <fcolumns> is a comma-separated list of column names. This parameter is
 *  optional.
 *
 * Once an 'open_index' request is issued, the HandlerSocket plugin opens the
 * specified index and keep it open until the client connection is closed. Each
 * open index is identified by <indexid>. If <indexid> is already open, the old
 * open index is closed. You can open the same combination of <dbname>
 * <tablename> <indexname> multple times, possibly with different <columns>.
 * For efficiency, keep <indexid> small as far as possible.
 * ----------------------------------------------------------------------------
 *
 * HSIndexDescriptor class contains information about index according to the
 * HS protocol description
 */

public class HSIndexDescriptor {
	/**
	 * The following fields are equivalent to the corresponding fields of the
	 * protocol description
	 */
	private final String indexId;
	private final String dbName;
	private final String tableName;
	private final String indexName;
	private final List<String> columns;
	private final List<String> filterColumns;

	/**
	 * This field ... todo
	 */
	private final List<String> indexColumns;

	private static AtomicLong generator = new AtomicLong(0L);

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
		
		if (columns == null || tableName == null || indexName == null || dbName == null) {
			throw new IllegalArgumentException();
		}

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
	
	public List<String> getFilterColumns() {
		return filterColumns;
	}
	

	
	
	public int getColumnPos(String name) {
		return columns.indexOf(name);
	}

	public boolean hasColumn(String name) {
		return columns.indexOf(name) != -1;
	}

	
	

	public int getFilterColumnPos(String name) {
		return hasFilterColumns() ? filterColumns.indexOf(name) : -1;
	}

	public boolean hasFilterColumn(String name) {
		return hasFilterColumns() && filterColumns.indexOf(name) != -1;
	}

	public boolean hasFilterColumns() {
		return filterColumns != null;
	}
	
	
	public int getIndexColumnPos(String name) {
		return indexColumns.indexOf(name);
	}

	public boolean hasIndexColumn(String name) {
		return filterColumns.indexOf(name) != -1;
	}
}