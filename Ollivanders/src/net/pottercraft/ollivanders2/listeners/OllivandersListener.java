package net.pottercraft.ollivanders2.listeners;

import net.pottercraft.ollivanders2.Ollivanders2;
import net.pottercraft.ollivanders2.Ollivanders2API;
import net.pottercraft.ollivanders2.Ollivanders2OwlPost;
import net.pottercraft.ollivanders2.OllivandersSchedule;
import net.pottercraft.ollivanders2.common.Ollivanders2Common;
import net.pottercraft.ollivanders2.effect.O2EffectType;
import net.pottercraft.ollivanders2.item.O2ItemType;
import net.pottercraft.ollivanders2.player.O2Player;
import net.pottercraft.ollivanders2.player.O2WandCoreType;
import net.pottercraft.ollivanders2.player.O2WandWoodType;
import net.pottercraft.ollivanders2.player.events.OllivandersPlayerNotDestinedWandEvent;
import net.pottercraft.ollivanders2.spell.APPARATE;
import net.pottercraft.ollivanders2.spell.Divination;
import net.pottercraft.ollivanders2.spell.O2Spell;
import net.pottercraft.ollivanders2.spell.O2Spells;
import net.pottercraft.ollivanders2.spell.Transfiguration;
import net.pottercraft.ollivanders2.spell.AMATO_ANIMO_ANIMATO_ANIMAGUS;
import net.pottercraft.ollivanders2.spell.MORTUOS_SUSCITATE;
import net.pottercraft.ollivanders2.spell.PORTUS;
import net.pottercraft.ollivanders2.spell.O2SpellType;
import net.pottercraft.ollivanders2.potion.O2Potion;
import net.pottercraft.ollivanders2.potion.O2SplashPotion;
import net.pottercraft.ollivanders2.stationaryspell.ALIQUAM_FLOO;
import net.pottercraft.ollivanders2.stationaryspell.COLLOPORTUS;
import net.pottercraft.ollivanders2.stationaryspell.PROTEGO_TOTALUM;
import net.pottercraft.ollivanders2.stationaryspell.REPELLO_MUGGLETON;
import net.pottercraft.ollivanders2.stationaryspell.O2StationarySpell;
import net.pottercraft.ollivanders2.stationaryspell.O2StationarySpellType;
import net.pottercraft.ollivanders2.stationaryspell.MOLLIARE;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Effect;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.potion.PotionEffect;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Listener for events from the plugin
 *
 * @author lownes
 * @author Azami7
 */
public class OllivandersListener implements Listener
{
   private final Ollivanders2 p;
   private final Ollivanders2Common common;

   private final ArrayList<Listener> listeners = new ArrayList<>();

   /**
    * Constructor
    *
    * @param plugin reference to plugin
    */
   public OllivandersListener(@NotNull Ollivanders2 plugin)
   {
      p = plugin;
      common = new Ollivanders2Common(plugin);
   }

   /**
    * Register all listeners on enable
    */
   public void onEnable ()
   {
      for (Listener listener : listeners)
      {
         p.getServer().getPluginManager().registerEvents(listener, p);
      }
   }

   /**
    * Fires on player move
    *
    * @param event the player move event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerMove (@NotNull PlayerMoveEvent event)
   {
      if (event.getPlayer().isPermissionSet("Ollivanders2.BYPASS"))
      {
         if (event.getPlayer().hasPermission("Ollivanders2.BYPASS"))
         {
            return;
         }
      }
      Location toLoc = event.getTo();
      Location fromLoc = event.getFrom();

      if (toLoc == null || toLoc.getWorld() == null || fromLoc.getWorld() == null)
         return;

      for (O2StationarySpell spell : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
      {
         if (spell.location.getWorld() == null)
            continue;

         if (spell instanceof PROTEGO_TOTALUM &&
                 toLoc.getWorld().getUID().equals(spell.location.getWorld().getUID()) &&
                 fromLoc.getWorld().getUID().equals(spell.location.getWorld().getUID()))
         {
            int radius = spell.radius;
            Location spellLoc = spell.location;
            if (((fromLoc.distance(spellLoc) < radius - 0.5 && toLoc.distance(spellLoc) > radius - 0.5)
                    || (toLoc.distance(spellLoc) < radius + 0.5 && fromLoc.distance(spellLoc) > radius + 0.5)))
            {
               event.setCancelled(true);
               spell.flair(10);
               common.printDebugMessage("protegoTotalum: cancelling PlayerMoveEvent", null, null, false);
            }
         }
      }
   }

   /**
    * Checks if a player is inside an active floo fireplace and is saying a destination
    *
    * @param event the player chat event
    */
   @EventHandler(priority = EventPriority.LOW)
   public void onFlooChat (@NotNull AsyncPlayerChatEvent event)
   {
      Player player = event.getPlayer();
      String chat = event.getMessage();
      for (O2StationarySpell stat : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
      {
         if (stat instanceof ALIQUAM_FLOO)
         {
            ALIQUAM_FLOO aliquam = (ALIQUAM_FLOO) stat;
            if (player.getLocation().getBlock().equals(aliquam.getBlock()) && aliquam.isWorking())
            {
               //Floo network
               if (player.isPermissionSet("Ollivanders2.Floo"))
               {
                  if (!player.hasPermission("Ollivanders2.Floo"))
                  {
                     player.sendMessage(Ollivanders2.chatColor + "You do not have permission to use the Floo Network.");
                     return;
                  }
               }
               aliquam.stopWorking();
               List<ALIQUAM_FLOO> alis = new ArrayList<>();

               for (O2StationarySpell ali : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
               {
                  if (ali instanceof ALIQUAM_FLOO)
                  {
                     ALIQUAM_FLOO dest = (ALIQUAM_FLOO) ali;
                     alis.add(dest);
                     if (dest.getFlooName().equals(chat.trim().toLowerCase()))
                     {
                        p.addTeleportEvent(player, player.getLocation(), dest.location);
                        return;
                     }
                  }
               }
               int randomIndex = (int) (alis.size() * Math.random());
               p.addTeleportEvent(player, player.getLocation(), alis.get(randomIndex).location);
               return;
            }
         }
      }
   }

   /**
    * Handles all actions related to players speaking.
    *
    * @param event the player chat event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerChat (@NotNull AsyncPlayerChatEvent event)
   {
      Player sender = event.getPlayer();
      String message = event.getMessage();

      common.printDebugMessage("onPlayerChat: message = " + message, null, null, false);

      //
      // Parse to see if they were casting a spell
      //
      O2SpellType spellType = parseSpell(message);

      if (spellType != null)
      {
         //
         // Handle removing recipients from chat
         //
         List<O2StationarySpell> muffliatos = Ollivanders2API.getStationarySpells(p).getActiveStationarySpellsAtLocationByType(sender.getLocation(), O2StationarySpellType.MUFFLIATO);
         updateRecipients(sender, spellType, event.getRecipients(), muffliatos);

         //
         // Handle spell casting
         //
         doSpellCasting(sender, spellType, message.split(" "));
      }

      common.printDebugMessage("onPlayerChat: return", null, null, false);
   }

   /**
    * Parse a spell from a chat
    *
    * @param message the words chatted by the player
    * @return a spell type if found, null otherwise
    */
   @Nullable
   private O2SpellType parseSpell(@NotNull String message)
   {
      O2SpellType spellType;

      // first try all the words as one spell name
      spellType = Ollivanders2API.getSpells(p).getSpellTypeByName(message);

      if (spellType != null)
      {
         return spellType;
      }

      String[] words = message.split(" ");

      StringBuilder spellName = new StringBuilder();
      for (int i = 0; i < words.length; i++)
      {
         spellName.append(words[i]);
         spellType = Ollivanders2API.getSpells(p).getSpellTypeByName(spellName.toString());

         if (spellType != null)
            break;

         if (i == O2Spell.max_spell_words)
         {
            break;
         }

         spellName.append(" ");
      }

      if (spellType != null)
      {
         common.printDebugMessage("Spell is " + spellType, null, null, false);
      }
      else
      {
         common.printDebugMessage("No spell found", null, null, false);
      }

      return spellType;
   }

   /**
    * Handle updating chat recipients
    *
    * @param player     the player chatting
    * @param spellType  the spell type chatted
    * @param recipients the recipients for this chat
    * @param muffliatos all muffliato stationary spells
    */
   private void updateRecipients(@NotNull Player player, @NotNull O2SpellType spellType, @NotNull Set<Player> recipients, List<O2StationarySpell> muffliatos)
   {
      // remove all recipients if this is not a "spoken" spell
      if (spellType == O2SpellType.APPARATE || Divination.divinationSpells.contains(spellType))
      {
         recipients.clear();
         return;
      }

      // handle spell chat dropoff
      Set<Player> temp = new HashSet<>(recipients);
      for (Player recipient : temp)
      {
         Location location = player.getLocation();
         if (!Ollivanders2API.common.isInside(location, recipient.getLocation(), Ollivanders2.chatDropoff))
         {
            try
            {
               recipients.remove(recipient);
            }
            catch (Exception e)
            {
               common.printDebugMessage("OllivandersListener.updateRecipient: exception removing recipient", e, null, true);
            }
         }
      }

      // If sender is in a MUFFLIATO, remove recipients not also in the MUFFLIATO radius
      if (muffliatos != null && muffliatos.size() > 0)
      {
         common.printDebugMessage("OllivandersListener.updateRecipient: MUFFLIATO detected", null, null, false);

         temp = new HashSet<>(recipients);
         for (Player recipient : temp)
         {
            for (O2StationarySpell muffliato : muffliatos)
            {
               Location recipientLocation = recipient.getLocation();
               if (!muffliato.isInside(recipientLocation))
               {
                  recipients.remove(recipient);
               }
            }
         }
      }
   }

   /**
    * Handle when a player says a spell
    *
    * @param player    the player casting the spell
    * @param spellType the spell cast
    * @param words     the args for this spell, if relevant
    */
   private void doSpellCasting(@NotNull Player player, @NotNull O2SpellType spellType, @NotNull String[] words)
   {
      if (p.canCast(player, spellType, true))
      {
         if (Ollivanders2.bookLearning && p.getO2Player(player).getSpellCount(spellType) < 1)
         {
            // if bookLearning is set to true then spell count must be > 0 to cast this spell
            common.printDebugMessage("doSpellCasting: bookLearning enforced", null, null, false);
            player.sendMessage(Ollivanders2.chatColor + "You do not know that spell yet. To learn a spell, you'll need to read a book about that spell.");

            return;
         }

         boolean castSuccess = true;

         if (!Ollivanders2API.playerCommon.holdsWand(player))
         {
            // if they are not holding their destined wand, casting success is reduced
            common.printDebugMessage("doSpellCasting: player not holding destined wand", null, null, false);

            int uses = p.getO2Player(player).getSpellCount(spellType);
            castSuccess = Math.random() < (1.0 - (100.0 / (uses + 101.0)));
         }

         // wandless spells
         if (O2Spells.wandlessSpells.contains(spellType) || Divination.divinationSpells.contains(spellType))
         {
            common.printDebugMessage("doSpellCasting: allow wandless casting of " + spellType, null, null, false);
            castSuccess = true;
         }

         if (castSuccess)
         {
            common.printDebugMessage("doSpellCasting: begin casting " + spellType, null, null, false);

            if (spellType == O2SpellType.APPARATE)
            {
               p.addProjectile(new APPARATE(p, player, 1.0, words));
            }
            else if (spellType == O2SpellType.PORTUS)
            {
               p.addProjectile(new PORTUS(p, player, 1.0, words));
            }
            else if (spellType == O2SpellType.AMATO_ANIMO_ANIMATO_ANIMAGUS)
            {
               p.addProjectile(new AMATO_ANIMO_ANIMATO_ANIMAGUS(p, player, 1.0));
            }
            else if (Divination.divinationSpells.contains(spellType))
            {
               if (!divine(spellType, player, words))
               {
                  return;
               }
            }
            else
            {
               O2Player o2p = p.getO2Player(player);
               o2p.setWandSpell(spellType);
               p.setO2Player(player, o2p);
            }

            boolean fastLearning = false;
            if (Ollivanders2API.getPlayers(p).playerEffects.hasEffect(player.getUniqueId(), O2EffectType.FAST_LEARNING))
            {
               fastLearning = true;
            }
            p.incrementSpellCount(player, spellType);
            if (fastLearning)
            {
               p.incrementSpellCount(player, spellType);
            }
         }
      }
      else
      {
         common.printDebugMessage("doSpellCasting: Either no spell cast attempted or not allowed to cast", null, null, false);
      }
   }

   /**
    * Monitors chat events for the owl-post keywords and enacts the owl-post system
    *
    * @param event Chat event of type AsyncPlayerChatEvent
    */
   @EventHandler(priority = EventPriority.HIGH)
   public void owlPost (@NotNull AsyncPlayerChatEvent event)
   {
      String message = event.getMessage();

      if (!message.toLowerCase().startsWith(Ollivanders2OwlPost.deliveryKeyword.toLowerCase()))
         return;

      new BukkitRunnable()
      {
         @Override
         public void run()
         {
            Ollivanders2API.getOwlPost(p).processOwlPostRequest(event.getPlayer(), message);
         }
      }.runTaskLater(p, 3);
   }

   /**
    * This creates the spell projectile.
    *
    * @param player the player that cast the spell
    * @param name   the name of the spell cast
    * @param wandC  the wand check value for the held wand
    */
   @Nullable
   private O2Spell createSpellProjectile(@NotNull Player player, @NotNull O2SpellType name, double wandC)
   {
      common.printDebugMessage("OllivandersListener.createSpellProjectile: enter", null, null, false);

      if (Ollivanders2Common.libsDisguisesSpells.contains(name) && !Ollivanders2.libsDisguisesEnabled)
      {
         return null;
      }

      //spells go here, using any of the three types of m
      String spellClass = "net.pottercraft.ollivanders2.spell." + name.toString();

      Constructor<?> c;
      try
      {
         c = Class.forName(spellClass).getConstructor(Ollivanders2.class, Player.class, Double.class);
      }
      catch (Exception e)
      {
         common.printDebugMessage("OllivandersListener.createSpellProjectile: exception creating spell constructor", e, null, true);
         return null;
      }

      O2Spell spell;

      try
      {
         spell = (O2Spell) c.newInstance(p, player, wandC);
      }
      catch (Exception e)
      {
         common.printDebugMessage("OllivandersListener.createSpellProjectile: exception creating spell", e, null, true);
         return null;
      }

      return spell;
   }

   /**
    * Action by player to cast a spell
    *
    * @param player the player casting the spell
    * @since 2.2.7
    */
   private void castSpell(@NotNull Player player)
   {
      O2Player o2p = Ollivanders2API.getPlayers(p).getPlayer(player.getUniqueId());
      if (o2p == null)
      {
         common.printDebugMessage("Unable to find o2player casting spell.", null, null, false);
         return;
      }

      O2SpellType spellType = o2p.getWandSpell();

      // if no spell set, check to see if they have a master spell
      boolean nonverbal = false;
      if (spellType == null && Ollivanders2.enableNonVerbalSpellCasting)
      {
         spellType = o2p.getMasterSpell();
         nonverbal = true;
      }

      if (spellType != null)
      {
         double wandCheck;
         boolean playerHoldsWand = Ollivanders2API.playerCommon.holdsWand(player, EquipmentSlot.HAND);
         if (playerHoldsWand)
         {
            common.printDebugMessage("OllivandersListener:castSpell: player holds a wand in their primary hand", null, null, false);
            wandCheck = Ollivanders2API.playerCommon.wandCheck(player, EquipmentSlot.HAND);
            allyWand(player);
         }
         else
         {
            common.printDebugMessage("OllivandersListener:castSpell: player does not hold a wand in their primary hand", null, null, false);
            return;
         }

         O2Spell castSpell = createSpellProjectile(player, spellType, wandCheck);
         if (castSpell == null)
         {
            return;
         }

         p.addProjectile(castSpell);

         o2p.setSpellRecentCastTime(spellType);
         if (!nonverbal)
         {
            o2p.setPriorIncantatem(spellType);
         }

         common.printDebugMessage("OllivandersListener:castSpell: " + player.getName() + " cast " + castSpell.getName(), null, null, false);

         o2p.setWandSpell(null);
      }
   }

   /**
    * Handle events when player interacts with an item in their hand.
    *
    * @param event the player interact event
    */
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerInteract (@NotNull PlayerInteractEvent event)
   {
      Player player = event.getPlayer();
      Action action = event.getAction();

      common.printDebugMessage("onPlayerInteract: enter", null, null, false);

      //
      // A right or left click of the primary hand
      //
      if ((event.getHand() == EquipmentSlot.HAND))
      {
         common.printDebugMessage("onPlayerInteract: primary hand right or left click", null, null, false);

         new BukkitRunnable()
         {
            @Override
            public void run()
            {
               if (!event.isCancelled())
                  primaryHandInteractEvents(event);
            }
         }.runTaskLater(p, Ollivanders2Common.ticksPerSecond);
      }

      //
      // A right or left click that is not their primary hand
      //
      if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR)
      {
         if (Ollivanders2API.playerCommon.holdsWand(player, EquipmentSlot.OFF_HAND))
         {
            rotateNonVerbalSpell(player, action);
         }
      }
   }

   /**
    * Handle primary hand interact events.
    *
    * @param event the interact event
    */
   private void primaryHandInteractEvents (@NotNull PlayerInteractEvent event)
   {
      Player player = event.getPlayer();
      Action action = event.getAction();

      //
      // A right or left click of the primary hand when holding a wand is used to make a magical action.
      //
      if ((Ollivanders2API.playerCommon.holdsWand(player, EquipmentSlot.HAND)))
      {
         common.printDebugMessage("primaryHandInteractEvents: player holding a wand", null, null, false);

         //
         // A left click of the primary hand is used to cast a spell
         //
         if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
         {
            common.printDebugMessage("primaryHandInteractEvents: left click action", null, null, false);
            castSpell(player);
         }

         //
         // A right click is used:
         // - to determine if the wand is the player's destined wand
         // - to brew a potion if they are holding a glass bottle in their off hand and facing a cauldron
         //
         else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
         {
            if (!Ollivanders2API.playerCommon.holdsWand(player))
            {
               return;
            }

            common.printDebugMessage("primaryHandInteractEvents: right click action", null, null, false);

            Block cauldron = (Ollivanders2API.common.playerFacingBlockType(player, Material.CAULDRON));
            if ((cauldron != null) && (player.getInventory().getItemInOffHand().getType() == Material.GLASS_BOTTLE))
            {
               common.printDebugMessage("primaryHandInteractEvents: brewing potion", null, null, false);
               brewPotion(player, cauldron);
               return;
            }

            common.printDebugMessage("primaryHandInteractEvents: waving wand", null, null, false);

            // play a sound and visual effect when they right-click their destined wand with no spell
            if (Ollivanders2API.playerCommon.wandCheck(player, EquipmentSlot.HAND) < 2)
            {
               Location location = player.getLocation();
               location.setY(location.getY() + 1.6);
               player.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
               player.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
               p.getO2Player(player).setFoundWand(true);
            }
            else
            {
               OllivandersPlayerNotDestinedWandEvent wandEvent = new OllivandersPlayerNotDestinedWandEvent(player);
               p.getServer().getPluginManager().callEvent(wandEvent);
            }
         }
      }
   }

   /**
    * If non-verbal spell casting is enabled, selects a new spell from mastered spells.
    *
    * @param player the player rotating spells
    * @param action the player action
    */
   private void rotateNonVerbalSpell (@NotNull Player player, @NotNull Action action)
   {
      if (!Ollivanders2.enableNonVerbalSpellCasting)
         return;

      common.printDebugMessage("Rotating mastered spells for non-verbal casting.", null, null, false);

      if (!Ollivanders2API.playerCommon.holdsWand(player, EquipmentSlot.OFF_HAND))
         return;

      O2Player o2p = Ollivanders2API.getPlayers(p).getPlayer(player.getUniqueId());
      if (o2p == null)
         return;

      boolean reverse = false;
      // right click rotates through spells backwards
      if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
         reverse = true;

      o2p.shiftMasterSpell(reverse);
      O2SpellType spell = o2p.getMasterSpell();
      if (spell != null)
      {
         String spellName = Ollivanders2API.common.firstLetterCapitalize(Ollivanders2API.common.enumRecode(spell.toString()));
         player.sendMessage(Ollivanders2.chatColor + "Wand master spell set to " + spellName);
      }
      else
      {
         if (Ollivanders2.debug)
         {
            player.sendMessage(Ollivanders2.chatColor + "You have not mastered any spells.");
         }
      }
   }

   /**
    * Handle player joining event.
    *
    * @param event the player join event
    */
   @EventHandler(priority = EventPriority.NORMAL)
   public void onPlayerJoin (@NotNull PlayerJoinEvent event)
   {
      Player player = event.getPlayer();

      O2Player o2p = Ollivanders2API.getPlayers(p).getPlayer(player.getUniqueId());

      if (o2p == null) // new player
      {
         o2p = p.getO2Player(player);
      }
      else // existing player
      {

         // update player's display name in case it has changed
         if (!o2p.getPlayerName().equalsIgnoreCase(player.getName()))
         {
            o2p.setPlayerName(player.getName());
         }

         // add player to their house team
         Ollivanders2API.getHouses(p).addPlayerToHouseTeam(player);

         // do player join actions
         o2p.onJoin();
      }

      // re-add them to player list (in case they have changed from above actions)
      p.setO2Player(player, o2p);
   }

   /**
    * Handle player quitting.
    *
    * @param event the quit event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerQuit (@NotNull PlayerQuitEvent event)
   {
      playerQuit(event.getPlayer());
   }

   /**
    * Handle player being kicked from the server.
    *
    * @param event the kick event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerKick (@NotNull PlayerKickEvent event)
   {
      playerQuit(event.getPlayer());
   }

   /**
    * Upkeep when a player leaves the game.
    *
    * @param player the player
    */
   private void playerQuit (@NotNull Player player)
   {
      O2Player o2p = Ollivanders2API.getPlayers(p).getPlayer(player.getUniqueId());
      if (o2p == null)
         return;

      // do player quit actions
      o2p.onQuit();
   }

   /**
    * Handle player death event.
    *
    * @param event the player death event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerDeath (@NotNull PlayerDeathEvent event)
   {
      if (Ollivanders2.enableDeathExpLoss)
      {
         O2Player o2p = Ollivanders2API.getPlayers(p).getPlayer(event.getEntity().getUniqueId());

         if (o2p == null)
            return;

         o2p.onDeath();

         p.setO2Player(event.getEntity(), o2p);
      }
   }

   /**
    * This checks if a player kills another player, and if so, adds a soul
    * to the attacking o2player
    *
    * @param event the entity damage event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onEntityDamage (@NotNull EntityDamageByEntityEvent event)
   {
      if (event.getEntity() instanceof Player)
      {
         Player damaged = (Player) event.getEntity();

         if (event.getDamager() instanceof Player)
         {
            Player attacker = (Player) event.getDamager();
            if (damaged.getHealth() - event.getDamage() <= 0)
            {
               p.getO2Player(attacker).addSoul();
            }
         }
      }
   }

   /**
    * Handles when players receive damage.
    *
    * @param event the player damage event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onPlayerDamage (@NotNull EntityDamageEvent event)
   {
      if (checkSpongify(event))
      {
         return;
      }

      //Horcrux code
      List<O2StationarySpell> stationarySpells = Ollivanders2API.getStationarySpells(p).getActiveStationarySpells();
      if (event.getEntity() instanceof Player)
      {
         Player player = (Player) event.getEntity();
         UUID pid = event.getEntity().getUniqueId();
         if ((player.getHealth() - event.getDamage()) <= 0)
         {
            for (O2StationarySpell stationary : stationarySpells)
            {
               if (stationary.getSpellType() == O2StationarySpellType.HORCRUX && stationary.getCasterID().equals(pid))
               {
                  Location tp = stationary.location;
                  tp.setY(tp.getY() + 1);
                  p.addTeleportEvent(player, player.getLocation(), tp);

                  Collection<PotionEffect> potions = ((Player) event.getEntity()).getActivePotionEffects();
                  for (PotionEffect potion : potions)
                  {
                     ((Player) event.getEntity()).removePotionEffect(potion.getType());
                  }
                  event.setCancelled(true);
                  common.printDebugMessage("onPlayerDamage: cancelling EntityDamageEvent", null, null, false);

                  AttributeInstance playerHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH);
                  if (playerHealth != null)
                  {
                     player.setHealth(playerHealth.getBaseValue());
                     Ollivanders2API.getStationarySpells(p).removeStationarySpell(stationary);
                     return;
                  }
               }
            }
         }
      }
   }

   /**
    * Checks to see if the entity was within a spongify stationary spell object. If so, cancels the damage event
    *
    * @param event the Entity  Damage Event
    * @return true if the entity was within spongify
    */
   private boolean checkSpongify(@NotNull EntityDamageEvent event)
   {
      Entity entity = event.getEntity();
      for (O2StationarySpell spell : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
      {
         if (spell instanceof MOLLIARE && event.getCause() == DamageCause.FALL)
         {
            if (spell.isInside(entity.getLocation()))
            {
               event.setCancelled(true);
               common.printDebugMessage("checkSpongify: cancelling EntityDamageEvent", null, null, false);
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Cancels any block place event inside of a colloportus object
    *
    * @param event the block place event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusBlockPlaceEvent (@NotNull BlockPlaceEvent event)
   {
      if (Ollivanders2API.getStationarySpells(p).isInsideOf(O2StationarySpellType.COLLOPORTUS, event.getBlock().getLocation()))
      {
         if (event.getPlayer().isPermissionSet("Ollivanders2.BYPASS"))
         {
            if (!event.getPlayer().hasPermission("Ollivanders2.BYPASS"))
            {
               event.getBlock().breakNaturally();
            }
         }
         else
         {
            event.getBlock().breakNaturally();
         }
      }
   }

   /**
    * Cancels any block break event inside of a colloportus object
    *
    * @param event the block break event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusBlockBreakEvent (@NotNull BlockBreakEvent event)
   {
      if (Ollivanders2API.getStationarySpells(p).isInsideOf(O2StationarySpellType.COLLOPORTUS, event.getBlock().getLocation()))
      {
         if (event.getPlayer().isPermissionSet("Ollivanders2.BYPASS"))
         {
            if (!event.getPlayer().hasPermission("Ollivanders2.BYPASS"))
            {
               event.setCancelled(true);
               common.printDebugMessage("onColloportusBlockBreakEvent: cancelling BlockBreakEvent", null, null, false);
            }
         }
         else
         {
            event.setCancelled(true);
            common.printDebugMessage("onColloportusBlockBreakEvent: cancelling BlockBreakEvent", null, null, false);
         }
      }
   }

   /**
    * Cancels any block physics event inside of a colloportus object
    *
    * @param event the block physics event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusBlockPhysicsEvent (@NotNull BlockPhysicsEvent event)
   {
      if (Ollivanders2API.getStationarySpells(p).isInsideOf(O2StationarySpellType.COLLOPORTUS, event.getBlock().getLocation()))
      {
         event.setCancelled(true);
         common.printDebugMessage("onColloportusBlockPhysicsEvent: cancelling BlockPhysicsEvent", null, null, false);
      }
   }

   /**
    * Cancels any block interact event inside a colloportus object
    *
    * @param event the player interact event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusPlayerInteract (@NotNull PlayerInteractEvent event)
   {
      if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
      {
         if (event.getClickedBlock() == null)
            return;

         if (Ollivanders2API.getStationarySpells(p).isInsideOf(O2StationarySpellType.COLLOPORTUS, event.getClickedBlock().getLocation()))
         {
            if (event.getPlayer().isPermissionSet("Ollivanders2.BYPASS"))
            {
               if (!event.getPlayer().hasPermission("Ollivanders2.BYPASS"))
               {
                  event.setCancelled(true);
                  common.printDebugMessage("onColloportusPlayerInteract: cancelling PlayerInteractEvent", null, null, false);
               }
            }
            else
            {
               event.setCancelled(true);
               common.printDebugMessage("onColloportusPlayerInteract: cancelling PlayerInteractEvent", null, null, false);
            }
         }
      }
   }

   /**
    * Cancels any piston extend event inside a colloportus
    *
    * @param event the block piston extend event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusPistonExtend (@NotNull BlockPistonExtendEvent event)
   {
      ArrayList<COLLOPORTUS> colloportusSpells = new ArrayList<>();
      for (O2StationarySpell stat : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
      {
         if (stat instanceof COLLOPORTUS)
         {
            colloportusSpells.add((COLLOPORTUS) stat);
         }
      }

      List<Block> blocks = event.getBlocks();
      BlockFace direction = event.getDirection();
      for (Block block : blocks)
      {
         Block newBlock = block.getRelative(direction.getModX(), direction.getModY(), direction.getModZ());
         for (COLLOPORTUS colloportus : colloportusSpells)
         {
            if (colloportus.isInside(newBlock.getLocation()) || colloportus.isInside(block.getLocation()))
            {
               event.setCancelled(true);
               common.printDebugMessage("onColloportusPistonExtend: cancelling BlockPistonExtendEvent", null, null, false);
               return;
            }
         }
      }
   }

   /**
    * Cancels any piston retract event inside of a colloportus
    *
    * @param event the block Piston Retract Event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusPistonRetract (@NotNull BlockPistonRetractEvent event)
   {
      if (event.isSticky())
      {
         if (Ollivanders2API.getStationarySpells(p).isInsideOf(O2StationarySpellType.COLLOPORTUS, event.getBlock().getLocation()))
         {
            event.setCancelled(true);
            common.printDebugMessage("onColloportusPistonRetract: cancelling BlockPistonRetractEvent", null, null, false);
         }
      }
   }

   /**
    * Cancels any block change by an entity inside of a colloportus
    *
    * @param event the entity Change Block Event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onColloportusEntityChangeBlock (@NotNull EntityChangeBlockEvent event)
   {
      Location loc = event.getBlock().getLocation();
      if (loc.getWorld() == null)
         return;

      Entity entity = event.getEntity();
      if (Ollivanders2API.getStationarySpells(p).isInsideOf(O2StationarySpellType.COLLOPORTUS, loc))
      {
         event.setCancelled(true);
         common.printDebugMessage("onColloportusEntityChangeBlock: cancelling EntityChangeBlockEvent", null, null, false);
         if (event.getEntityType() == EntityType.FALLING_BLOCK)
         {
            loc.getWorld().dropItemNaturally(loc, new ItemStack(((FallingBlock) entity).getBlockData().getMaterial()));
         }
      }
   }

   /**
    * If a block is broken that is temporary, prevent it from dropping anything.
    *
    * @param event the block break event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void onTemporaryBlockBreak (@NotNull BlockBreakEvent event)
   {
      Block block = event.getBlock();

      if (p.isTempBlock(block))
      {
         event.setDropItems(false);
      }
   }

   /**
    * If a block is inside colloportus, then don't blow it up.
    *
    * @param event the entity explode event
    */
   @EventHandler(priority = EventPriority.HIGH)
   public void onExplosion (@NotNull EntityExplodeEvent event)
   {
      List<Block> blockListCopy = new ArrayList<>(event.blockList());

      for (Block block : blockListCopy)
      {
         for (O2StationarySpell stat : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
         {
            if (stat instanceof COLLOPORTUS)
            {
               if (stat.isInside(block.getLocation()))
               {
                  event.blockList().remove(block);
               }
            }
         }
      }
   }

   /**
    * If a wand is not already allied with a player, this allies it.
    *
    * @param player player holding a wand.
    */
   private void allyWand (@NotNull Player player)
   {
      ItemStack wand = player.getInventory().getItemInMainHand();
      ItemMeta wandMeta = wand.getItemMeta();
      if (wandMeta == null)
         return;

      List<String> wandLore = wandMeta.getLore();

      if (wandLore != null && wandLore.size() == 1)
      {
         wandLore.add(player.getUniqueId().toString());
         wandMeta.setLore(wandLore);
         wand.setItemMeta(wandMeta);
         player.getInventory().setItemInMainHand(wand);
      }
   }

   /**
    * Prevents a transfigured entity from changing any blocks by exploding.
    *
    * @param event the entity explode event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void transfiguredEntityExplodeCancel (@NotNull EntityExplodeEvent event)
   {
      for (O2Spell proj : p.getProjectiles())
      {
         if (proj instanceof Transfiguration)
         {
            Transfiguration trans = (Transfiguration) proj;
            if (trans.getToID() == event.getEntity().getUniqueId())
            {
               event.setCancelled(true);
               common.printDebugMessage("transfiguredEntityExplodeCancel: cancelling EntityExplodeEvent", null, null, false);
            }
         }
      }
   }

   /**
    * When an item is picked up by a player, if the item is a portkey, the player will be teleported there.
    *
    * @param event the player Pickup Item Event
    */
   @EventHandler(priority = EventPriority.NORMAL)
   public void portkeyPickUp (@NotNull EntityPickupItemEvent event)
   {
      Entity entity = event.getEntity();
      if (entity instanceof Player)
      {
         Player player = (Player) entity;
         Item item = event.getItem();
         ItemMeta meta = item.getItemStack().getItemMeta();
         if (meta == null)
            return;

         List<String> lore = null;

         if (meta.hasLore())
            lore = meta.getLore();

         if (lore == null)
            lore = new ArrayList<>();

         List<String> temp = new ArrayList<>(lore);

         for (String s : temp)
         {
            if (s.startsWith(PORTUS.portus))
            {
               String[] portArray = s.split(" ");
               Location to;
               to = new Location(Bukkit.getServer().getWorld(UUID.fromString(portArray[1])),
                       Double.parseDouble(portArray[2]),
                       Double.parseDouble(portArray[3]),
                       Double.parseDouble(portArray[4]));
               to.setDirection(player.getLocation().getDirection());
               for (Entity e : player.getWorld().getEntities())
               {
                  if (player.getLocation().distance(e.getLocation()) <= 2)
                  {
                     p.addTeleportEvent(player, player.getLocation(), to);
                  }
               }
               p.addTeleportEvent(player, player.getLocation(), to);
               lore.remove(s);
               meta.setLore(lore);
               item.getItemStack().setItemMeta(meta);
               return;
            }
         }
      }
   }

   /**
    * Cancels any targeting of players with the Cloak of Invisibility
    * or inside of a REPELLO_MUGGLETON while the targeting entity is
    * outside it.
    *
    * @param event the Entity Target Event
    */
   @EventHandler(priority = EventPriority.HIGHEST)
   public void cloakPlayer (@NotNull EntityTargetEvent event)
   {
      Entity target = event.getTarget();
      if (target instanceof Player)
      {
         if (p.getO2Player((Player) target).isInvisible())
         {
            event.setCancelled(true);
            common.printDebugMessage("cloakPlayer: cancelling EntityTargetEvent", null, null, false);
         }
      }
      if (target != null)
      {
         for (O2StationarySpell stat : Ollivanders2API.getStationarySpells(p).getActiveStationarySpells())
         {
            if (stat instanceof REPELLO_MUGGLETON)
            {
               if (stat.isInside(target.getLocation()))
               {
                  if (!stat.isInside(event.getEntity().getLocation()))
                  {
                     event.setCancelled(true);
                     common.printDebugMessage("cloakPlayer: cancelling EntityTargetEvent", null, null, false);
                  }
               }
            }
         }
      }
   }

   /**
    * Cancels any targeting of players who own inferi by that inferi
    *
    * @param event the Entity Target Event
    */
   @EventHandler(priority = EventPriority.HIGH)
   public void inferiTarget (@NotNull EntityTargetEvent event)
   {
      Entity target = event.getTarget();
      Entity entity = event.getEntity();
      for (O2Spell sp : p.getProjectiles())
      {
         if (sp instanceof MORTUOS_SUSCITATE)
         {
            Transfiguration trans = (Transfiguration) sp;
            if (trans.getToID() == entity.getUniqueId() && trans.player == target)
            {
               event.setCancelled(true);
               common.printDebugMessage("inferiTarget: cancelling EntityTargetEvent", null, null, false);
            }
         }
      }
   }

   /**
    * This drops a random wand when a witch dies
    *
    * @param event the entity death event
    */
   @EventHandler(priority = EventPriority.NORMAL)
   public void witchWandDrop (@NotNull EntityDeathEvent event)
   {
      if (event.getEntityType() == EntityType.WITCH && Ollivanders2.enableWitchDrop)
      {
         int wandType = Math.abs(Ollivanders2Common.random.nextInt() % 4);
         int coreType = Math.abs(Ollivanders2Common.random.nextInt() % 4);

         ItemStack wand = Ollivanders2API.getItems(p).getItemByType(O2ItemType.WAND, 1);
         if (wand == null)
         {
            common.printDebugMessage("OllivandersListener.witchWandDrop: wand is null", null, null, false);
            return;
         }

         List<String> lore = new ArrayList<>();
         lore.add(O2WandWoodType.getAllWoodsByName().get(wandType) + " and " + O2WandCoreType.getAllCoresByName().get(coreType));

         ItemMeta meta = wand.getItemMeta();
         if (meta == null)
            return;

         meta.setLore(lore);
         wand.setItemMeta(meta);
         event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), wand);
      }
   }

   /**
    * When a player consumes something, see if it was a potion and apply the effect if it was.
    *
    * @param event the player item consume event
    */
   @EventHandler(priority = EventPriority.NORMAL)
   public void onPlayerDrink (@NotNull PlayerItemConsumeEvent event)
   {
      ItemStack item = event.getItem();
      if (item.getType() == Material.POTION)
      {
         Player player = event.getPlayer();
         common.printDebugMessage(player.getDisplayName() + " drank a potion.", null, null, false);

         O2Player o2p = p.getO2Player(player);

         ItemMeta meta = item.getItemMeta();
         if (meta == null)
            return;

         if (meta.hasLore())
         {
            O2Potion potion = Ollivanders2API.getPotions(p).findPotionByItemMeta(meta);

            if (potion != null)
            {
               if (!Ollivanders2.libsDisguisesEnabled && Ollivanders2Common.libDisguisesPotions.contains(potion.getPotionType()))
               {
                  return;
               }

               potion.drink(o2p, player);
            }
         }
      }
   }

   /**
    * Event fires when a player right clicks with a broom in their hand
    *
    * @param event the player interact event
    */
   @EventHandler(priority = EventPriority.HIGH)
   public void broomClick (@NotNull PlayerInteractEvent event)
   {
      Player player = event.getPlayer();

      if (((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
            && (Ollivanders2API.common.isBroom(player.getInventory().getItemInMainHand())))
      {
         UUID playerUid = player.getUniqueId();
         Set<UUID> flying = OllivandersSchedule.getFlying();
         if (flying.contains(playerUid))
         {
            flying.remove(playerUid);
         }
         else
         {
            flying.add(playerUid);
         }
      }
   }

   /**
    * When a user holds their spell journal, replace it with an updated version of the book.
    *
    * @param event the player item held event
    */
   @EventHandler (priority = EventPriority.LOWEST)
   public void onSpellJournalHold (@NotNull PlayerItemHeldEvent event)
   {
      // only run this if spellJournal is enabled
      if (!Ollivanders2.useSpellJournal)
         return;

      Player player = event.getPlayer();
      int slotIndex = event.getNewSlot();

      ItemStack heldItem = player.getInventory().getItem(slotIndex);
      if (heldItem != null && heldItem.getType() == Material.WRITTEN_BOOK)
      {
         BookMeta bookMeta = (BookMeta) heldItem.getItemMeta();
         if (bookMeta == null)
            return;

         String bookTitle = bookMeta.getTitle();
         if (bookTitle == null)
            return;

         if (bookMeta.getTitle().equalsIgnoreCase("Spell Journal"))
         {
            O2Player o2Player = p.getO2Player(player);
            ItemStack spellJournal = o2Player.getSpellJournal();

            player.getInventory().setItem(slotIndex, spellJournal);
         }
      }
   }

   /**
    * Handle potion making actions.
    *
    * @param event the player toggle sneak event
    */
   @EventHandler (priority = EventPriority.NORMAL)
   public void onPotionBrewing (@NotNull PlayerToggleSneakEvent event)
   {
      Player player = event.getPlayer();

      // is the player sneaking
      if (!event.isSneaking())
      {
         return;
      }

      Block cauldron = Ollivanders2API.common.playerFacingBlockType(player, Material.CAULDRON);
      if (cauldron == null)
      {
         return;
      }

      // check that the item held is in their left hand
      ItemStack heldItem = player.getInventory().getItemInOffHand();
      if (heldItem.getType() == Material.AIR || heldItem.getAmount() == 0)
      {
         return;
      }

      ItemMeta meta = heldItem.getItemMeta();
      if (meta == null)
         return;

      String ingredientName = heldItem.getItemMeta().getDisplayName();

      // put the item in the player's off hand in to the cauldron
      Location spawnLoc = cauldron.getLocation();
      World world = cauldron.getWorld();

      Item item = world.dropItem(spawnLoc.add(+0.5, +0.5, +0.5), heldItem.clone());
      player.sendMessage(Ollivanders2.chatColor + "Added " + ingredientName);

      item.setVelocity(new Vector(0, 0, 0));
      player.getInventory().setItemInOffHand(null);
   }

   /**
    * Brew a potion from the ingredients in a cauldron.
    *
    * @param player   the player brewing the potion
    * @param cauldron the cauldron of ingredients
    * @apiNote assumes player is holding a glass bottle in their off hand and will set off hand item to null
    */
   private void brewPotion(@NotNull Player player, @NotNull Block cauldron)
   {
      common.printDebugMessage("OllivandersListener:brewPotion: brewing potion", null, null, false);

      Block under = cauldron.getRelative(BlockFace.DOWN);
      if (Ollivanders2Common.hotBlocks.contains(under.getType()))
      {
         ItemStack potion = Ollivanders2API.getPotions(p).brewPotion(cauldron, player);

         if (potion == null)
         {
            player.sendMessage(Ollivanders2.chatColor + "The cauldron appears unchanged. Perhaps you should check your recipe");
            return;
         }

         // remove ingredients from cauldron
         for (Entity e : cauldron.getWorld().getNearbyEntities(cauldron.getLocation(), 1, 1, 1))
         {
            if (e instanceof Item)
            {
               e.remove();
            }
         }

         player.getWorld().playEffect(cauldron.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
         player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
         player.getInventory().setItemInOffHand(potion);
      }
      else
      {
         common.printDebugMessage("Cauldron is not over a hot block", null, null, false);
      }
   }

   /**
    * Handle effects for O2SplashPotion throws
    *
    * @param event the potion splash event
    */
   @EventHandler (priority = EventPriority.HIGH)
   public void onSplashPotion (@NotNull PotionSplashEvent event)
   {
      ThrownPotion thrown = event.getEntity();
      ItemMeta meta = thrown.getItem().getItemMeta();
      if (meta == null)
         return;

      O2Potion potion = Ollivanders2API.getPotions(p).findPotionByItemMeta(meta);

      if (potion != null)
      {
         if (potion instanceof O2SplashPotion)
         {
            ((O2SplashPotion)potion).thrownEffect(event);
         }
      }
   }

   /**
    * @param spellType the type of divination spell
    * @param sender the player doing the spell
    * @param words the additional args to the spell
    * @return true if spell was successfully created, false otherwise
    */
   private boolean divine (@NotNull O2SpellType spellType, @NotNull Player sender, @NotNull String[] words)
   {
      common.printDebugMessage("Casting divination spell", null, null, false);

      // parse the words for the target player's name
      if (words.length < 2)
      {
         sender.sendMessage(Ollivanders2.chatColor + "You must say the name of the player. Example: 'astrologia steve'.");
         return false;
      }

      // name should be the last word the player said
      String targetName = words[words.length - 1];
      Player target = p.getServer().getPlayer(targetName);

      if (target == null)
      {
         sender.sendMessage(Ollivanders2.chatColor + "Unable to find player named " + targetName + ".");
         return false;
      }

      O2Spell spell = createSpellProjectile(sender, spellType, 1.0);

      if (!(spell instanceof Divination))
      {
         return false;
      }

      ((Divination) spell).setTarget(target);
      p.addProjectile(spell);

      return true;
   }
}