package ru.fusionstudio.fusionsalary.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public void setCooldown(Player player, long seconds) {
        UUID playerId = player.getUniqueId();
        long cooldownTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        cooldowns.put(playerId, cooldownTime);
    }

    public boolean hasCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        return cooldowns.containsKey(playerId) && cooldowns.get(playerId) > System.currentTimeMillis();
    }

    public long getRemainingCooldown(Player player) {
        return (cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
    }

    public String getCooldownTime(Player player) {
        long cooldown = getRemainingCooldown(player);

        if (cooldown <= 0) return "сейчас";

        StringBuilder str = new StringBuilder();
        int days = (int) TimeUnit.SECONDS.toDays(cooldown);
        long hours = TimeUnit.SECONDS.toHours(cooldown) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(cooldown) % 60;
        long seconds = cooldown % 60;
        if (days > 0)
            str.append(choosePluralMerge(days, "день", "дня", "дней")).append(" ");
        if (hours > 0L)
            str.append(choosePluralMerge(hours, "час", "часа", "часов")).append(" ");
        if (minutes > 0L)
            str.append(choosePluralMerge(minutes, "минута", "минуты", "минут")).append(" ");
        if (seconds > 0L)
            str.append(choosePluralMerge(seconds, "секунда", "секунды", "секунд")).append(" ");
        return str.toString().trim();
    }

    public String choosePluralMerge(long number, String caseOne, String caseTwo, String caseFive) {
        StringBuilder str = new StringBuilder(number + " ");
        long absNumber = Math.abs(number);
        long lastDigit = absNumber % 10;
        long lastTwoDigits = absNumber % 100;
        if (lastDigit == 1 && lastTwoDigits != 11) {
            str.append(caseOne);
        } else if (lastDigit >= 2 && lastDigit <= 4 && (lastTwoDigits < 10 || lastTwoDigits >= 20)) {
            str.append(caseTwo);
        } else {
            str.append(caseFive);
        }
        return str.toString();
    }

}
