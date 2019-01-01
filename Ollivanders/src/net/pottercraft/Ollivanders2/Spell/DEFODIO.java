package net.pottercraft.Ollivanders2.Spell;

import java.util.ArrayList;
import java.util.List;

import net.pottercraft.Ollivanders2.Ollivanders2API;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.pottercraft.Ollivanders2.StationarySpell.COLLOPORTUS;
import net.pottercraft.Ollivanders2.Ollivanders2;
import net.pottercraft.Ollivanders2.StationarySpell.StationarySpellObj;

/**
 * Mines a line of blocks of length depending on the player's level in this spell.
 *
 * @version Ollivanders2
 * @author lownes
 * @author Azami7
 */
public final class DEFODIO extends Charms
{
   private int depth;

   /**
    * Default constructor for use in generating spell text.  Do not use to cast the spell.
    */
   public DEFODIO ()
   {
      super();

      spellType = O2SpellType.DEFODIO;

      flavorText = new ArrayList<String>() {{
         add("The Gouging Spell enables a witch or wizard to carve through earth and stone with ease. From budding Herbologists digging for Snargaluff seedlings to treasure-hunting curse breakers uncovering ancient wizard tombs, the Gouging Spell makes all manner of heavy labour a matter of pointing a wand.");
         add("The Gouging Charm");
      }};

      text = "Mines a line of blocks.";
   }

   /**
    * Constructor.
    *
    * @param plugin a callback to the MC plugin
    * @param player the player who cast this spell
    * @param rightWand which wand the player was using
    */
   public DEFODIO (Ollivanders2 plugin, Player player, Double rightWand)
   {
      super(plugin, player, rightWand);

      spellType = O2SpellType.DEFODIO;
      setUsesModifier();
      depth = (int) usesModifier;
   }

   public void checkEffect ()
   {
      Location newl = location.clone();
      Location forward = newl.add(vector);
      Block block = forward.getBlock();
      List<Block> tempBlocks = p.getTempBlocks();
      if (depth > 0)
      {
         for (StationarySpellObj stat : Ollivanders2API.getStationarySpells().getActiveStationarySpells())
         {
            if (stat instanceof COLLOPORTUS)
            {
               if (stat.isInside(block.getLocation()))
               {
                  return;
               }
            }
         }
         if (block.getType() != Material.BEDROCK && !tempBlocks.contains(block))
         {
            if (block.breakNaturally())
            {
               depth--;
            }
         }
         else
         {
            kill();
         }
      }
      move();
   }
}