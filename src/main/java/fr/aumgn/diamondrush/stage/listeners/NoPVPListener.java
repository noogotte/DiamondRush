package fr.aumgn.diamondrush.stage.listeners;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.stage.Stage;

public class NoPVPListener implements Listener{

    public Stage stage;

    public NoPVPListener(Stage stage) {
        this.stage = stage;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity targetEntity = event.getEntity();
        Entity damagerEntity = event.getDamager();

        if (damagerEntity instanceof Projectile) {
            damagerEntity = ((Projectile) damagerEntity).getShooter();
        }

        if (damagerEntity instanceof Tameable) {
            AnimalTamer owner = ((Tameable) damagerEntity).getOwner();
            if (owner instanceof Entity) {
                damagerEntity = (Entity) owner;
            }
        }

        if (targetEntity instanceof Tameable) {
            AnimalTamer owner = ((Tameable) targetEntity).getOwner();
            if (owner instanceof Entity) {
                targetEntity = (Entity) owner;
            }
        }

        if (!(targetEntity instanceof Player)
                || !(damagerEntity instanceof Player)) {
            return;
        }

        Player target = (Player) targetEntity;
        Player damager = (Player) damagerEntity;
        Game game = stage.getGame();
        if (game.contains(damager) || game.contains(target)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Tameable)) {
            return;
        }

        AnimalTamer owner = ((Tameable)entity).getOwner();
        if (!(owner instanceof Player)) {
            return;
        }

        if (stage.getGame().contains((Player) owner)) {
            event.setCancelled(true);
        }
    }
}