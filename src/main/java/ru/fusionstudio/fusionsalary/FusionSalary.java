package ru.fusionstudio.fusionsalary;

import ru.fusionstudio.fusionsalary.commands.SalaryCommand;
import ru.fusionstudio.fusionsalary.managers.CooldownManager;
import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class FusionSalary extends JavaPlugin {

    private Permission permissions;

    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        if (!this.setupPermissions()) {
            this.getLogger().warning("Не удалось подключиться к Vault. Плагин будет отключен.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.saveDefaultConfig();
        this.cooldownManager = new CooldownManager();
        this.getCommand("salary").setExecutor(new SalaryCommand(this));
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return (permissions != null);
    }

}
