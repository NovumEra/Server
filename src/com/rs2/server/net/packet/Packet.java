package com.rs2.server.net.packet;

import com.rs2.server.net.StreamBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class Packet {

	private final StreamBuffer.InBuffer buffer;
	private final int opcode, length;

	public Packet(final int opcode, final int length) {
		this(opcode, StreamBuffer.newInBuffer(ChannelBuffers.buffer(length)));
	}

	public Packet(int opcode, StreamBuffer.InBuffer payload) {
		this.buffer = payload;
		this.opcode = opcode;
		this.length = payload.getBuffer().capacity();
	}

	public String getString() {
		StringBuilder builder = new StringBuilder();
		int data;
		while ((data = buffer.readByte()) != ((byte) '\n')) {
			builder.append((char) data);
		}
		return builder.toString();
	}

	public StreamBuffer.InBuffer getBuffer() {
		return buffer;
	}

	public int getOpcode() {
		return opcode;
	}

	public int getLength() {
		return length;
	}
}
