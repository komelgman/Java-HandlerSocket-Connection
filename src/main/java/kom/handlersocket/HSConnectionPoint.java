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

public class HSConnectionPoint {

	private final String host;
	private final HSConnectionMode supportedMode;
	private final int readOnlyPort;
	private final int readWritePort;

	public HSConnectionPoint(String host) {
		this(host, HSConnectionMode.READ_WRITE, 9998, 9999);
	}

	public HSConnectionPoint(String host, HSConnectionMode supportedMode) {
		this(host, supportedMode, 9998, 9999);
	}

	public HSConnectionPoint(String host, HSConnectionMode supportedMode, int readOnlyPort, int readWritePort) {
		this.host = host;
		this.supportedMode = supportedMode;
		this.readOnlyPort = readOnlyPort;
		this.readWritePort = readWritePort;
	}

	@Override
	public int hashCode() {
		return host.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (!(other instanceof HSConnectionPoint)) return false;

		HSConnectionPoint _other = (HSConnectionPoint) other;
		return this.host.equals(_other.getHost());
	}

	public String getHost() {
		return host;
	}

	public HSConnectionMode getSupportedMode() {
		return supportedMode;
	}

	public int getReadOnlyPort() {
		return readOnlyPort;
	}

	public int getReadWritePort() {
		return readWritePort;
	}

	public int getPort(HSConnectionMode mode) {
		if (HSConnectionMode.READ_ONLY == mode) {
			return readOnlyPort;
		} else {
			return readWritePort;
		}
	}
}
