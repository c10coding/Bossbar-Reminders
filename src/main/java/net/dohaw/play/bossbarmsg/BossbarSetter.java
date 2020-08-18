package net.dohaw.play.bossbarmsg;

import me.c10coding.coreapi.chat.ChatFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BossbarSetter implements Listener {

    private BossbarMsg plugin;
    private FileConfiguration config;
    private ChatFactory chatFactory;

    public BossbarSetter(BossbarMsg plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.chatFactory = plugin.getAPI().getChatFactory();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        double durationInSeconds = config.getInt("Duration");
        double progressPerSecond = (1 / durationInSeconds) / 20;

        BarColor color = matchWithBarColor();
        List<String> reminders = getReminders();
        Player p = e.getPlayer();

        new BukkitRunnable(){
            int index = 0;
            String msg = reminders.get(index);
            BossBar bb = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', chatFactory.colorString(msg)), color, BarStyle.SOLID);
            public void run(){

                if(!bb.getPlayers().contains(p)){
                    bb.addPlayer(p);
                }

                double currentProgress = bb.getProgress();
                if(currentProgress - progressPerSecond > 0){
                    bb.setProgress(currentProgress - progressPerSecond);
                }else{
                    bb.setProgress(1);
                    index++;

                    if(index == reminders.size()){
                        index = 0;
                    }
                    bb.setTitle(chatFactory.colorString(reminders.get(index)));

                }
            }

        }.runTaskTimer(plugin, 0L, 1L);

    }

    private BarColor matchWithBarColor(){
        String colorCode = config.getString("BossbarColor");
        switch(colorCode){
            case "&b":
                return BarColor.BLUE;
            case "&a":
                return BarColor.GREEN;
            case "&d":
                return BarColor.PINK;
            case "&4":
                return BarColor.RED;
            case "&f":
                return BarColor.WHITE;
            case "&e":
                return BarColor.YELLOW;
            default:
                return null;
        }
    }

    private List<String> getReminders(){
        return config.getStringList("Reminders");
    }

}
