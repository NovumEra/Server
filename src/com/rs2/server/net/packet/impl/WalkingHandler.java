package com.rs2.server.net.packet.impl;

import com.rs2.server.model.entity.player.Player;
import com.rs2.server.model.map.Position;
import com.rs2.server.net.StreamBuffer;
import com.rs2.server.net.packet.Packet;
import com.rs2.server.net.packet.PacketHandler;

import java.io.IOException;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 15:42.
 */
public class WalkingHandler implements PacketHandler {

    @Override
    public void handle(Player player, Packet packet) throws IOException {

        StreamBuffer.InBuffer in = packet.getBuffer();

        int length = packet.getLength();
        if (packet.getOpcode() == 248) {
            length -= 14;
        }
        int steps = (length - 5) / 2;
        int[][] path = new int[steps][2];
        int firstStepX = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
        for (int i = 0; i < steps; i++) {
            path[i][0] = in.readByte();
            path[i][1] = in.readByte();
        }
        int firstStepY = in.readShort(StreamBuffer.ByteOrder.LITTLE);

        player.getMovementHandler().reset();
        player.getMovementHandler().setRunPath(in.readByte(StreamBuffer.ValueType.C) == 1);
        player.getMovementHandler().addToPath(new Position(firstStepX, firstStepY));
        for (int i = 0; i < steps; i++) {
            path[i][0] += firstStepX;
            path[i][1] += firstStepY;
            player.getMovementHandler().addToPath(new Position(path[i][0], path[i][1]));
        }
        player.getMovementHandler().finish();
    }
}
