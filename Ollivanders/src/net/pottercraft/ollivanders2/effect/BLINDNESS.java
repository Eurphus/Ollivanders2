package net.pottercraft.ollivanders2.effect;

import net.pottercraft.ollivanders2.Ollivanders2;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Make a player blind
 *
 * @author Azami7
 * @since 2.2.9
 */
public class BLINDNESS extends PotionEffectSuper
{
   public BLINDNESS (Ollivanders2 plugin, Integer duration, UUID pid)
   {
      super(plugin, duration, pid);

      effectType = O2EffectType.BLINDNESS;
      potionEffectType = PotionEffectType.BLINDNESS;
      informousText = legilimensText = "cannot see";

      strength = 1;

      divinationText.add("shall be cursed");
      divinationText.add("shall be afflicted in the mind");
      divinationText.add("will become unable to see");
      divinationText.add("will be struck by a terrible affliction");
   }
}