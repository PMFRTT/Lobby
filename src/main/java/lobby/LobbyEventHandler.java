package lobby;

import core.bungee.CoreBungeeCordClient;
import core.core.CoreSendStringPacket;
import core.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class LobbyEventHandler implements Listener {

    LobbyMain main;


    public LobbyEventHandler(LobbyMain main) {
        this.main = main;
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, main);
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        main.playerTimer.remove(p.getDisplayName());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {


        main.portalTimer.put(e.getPlayer().getDisplayName(), 5);
        Player player = e.getPlayer();
        player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
        player.getInventory().clear();
        player.getInventory().setItem(4, main.selector);
        player.getInventory().setItem(8, main.quit);
        Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable() {
            @Override
            public void run() {
                CoreSendStringPacket.sendPacketToTitle(e.getPlayer(), Utils.colorize("&3Moin"), Utils.colorize(e.getPlayer().getDisplayName()));
                CoreSendStringPacket.sendPacketToHotbar(e.getPlayer(), Utils.colorize("Wilkommen auf &3PMFRTT-Networks&f!"));
            }
        }, 40L);

    }


    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand().equals(main.selector)) {
            p.openInventory(LobbyInventory.getServerSelectorInventory(p));
        } else if (p.getItemInHand().equals(main.quit)) {
            p.kickPlayer(ChatColor.DARK_RED + "Du hast das Spiel verlassen!");
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {


        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        World world = main.getServer().getWorld("world_test");
        player.teleport(new Location(world, 0, 10, 0));
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().equals(LobbyInventory.ServerSelector)) {
                if (e.getSlot() == 0) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "BingoServer");

                } else if (e.getSlot() == 1) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "ChallengeServer");

                } else if (e.getSlot() == 2) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "SurvivalServer");

                } else if (e.getSlot() == 3) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "MobArena");

                }else if(e.getSlot() == 4){
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "Skyblock");

                } else if (e.getSlot() == 8) {
                    e.setCancelled(true);
                    CoreBungeeCordClient.moveToServer(player, "DebugServer");

                } else {
                    e.setCancelled(true);
                }


            }
        }

    }

}
