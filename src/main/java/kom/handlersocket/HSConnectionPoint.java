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
