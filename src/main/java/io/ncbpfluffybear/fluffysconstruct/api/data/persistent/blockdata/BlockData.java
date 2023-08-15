package io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.DirtyType;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockData implements PersistentDataContainer {

    private final Map<String, Object> contents;
    private Location location;

    public BlockData(Location location) {
        this.contents = new HashMap<>();
        this.location = location;
        FCPlugin.getPersistenceUtils().markBlockDirty(this.location, DirtyType.BLOCKDATA);
    }


    /**
     * For some reason, the constructor deserialization doesn't work. I will cry.
     */
    public BlockData(Map<String, Object> serialized) {
        contents = new HashMap<>(serialized);
    }

    @Override
    public <T, Z> void set(@Nonnull NamespacedKey key, @Nonnull PersistentDataType<T, Z> type, @Nonnull Z value) {
        contents.put(key.getKey(), value);
        FCPlugin.getPersistenceUtils().markBlockDirty(this.location, DirtyType.BLOCKDATA);
    }

    @Override
    public <T, Z> boolean has(@Nonnull NamespacedKey key, @Nonnull PersistentDataType<T, Z> type) {
        return contents.containsKey(key.getKey());
    }

    @Nullable
    @Override
    public <T, Z> Z get(@Nonnull NamespacedKey key, @Nonnull PersistentDataType<T, Z> type) {
        return type.getComplexType().cast(contents.get(key.getKey()));
    }

    @Nonnull
    @Override
    public <T, Z> Z getOrDefault(@Nonnull NamespacedKey key, @Nonnull PersistentDataType<T, Z> type, @Nonnull Z defaultValue) {
        Z result = get(key, type);
        return result == null ? defaultValue : result;
    }

    @Override
    public void remove(@Nonnull NamespacedKey key) {
        contents.remove(key.getKey());
        FCPlugin.getPersistenceUtils().markBlockDirty(this.location, DirtyType.BLOCKDATA);
    }

    @Override
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public Map<String, Object> getAll() {
        return contents;
    }

    @Nonnull
    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        throw new NotImplementedException();
    }

    @Nonnull
    @Override
    public Set<NamespacedKey> getKeys() {
        throw new NotImplementedException();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Not using {@link org.bukkit.configuration.serialization.ConfigurationSerializable} in case package location changes
     */
    @Nonnull
    public Map<String, Object> serialize() {
        return contents;
    }
}
