package io.ncbpfluffybear.fluffysconstruct.repository;

import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SmelteryRepository {

    private final Map<UUID, SmelterySystem> smelteries;

    public SmelteryRepository() {
        this.smelteries = new HashMap<>();
    }

    public SmelterySystem newSmeltery(Location location) {
        UUID uuid = UUID.randomUUID();
        SmelterySystem newSystem = new SmelterySystem(uuid, location);
        smelteries.put(uuid, newSystem);
        return newSystem;
    }

    public SmelterySystem getSystem(String uuid) {
        return smelteries.get(UUID.fromString(uuid));
    }
}
