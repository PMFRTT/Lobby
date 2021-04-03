package lobby;

import core.core.CoreMain;
import lobby.jumpandrun.JumpAndRun;
import lobby.jumpandrun.JumpAndRunHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


import net.md_5.bungee.api.ChatColor;

public class LobbyMain extends JavaPlugin implements Listener {

    public static ItemStack selector = new ItemStack(Material.COMPASS, 1);
    public static ItemMeta meta = selector.getItemMeta();

    public static ItemStack quit = new ItemStack(Material.BARRIER, 1);
    public static ItemMeta quitMeta = quit.getItemMeta();

    public static JumpAndRun jumpAndRun;
    public static JumpAndRunHandler jumpAndRunHandler;

    public void onEnable() {

        CoreMain.setPlugin(this);
        prepareJumpAndRun();

        LobbyEventHandler lobbyEventHandler = new LobbyEventHandler(this);
        lobbyEventHandler.initialize();

        MinigameSchiffeVersenken minigameSchiffeVersenken = new MinigameSchiffeVersenken(this);
        LobbyCommandExecutor lobbyCommandExecutor = new LobbyCommandExecutor(this, minigameSchiffeVersenken);

        getCommand("challenge").setExecutor(lobbyCommandExecutor);

        meta.setDisplayName(ChatColor.AQUA + "Serverwahl");
        selector.setItemMeta(meta);

        quitMeta.setDisplayName(ChatColor.DARK_RED + "Quit");
        quit.setItemMeta(quitMeta);

    }

    private void prepareJumpAndRun(){
        jumpAndRun = new JumpAndRun(this);
        jumpAndRunHandler = new JumpAndRunHandler(this);
        jumpAndRunHandler.init();

        for(Player player : Bukkit.getOnlinePlayers()){
            jumpAndRun.addPlayerTimer(player);
        }
    }
}
