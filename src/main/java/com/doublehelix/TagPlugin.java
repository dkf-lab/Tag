package com.doublehelix;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.logging.Logger;

@Plugin(name="Tag", version="1.1-SNAPSHOT")
@ApiVersion(ApiVersion.Target.v1_15)
@Description("A Simple Game of Tag Plugin")
@Author("doublehelix457")
@Commands(@Command(name="tag", desc="main tag command"))
@Permission(name="tag", children={
        @ChildPermission(name="tag.create"),
        @ChildPermission(name="tag.start"),
        @ChildPermission(name="tag.stop"),
        @ChildPermission(name="tag.join"),
        @ChildPermission(name="tag.leave"),
        @ChildPermission(name="tag.kick"),
        @ChildPermission(name="tag.setspawn")})
@Permission(name="tag.sign", children={
        @ChildPermission(name="tag.sign.create"),
        @ChildPermission(name="tag.sign.use")
})
public class TagPlugin extends JavaPlugin {

    private static TagPlugin inst;

    Logger log;
    FileConfiguration config = new YamlConfiguration();

    public void onEnable()
    {
        inst = this;
        getConfig().options().copyDefaults(true);
        this.config = getConfig();
        this.log = getLogger();
        getCommand("tag").setExecutor(new TagCommand());
        Bukkit.getPluginManager().registerEvents(new TagListener(), TagPlugin.inst());
        this.log.info("[tag] tag plugin succesfully enabled!");
        saveDefaultConfig();
    }

    public void onDisable()
    {
        this.log.info("[tag] Tag has been disabled!");
    }

    public static TagPlugin inst() {
        return inst;
    }
}
