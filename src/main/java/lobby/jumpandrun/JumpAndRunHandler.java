package lobby.jumpandrun;

import lobby.LobbyMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class JumpAndRunHandler implements Listener {

    Plugin plugin;

    public JumpAndRunHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        JumpAndRunTimer jumpAndRunTimer = LobbyMain.jumpAndRun.getPlayerTimer(player);
        if (player.getLocation().getBlockY() < 15) {
            if (!jumpAndRunTimer.isPaused()) {
                jumpAndRunTimer.failed();
            }
        }
        if (player.getLocation().getBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
            if (jumpAndRunTimer.isPaused()) {
                jumpAndRunTimer.start();
            } else {
                jumpAndRunTimer.setCheckPoint();
            }
        } else if (player.getLocation().getBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
            jumpAndRunTimer.end();
        }

    }

}
