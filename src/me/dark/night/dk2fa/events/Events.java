package me.dark.night.dk2fa.events;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import me.dark.night.dk2fa.Main;
import me.dark.night.dk2fa.dao.UsersDao;
import me.dark.night.dk2fa.utils.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public class Events implements Listener {

    public Events(Main main){
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (UsersDao.verify(p.getName())) {
            UsersDao.addInVerification(p.getName());
            Config.messageJoin.forEach(msg -> p.sendMessage(msg.replace("{id}", UsersDao.getSecretId(p.getName()))));
        }
    }

    @EventHandler
    void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()))
            e.setCancelled(true);
    }

    @EventHandler
    void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()))
            e.setCancelled(true);
    }

    @EventHandler
    void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()))
            e.setCancelled(true);
    }

    @EventHandler
    void onInteractEntity(PlayerInteractAtEntityEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()))
            e.setCancelled(true);
    }

    @EventHandler
    void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (UsersDao.inVerification(p.getName()))
                e.setCancelled(true);
        }
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()) && e.getAction().name().toLowerCase().contains("right"))
            e.setCancelled(true);
    }
    
    @EventHandler
    void onChangeWorld(InventoryOpenEvent e){
        Player p = (Player) e.getPlayer();
        if (UsersDao.inVerification(p.getName()))
            e.setCancelled(true);
    }

    @EventHandler
    void onTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()))
            e.setCancelled(true);
    }

    @EventHandler
    void onCommand(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (UsersDao.inVerification(p.getName()) && e.getMessage().startsWith("/"))
            e.setCancelled(true);
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        UsersDao.removeInVerification(p.getName());
    }

    @EventHandler
    void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String msg = e.getMessage();
        GoogleAuthenticator gAuth = new GoogleAuthenticator(new GoogleAuthenticatorConfig());
        if (UsersDao.inVerification(p.getName())) {
            e.setCancelled(true);
            String secretId = UsersDao.getSecretId(p.getName());
            try {
                if (gAuth.authorize(secretId, Integer.parseInt(msg))) {
                    UsersDao.removeInVerification(p.getName());
                    p.sendMessage("§aO código estava correto, agora você está livre para jogar!");
                } else
                    p.sendMessage("§cO código está incorreto, tente novamente!");
            } catch (Exception exception) {
                p.sendMessage("§cO código deve conter apenas números");
            }
        }
    }

}
