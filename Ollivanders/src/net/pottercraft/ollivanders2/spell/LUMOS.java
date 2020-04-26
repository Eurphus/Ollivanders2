package net.pottercraft.ollivanders2.spell;

import net.pottercraft.ollivanders2.Ollivanders2;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 * Gives night vision for an amount of time depending on the player's spell level.
 *
 * @author lownes
 * @author Azami7
 */
public final class LUMOS extends Charms
{
   /**
    * Default constructor for use in generating spell text.  Do not use to cast the spell.
    */
   public LUMOS ()
   {
      super();

      spellType = O2SpellType.LUMOS;

      flavorText = new ArrayList<String>() {{
         add("If in any doubt about your abilities you would do better to buy yourself a magic lantern.");
         add("The Wand-Lighting Charm");
         add("\"Ron, where are you? Oh this is stupid - lumos!\"  She illuminated her wand and directed its narrow beam across the path. Ron was lying sprawled on the ground.");
         add("The Wand-Lighting Charm is simple, but requires concentration.  Take care not to accidentally set your wand alight as damage of this kind can be permanent.");
      }};

      text = "Gives night vision.";
   }

   /**
    * Constructor.
    *
    * @param plugin a callback to the MC plugin
    * @param player the player who cast this spell
    * @param rightWand which wand the player was using
    */
   public LUMOS (Ollivanders2 plugin, Player player, Double rightWand)
   {
      super(plugin, player, rightWand);

      spellType = O2SpellType.LUMOS;
      setUsesModifier();
   }

   @Override
   public void checkEffect ()
   {
      player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (int) (usesModifier * 1200), 1), true);
      kill();
   }
}