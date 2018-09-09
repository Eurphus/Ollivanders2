package net.pottercraft.Ollivanders2.Spell;

import net.pottercraft.Ollivanders2.Ollivanders2;
import org.bukkit.entity.Player;

/**
 * The super class for all ItemStack transfigurations.  This cannot be used on Entities or Blocks.
 *
 * @since 2.2.6
 * @see BlockTransfigurationSuper
 * @author Azami7
 */
public class ItemStackTransfigurationSuper extends BlockTransfigurationSuper
{
   /**
    * Default constructor for use in generating spell text.  Do not use to cast the spell.
    */
   public ItemStackTransfigurationSuper (O2SpellType type)
   {
      super(type);
   }

   /**
    * Constructor for casting a transfiguration spell.
    *
    * @param plugin
    * @param player
    * @param type
    * @param rightWand
    */
   public ItemStackTransfigurationSuper (Ollivanders2 plugin, Player player, O2SpellType type, Double rightWand)
   {
      super(plugin, player, type, rightWand);

      permanent = true;
      radius = 1;
   }
}
