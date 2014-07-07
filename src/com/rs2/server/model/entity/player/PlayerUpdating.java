package com.rs2.server.model.entity.player;

import com.rs2.server.model.World;
import com.rs2.server.model.map.Position;
import com.rs2.server.net.StreamBuffer;
import com.rs2.server.util.Util;

import java.util.Iterator;

import static com.rs2.server.util.Util.*;

/**
 * Created by Cory Nelson
 * on 06/07/14.
 * at 13:49.
 */
public class PlayerUpdating {

    public static void update(Player player) {

        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(20240);
        StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(20000);

        out.writeVariableShortPacketHeader(player.getClient().getCipher(), 81);
        out.setAccessType(StreamBuffer.AccessType.BIT_ACCESS);

        updateLocalPlayerMovement(player, out);
        if (player.isUpdateRequired()) {
            updateState(player, block, false, true);
        }

        updateLocalPlayers(out, block, player);
        updateLocalPlayerList(out, block, player);

        if (block.getBuffer().writerIndex() > 0) {
            out.writeBits(11, 2047);
            out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
            out.writeBytes(block.getBuffer());
        } else {
            out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
        }

        out.finishVariableShortPacketHeader();
        player.getClient().send(out.getBuffer());
    }

    private static void updateLocalPlayers(StreamBuffer.OutBuffer out, StreamBuffer.OutBuffer block, Player player) {
        out.writeBits(8, player.getLocalPlayers().getEntityCount());

        Iterator<Player> it = player.getLocalPlayers().iterator();
        while(it.hasNext()) {
            Player p = it.next();
            if (p.getPosition().isViewableFrom(player.getPosition())) {
                updatePlayerMovement(p, out);
                if (p.isUpdateRequired()) {
                    updateState(p, block, false, false);
                }
            } else {
                out.writeBit(true);
                out.writeBits(2, 3);
                it.remove();
            }
        }
    }

    private static void updateLocalPlayerList(StreamBuffer.OutBuffer out, StreamBuffer.OutBuffer block, Player player) {
        for (int i = 0; i < World.getPlayers().getCapacity(); i++) {
            if (player.getLocalPlayers().getEntityCount() >= 255) {
                break;
            }
            Player other = World.getPlayers().getEntity(i);
            if (other == null || other == player) {
                continue;
            }
            if (!player.getLocalPlayers().contains(i) && other.getPosition().isViewableFrom(player.getPosition())) {
                player.getLocalPlayers().addEntity(i, other);
                addPlayer(out, player, other);
                updateState(other, block, true, false);
            }
        }
    }

    private static void updateState(Player player, StreamBuffer.OutBuffer block, boolean forceAppearance, boolean noChat) {
        int mask = 0x0;

        if (player.getUpdateFlags().needsUpdating(PLAYER_CHAT_UPDATE_MASK) && !noChat) {
            mask |= PLAYER_CHAT_UPDATE_MASK;
        }

        if (player.getUpdateFlags().needsUpdating(PLAYER_APPEARANCE_UPDATE_MASK) || forceAppearance) {
            mask |= PLAYER_APPEARANCE_UPDATE_MASK;
        }

        if (mask >= 0x100) {
            mask |= 0x40;
            block.writeShort(mask, StreamBuffer.ByteOrder.LITTLE);
        } else {
            block.writeByte(mask);
        }

        if (player.getUpdateFlags().needsUpdating(PLAYER_CHAT_UPDATE_MASK) && !noChat) {
            appendChat(player, block);
        }

        if (player.getUpdateFlags().needsUpdating(PLAYER_APPEARANCE_UPDATE_MASK) || forceAppearance) {
            appendAppearance(player, block);
        }
    }

    private static void updateLocalPlayerMovement(Player player, StreamBuffer.OutBuffer out) {
        boolean updateRequired = player.isUpdateRequired();
        if (player.isNeedsPlacement()) {
            out.writeBit(true);
            int posX = player.getPosition().getLocalX(player.getCurrentRegion());
            int posY = player.getPosition().getLocalY(player.getCurrentRegion());
            appendPlacement(out, posX, posY, player.getPosition().getZ(), player.isResetMovementQueue(), updateRequired);
            player.setNeedsPlacement(false);
        } else {
            int pDir = player.getPrimaryDirection();
            int sDir = player.getSecondaryDirection();
            if (pDir != -1) {
                out.writeBit(true);
                if (sDir != -1) {
                    append(out, 2, pDir, sDir, updateRequired);
                } else {
                    append(out, 1, pDir, -1, updateRequired);
                }
            } else {
                if (updateRequired) {
                    out.writeBit(true);
                    append(out, 0, -1, -1, false);
                } else {
                    out.writeBit(false);
                }
            }
        }
    }

    private static void updatePlayerMovement(Player player, StreamBuffer.OutBuffer out) {
        boolean updateRequired = player.isUpdateRequired();
        int pDir = player.getPrimaryDirection();
        int sDir = player.getSecondaryDirection();

        if (pDir != -1) {
            out.writeBit(true);
            if (sDir != -1) {
                append(out, 2, pDir, sDir, updateRequired);
            } else {
                append(out, 1, pDir, -1, updateRequired);
            }
        } else {
            if (updateRequired) {
                out.writeBit(true);
                append(out, 0, -1, -1, false);
            } else {
                out.writeBit(false);
            }
        }
    }

    private static void appendPlacement(StreamBuffer.OutBuffer out, int posX, int posY, int z, boolean resetMovementQueue, boolean updateRequired) {
        out.writeBits(2, 3);

        out.writeBits(2, z);
        out.writeBit(resetMovementQueue);
        out.writeBit(updateRequired);
        out.writeBits(7, posY);
        out.writeBits(7, posX);
    }

    public static void append(StreamBuffer.OutBuffer out, int type, int direction, int direction2, boolean attributesUpdate) {
        out.writeBits(2, type);
        if(type > 0)
            out.writeBits(3, direction);
        if(type > 1)
            out.writeBits(3, direction2);
        if(type > 0)
            out.writeBit(attributesUpdate);
    }

    private static void addPlayer(StreamBuffer.OutBuffer out, Player player, Player other) {
        out.writeBits(11, other.getIndex());
        out.writeBit(true);
        out.writeBit(true);

        Position delta = player.getPosition().delta(other.getPosition());
        out.writeBits(5, delta.getY());
        out.writeBits(5, delta.getX());
    }

    private static void appendChat(Player player, StreamBuffer.OutBuffer out) {
        out.writeShort(((player.getUpdateFlags().getChatColor() & 0xff) << 8) + (player.getUpdateFlags().getChatEffects() & 0xff), StreamBuffer.ByteOrder.LITTLE);
        out.writeByte(player.getAbilities().getPrimaryAbility().ordinal());
        out.writeByte(player.getUpdateFlags().getChatText().length, StreamBuffer.ValueType.C);
        out.writeBytesReverse(player.getUpdateFlags().getChatText());
    }

    private static void appendAppearance(Player player, StreamBuffer.OutBuffer out) {
        StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(128);

        block.writeByte(0); // Gender
        block.writeByte(0); // Skull icon

        block.writeByte(0);

        block.writeByte(0);

        block.writeByte(0);

        block.writeByte(0);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_CHEST]);

        block.writeByte(0);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_ARMS]);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_LEGS]);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_HEAD]);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_HANDS]);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_FEET]);

        block.writeShort(0x100 + player.getAppearance()[Util.APPEARANCE_SLOT_BEARD]);

        block.writeByte(player.getColors()[0]);
        block.writeByte(player.getColors()[1]);
        block.writeByte(player.getColors()[2]);
        block.writeByte(player.getColors()[3]);
        block.writeByte(player.getColors()[4]);

        block.writeShort(0x328); // stand
        block.writeShort(0x337); // stand turn
        block.writeShort(0x333); // walk
        block.writeShort(0x334); // turn 180
        block.writeShort(0x335); // turn 90 cw
        block.writeShort(0x336); // turn 90 ccw
        block.writeShort(0x338); // run

        block.writeLong(Util.nameToLong(player.getClient().getUsername()));
        block.writeByte(3); // Combat level.
        block.writeShort(0); // Total level.

        // Append the block length and the block to the packet.
        out.writeByte(block.getBuffer().writerIndex(), StreamBuffer.ValueType.C);
        out.writeBytes(block.getBuffer());
    }
}
