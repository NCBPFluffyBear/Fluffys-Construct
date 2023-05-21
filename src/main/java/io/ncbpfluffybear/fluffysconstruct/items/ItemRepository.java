package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRepository {

    private final Map<Integer, FCItem> fcItems;
    private final Map<String, Integer> keyMap; // Translates typeable strings to int IDs

    public ItemRepository() {
        fcItems = new HashMap<>();
        keyMap = new HashMap<>();
    }

    /**
     * Registers an item by assigning it an ID. ID is automatically assigned.
     * @param item the {@link FCItem} to register
     */
    public void registerItem(FCItem item) {
        if (fcItems.containsKey(item.getId())) {
            ChatUtils.logError("Item " + item);
            return;
        }
        this.fcItems.put(item.getId(), item);
        this.keyMap.put(item.getKey(), item.getId());
    }

    @Nullable
    public FCItem getItemById(int id) {
        try {
            return this.fcItems.get(id);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Gets an item by its alphabetical name.
     * This should be only used for interfacing with users,
     * and not internally.
     */
    @Nullable
    public FCItem getItemByKey(String key) {
        int id = this.keyMap.getOrDefault(key, -1);
        if (id == -1) {
            return null;
        }

        return getItemById(id);
    }

}
