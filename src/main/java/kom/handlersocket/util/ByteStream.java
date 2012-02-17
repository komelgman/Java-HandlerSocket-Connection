package kom.handlersocket.util;

import kom.handlersocket.HS;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class ByteStream {
	private final int maximumBufferIncrement;
	private final int initialBufferSize;
	private final Charset charset;

	private int cursor;

	private byte[] buffer;
	private byte[] unsafeBuffer;
	private byte[] smallBuffer;

	public ByteStream(final int bufferSize, final int maximumBufferIncrement, Charset charset) {
		this.maximumBufferIncrement = maximumBufferIncrement;
		this.initialBufferSize = bufferSize;
		this.charset = charset;

		this.buffer = new byte[bufferSize];
		this.unsafeBuffer = new byte[2];
		this.unsafeBuffer[0] = HS.UNSAFE_BYTE_MARKER;
		this.smallBuffer = new byte[8];
	}

	public synchronized void writeByte(byte b, boolean safe) {
		unsafeBuffer[1] = (byte) (0xFF & b);

		if (safe && unsafeBuffer[1] < 0x10) {
			unsafeBuffer[1] ^= HS.UNSAFE_BYTE_MASK;
			_write(unsafeBuffer, 0, 2, false);
		} else {
			_write(unsafeBuffer, 1, 1, false);
		}
	}

	public synchronized void writeBytes(final byte[] b, boolean safe) throws IOException {
		_write(b, 0, b.length, safe);
	}

	public synchronized void writeBytes(final byte[] b, final int offset, final int length, boolean safe) throws IOException {
		_write(b, offset, length, safe);
	}
	
	public synchronized void writeString(String str, boolean safe) throws IOException {
		final byte[] b = str.getBytes(charset);
		_write(b, 0, b.length, safe);
	}
	
	public synchronized void writeStrings(List<String> strings, byte[] delimiter, boolean safe) {
		int count = strings.size();
		byte[] b;

		for (String str : strings) {
			b = str.getBytes(charset);
			_write(b, 0, b.length, safe);
			if (--count > 0) {
				_write(delimiter, 0, delimiter.length, false);
			}
		}
	}

//	public synchronized void writeInt(int b, boolean safe) {
//		smallBuffer[0] = (byte)( b >> 24 );
//		smallBuffer[1] = (byte)( b >> 16 );
//		smallBuffer[2] = (byte)( b >> 8 );
//		smallBuffer[3] = (byte)( b >> 0 );
//
//		_write(smallBuffer, 0, 4, safe);
//	}


	private void _write(final byte[] bytes, final int offset, final int length, boolean safe) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		if (offset < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (bytes == null) {
			throw new NullPointerException();
		}
		if ((length + offset) > bytes.length) {
			throw new IndexOutOfBoundsException();
		}

		if (safe) {
			if (cursor + 2 * length > this.buffer.length) {
				ensureSize(cursor + 2 * length);
			}

			for(int i = offset; i < offset + length; ++i) {
				byte b = bytes[i];

				if (/*unsigned*/ (b & 0xFF) < 0x10) {
					buffer[cursor++] = HS.UNSAFE_BYTE_MARKER;
					buffer[cursor++] = (byte) (b ^ HS.UNSAFE_BYTE_MASK);
				} else {
					buffer[cursor++] = b;
				}
			}
		} else {
			if (cursor + length > this.buffer.length) {
				ensureSize(cursor + length);
			}

			System.arraycopy(bytes, offset, this.buffer, cursor, length);
			cursor += length;
		}
	}

	public synchronized void reset() {
		this.buffer = new byte[initialBufferSize];
		cursor = 0;
	}

	private void ensureSize(final int size) {
		final int computedSize = (int)Math.min((this.buffer.length + 1) * 1.5, this.buffer.length + maximumBufferIncrement);
		final int newSize = Math.max(size, computedSize);
		final byte[] newBuffer = new byte[newSize];

		System.arraycopy(this.buffer, 0, newBuffer, 0, cursor);

		this.buffer = newBuffer;
	}

	public void flush() throws IOException {
		if ((buffer.length - cursor) > 50000) {
			System.out.println("WASTED: " + (buffer.length - cursor));
		}
	}

	public void close() throws IOException {
	}

	public synchronized byte[] toByteArray() {
		final byte[] result = new byte[cursor];
		System.arraycopy(buffer, 0, result, 0, cursor);
		return result;
	}

	public int getLength() {
		return cursor;
	}

	public byte[] getRaw() {
		if ((buffer.length - cursor) > 50000) {
			System.out.println("WASTED: " + (buffer.length - cursor) + " Length: " + buffer.length);
		}
		
		return buffer;
	}
}