package com.doublehelix;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TagListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (TagManager.getTaggers().contains(event.getPlayer())) {
            TagManager.getTaggers().remove(event.getPlayer());
            event.setQuitMessage("Leaving in the middle of tag? LAME!");
            TagManager.quitter(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if ((event.getEntityType() == EntityType.PLAYER) && (event.getDamager().getType() == EntityType.PLAYER))
        {
            Player damaged = (Player)event.getEntity();
            Player damager = (Player)event.getDamager();

            if(damager == TagManager.getIt()){
                if(TagManager.getTaggers().contains(damaged)){
                    TagManager.setIt(damaged);
                    event.setDamage(0);
                    event.setDamage(EntityDamageEvent.DamageModifier.BASE, 0);
                    TagUtil.sendMessageToPlayers(ChatColor.GREEN + (event.getEntity()).getName() + " is it! " + (event.getDamager()).getName() + " is no longer it.");

                } else {
                    event.setCancelled(true);
                    TagUtil.sendTagMessage(damager, ChatColor.RED + "This user is not part of the tag game!");
                }
            }
            else if(TagManager.getTaggers().contains(damager) && !TagManager.getTaggers().contains(damaged)) {
                event.setCancelled(true);
                TagUtil.sendTagMessage(damager, ChatColor.RED + "You can't hit this user, they are in a game of tag!");
            } else {
                    TagUtil.sendTagMessage(damager, ChatColor.RED + "You're not it, you can't tag someone!");
                    event.setDamage(0);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if ((TagManager.getTaggers().contains(event.getPlayer())))
        {
            event.setCancelled(true);
            TagUtil.sendTagMessage(event.getPlayer(), ChatColor.RED + "You can't place blocks while in a game of tag.");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if ((TagManager.getTaggers().contains(event.getPlayer())))
        {
            event.setCancelled(true);
            TagUtil.sendTagMessage(event.getPlayer(),ChatColor.RED + "You can't break blocks while in a game of tag.");
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {
        if ((event.getPlayer().hasPermission("tag.sign.create")) &&
                (event.getLine(0).equalsIgnoreCase("[join tag]")))
        {
            TagUtil.sendTagMessage(event.getPlayer(), "You have successfully created a join sign.");
            event.setLine(0, "[Join tag]");
            event.setLine(1, ChatColor.COLOR_CHAR + "2Right Click");
            event.setLine(2, ChatColor.COLOR_CHAR + "2to");
            event.setLine(3, ChatColor.COLOR_CHAR + "2Join tag");
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if (event.getEntityType() == EntityType.PLAYER)
        {
            if ((TagManager.getTaggers().contains((Player) event.getEntity()))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntityType() == EntityType.PLAYER)
        {
            if (TagManager.getTaggers().contains((Player) event.getEntity())) {
                event.setDamage(0);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if ((event.getClickedBlock() != null) && ((event.getClickedBlock().getState() instanceof Sign)) && (((Sign)event.getClickedBlock().getState()).getLine(0).equalsIgnoreCase("[join tag]"))) {
            if (event.getPlayer().hasPermission("tag.sign.use"))
            {
                if (TagManager.doesGameExsist())
                {
                    TagUtil.sendTagMessage(event.getPlayer(),"You have successfully joined the tag game." );
                    TagManager.addPlayerToGame(event.getPlayer());
                }
                else
                {
                    TagUtil.sendTagMessage(event.getPlayer(), ChatColor.RED + "There is not tag game in progress, ask a staff member to start a game.");
                }
            }
            else {
                TagUtil.sendTagMessage(event.getPlayer(), ChatColor.RED + "You do not have permission to use this sign.");
            }
        }
    }
}
