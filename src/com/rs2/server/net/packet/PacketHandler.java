package com.rs2.server.net.packet;

import com.rs2.server.model.entity.player.Player;

import java.io.IOException;

public interface PacketHandler {
	public void handle(Player player, Packet packet) throws IOException;
}
