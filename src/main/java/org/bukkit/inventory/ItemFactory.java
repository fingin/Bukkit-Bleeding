package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemFactory {

    /**
     * This creates a new item meta for the item stack.
     * This is the same as calling {@link #getItemMeta(Material)} with {@link ItemStack#getType()}
     * @param itemstack The stack to check material of
     * @return a new ItemMeta that could be applied to the specified ItemStack.
     */
    ItemMeta getItemMeta(final ItemStack itemstack);

    /**
     * This creates a new item meta for the material.
     * @param material The material to consider as base for the meta
     * @return a new ItemMeta that could be applied to an item stack of the specified material
     */
    ItemMeta getItemMeta(final Material material);

    /**
     * This method checks the item meta to confirm that it is applicable to the specified ItemStack.
     * @param meta Meta to check
     * @param stack Item that meta will be applied to
     * @return true if the meta can be applied, false otherwise
     */
    boolean isValidMeta(final ItemMeta meta, final ItemStack stack);

    /**
     * This method is used to compare two item metas.
     * @param meta1 First meta to compare
     * @param meta2 Second meta to compare
     * @return true if both meta would change an item
     */
    boolean equals(ItemMeta meta1, ItemMeta meta2);
}
