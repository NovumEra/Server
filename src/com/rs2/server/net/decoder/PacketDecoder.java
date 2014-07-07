package com.rs2.server.net.decoder;

import com.rs2.server.net.StreamBuffer;
import com.rs2.server.net.packet.Packet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.security.SecureRandom;

/**
 * Created by Cory Nelson
 * on 04/07/14.
 * at 16:31.
 */
public class PacketDecoder extends FrameDecoder {

    private final SecureRandom cipher;
    private int opcode;
    private int length;

    public PacketDecoder(SecureRandom cipher) {
        this.cipher = cipher;
        this.opcode = -1;
        this.length = -1;
    }

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buffer) throws Exception {
        if (opcode == -1) {
            if (buffer.readableBytes() >= 1) {
                opcode = buffer.readByte() & 0xFF;
                opcode = (opcode - cipher.nextInt()) & 0xFF;
                length = packetLengths[opcode];
            } else {
                return null;
            }
            if (length == -1) {
                if (buffer.readableBytes() >= 1) {
                    length = buffer.readByte() & 0xFF;
                } else {
                    return null;
                }
            }
            if (buffer.readableBytes() >= length) {
                byte[] data = new byte[length];
                buffer.readBytes(data);
                try {
                    ChannelBuffer payload = ChannelBuffers.buffer(length);
                    payload.writeBytes(data);
                    return new Packet(opcode, StreamBuffer.newInBuffer(payload));
                } finally {
                    opcode = -1;
                    length = -1;
                }
            }
        }
        return null;
    }

    public static final int packetLengths[] = {
            0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
            0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
            0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
            2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
            0, 0, 0, 12, 0, 0, 0, 0, 8, 0, // 50
            0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
            6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
            0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
            0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
            0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
            0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
            1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
            0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
            0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
            0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
            0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
            0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
            0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
            2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
            4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
            0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
            1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
            0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
            0, 0, 6, 6, 0, 0, 0 // 250
    };
}
