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

    public void register() {
        repository.registerItem(ItemList.GROUT);
        repository.registerItem(ItemList.SEARED_BRICKS);
        repository.registerItem(ItemList.FURNACE);
        repository.registerItem(ItemList.CONTROLLER);
        repository.registerItem(ItemList.SEARED_TANK);
    }

}
