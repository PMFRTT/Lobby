package lobby.jumpandrun;

import core.Utils;
import core.core.CoreMain;
import core.core.CoreSendStringPacket;
import core.currency.Currency;
import core.timer.Timer;
import core.timer.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Random;


public class JumpAndRunTimer extends Timer {

    private final World world = Bukkit.getWorld("world");
    private final Player player;

    private final Location spawn = new Location(world, -41, 21, 89);

    private Location checkPoint = spawn;

    public JumpAndRunTimer(Plugin plugin, Player player) {
        super(plugin, TimerType.INCREASING, Utils.colorize(""), "", false, "", player);
        this.player = player;
    }

    public void start() {
        setCheckPoint();
        super.resume();
    }

    public void end() {
        super.pause();
        float reward = new Random().nextFloat();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        setCheckPoint(spawn);
        player.teleport(spawn);

        CoreSendStringPacket.sendPacketToTitle(player, Utils.colorize("Beendet"), Utils.colorize("Du hast das Jump-and-Run in &b" + Utils.formatTimerTimeTicksThreeDecimal(getTicks()) + "&f beendet"));
        super.setSeconds(0);
    }

    public int getTicks() {
        return super.getTicks();
    }

    public void setCheckPoint() {
        if (this.checkPoint.getBlockX() != this.player.getLocation().getBlockX() || this.checkPoint.getBlockY() != this.player.getLocation().getBlockY() || this.checkPoint.getBlockZ() != this.player.getLocation().getBlockZ()) {
            this.checkPoint = this.player.getLocation();
            CoreSendStringPacket.sendPacketToTitle(player, Utils.colorize("&eCheckpoint"), "");
        }
    }


    public void setCheckPoint(Location location) {
        this.checkPoint = location;
    }

    public void failed() {
        this.player.teleport(this.checkPoint);
    }
}
