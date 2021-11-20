package com.doublehelix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TagManager {
    private static Map<Player, Location> last_locations = new HashMap();
    private static boolean gameRunning = false;
    private static boolean gameCreated = false;
    private static Location spawn;
    private static  ArrayList<Player> taggers = new ArrayList<>();
    private static Player IT;
    private static CountdownTimer timer;
    public static boolean checkForTeleport = false;

    public static boolean isGameRunning() { return gameRunning; }
    public static boolean doesGameExist() {
        if (!gameCreated) {
            createGame();
        }
        return gameCreated;
    }
    public static ArrayList<Player> getTaggers(){return taggers;}
    public static Player getIt(){return IT;}
    public static void setIt(Player p){IT=p;}
    public static boolean isIt(Player tagger){return tagger == IT;}

    public static void createGame(){
        if(gameCreated || gameRunning) {
            return;
        }
        spawn = TagUtil.getTagSpawn();
        gameCreated = true;
    }

    public static String startGame(){
        if (!gameCreated) {
            return ChatColor.RED + "The game has not yet been created!";
        }
        if (gameRunning) {
            return ChatColor.RED + "The game has already started!";
        }
        if (taggers.size() < 2) {
            return ChatColor.RED + "Not enough players!";
        }
        checkForTeleport = true;
        timer = null;
        gameRunning = true;
        IT = TagUtil.getRandomIt();
        for(Player p : taggers) {
            if(p != IT){
                last_locations.put(p, p.getLocation());
                p.teleport(spawn);
            }

        }
        TagUtil.sendMessageToPlayers(IT.getName() + " is it! You have 5 seconds. Start running!!!");
        Runnable delayTP = () -> {
            synchronized (last_locations) {
                last_locations.put(IT, IT.getLocation());
            }
            checkForTeleport = true;
            IT.teleport(spawn);
            checkForTeleport = false;
            TagUtil.sendMessageToPlayers(IT.getName() + " is on the hunt!");
        };
        Bukkit.getScheduler().runTaskLater(TagPlugin.inst(), delayTP, 100);
        // schedule task to finish game
        Bukkit.getScheduler().runTaskLater(TagPlugin.inst(), TagManager::endGame, 5*60*20); // 5min
        checkForTeleport = false;
        return null;
    }

    public static void endGame(){
        TagUtil.sendMessageToPlayers(TagUtil.color("&aThe game has ended! &7Thanks for playing!"));
        TagUtil.sendMessageToPlayers(TagUtil.color("&c" + TagManager.getIt().getName() + "&7 was the &closer&7!"));
        checkForTeleport = true;
        if(TagManager.isGameRunning()) {
            for (Player p : taggers) {
                p.teleport(last_locations.get(p));
            }
            last_locations.clear();
        }
        checkForTeleport = false;
        taggers.clear();
        IT = null;
        gameRunning = false;
        gameCreated = false;
    }

    public static void kickPlayer(Player p){
        taggers.remove(p);
        if(last_locations.containsKey(p)) {
            p.teleport(last_locations.get(p));
            last_locations.remove(p);
        }
        TagUtil.sendTagMessage(p, ChatColor.RED + "You have been kicked from the game.");
        if(taggers.size() < 2){
            endGame();
            return;
        }
        if(p == IT){
            IT = TagUtil.getRandomIt();
            TagUtil.sendMessageToPlayers(p.getName() + " has left the game! " + IT.getName() + " is now IT!");
        }
    }

    public static void quitter(Player p){
        if(!taggers.contains(p)){
            TagUtil.sendTagMessage(p, ChatColor.RED + "You are not in a game of tag!");
        }
        taggers.remove(p);
        if(last_locations.containsKey(p)) {
            p.teleport(last_locations.get(p));
            last_locations.remove(p);
        }
        TagUtil.sendTagMessage(p, ChatColor.RED + "You have left the game.");
        if(taggers.size() < 2){
            endGame();
            return;
        }
        if(p == IT){
            IT = TagUtil.getRandomIt();
            TagUtil.sendMessageToPlayers(p.getName() + " has left the game! " + IT.getName() + " is now IT!");
        }
    }

    public static void addPlayerToGame(Player p){
        createGame();
        taggers.add(p);
        if(gameRunning) {
            last_locations.put(p, p.getLocation());
            p.teleport(spawn);
            TagUtil.sendMessageToPlayers(p.getName() + " has joined Tag! Better late than never!");
            return;
        } else {
            if (taggers.size() >= 2) {
                // check if timer is ongoing
                if (timer == null) {
                    CountdownTimer timer = new CountdownTimer(TagPlugin.inst(), 60,
                            () -> { },
                            () -> {
                                TagUtil.sendMessageToPlayers("Starting game!");
                                startGame();
                            },
                            (t) ->  {
                                if (t.getSecondsLeft() % 5 == 0) {
                                    TagUtil.sendMessageToPlayers("Time left: " + t.getSecondsLeft());
                                } else {
                                    int i = t.getSecondsLeft();
                                    if (i == 1|i==2|i==3|i==4) {
                                        TagUtil.sendMessageToPlayers("Time left: " + t.getSecondsLeft());
                                    }
                                }
                            }
                    );
                    timer.scheduleTimer();
                }
            }
        }
        TagUtil.sendMessageToPlayers(p.getName() + " has joined Tag!");
    }
}
