package com.projectkorra.probending.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamCommand extends PBCommand {

	public TeamCommand() {
		super("team", "/probending team", "Displays help for all Team commands.", new String[] {"team", "teams", "t"}, true);
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender) || !correctLength(sender, args.size(), 0, 1)) {
			return;
		}
		int page = 1;
		if (args.size() == 1) {
			if (isNumeric(args.get(0))) {
				page = Integer.parseInt(args.get(0));
			}
		}
		List<String> strings = new ArrayList<String>();
		for (PBCommand command : instances.values()) {
			if (command.isChild() && Arrays.asList(command.getParentAliases()).contains("team")
					&& sender.hasPermission("probending.command.team." + command.getName())) {
				strings.add(command.getProperUse() + ChatColor.WHITE + " - " + command.getDescription());
			}
		}
		for (String s : getPage(strings, ChatColor.GOLD + "Team Commands:", page, true)) {
			sender.sendMessage(ChatColor.DARK_AQUA + s);
		}
		return;
	}
}
