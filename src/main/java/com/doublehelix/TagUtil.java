package com.doublehelix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TagUtil {

    public static Player getRandomIt() {
        return TagManager.getTaggers().get(new Random().nextInt(TagManager.getTaggers().size()));
    }


    public static Player getPlayerFromString(String findee)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getName().toLowerCase().startsWith(findee)) {
                return p;
            }
        }
        return null;
    }

    public static Location getTagSpawn()
    {
        World main = Bukkit.getServer().getWorld(TagPlugin.inst().getConfig().getString("World"));
        if (main == null) {
            return new Location(Bukkit.getServer().getWorlds().get(0), TagPlugin.inst().getConfig().getInt("x"), TagPlugin.inst().getConfig().getInt("y"), TagPlugin.inst().getConfig().getInt("z"));
        } else {
            return new Location(main, TagPlugin.inst().getConfig().getInt("x"), TagPlugin.inst().getConfig().getInt("y"), TagPlugin.inst().getConfig().getInt("z"));
        }
    }

    public static void setTagSpawn(Location loc)
    {
        try
        {
            TagPlugin.inst().getConfig().set("x", loc.getX());
            TagPlugin.inst().getConfig().set("y", loc.getY());
            TagPlugin.inst().getConfig().set("z", loc.getZ());
            TagPlugin.inst().getConfig().set("World", loc.getWorld().getName());
            TagPlugin.inst().saveConfig();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static List<Player> getPercentageOfMap(Map<Player, Boolean> playermap, int percentage)
    {
        List resultlist = new ArrayList();

        List selectingPlayerList = new ArrayList();
        for (Player key : playermap.keySet()) {
            if ((playermap.get(key))) {
                selectingPlayerList.add(key);
            }
        }
        if (selectingPlayerList.size() > 0)
        {
            Random rand = new Random();

            int amountNeeded = (int)(selectingPlayerList.size() / 100.0D * 10.0D);

            amountNeeded = amountNeeded > 0 ? amountNeeded : 1;
            for (int i = 0; i < amountNeeded; i++)
            {
                int randomIndex = rand.nextInt(selectingPlayerList.size());
                Player selected = (Player)selectingPlayerList.get(randomIndex);
                resultlist.add(selected);
                selectingPlayerList.remove(selected);
            }
        }
        return resultlist;
    }

    public static void sendMessageToPlayers(String message){
        for(Player p : TagManager.getTaggers()) sendTagMessage(p, message);
    }

    public static void sendTagMessage(Player p, String message){
        p.sendMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Tag" + ChatColor.DARK_GREEN + "] " + message);
    }

}
