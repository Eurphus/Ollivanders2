package net.pottercraft.ollivanders2.spell;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sk89q.worldguard.protection.flags.Flags;
import net.pottercraft.ollivanders2.O2MagicBranch;
import net.pottercraft.ollivanders2.Ollivanders2;
import net.pottercraft.ollivanders2.Ollivanders2API;
import net.pottercraft.ollivanders2.common.Ollivanders2Common;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * The super class for transfiguration of objects.  Not for use on players or entities.
 *
 * @author Azami7
 * @since 2.2.5
 */
public abstract class BlockTransfiguration extends TransfigurationBase
{
    /**
     * A map of the transfigured blocks and their original types for use with revert()
     */
    final HashMap<Block, Material> changedBlocks = new HashMap<>();

    /**
     * If this is populated, any material type key will be changed to the value
     */
    protected Map<Material, Material> transfigurationMap = new HashMap<>();

    /**
     * The material type to change this block to.
     */
    Material transfigureType = Material.AIR;

    /**
     * How many blocks out from the target are affects.  Usually for permanent spells this is 1.
     */
    protected int radius = 1;

    /**
     * Allows spell variants to change the radius of the spell.
     */
    double radiusModifier = 1.0;

    /**
     * A blacklist of Material types that will not be affected by this spell.  Only used if the whitelist is empty.
     */
    List<Material> materialBlacklist = new ArrayList<>();

    /**
     * A whitelist of Material types that will be affected by this spell.
     */
    List<Material> materialWhitelist = new ArrayList<>();

    /**
     * Default constructor for use in generating spell text.  Do not use to cast the spell.
     *
     * @param plugin the Ollivanders2 plugin
     */
    public BlockTransfiguration(Ollivanders2 plugin)
    {
        super(plugin);

        branch = O2MagicBranch.TRANSFIGURATION;
    }

    /**
     * Constructor.
     *
     * @param plugin    a callback to the MC plugin
     * @param player    the player who cast this spell
     * @param rightWand which wand the player was using
     */
    public BlockTransfiguration(@NotNull Ollivanders2 plugin, @NotNull Player player, @NotNull Double rightWand)
    {
        super(plugin, player, rightWand);

        branch = O2MagicBranch.TRANSFIGURATION;

        // material black list
        materialBlackList.addAll(Ollivanders2Common.unbreakableMaterials);

        // required worldGuard state flags
        if (Ollivanders2.worldGuardEnabled)
           worldGuardFlags.add(Flags.BUILD);

        minDuration = 15 * Ollivanders2Common.ticksPerSecond;
        maxDuration = 10 * Ollivanders2Common.ticksPerMinute;
        spellDuration = minDuration;

        successMessage = "Transfiguration successful.";
    }

    /**
     * Transfigure the target block or blocks. Will not change the block if it is on the materialBlacklist list or if the
     * target block is already the transfigure type.
     */
    protected void transfigure()
    {
        if (isTransfigured || !hasHitTarget() || getTargetBlock() == null)
            return;

        // get the objects to be transfigured, target block is not always the block at the location (such as aguamenti), so we cannot use 'location'
        for (Block blockToChange : Ollivanders2API.common.getBlocksInRadius(getTargetBlock().getLocation(), (int) (radius * radiusModifier)))
        {
            if (!canTransfigure(blockToChange))
                continue;

            Material originalMaterial = blockToChange.getType();
            // if not permanent, keep track of what the original block was
            if (!permanent)
                changedBlocks.put(blockToChange, originalMaterial);

            if (transfigurationMap.containsKey(originalMaterial))
                blockToChange.setType(transfigurationMap.get(originalMaterial));
            else
                blockToChange.setType(transfigureType);

            isTransfigured = true;
        }

        if (permanent)
            kill();
    }

    /**
     * Determines if this block can be changed by this Transfiguration spell.
     *
     * @param block the block to check
     * @return true if the block can be changed, false otherwise.
     */
    boolean canTransfigure(@NotNull Block block)
    {
        // get block type
        Material blockType = block.getType();

        boolean canChange = true;

        if (blockType == transfigureType) // do not change if this block is already the target type
            canChange = false;
        else if (block.getState() instanceof Entity) // do not change if this block is an Entity
            canChange = false;
        else if (materialBlacklist.contains(blockType)) // do not change if this block is in the blacklist
            canChange = false;
        else if (!materialWhitelist.isEmpty()) // do not change if the whitelist exists and this block is not in it
        {
            if (!materialWhitelist.contains(blockType))
                canChange = false;
        }
        else // do not change if this block is already the subject of a temporary transfiguration
        {
            for (O2Spell spell : p.getProjectiles())
            {
                if (spell instanceof TransfigurationBase)
                {
                    if (((TransfigurationBase)spell).isBlockTransfigured(block))
                        return false;
                }
            }
        }

        return canChange;
    }

    /**
     * Restore the block to its original type if this was not a permanent change
     */
    @Override
    public void revert()
    {
        if (permanent)
            return;

        for (Block block : changedBlocks.keySet())
        {
            Material originalMaterial = changedBlocks.get(block);
            block.setType(originalMaterial);
        }

        changedBlocks.clear();

        // do any spell specific revert actions beyond changing the block back
        doRevert();
    }

    /**
     * Spell specific revert actions beyond changing the block type back. Needs to be overridden by child classes.
     */
    void doRevert () {}

    /**
     * Is this spell currently affecting this block?
     *
     * @param block the block to check
     * @return true if this spell is affecting this block, false otherwise
     */
    @Override
    public boolean isBlockTransfigured(@NotNull Block block)
    {
        if (permanent)
            // if this spell is permanent, it is no longer considered as "affecting" the block, the block *is* the new type
            // this condition should never happen because permanent spells should be killed but just in case one is hanging around
            return false;

        return changedBlocks.containsKey(block);
    }

    /**
     * Is this entity transfigured by this spell
     *
     * @param entity the entity to check
     * @return true if transfigured, false otherwise
     */
    @Override
    public boolean isEntityTransfigured(@NotNull Entity entity)
    {
        return false;
    }
}
