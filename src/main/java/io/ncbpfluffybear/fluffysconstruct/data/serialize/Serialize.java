package io.ncbpfluffybear.fluffysconstruct.data.serialize;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Serialize {

    public static Location parseLocation(String locationKey) {
        String[] splitLocation = locationKey.split(":");
        return new Location(Bukkit.getWorld(UUID.fromString(splitLocation[0])),
                Integer.parseInt(splitLocation[1]), Integer.parseInt(splitLocation[2]), Integer.parseInt(splitLocation[3])
        ); // World:X:Y:Z
    }

    public static String serializeLocation(Location loc) {
        return loc.getWorld().getUID() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

    public static Set<Location> parseLocations(@Nullable String locationKeys) {
        Set<Location> locations = new HashSet<>();
        if (locationKeys == null) return locations;
        String[] split = locationKeys.split(",");
        for (String toParse : split) {
            locations.add(parseLocation(toParse));
        }
        return locations;
    }

    public static String serializeLocations(Set<Location> locations) {
        StringBuilder serialized = new StringBuilder();
        int count = 0;
        for (Location location : locations) {
            serialized.append(serializeLocation(location));
            if (++count != locations.size()) {
                serialized.append(",");
            }
        }
        return serialized.toString();
    }

    public static String addLocation(String locations, Location toAdd) {
        Set<Location> locList = parseLocations(locations);
        locList.add(toAdd);
        return serializeLocations(locList);
    }

    public static String removeLocation(String locations, Location toRemove) {
        Set<Location> locList = parseLocations(locations);
        locList.remove(toRemove);
        return serializeLocations(locList);
    }

    /**
     * Serializes pairs of items and slots into a
     * Base64 encoded string
     */
    public static String serializeItems(Map<Integer, ItemStack> toSave) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream); // For serializing/deserializing ItemStacks

            for (Map.Entry<Integer, ItemStack> slotData : toSave.entrySet()) {
                dataOutput.writeInt(slotData.getKey());
                dataOutput.writeObject(slotData.getValue());
            }

            dataOutput.close(); // Flushes as well
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Map<Integer, ItemStack> deserializeItems(String encoded) {
        Map<Integer, ItemStack> items = new HashMap<>();
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(encoded));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream); // For serializing/deserializing ItemStacks

            while (dataInput.available() != 0) {
                items.put(dataInput.readInt(), (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return items;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deserialize items.", e);
        }
    }


}
