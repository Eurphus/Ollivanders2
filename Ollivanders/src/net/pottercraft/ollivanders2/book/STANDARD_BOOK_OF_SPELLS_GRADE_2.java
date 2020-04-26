package net.pottercraft.ollivanders2.book;

import net.pottercraft.ollivanders2.O2MagicBranch;
import net.pottercraft.ollivanders2.spell.O2SpellType;
import net.pottercraft.ollivanders2.Ollivanders2;

/**
 * Standard Book of Spells Grade 2
 *
 * Missing spells:
 * Skurge - https://github.com/Azami7/Ollivanders2/issues/72
 * Tarantallegra - https://github.com/Azami7/Ollivanders2/issues/77
 *
 * @since 2.2.4
 * @author Azami7
 */
public class STANDARD_BOOK_OF_SPELLS_GRADE_2 extends O2Book
{
   public STANDARD_BOOK_OF_SPELLS_GRADE_2 (Ollivanders2 plugin)
   {
      super(plugin);

      title = shortTitle = "Standard Book of Spells Grade 2";
      author = "Miranda Goshawk";
      branch = O2MagicBranch.CHARMS;

      spells.add(O2SpellType.DIFFINDO);
      spells.add(O2SpellType.EXPELLIARMUS);
      spells.add(O2SpellType.ENGORGIO);
      spells.add(O2SpellType.REDUCIO);
      spells.add(O2SpellType.INCENDIO);
      spells.add(O2SpellType.IMMOBULUS);
      spells.add(O2SpellType.FINITE_INCANTATEM);
      spells.add(O2SpellType.OBLIVIATE);
      spells.add(O2SpellType.FLIPENDO);
      //spells.add(O2SpellType.SKURGE);
      //spells.add(O2SpellType.TARANTALLEGRA);
   }
}