package fun.fusionmine.fusionsalary.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Utils {

    @Getter
    private static final JavaPlugin inst = JavaPlugin.getProvidingPlugin(Utils.class);

    public String color(String path) {
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public List<String> colorList(List<String> input) {
        List<String> set = new ArrayList<>();
        input.forEach(s -> set.add(color(s)));
        return set;
    }

    public static String getMsg(String path) {
        ConfigurationSection section = getInst().getConfig().getConfigurationSection("messages");
        return color(section.getString("prefix") + section.getString(path));
    }

    public void sendActions(List<String> messages, Player player) {
        messages.forEach(str -> {
            if (str.startsWith("[title]")) {
                String[] title = str.replace("[title]", "").trim().split(";", 2);
                player.sendTitle(title[0], title[1], 15, 80, 15);
            }
            if (str.startsWith("[message]")) {
                String msg = str.replace("[message] ", "").replace("[message]", "");
                player.sendMessage(msg);
            }
            if (str.startsWith("[sound]")) {
                String sound = str.replace("[sound]", "").trim();
                player.playSound(player.getLocation(), Sound.valueOf(sound.toUpperCase()), 100.0F, 1.0F);
            }
            if (str.startsWith("[console]")) {
                String cmd = str.replace("[console]", "").trim().replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        });
    }

}
