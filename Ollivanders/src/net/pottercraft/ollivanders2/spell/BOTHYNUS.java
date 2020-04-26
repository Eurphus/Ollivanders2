package net.pottercraft.ollivanders2.spell;

import java.util.ArrayList;

import net.pottercraft.ollivanders2.O2MagicBranch;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

import net.pottercraft.ollivanders2.Ollivanders2;

/**
 * Shoots yellow star fireworks in to the air.
 *
 * @author Azami7
 * @see Pyrotechnia
 */
public final class BOTHYNUS extends Pyrotechnia
{
   /**
    * Default constructor for use in generating spell text.  Do not use to cast the spell.
    */
   public BOTHYNUS()
   {
      super();

      spellType = O2SpellType.BOTHYNUS;
      branch = O2MagicBranch.CHARMS;

      text = "Creates one or more yellow star fireworks.";
   }

   /**
    * Constructor.
    *
    * @param plugin a callback to the MC plugin
    * @param player the player who cast this spell
    * @param rightWand which wand the player was using
    */
   public BOTHYNUS (Ollivanders2 plugin, Player player, Double rightWand)
   {
      super(plugin, player, rightWand);
      spellType = O2SpellType.BOTHYNUS;
      branch = O2MagicBranch.CHARMS;

      initSpell();

      fireworkColors = new ArrayList<>();
      fireworkColors.add(Color.YELLOW);
      fireworkType = Type.STAR;

      setMaxFireworks(10);
   }
}
