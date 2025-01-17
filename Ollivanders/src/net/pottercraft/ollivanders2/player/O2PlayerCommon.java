package net.pottercraft.ollivanders2.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.pottercraft.ollivanders2.Ollivanders2;
import net.pottercraft.ollivanders2.Ollivanders2API;
import net.pottercraft.ollivanders2.common.Ollivanders2Common;
import net.pottercraft.ollivanders2.item.O2ItemType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Player common functions.
 *
 * @author Azami7
 */
public final class O2PlayerCommon
{
   final Ollivanders2 p;

   final Ollivanders2Common common;

   /**
    * Constructor
    *
    * @param plugin a reference to the MC plugin using these common functions
    */
   public O2PlayerCommon(@NotNull Ollivanders2 plugin)
   {
      p = plugin;
      common = new Ollivanders2Common(plugin);
   }

   private static final List<EntityType> commonAnimagusShapes = new ArrayList<>()
   {{
      add(EntityType.COW);
      add(EntityType.PIG);
      add(EntityType.HORSE);
      add(EntityType.SHEEP);
      add(EntityType.RABBIT);
      add(EntityType.MULE);
      add(EntityType.DONKEY);
      add(EntityType.CAT);
      add(EntityType.WOLF);
      add(EntityType.LLAMA);
      add(EntityType.FOX);
   }};

   private static final List<EntityType> rareAnimagusShapes = new ArrayList<>()
   {{
      add(EntityType.OCELOT);
      add(EntityType.POLAR_BEAR);
      add(EntityType.TRADER_LLAMA);
      add(EntityType.PANDA);
      add(EntityType.TURTLE);
      add(EntityType.IRON_GOLEM);
   }};

   private static final List<EntityType> hostileAnimagusShapes = new ArrayList<>()
   {{
      add(EntityType.SPIDER);
      add(EntityType.SLIME);
      add(EntityType.CAVE_SPIDER);
      add(EntityType.CREEPER);
      add(EntityType.SILVERFISH);
      add(EntityType.SHULKER);
      add(EntityType.HOGLIN);
      add(EntityType.PIGLIN);
      add(EntityType.STRIDER);
   }};

   /**
    * Take an integer and get the corresponding year
    *
    * @param year The year; must be between 1 and 7
    * @return The corresponding year or null if invalid input
    */
   @Nullable
   public static Year intToYear(int year)
   {
      switch (year)
      {
         case 1:
            return Year.YEAR_1;
         case 2:
            return Year.YEAR_2;
         case 3:
            return Year.YEAR_3;
         case 4:
            return Year.YEAR_4;
         case 5:
            return Year.YEAR_5;
         case 6:
            return Year.YEAR_6;
         case 7:
            return Year.YEAR_7;
         default:
            return null;
      }
   }

   /**
    * Get the animagus form for this player
    *
    * @return a list of all possible EntityTypes for animagus form
    */
   @NotNull
   public EntityType getAnimagusForm(@NotNull UUID pid)
   {
      // 1% chance to get a rare form
      int form = Math.abs(pid.hashCode() % 100);
      if (form < 1)
      {
         form = Math.abs(pid.hashCode() % rareAnimagusShapes.size());
         return rareAnimagusShapes.get(form);
      }

      // if using hostile mob animagi, 10% chance of getting a hostile mob form
      form = Math.abs(pid.hashCode() % 10);
      if (form < 1)
      {
         form = Math.abs(pid.hashCode() % hostileAnimagusShapes.size());
         return hostileAnimagusShapes.get(form);
      }

      // equal chance for common forms
      form = Math.abs(pid.hashCode() % commonAnimagusShapes.size());
      return commonAnimagusShapes.get(form);
   }

   /**
    * Determine if an EntityType is an allowed Animagus form.
    *
    * @param form the animagus form to check
    * @return true if this is an allowed form, false otherwise
    */
   public boolean isAllowedAnimagusForm(@NotNull EntityType form)
   {
      if (Ollivanders2.useHostileMobAnimagi)
      {
         if (hostileAnimagusShapes.contains(form))
            return true;
      }

      if (rareAnimagusShapes.contains(form))
         return true;

      return commonAnimagusShapes.contains(form);
   }

   /**
    * Checks what kind of wand a player holds in their primary hand. Returns a value based on the
    * wand and it's relation to the player.
    *
    * @param player player being checked. The player must be holding a wand.
    * @return 2 - the wand is not player's type AND/OR is not allied to player.<p>
    * 1 - the wand is player's type and is allied to player OR the wand is the elder wand and is not allied to player.<p>
    * 0.5 - the wand is the elder wand and it is allied to player.
    * @assumes player not null, player holding a wand (holdsWand has already been checked)
    */
   public double wandCheck(@NotNull Player player)
   {
      return wandCheck(player, EquipmentSlot.HAND);
   }

   /**
    * Checks what kind of wand a player holds. Returns a value based on the wand and it's relation to the player.
    *
    * @param player player being checked. The player must be holding a wand.
    * @param hand   the hand that is holding the wand to check.
    * @return 2 - The wand is not player's type AND/OR is not allied to player.<p>
    * 1 - The wand is player's type and is allied to player OR the wand is the elder wand and is not allied to player.<p>
    * 0.5 - The wand is the elder wand and it is allied to player.
    * @assumes player not null, player is holding a wand in the equipment slot passed in
    * @since 2.2.7
    */
   public double wandCheck(@NotNull Player player, @NotNull EquipmentSlot hand)
   {
      ItemStack item;

      if (hand == EquipmentSlot.HAND)
      {
         item = player.getInventory().getItemInMainHand();
      }
      else
      {
         item = player.getInventory().getItemInOffHand();
      }

      return doWandCheck(player, item);
   }

   /**
    * Checks what kind of wand a player holds. Returns a value based on the wand and it's relation to the player.
    *
    * @param player the player to check
    * @param itemStack the item to check
    * @return 2 - The wand is not player's type
    * 1 - The wand is player's type
    * 0.5 - The wand is the elder wand
    * -1 - The player is not holding a wand
    * @since 2.2.7
    */
   private double doWandCheck(@NotNull Player player, @NotNull ItemStack itemStack)
   {
      ItemMeta meta = itemStack.getItemMeta();
      if (meta == null)
         // not an ollivanders item
         return -1;

      if (O2ItemType.ELDER_WAND.isItemThisType(itemStack))
         // elder wand
         return 0.5;

      if (!O2ItemType.WAND.isItemThisType(itemStack))
         // not a wand
         return -1;

      O2Player o2p = Ollivanders2API.getPlayers().getPlayer(player.getUniqueId());
      if (o2p == null)
      {
         // not a player
         common.printDebugMessage("O2Player is null", null, null, true);
         return -1;
      }

      if (!(Ollivanders2API.getItems().getWands().isDestinedWand(o2p, itemStack)))
      {
         // not the player's destined wand
         common.printDebugMessage("O2PlayerCommon.doWandCheck: player holds a wand which is not their destined wand", null, null, false);
         return 2;
      }

      // player's destined wand
      common.printDebugMessage("O2PlayerCommon.doWandCheck: player holds their destined wand", null, null, false);
      return 1;
   }
}