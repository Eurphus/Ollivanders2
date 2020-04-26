package net.pottercraft.ollivanders2.effect;

import java.util.UUID;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.TargetedDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.*;

import net.pottercraft.ollivanders2.Ollivanders2;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Change the the form of a player in to another entity.
 *
 * Requires libDisguises
 *
 * @author Azami7
 * @since 2.2.8
 */
public abstract class ShapeShiftSuper extends O2Effect
{
   boolean transformed = false;

   TargetedDisguise disguise;
   EntityType form;
   LivingWatcher watcher;

   /**
    * Constructor
    *
    * @param plugin a callback to the MC plugin
    * @param duration the duration of the effect
    * @param pid the player this effect acts on
    */
   public ShapeShiftSuper (Ollivanders2 plugin, Integer duration, UUID pid)
   {
      super(plugin, duration, pid);
   }

   /**
    * Handle upkeep of this effect.
    */
   @Override
   public void checkEffect ()
   {
      if (!Ollivanders2.libsDisguisesEnabled)
      {
         transformed = false;

         kill();
         return;
      }

      if (!permanent)
      {
         age(1);
      }

      upkeep();
   }

   /**
    * Do the upkeep for this specific shape shift effect.
    */
   protected void upkeep ()
   {
      // by default, do nothing, this needs to be written in the child classes
   }

   /**
    * Transfigure the player to the new form.
    */
   protected void transform ()
   {
      Player target = p.getServer().getPlayer(targetID);

      if (target != null && form != null)
      {
         if (Ollivanders2.debug)
         {
            p.getLogger().info("transforming " + target.getName());
         }

         // disguisePlayer the player
         DisguiseType disguiseType = DisguiseType.getType(form);
         disguise = new MobDisguise(disguiseType);
         watcher = (LivingWatcher) disguise.getWatcher();

         customizeWatcher();

         DisguiseAPI.disguiseToAll(target, disguise);
         transformed = true;
      }
      else
      {
         kill();
      }
   }

   /**
    * Transfigure the player back to human form and kill this effect.
    */
   @Override
   public void kill ()
   {
      restore();

      kill = true;
   }

   /**
    * Restore the player back to their human form.
    */
   public void restore ()
   {
      if (transformed)
      {
         if (disguise != null)
         {
            Entity entity = disguise.getEntity();
            try
            {
               DisguiseAPI.undisguiseToAll(entity);
            }
            catch (Exception e)
            {
               // in case entity no longer exists
            }
         }

         transformed = false;
      }
   }

   /**
    * Override this to set the specific form this player will transfigure in to.
    */
   protected void customizeWatcher ()
   {

   }
}