package com.rs2.server.net.packet.impl;


import com.rs2.server.model.content.command.Commands;
import com.rs2.server.model.entity.player.Player;
import com.rs2.server.net.packet.Packet;
import com.rs2.server.net.packet.PacketHandler;

import java.io.IOException;

public class CommandHandler implements PacketHandler {
	@Override
	public void handle(Player player, Packet packet) throws IOException {
		Commands.execute(packet.getString(), player);
	}
}
