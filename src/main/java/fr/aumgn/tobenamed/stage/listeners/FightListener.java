package fr.aumgn.tobenamed.stage.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.FightStage;

public class FightListener implements Listener {

    private FightStage stage;

    public FightListener(FightStage stage) {
        this.stage = stage;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent damageCause = player.getLastDamageCause();
        if (!(damageCause instanceof EntityDamageByEntityEvent)) {
            return;
        }

        Entity damagerEntity = ((EntityDamageByEntityEvent) damageCause).getDamager();
        if (damagerEntity instanceof Projectile) {
            damagerEntity = ((Projectile) damagerEntity).getShooter();
        }
        if (!(damagerEntity instanceof Player)) {
            return;
        }

        Player damager = (Player) damagerEntity;
        Game game = stage.getGame();
        if (!game.contains(damager) || !game.contains(player)) {
            return;
        }

        if (game.getTeam(player) == game.getTeam(damager)) {
            return;
        }

        event.getDrops().add(new ItemStack(Material.DIAMOND));
    }
}
