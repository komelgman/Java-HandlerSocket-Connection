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

package kom.handlersocket.core;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class SafeByteStream {
	private final int maximumBufferIncrement;
	private final int initialBufferSize;
	private final Charset charset;

	private int cursor;

	private byte[] buffer;
	private byte[] unsafeBuffer;

	public SafeByteStream(final int bufferSize, final int maximumBufferIncrement, Charset charset) {
		this.maximumBufferIncrement = maximumBufferIncrement;
		this.initialBufferSize = bufferSize;
		this.charset = charset;

		this.buffer = new byte[bufferSize];
		this.unsafeBuffer = new byte[2];
		this.unsafeBuffer[0] = HSProto.UNSAFE_BYTE_MARKER;
	}

	public synchronized void writeByte(byte b, boolean safe) {
		if (safe && /*unsigned*/ (0xFF & b) < 0x10) {
			unsafeBuffer[1] = (byte) (b ^ HSProto.UNSAFE_BYTE_MASK);
			_write(unsafeBuffer, 0, 2, false);
		} else {
			unsafeBuffer[1] = b;
			_write(unsafeBuffer, 1, 1, false);
		}
	}

	public synchronized void writeBytes(final byte[] b, boolean safe) throws IOException {
		_write(b, 0, b.length, safe);
	}

	public synchronized void writeBytes(final byte[] b, final int offset, final int length, boolean safe)
			throws IOException {
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

			for (int i = offset; i < offset + length; ++i) {
				byte b = bytes[i];

				if (/*unsigned*/ (b & 0xFF) < 0x10) {
					buffer[cursor++] = HSProto.UNSAFE_BYTE_MARKER;
					buffer[cursor++] = (byte) (b ^ HSProto.UNSAFE_BYTE_MASK);
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
		final int computedSize = (int) Math.min((this.buffer.length + 1) * 1.5, this.buffer.length
				+ maximumBufferIncrement);
		final int newSize = Math.max(size, computedSize);
		final byte[] newBuffer = new byte[newSize];

		System.arraycopy(this.buffer, 0, newBuffer, 0, cursor);

		this.buffer = newBuffer;
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