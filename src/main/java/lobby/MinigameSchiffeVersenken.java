package lobby;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class MinigameSchiffeVersenken implements Listener {

    private static LobbyMain main;
    private static Player activePlayer;
    private static boolean hasWon = false;

    public MinigameSchiffeVersenken(LobbyMain main) {
        this.main = main;
    }

    public static void gameLoop(Player challenger, Player challenged) {
        challenged.openInventory(createChallengedInventory(challenged, challenger));
        challenger.openInventory(createChallengerInventory(challenger, challenged));

        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {










              if(hasWon){
                  cancel();
              }
            }
        }.runTaskTimerAsynchronously(main, 0L, 1L);


    }


    private static Inventory createChallengerInventory(Player challenger, Player challenged) {
        Inventory temp = Bukkit.createInventory((InventoryHolder) challenger, 54, Utils.colorize("Schiffe versenken - &c" + challenged.getDisplayName()));
        temp.setItem(0, getHead(challenged));
        temp.setItem(8, getHead(challenger));

        return temp;
    }

    private static Inventory createChallengedInventory(Player challenged, Player challenger) {
        Inventory temp = Bukkit.createInventory((InventoryHolder) challenged, 54, Utils.colorize("Schiffe versenken - &c" + challenger.getDisplayName()));
        temp.setItem(0, getHead(challenged));
        temp.setItem(8, getHead(challenger));

        return temp;
    }

    private static ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(player.getName());
        item.setItemMeta(meta);
        return item;
    }


}
