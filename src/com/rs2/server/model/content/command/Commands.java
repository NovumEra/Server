package com.rs2.server.model.content.command;

import com.rs2.server.model.entity.player.Player;

import java.util.HashMap;

public class Commands {

	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	static {
		submit(new Command("test") {
			@Override
			public boolean execute(Player player, String[] arguments) throws Exception {
				return true;
			}
		});
	}

	public static void submit(Command... commands) {
		for(Command cmd : commands) {
			Commands.commands.put(cmd.getCommand(), cmd);
		}
	}

	public static boolean execute(String input, Player player) {
		try {
			String cmd = input.split(" ")[0];
			String[] args = input.replace(cmd+" ", "").replace(", ", ",").split(",");

			if(commands.containsKey(cmd))  {
				Command command = commands.get(cmd);
				if(command.canExecute(player)) {
					return command.execute(player, args);
				} else {
					System.out.println("You do not have the ability to use this command.");
				}
			} else {
				System.out.println("Unrecognised Command '"+input+"'.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


}
