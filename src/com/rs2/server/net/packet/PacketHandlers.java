package com.rs2.server.net.packet;

import com.rs2.server.model.entity.player.Player;
import com.rs2.server.net.packet.impl.CommandHandler;
import com.rs2.server.net.packet.impl.WalkingHandler;

import java.io.IOException;

public class PacketHandlers {

	private static PacketHandler[] handlers = new PacketHandler[255];

	static {
		PacketHandler silent = new PacketHandler() {
			@Override
			public void handle(Player player, Packet packet) throws IOException {
				//ActionSender.sendLevelUpMessage(player, Skill.RUNE_CRAFTING, 99);
			}
		};

        handlers[0] = silent;
        handlers[241] = silent;
		handlers[103] = new CommandHandler();

        WalkingHandler walkingHandler = new WalkingHandler();
        handlers[98] = walkingHandler;
        handlers[164] = walkingHandler;
        handlers[248] = walkingHandler;
	}

	public static void handlePacket(final Player player, final Packet packet) throws IOException {
		if (handlers[packet.getOpcode()] != null) {
			handlers[packet.getOpcode()].handle(player, packet);
		} else {
			System.out.println(packet.getOpcode()+": not found.");
		}
	}
}
