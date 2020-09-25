package me.dark.night.dk2fa;

import me.dark.night.dk2fa.dao.UsersDao;
import me.dark.night.dk2fa.events.Events;
import me.dark.night.dk2fa.process.UserProcess;
import me.dark.night.dk2fa.utils.Config;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setup();
    }

    @Override
    public void onDisable() {
        UsersDao.upToMySQL();
        HandlerList.unregisterAll(this);
    }

    private void setup(){
        UsersDao.upToMap();
        new Events(this);
        new Config(this);
        new UserProcess(this);
    }

    public static Main getInstance(){
        return getPlugin(Main.class);
    }

}
