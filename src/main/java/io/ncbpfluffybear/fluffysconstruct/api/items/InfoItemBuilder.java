package io.ncbpfluffybear.fluffysconstruct.api.items;

import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Used to build info items in GUIs
 */
public class InfoItemBuilder {

    private Material material;
    private final String nameTemplate;
    private final String[] loreTemplate;

    private Object[] nameArgs;
    private Object[] loreArgs;

    public InfoItemBuilder(Material defaultMaterial, String nameTemplate, String... loreTemplate) {
        this.material = defaultMaterial;
        this.nameTemplate = nameTemplate;
        this.loreTemplate = loreTemplate;
        this.nameArgs = new String[0];
        this.loreArgs = new String[0];
    }

    public InfoItemBuilder setNameArgs(Object... nameArgs) {
        this.nameArgs = nameArgs;
        return this;
    }

    public InfoItemBuilder setLoreArgs(Object... loreArgs) {
        this.loreArgs = loreArgs;
        return this;
    }

    public InfoItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemStack build() {
        return new CustomItem(this.material, StringUtils.format(nameTemplate, nameArgs), StringUtils.format(loreTemplate, loreArgs));
    }
}
