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

package kom.handlersocket.query;

import kom.handlersocket.core.HSProto;

import java.util.Arrays;
import java.util.List;

public class HSUpdateQuery extends HSModQuery {
	public HSUpdateQuery() {
		this(CompareOperator.GE, Arrays.asList(""), null);
	}

	public HSUpdateQuery(CompareOperator operator, List<String> conditions, List<String> values) {
		super(HSProto.OPERATOR_UPDATE, HSProto.OPERATOR_GET_AND_UPDATE, operator, conditions, values);
	}
}