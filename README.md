# Tag

## THE CODE ABUSES STATIC CALLS!
As I do not have the time, I will not be fixing this; if anyone is forking this, however, please re-write the functions to not abuse static.

A simple tag minigame plugin for Bukkit/Spigot/PaperSpigot

Features:
  Pros:
  - Super Simple Minigame Plugin
  - Easy SetUp

  Cons:
  - Currently Requires a Permissions Plugin
  - Only one game of tag at a time, in one world.
  
Installation:  
  Drop the Jar in your Plugins folder.
  
SetUp:  
Set the Game spawn to the world/location of your choice, Default is the world spawn in your server.properties.  
/tag create - Create a "lobby" that allows players to join  
/tag start - start the game, itll keep going until you stop it. Requires at least two players.  
(Randomly picks someone to be IT. Gives everyone a 5 seconds headstart.)  

Commands:  
/tag create - create a "lobby" for the game  
/tag start - start the game, requires a minimum of 2 players.  
/tag join - join an exsisting or ongoing game of tag  
/tag leave - leave a game, though that's pretty lame.  
/tag setspawn - set where players should teleport to when the game starts  
/tag kick <player> - boot a moronic player, or maybe you're just a sore looser?  
/tag version - get the version, useful for the issue tracker  

Permissions:
There are no wildcards, get over it.  
tag - the global permission for the /tag command and sub commands  
tag.create - Allows a player to create a "lobby"  
tag.start - Allows a player to start a game of tag.  
tag.join - Allows a player to join the lobby/ running game.  
tag.leave - Allows a player to leave the lobby/ running game.  
tag.setspawn - Allows a player to set the spawn location when the game is started.  
tag.sign.create - Allows a player to create a join sign.  
tag.sign.use - Allows a player to join the running game of tag by right-clicking the sign, similar to /tag join.  

To-Do:  
Before creating an issue tracker issue, I am currently working on the following.  
- Needs Multiworld Support (However, Tag Spawns can be set in other than the default world already)  
- Needs Bungee Support  
- Needs Multiple Lobbies/ Games running at the same time  
- Needs Default Permissions (Currently Requires a Permissions Plugin)  
