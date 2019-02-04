package net.pottercraft.Ollivanders2.Potion;

import net.pottercraft.Ollivanders2.Effect.HIGHER_SKILL;
import net.pottercraft.Ollivanders2.Item.O2ItemType;
import net.pottercraft.Ollivanders2.Ollivanders2API;
import net.pottercraft.Ollivanders2.Player.O2Player;
import net.pottercraft.Ollivanders2.Ollivanders2;
import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * All spells cast are twice as powerful.
 *
 * @author Azami7
 * @author cakenggt
 */
public final class BARUFFIOS_BRAIN_ELIXIR extends O2Potion
{
   /**
    * Constructor
    *
    * @param plugin a callback to the plugin
    */
   public BARUFFIOS_BRAIN_ELIXIR (Ollivanders2 plugin)
   {
      super(plugin);

      potionType = O2PotionType.BARUFFIOS_BRAIN_ELIXIR;
      potionLevel = PotionLevel.EXPERT;

      ingredients.put(O2ItemType.RUNESPOOR_EGG, 1);
      ingredients.put(O2ItemType.GINGER_ROOT, 2);
      ingredients.put(O2ItemType.STANDARD_POTION_INGREDIENT, 2);

      text = "Baruffio's Brain Elixir is a potion that increases the taker's brain power. All spells cast are twice as powerful.";

      flavorText.add("\"I've performed tests on the potion sample you collected. My best guess is that it was supposed to be Baruffio's Brain Elixir. Now, that's a potion which doesn't work at the best of times, but whoever brewed this was seriously incompetent! Forget boosting one's brain; this concoction would most likely melt it!\" —Gethsemane Prickle");
      potionColor = Color.fromRGB(255, 251, 222);
   }

   public void drink (O2Player o2p, Player player)
   {
      HIGHER_SKILL effect = new HIGHER_SKILL(p, duration, player.getUniqueId());
      Ollivanders2API.getPlayers().playerEffects.addEffect(effect);

      player.sendMessage(Ollivanders2.chatColor + "You feel clarity of thought.");
   }
}