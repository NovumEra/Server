package com.rs2.server.model.content.command;

import com.rs2.server.model.entity.player.Abilities;
import com.rs2.server.model.entity.player.Player;

public abstract class Command {

	protected final String command;
	protected final Abilities.Ability[] abilities;

	public Command(String command, Abilities.Ability... abilities) {
		this.command = command;
		this.abilities = abilities;
	}

	protected boolean hasRequiredRank(Player player) {
		return player.getAbilities().hasAbility(abilities);
	}

	public boolean canExecute(Player player) {
		return hasRequiredRank(player);
	}

	public abstract boolean execute(Player player, String[] arguments) throws Exception;

	public String getCommand() {
		return command;
	}
}
