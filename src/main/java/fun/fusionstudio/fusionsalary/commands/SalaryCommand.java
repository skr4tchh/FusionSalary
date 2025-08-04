package fun.fusionstudio.fusionsalary.commands;

import fun.fusionstudio.fusionsalary.FusionSalary;
import fun.fusionstudio.fusionsalary.utils.Utils;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class SalaryCommand implements CommandExecutor {

    private FusionSalary plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player)sender;
        if(!player.hasPermission("fusionsalary.allow")) {
            player.sendMessage(Utils.getMsg("perms"));
            return false;
        }

        if(plugin.getCooldownManager().hasCooldown(player)) {
            player.sendMessage(Utils.getMsg("cooldown")
                    .replace("%time%", plugin.getCooldownManager().getCooldownTime(player))
            );
            return false;
        }

        String groupName = plugin.getPermissions().getPrimaryGroup(player);
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("salaries");
        if (section != null) {
            ConfigurationSection groupSection = section.getConfigurationSection(groupName);
            if (groupSection != null) {
                int cooldown = groupSection.getInt("cooldown");
                List<String> rewards = Utils.colorList(groupSection.getStringList("actions-on-give"));
                Utils.sendActions(rewards, player);
                plugin.getCooldownManager().setCooldown(player, cooldown);
                return true;
            }
        }

        player.sendMessage(Utils.getMsg("not-salary"));
        return false;
    }

}
