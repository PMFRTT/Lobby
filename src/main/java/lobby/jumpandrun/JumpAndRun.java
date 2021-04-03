package lobby.jumpandrun;

import lobby.LobbyMain;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class JumpAndRun {

    private static final HashMap<Player, JumpAndRunTimer> jumpAndRunTimerHashMap = new HashMap<Player, JumpAndRunTimer>();
    private LobbyMain lobbyMain;

    public JumpAndRun(LobbyMain lobbyMain) {
        this.lobbyMain = lobbyMain;
    }

    public void addPlayerTimer(Player player) {
        jumpAndRunTimerHashMap.put(player, new JumpAndRunTimer(lobbyMain, player));
    }

    public JumpAndRunTimer getPlayerTimer(Player player) {
        return jumpAndRunTimerHashMap.getOrDefault(player, null);
    }

    public void removePlayerTimer(Player player) {
        if (jumpAndRunTimerHashMap.containsKey(player)) {
            jumpAndRunTimerHashMap.remove(player);
        }
    }
}
