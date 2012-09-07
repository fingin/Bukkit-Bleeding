package org.bukkit.inventory.meta;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;

/**
 * This type represents the storage mechanism for auxiliary item data.
 * An implementation will handle the creation and appliance for ItemMeta.
 * This class should not be implemented by a plugin in a live environment.
 */
public interface ItemMeta extends Cloneable, ConfigurationSerializable {
    ItemMeta clone();

    /**
     * Checks for existence of the specified enchantment
     * @param ench enchantment to check
     * @return true if this enchantment exists for this meta
     */
    boolean hasEnchant(Enchantment ench);

    /**
     * Checks for the level of the specified enchantment
     * @param ench enchantment to check
     * @return The level that the specified enchantment has, or 0 if none
     */
    int getEnchantLevel(Enchantment ench);

    /**
     * This method gets a copy the enchantments in this ItemMeta
     * @return An immutable copy of the enchantments
     */
    Map<Enchantment, Integer> getEnchants();

    /**
     * This method adds the specified enchantment to this item meta
     * @param ench Enchantment to add
     * @param level Level for the enchantment
     * @param ignoreRestrictions this indicates the enchantment should be applied, ignoring level limits and item type
     * @return true if the item meta changed as a result of this call, false otherwise
     */
    boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions);

    /**
     * This method removes the specified enchantment from this item meta
     * @param ench Enchantment to remove
     * @return true if the item meta changed as a result of this call, false otherwise
     */
    boolean removeEnchant(Enchantment ench);
}
