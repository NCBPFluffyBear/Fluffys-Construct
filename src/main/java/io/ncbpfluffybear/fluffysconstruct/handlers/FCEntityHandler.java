package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataType;


public class FCEntityHandler implements Listener {

    public FCEntityHandler() {
    }

    /**
     * Prevent players from removing equipment from display stands
     */
    @EventHandler
    private void onArmorStandEquipmentRemove(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity.getType() != EntityType.ARMOR_STAND) {
            return;
        }

        Byte isFc = entity.getPersistentDataContainer().get(Constants.FC_ENTITY_KEY, PersistentDataType.BYTE);
        if (isFc != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void onFallingBlockInteract(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity.getType() != EntityType.FALLING_BLOCK) {
            return;
        }

        Byte isFc = entity.getPersistentDataContainer().get(Constants.FC_ENTITY_KEY, PersistentDataType.BYTE);
        if (isFc != null) {
            entity.remove();
        }
    }
}
