package net.pottercraft.Ollivanders2.Potion;

import net.pottercraft.Ollivanders2.Effect.AWAKE;
import net.pottercraft.Ollivanders2.Item.O2ItemType;
import net.pottercraft.Ollivanders2.Ollivanders2;
import net.pottercraft.Ollivanders2.Ollivanders2API;
import net.pottercraft.Ollivanders2.Player.O2Player;
import org.bukkit.Color;
import org.bukkit.entity.Player;

/**
 * Wideye potion prevents the drinker from falling asleep.
 *
 * @author Azami7
 * @since 2.2.8
 */
public class WIDEYE_POTION extends O2Potion
{
   /**
    * Constructor
    *
    * @param plugin a callback to the plugin
    */
   public WIDEYE_POTION (Ollivanders2 plugin)
   {
      super(plugin);

      potionType = O2PotionType.WIDEYE_POTION;
      potionLevel = PotionLevel.BEGINNER;

      ingredients.put(O2ItemType.GROUND_SNAKE_FANGS, 3);
      ingredients.put(O2ItemType.BILLYWIG_STING_SLIME, 6);
      ingredients.put(O2ItemType.WOLFSBANE, 2);
      ingredients.put(O2ItemType.STANDARD_POTION_INGREDIENT, 4);

      text = "The Wideye Potion, also known as the Awakening Potion, is used to prevent the drinker from falling asleep.";

      potionColor = Color.fromRGB(13, 55, 13);
   }

   @Override
   public void drink (O2Player o2p, Player player)
   {
      AWAKE effect = new AWAKE(p, duration, player.getUniqueId());
      Ollivanders2API.getPlayers().playerEffects.addEffect(effect);

      player.sendMessage(Ollivanders2.chatColor + "You feel alert.");
   }
}
