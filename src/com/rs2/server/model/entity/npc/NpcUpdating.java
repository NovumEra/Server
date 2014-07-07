package com.rs2.server.model.entity.npc;

import com.rs2.server.model.entity.player.Player;
import com.rs2.server.net.StreamBuffer;

/**
 * Created by Cory Nelson
 * on 07/07/14.
 * at 13:46.
 */
public class NpcUpdating {

    public static void update(Player player) {
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
        StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(1024);

        // Initialize the update packet.
        out.writeVariableShortPacketHeader(player.getClient().getCipher(), 65);
        out.setAccessType(StreamBuffer.AccessType.BIT_ACCESS);

        // Update the NPCs in the local list.
        out.writeBits(8, 0);

        // Append the update block to the packet if need be.
        if (block.getBuffer().writerIndex() > 0) {
            out.writeBits(14, 16383);
            out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
            out.writeBytes(block.getBuffer());
        } else {
            out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
        }

        // Ship the packet out to the client.
        out.finishVariableShortPacketHeader();
        player.getClient().send(out.getBuffer());
    }
}
