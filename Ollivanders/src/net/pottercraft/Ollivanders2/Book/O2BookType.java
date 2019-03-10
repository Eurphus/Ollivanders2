package net.pottercraft.Ollivanders2.Book;

/**
 * All Ollivanders2 book types.
 *
 * @since 2.2.4
 * @author Azami7
 */
public enum O2BookType
{
   A_BEGINNERS_GUIDE_TO_TRANSFIGURATION(A_BEGINNERS_GUIDE_TO_TRANSFIGURATION.class),
   ACHIEVEMENTS_IN_CHARMING(ACHIEVEMENTS_IN_CHARMING.class),
   ADVANCED_FIREWORKS(ADVANCED_FIREWORKS.class),
   ADVANCED_POTION_MAKING(ADVANCED_POTION_MAKING.class),
   ADVANCED_TRANSFIGURATION(ADVANCED_TRANSFIGURATION.class),
   BASIC_FIREWORKS(BASIC_FIREWORKS.class),
   BASIC_HEXES(BASIC_HEXES.class),
   BREAK_WITH_A_BANSHEE(BREAK_WITH_A_BANSHEE.class),
   CHADWICKS_CHARMS_VOLUME_1 (CHADWICKS_CHARMS_VOLUME_1.class),
   CHARMING_COLORS(CHARMING_COLORS.class),
   CONFRONTING_THE_FACELESS(CONFRONTING_THE_FACELESS.class),
   CURSES_AND_COUNTERCURSES(CURSES_AND_COUNTERCURSES.class),
   DE_MEDICINA_PRAECEPTA(DE_MEDICINA_PRAECEPTA.class),
   ESSENTIAL_DARK_ARTS(ESSENTIAL_DARK_ARTS.class),
   EXTREME_INCANTATIONS(EXTREME_INCANTATIONS.class),
   //FANTASTIC_BEASTS (FANTASTIC_BEASTS.class),
   FOR_THE_GREATER_GOOD(FOR_THE_GREATER_GOOD.class),
   GADDING_WITH_GHOULS(GADDING_WITH_GHOULS.class),
   HARMONIOUS_CONNECTIONS(HARMONIOUS_CONNECTIONS.class),
   HOLIDAYS_WITH_HAGS(HOLIDAYS_WITH_HAGS.class),
   INTERMEDIATE_TRANSFIGURATION(INTERMEDIATE_TRANSFIGURATION.class),
   JINXES_FOR_THE_JINXED(JINXES_FOR_THE_JINXED.class),
   MAGICAL_DRAFTS_AND_POTIONS(MAGICAL_DRAFTS_AND_POTIONS.class),
   MAGICK_MOSTE_EVILE(MAGICK_MOSTE_EVILE.class),
   MODERN_MAGICAL_TRANSPORTATION(MODERN_MAGICAL_TRANSPORTATION.class),
   MOSTE_POTENTE_POTIONS(MOSTE_POTENTE_POTIONS.class),
   NUMEROLOGY_AND_GRAMMATICA(NUMEROLOGY_AND_GRAMMATICA.class),
   OMENS_ORACLES_AND_THE_GOAT(OMENS_ORACLES_AND_THE_GOAT.class),
   POTION_OPUSCULE(POTION_OPUSCULE.class),
   PRACTICAL_DEFENSIVE_MAGIC(PRACTICAL_DEFENSIVE_MAGIC.class),
   QUINTESSENCE_A_QUEST(QUINTESSENCE_A_QUEST.class),
   SECRETS_OF_THE_DARKEST_ART(SECRETS_OF_THE_DARKEST_ART.class),
   SECRETS_OF_WANDLORE(SECRETS_OF_WANDLORE.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_1(STANDARD_BOOK_OF_SPELLS_GRADE_1.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_2(STANDARD_BOOK_OF_SPELLS_GRADE_2.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_3(STANDARD_BOOK_OF_SPELLS_GRADE_3.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_4(STANDARD_BOOK_OF_SPELLS_GRADE_4.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_5(STANDARD_BOOK_OF_SPELLS_GRADE_5.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_6(STANDARD_BOOK_OF_SPELLS_GRADE_6.class),
   STANDARD_BOOK_OF_SPELLS_GRADE_7(STANDARD_BOOK_OF_SPELLS_GRADE_7.class),
   TETRABIBLIOS(TETRABIBLIOS.class),
   THE_DARK_FORCES(THE_DARK_FORCES.class),
   THE_HEALERS_HELPMATE(THE_HEALERS_HELPMATE.class),
   TRAVELS_WITH_TROLLS(TRAVELS_WITH_TROLLS.class),
   UNFOGGING_THE_FUTURE(UNFOGGING_THE_FUTURE.class),
   VOYAGES_WITH_VAMPIRES(VOYAGES_WITH_VAMPIRES.class),
   WANDERINGS_WITH_WEREWOLVES(WANDERINGS_WITH_WEREWOLVES.class),
   YEAR_WITH_A_YETI(YEAR_WITH_A_YETI.class);

   private Class className;

   O2BookType (Class className)
   {
      this.className = className;
   }

   public Class getClassName()
   {
      return className;
   }
}
