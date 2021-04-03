package lobby;

import core.core.CoreMain;
import lobby.jumpandrun.JumpAndRun;
import lobby.jumpandrun.JumpAndRunHandler;
import lobby.servers.ServersInventory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class LobbyMain extends JavaPlugin implements Listener {

    public static JumpAndRun jumpAndRun;
    public static JumpAndRunHandler jumpAndRunHandler;

    public void onEnable() {

        CoreMain.setPlugin(this);
        prepareJumpAndRun();
        ServersInventory.init();

        LobbyEventHandler lobbyEventHandler = new LobbyEventHandler(this);
        lobbyEventHandler.initialize();

        MinigameSchiffeVersenken minigameSchiffeVersenken = new MinigameSchiffeVersenken(this);
        LobbyCommandExecutor lobbyCommandExecutor = new LobbyCommandExecutor(this, minigameSchiffeVersenken);

        Objects.requireNonNull(getCommand("challenge")).setExecutor(lobbyCommandExecutor);
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
