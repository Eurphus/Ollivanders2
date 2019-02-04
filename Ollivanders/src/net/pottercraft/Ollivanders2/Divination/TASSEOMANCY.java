package net.pottercraft.Ollivanders2.Divination;

import net.pottercraft.Ollivanders2.Ollivanders2;
import org.bukkit.entity.Player;

/**
 * Tasseomancy is the art of reading tea leaves to predict events in the future.
 * http://harrypotter.wikia.com/wiki/Tessomancy
 *
 * @author Azami7
 * @since 2.2.9
 */
public class TASSEOMANCY extends O2Divination
{
   public TASSEOMANCY (Ollivanders2 plugin, Player pro, Player tar, Integer exp)
   {
      super(plugin, pro, tar, exp);

      divintationType = O2DivinationType.TASSEOMANCY;
      maxAccuracy = 20;

      prophecyPrefix.add("The falcon ... a deadly enemy,");
      prophecyPrefix.add("The club ... an attack,");
      prophecyPrefix.add("The skull ... danger,");
      prophecyPrefix.add("The Grim ... death,");
      prophecyPrefix.add("The ring ... confusion,");
      prophecyPrefix.add("The snake ... enmity,");
      prophecyPrefix.add("The acorn ... good fortune,");
      prophecyPrefix.add("The mountain ... a hindrance,");
      prophecyPrefix.add("The cross ... trials and suffering,");
      prophecyPrefix.add("The sun ... great happiness,");
   }
}
