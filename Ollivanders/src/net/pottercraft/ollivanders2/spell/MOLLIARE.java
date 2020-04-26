package net.pottercraft.ollivanders2.spell;

import net.pottercraft.ollivanders2.Ollivanders2API;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.pottercraft.ollivanders2.Ollivanders2;
import net.pottercraft.ollivanders2.stationaryspell.O2StationarySpellType;

import java.util.ArrayList;

/**
 * Eliminates all fall damage.
 *
 * @author lownes
 * @author Azami7
 */
public final class MOLLIARE extends Charms
{
   /**
    * Default constructor for use in generating spell text.  Do not use to cast the spell.
    */
   public MOLLIARE ()
   {
      super();

      spellType = O2SpellType.MOLLIARE;

      flavorText = new ArrayList<String>() {{
         add("The Cushioning Charm.");
         add("Harry felt himself glide back toward the ground as though weightless, landing painlessly on the rocky passage floor.");
      }};

      text = "Molliare softens the ground in a radius around the site.  All fall damage will be negated in this radius.";
   }

   /**
    * Constructor.
    *
    * @param plugin a callback to the MC plugin
    * @param player the player who cast this spell
    * @param rightWand which wand the player was using
    */
   public MOLLIARE (Ollivanders2 plugin, Player player, Double rightWand)
   {
      super(plugin, player, rightWand);

      spellType = O2SpellType.MOLLIARE;
      setUsesModifier();
   }

   @Override
   public void checkEffect ()
   {
      move();
      Material targetBlockType = getBlock().getType();
      if (targetBlockType != Material.AIR && targetBlockType != Material.FIRE && targetBlockType != Material.WATER)
      {
         int duration = (int) (usesModifier * 1200);
         net.pottercraft.ollivanders2.stationaryspell.MOLLIARE molliare = new net.pottercraft.ollivanders2.stationaryspell.MOLLIARE(p, player.getUniqueId(), location, O2StationarySpellType.MOLLIARE, 5, duration);
         molliare.flair(10);
         Ollivanders2API.getStationarySpells().addStationarySpell(molliare);
         kill();
      }
   }
}