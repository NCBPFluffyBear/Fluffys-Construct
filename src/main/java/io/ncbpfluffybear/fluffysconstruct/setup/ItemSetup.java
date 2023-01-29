package io.ncbpfluffybear.fluffysconstruct.setup;

import io.ncbpfluffybear.fluffysconstruct.items.ItemRepository;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;

/**
 * Registers specified items. Items should be created in
 * {@link ItemList}.
 */
public class ItemSetup {

    private final ItemRepository repository;

    public ItemSetup(ItemRepository repository) {
        this.repository = repository;
    }

    /**
     * WARNING: Changing the registration order will affect item IDs!
     */
    public void register() {
        repository.registerItem(ItemList.GROUT);
        repository.registerItem(ItemList.SEARED_BRICKS);
        repository.registerItem(ItemList.FURNACE);
    }

}
