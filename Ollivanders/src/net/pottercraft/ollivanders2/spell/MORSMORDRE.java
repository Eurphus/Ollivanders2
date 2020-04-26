package net.pottercraft.ollivanders2.spell;

import net.pottercraft.ollivanders2.O2MagicBranch;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;

import net.pottercraft.ollivanders2.Ollivanders2;

import java.util.ArrayList;

/**
 * Cast the dark mark in the sky.
 *
 * @author Azami7
 */
public final class MORSMORDRE extends Pyrotechnia
{
   /**
    * Default constructor for use in generating spell text.  Do not use to cast the spell.
    */
   public MORSMORDRE()
   {
      super();

      branch = O2MagicBranch.DARK_ARTS;
      spellType = O2SpellType.MORSMORDRE;

      flavorText = new ArrayList<String>()
      {{
         add("\"Should the Dark Mark appear over any dwelling place or other  building, DO NOT ENTER, but contact the Auror office immediately.\" -Ministry of Magic");
         add("Then he realised it was a colossal skull, comprised of what looked like emerald stars, with a serpent protruding from its mouth like a tongue. As they watched, it rose higher and higher, blazing in a haze of greenish smoke, etched against the black sky like a new constellation.");
      }};

      text = "Conjures the Dark Mark in the sky.";
   }

   /**
    * Constructor.
    *
    * @param plugin a callback to the MC plugin
    * @param player the player who cast this spell
    * @param rightWand which wand the player was using
    */
   public MORSMORDRE (Ollivanders2 plugin, Player player, Double rightWand)
   {
      super(plugin, player, rightWand);

      branch = O2MagicBranch.DARK_ARTS;
      spellType = O2SpellType.MORSMORDRE;
      initSpell();

      fireworkColors = new ArrayList<>();
      fireworkColors.add(Color.GREEN);
      fireworkType = Type.CREEPER;
      maxFireworks = 20;
   }
}
