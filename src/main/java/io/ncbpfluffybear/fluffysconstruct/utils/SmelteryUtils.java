package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockData;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

public class SmelteryUtils {

    public SmelteryUtils() throws InstantiationException {
        throw new InstantiationException();
    }

    public static SmelterySystem createSystem(Location location) {
        SmelterySystem existingSystem = getSystem(location);
        if (existingSystem != null) {
            return existingSystem;
        }

        SmelterySystem newSystem = FCPlugin.getSmelteryRepository().newSmeltery(location);
        BlockDataRepository.getOrCreateDataAt(location).set(Keys.SYSTEM_UUID, PersistentDataType.STRING, newSystem.getUuid().toString());
        return newSystem;
    }

    /**
     * Requires that the location is a valid {@link io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery.Controller}
     */
    public static SmelterySystem getSystem(Location location) {
        if (!BlockDataRepository.hasData(location)) {
            return null;
        }
        BlockData data = BlockDataRepository.getDataAt(location);
        if (!data.has(Keys.SYSTEM_UUID, PersistentDataType.STRING)) {
            return null;
        }
        return FCPlugin.getSmelteryRepository().getSystem(data.get(Keys.SYSTEM_UUID, PersistentDataType.STRING));
    }

    public static SmelterySystem getSystem(String uuid) {
        return FCPlugin.getSmelteryRepository().getSystem(uuid);
    }

}
