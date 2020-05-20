package lobby;

import java.util.Collection;
import java.util.HashMap;

import core.CoreBungeeCordClient;
import core.CoreMain;
import core.CoreSendStringPacket;
import core.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;


import net.md_5.bungee.api.ChatColor;
import org.bukkit.scheduler.BukkitTask;

public class LobbyMain extends JavaPlugin implements Listener {

    public static ItemStack selector = new ItemStack(Material.COMPASS, 1);
    public static ItemMeta meta = selector.getItemMeta();

    public static ItemStack quit = new ItemStack(Material.BARRIER, 1);
    public static ItemMeta quitMeta = quit.getItemMeta();

    public static HashMap<String, Integer> playerTimer = new HashMap<String, Integer>();
    public static Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    public static HashMap<String, Integer> portalTimer = new HashMap<String, Integer>();

    private static HashMap<Player, Integer> JARTimer = new HashMap<Player, Integer>();

    private LobbyMain lobbyMain = this;

    public void onEnable() {

        LobbyEventHandler lobbyEventHandler = new LobbyEventHandler(this);
        lobbyEventHandler.initialize();

        MinigameSchiffeVersenken minigameSchiffeVersenken = new MinigameSchiffeVersenken(this);
        LobbyCommandExecutor lobbyCommandExecutor = new LobbyCommandExecutor(this, minigameSchiffeVersenken);

        getCommand("challenge").setExecutor(lobbyCommandExecutor);

        CoreMain.setPlugin(this);


        World w = Bukkit.getWorld("world");
        for (Player p : players) {
            portalTimer.put(p.getDisplayName(), 5);
        }


        meta.setDisplayName(ChatColor.AQUA + "Serverwahl");
        selector.setItemMeta(meta);

        quitMeta.setDisplayName(ChatColor.DARK_RED + "Quit");
        quit.setItemMeta(quitMeta);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {

                for (org.bukkit.entity.Entity e : w.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }

                for (Player p : players) {

                    if (!portalTimer.get(p.getDisplayName()).equals(0)) {
                        for (int i = portalTimer.get(p.getDisplayName()); i != 0; i--) {
                            portalTimer.put(p.getDisplayName(), i);
                        }
                    }

                    if (!playerTimer.containsKey(p.getDisplayName())) {
                        playerTimer.put(p.getDisplayName(), 0);
                    } else {
                        int i = playerTimer.get(p.getDisplayName());
                        i++;
                        if (i == 99999999) {
                            playerTimer.remove(p.getDisplayName());
                            p.kickPlayer(ChatColor.GOLD + "Du warst zu lange AFK!");
                        } else {
                            playerTimer.put(p.getDisplayName(), i);
                        }
                    }

                }
            }

        }, 0L, 20);

        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {

                for (Player player : players) {
                    // if (portalTimer.get(p.getDisplayName()) == 0) {

                    if (player.getLocation().getY() <= 10) {
                        player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
                        JARTimer.put(player, -1);
                    }

                    Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if (b.getType() == Material.SPRUCE_LOG || b.getType() == Material.SPRUCE_PLANKS) {
                        CoreBungeeCordClient.moveToServer(player, "BingoServer");

                    } else if (b.getType() == Material.SMOOTH_QUARTZ || b.getType() == Material.QUARTZ_PILLAR) {
                        CoreBungeeCordClient.moveToServer(player, "ChallengeServer");

                    } else if (b.getType().equals(Material.IRON_ORE) || b.getType().equals(Material.DARK_OAK_LOG)) {
                        CoreBungeeCordClient.moveToServer(player, "SurvivalServer");
                    } else if (b.getType() == Material.PRISMARINE_SLAB || b.getType() == Material.WHITE_WOOL) {
                        if (!JARTimer.containsKey(player)) {
                            startJumpAndRunTimer(player);
                        }
                    }


                    Location l = player.getLocation();
                    b = l.getBlock();

                    if (b.getType().equals(Material.STONE_PRESSURE_PLATE)) {
                        if (!JARTimer.containsKey(player)) {
                            CoreSendStringPacket.sendPacketToTitle(player, Utils.colorize(Utils.getRainbowString("Du alter Cheater")), Utils.colorize("&c in &f69:69:69 &cgeschafft!"));
                            player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 1.0f, .1f);
                        } else {
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                            CoreSendStringPacket.sendPacketToTitle(player, Utils.colorize(Utils.getRainbowString("Jump-and-Run")), Utils.colorize("&a in &f" + Utils.formatTimerTime(JARTimer.get(player) / 20) + " &ageschafft!"));
                            player.teleport(new Location(Bukkit.getWorld("world"), -40, 21, 88));
                            JARTimer.put(player, -1);
                        }
                    }
                }
            }

        }, 0L, 1);

    }

    private void startJumpAndRunTimer(Player player) {
        if (!JARTimer.containsKey(player)) {
            JARTimer.put(player, 0);
            BukkitTask runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (JARTimer.get(player) == -1) {
                        JARTimer.remove(player);
                        cancel();
                    } else {
                        player.spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 25);
                        JARTimer.put(player, JARTimer.get(player) + 1);
                        CoreSendStringPacket.sendPacketToHotbar(player, Utils.colorize("&0Jump-and-Run: &a" + Utils.formatTimerTime(JARTimer.get(player)/20)));
                    }
                }
            }.runTaskTimer(this, 0L, 1L);
        }

    }
}
