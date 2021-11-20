package com.doublehelix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("tag"))
        {
            // tag
            if (args.length == 0) {
                TagUtil.sendTagMessage(player,ChatColor.GOLD + "Usage of tag command: /tag <option>");
                return true;
            }
            // tag create
            if(args[0].equalsIgnoreCase("create")) {
                if(sender.hasPermission("tag.create")) {
                    if (!TagManager.doesGameExist() && !TagManager.isGameRunning()) {
                        TagManager.createGame();
                        return true;
                    } else {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is already an existing game of Tag!");
                        return true;
                    }
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to create a game!");
                return true;
            }
            // tag start
            if (args[0].equalsIgnoreCase("start"))
            {
                if (sender.hasPermission("tag.start"))
                {
                    String msg = TagManager.startGame();
                    if(msg != null){
                        TagUtil.sendTagMessage(player, msg);
                    }
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to start a game!");
                return true;
            }
            // tag stop
            if (args[0].equalsIgnoreCase("stop"))
            {
                if (sender.hasPermission("tag.stop"))
                {
                    if(!TagManager.doesGameExist() || !TagManager.isGameRunning()) {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is no running game to stop.");
                        return true;
                    }
                    TagManager.endGame();
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to stop the game!");
                return true;
            }
            // tag join
            if (args[0].equalsIgnoreCase("join"))
            {
                if (sender.hasPermission("tag.join"))
                {
                    if(!TagManager.doesGameExist()) {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is no running game of tag. Ask a staff member to create a game.");
                        return true;
                    }
                    if(!TagManager.getTaggers().contains(player)) {
                        TagUtil.sendTagMessage(player, ChatColor.RED + "You have successfully joined the tag game.");
                        TagManager.addPlayerToGame(player);
                        return true;
                    }else{
                        TagUtil.sendTagMessage(player, ChatColor.RED + "You are already in this game of tag!");
                        return true;
                    }
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to join the game!");
                return true;
            }
            // tag leave
            if (args[0].equalsIgnoreCase("leave"))
            {
                if (sender.hasPermission("tag.leave"))
                {
                    if(TagManager.getTaggers().contains(player))
                    {
                        TagManager.quitter(player);
                        return true;
                    }
                    TagUtil.sendTagMessage(player, ChatColor.RED + "You're not in a game of tag.");
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to leave the game!");
                return true;
            }
            // tag setspawn
            if (args[0].equalsIgnoreCase("setspawn"))
            {
                if (sender.hasPermission("tag.setspawn"))
                {
                    Location loc = (player).getLocation();
                    TagUtil.setTagSpawn(loc);
                    TagUtil.sendTagMessage(player, ChatColor.RED + "The tag location has been set!");
                    return true;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission!");
                return true;
            }
            // tag kick
            if (args[0].equalsIgnoreCase("kick"))
            {
                if (sender.hasPermission("tag.kick"))
                {
                    if(!TagManager.doesGameExist() || !TagManager.isGameRunning()){
                        TagUtil.sendTagMessage(player, ChatColor.RED + "There is no game of tag running!");
                    }
                    if (args.length == 1)
                    {
                        TagUtil.sendTagMessage(player, ChatColor.GREEN + "Usage: /tag kick <player>");
                        return true;
                    }
                    Player kickee = Bukkit.getServer().getPlayer(args[1]);
                    if (kickee != null)
                    {
                        if(!TagManager.getTaggers().contains(kickee)){
                            TagUtil.sendTagMessage(player,ChatColor.RED + "That player is not playing tag!");
                            return true;
                        }
                        TagManager.kickPlayer(kickee);
                        return true;
                    }
                    TagUtil.sendTagMessage(player, "Can't find player.");
                    return false;
                }
                TagUtil.sendTagMessage(player, ChatColor.RED + "You don't have permission to stop the game!");
                return true;
            }
            return false;
        }
        return false;
    }
}
