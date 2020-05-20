package lobby;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommandExecutor implements CommandExecutor {

    private LobbyMain main;

    public LobbyCommandExecutor(LobbyMain main, MinigameSchiffeVersenken minigameSchiffeVersenken){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getLabel().equalsIgnoreCase("challenge")){
            if(args.length == 1 && Bukkit.getPlayer(args[0]) != null){
                MinigameSchiffeVersenken minigameSchiffeVersenken = new MinigameSchiffeVersenken(main);
                minigameSchiffeVersenken.gameLoop((Player) sender, Bukkit.getPlayer(args[0]));
            }

        }

        return false;
    }
}
