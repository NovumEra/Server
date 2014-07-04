package com.rs2.server.net.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class Packet {

	private final ChannelBuffer buffer;
	private final int opcode, length;

	public Packet(final int opcode, final int length) {
		this(opcode, ChannelBuffers.buffer(length));
	}

	public Packet(int opcode, ChannelBuffer payload) {
		this.buffer = payload;
		this.opcode = opcode;
		this.length = payload.capacity();
	}

	public byte get() {
		return buffer.readByte();
	}

	public void get(byte[] data) {
		buffer.readBytes(data);
	}

	public void get(byte[] data, int offset, int length) {
		buffer.readBytes(data, offset, length);
	}

	public char getChar() {
		return buffer.readChar();
	}

	public short getShort() {
		return buffer.readShort();
	}

	public int getInt() {
		return buffer.readInt();
	}

	public long getLong() {
		return buffer.readLong();
	}

	public float getFloat() {
		return buffer.readFloat();
	}

	public double getDouble() {
		return buffer.readDouble();
	}

	public String getString() {
		StringBuilder builder = new StringBuilder();
		byte data;
		while ((data = buffer.readByte()) != ((byte) '\n')) {
			builder.append((char) data);
		}
		return builder.toString();
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public int getOpcode() {
		return opcode;
	}

	public int getLength() {
		return length;
	}
}
