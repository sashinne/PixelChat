package co.uk.hhservers.pixelchat;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.NbtKeys;
import com.pixelmonmod.pixelmon.util.NBTTools;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.format.TextColors.*;
import static org.spongepowered.api.text.format.TextStyles.BOLD;
import static org.spongepowered.api.text.format.TextStyles.UNDERLINE;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;

@SuppressWarnings("WeakerAccess")
public class PokeData {

    private static PokeData instance;

    public static PokeData getInstance(){
        return instance;
    }

    public static String getName(PartyStorage party, Integer slot){
        String name = "pokename";
        name = party.get(slot).getDisplayName();
        return name;
    }

    public static Boolean isNull(Pokemon slot){
        if(slot != null){ return false; }
        return true;
    }


    public static boolean isInBattle(EntityPixelmon pixelmon) {
        return pixelmon.battleController != null;
    }

    public static EnumType checkHiddenPowerType(Pokemon pokemonData) {
        //https://bulbapedia.bulbagarden.net/wiki/Hidden_Power_(move)/Calculation
        int f = pokemonData.getIVs().get(StatsType.SpecialDefence) % 2;
        int e = pokemonData.getIVs().get(StatsType.SpecialAttack) % 2;
        int d = pokemonData.getIVs().get(StatsType.Speed) % 2;
        int c = pokemonData.getIVs().get(StatsType.Defence) % 2;
        int b = pokemonData.getIVs().get(StatsType.Attack) % 2;
        int a = pokemonData.getIVs().get(StatsType.HP) % 2;

        double fedbca = 32 * f + 16 * e + 8 * d + 4 * c + 2 * b + a;
        int type = (int)Math.floor(fedbca * 15.0 / 63.0);
        if (type == 0) {
            return EnumType.Fighting;
        }
        if (type == 1) {
            return EnumType.Flying;
        }
        if (type == 2) {
            return EnumType.Poison;
        }
        if (type == 3) {
            return EnumType.Ground;
        }
        if (type == 4) {
            return EnumType.Rock;
        }
        if (type == 5) {
            return EnumType.Bug;
        }
        if (type == 6) {
            return EnumType.Ghost;
        }
        if (type == 7) {
            return EnumType.Steel;
        }
        if (type == 8) {
            return EnumType.Fire;
        }
        if (type == 9) {
            return EnumType.Water;
        }
        if (type == 10) {
            return EnumType.Grass;
        }
        if (type == 11) {
            return EnumType.Electric;
        }
        if (type == 12) {
            return EnumType.Psychic;
        }
        if (type == 13) {
            return EnumType.Ice;
        }
        if (type == 14) {
            return EnumType.Dragon;
        }
        if (type == 15) {
            return EnumType.Dark;
        }
        return EnumType.Normal;
    }

    public static boolean canLearnOtherMove(Pokemon pokemon, String move) {
        BaseStats baseStats = pokemon.getBaseStats();
        ArrayList<Attack> otherMoves = baseStats.tutorMoves;
        otherMoves.addAll(baseStats.eggMoves);
        for (Attack a : otherMoves) {
            if (!a.savedAttack.getLocalizedName().equals(move)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static boolean canLearnAbility(Pokemon pokemon, String ability) {
        BaseStats baseStats = pokemon.getBaseStats();
        for (String s : baseStats.abilities) {
            if (s == null || !s.equals(ability)) continue;
            return true;
        }
        return false;
    }

    public static boolean isHiddenAbility(Pokemon pokemon, String ability) {
        BaseStats baseStats = pokemon.getBaseStats();
        return baseStats.abilities[2] != null && baseStats.abilities[2].equalsIgnoreCase(ability);
    }

    public static String getHeldItemName(Pokemon pokemonData) {
        String itemText = "None";

        ItemStack itemStack = pokemonData.getHeldItem();
        //if(itemStack != ItemStack.EMPTY && itemStack.getItem() != ItemTypes.AIR) {//Prevent pixelmon bug that allows item:air
            //noinspection ConstantConditions
            org.spongepowered.api.item.inventory.ItemStack heldItemStack = (org.spongepowered.api.item.inventory.ItemStack) (Object) itemStack;//TODO check format and make less ugh
           itemText = heldItemStack.getType().getTranslation().get();
       // }

        return itemText;
    }

    public static ArrayList<Text> getFullStatTexts(Pokemon pokemonData) {//TODO neaten up
        ArrayList<Text> lore = new ArrayList<>();

        EnumSpecies enumSpecies = pokemonData.getSpecies();

        int ivHP = pokemonData.getIVs().get(StatsType.HP);
        int ivAttack = pokemonData.getIVs().get(StatsType.Attack);
        int ivDefence = pokemonData.getIVs().get(StatsType.Defence);
        int ivSpeed = pokemonData.getIVs().get(StatsType.Speed);
        int ivSpecialAttack = pokemonData.getIVs().get(StatsType.SpecialAttack);
        int ivSpecialDefense = pokemonData.getIVs().get(StatsType.SpecialDefence);
        int totalIvs = ivHP + ivAttack + ivDefence + ivSpeed + ivSpecialAttack + ivSpecialDefense;

        int evHP = pokemonData.getEVs().get(StatsType.HP);
        int evAttack = pokemonData.getEVs().get(StatsType.Attack);
        int evDefence = pokemonData.getEVs().get(StatsType.Defence);
        int evSpeed = pokemonData.getEVs().get(StatsType.Speed);
        int evSpecialAttack = pokemonData.getEVs().get(StatsType.SpecialAttack);
        int evSpecialDefense = pokemonData.getEVs().get(StatsType.SpecialDefence);
        int totalEvs = evHP + evAttack + evDefence + evSpeed + evSpecialAttack + evSpecialDefense;

        EnumNature nature = pokemonData.getNature();
        Gender gender = pokemonData.getGender();
        EnumGrowth growth = pokemonData.getGrowth();
        EnumPokeballs caughtBall = pokemonData.getCaughtBall();
        String originalTrainerNamer = pokemonData.getOriginalTrainer();
        String heldItemName = getHeldItemName(pokemonData);


        lore.add(Text.of(GRAY, "Level: ", YELLOW, pokemonData.getLevel()));
        lore.add(Text.of(GRAY, "Ability: ", YELLOW, pokemonData.getAbility().getName()));
        lore.add(Text.of(GRAY, "Nature: ", YELLOW, nature.name()));
        lore.add(Text.of(GRAY, "Item: ", YELLOW, heldItemName));
        lore.add(Text.EMPTY);
        lore.add(Text.of(GRAY, "Gender: ", AQUA, gender.name()));
        lore.add(Text.of(GRAY, "Size: ", YELLOW, growth.name()));
        lore.add(Text.of(GRAY, "Pokeball: ", YELLOW, caughtBall.name()));
        lore.add(Text.of(GRAY, "Form: ", YELLOW, (enumSpecies.getNumForms(false) > 0 ? ((Enum)pokemonData.getFormEnum()).name() : "N/A")));
        lore.add(Text.of(GRAY, "OT: ", YELLOW, originalTrainerNamer));
        lore.add(Text.EMPTY);

        lore.add(Text.of(GRAY, "IVs: ", YELLOW, totalIvs, "/186 ", GRAY, "(", (int)(((float)totalIvs * 100f)/ 186f) ,"%)"));
        lore.add(Text.of(GRAY, "   (", YELLOW, ivHP, GRAY, "/", YELLOW, ivAttack, GRAY, "/", YELLOW, ivDefence, GRAY, "/", YELLOW, ivSpecialAttack, GRAY, "/", YELLOW, ivSpecialDefense, GRAY, "/", YELLOW, ivSpeed, GRAY, ")"));
        lore.add(Text.of(GRAY, "   (", RED, "HP", GRAY, "/", RED, "A", GRAY, "/", RED, "D", GRAY, "/", RED, "SA", GRAY, "/", RED, "SD", GRAY, "/", RED, "SPD", GRAY, ")"));
        lore.add(Text.EMPTY);

        lore.add(Text.of(GRAY, "Evs: ", YELLOW, totalEvs, "/510 ", GRAY, "(", (int)(((float)totalEvs * 100f)/ 510f) ,"%)"));
        lore.add(Text.of(GRAY, "   (", YELLOW, evHP, GRAY, "/", YELLOW, evAttack, GRAY, "/", YELLOW, evDefence, GRAY, "/", YELLOW, evSpecialAttack, GRAY, "/", YELLOW, evSpecialDefense, GRAY, "/", YELLOW, evSpeed, GRAY, ")"));
        lore.add(Text.of(GRAY, "   (", RED, "HP", GRAY, "/", RED, "A", GRAY, "/", RED, "D", GRAY, "/", RED, "SA", GRAY, "/", RED, "SD", GRAY, "/", RED, "SPD", GRAY, ")"));
        lore.add(Text.EMPTY);

        lore.add(Text.of(GRAY, "Moves:"));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(0) != null ? pokemonData.getMoveset().get(0).savedAttack.getLocalizedName() : "None")));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(1) != null ? pokemonData.getMoveset().get(1).savedAttack.getLocalizedName() : "None")));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(2) != null ? pokemonData.getMoveset().get(2).savedAttack.getLocalizedName() : "None")));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(3) != null ? pokemonData.getMoveset().get(3).savedAttack.getLocalizedName() : "None")));

        /*
        lore.add(Text.of(RED, "[Stats]"));
        lore.add(Text.of(DARK_GREEN, NbtTools.getNicknameFromNbt(pokemonData),
                (pokemonData.getByte(IS_SHINY) == 1 ? Text.of(YELLOW, "\u2605") : Text.EMPTY),
                NEW_LINE, AQUA, "Level: ", pokemonData.getInteger(LEVEL),
                NEW_LINE, YELLOW, "Nature: ", EnumNature.getNatureFromIndex((int)pokemonData.getShort(NATURE)).toString(),
                NEW_LINE, GREEN, "Growth: ", EnumGrowth.getGrowthFromIndex((int)pokemonData.getShort(GROWTH)).toString(),
                NEW_LINE, GOLD, "Ability: ", (pokemonData.getInteger(ABILITY_SLOT) != 2 ? GOLD : GRAY), pokemonData.getString(ABILITY),
                NEW_LINE, DARK_PURPLE, "OT: ", pokemonData.getString(ORIGINAL_TRAINER),
                NEW_LINE, RED, "Item: ", itemText
        ));

        lore.add(Text.of(TextColors.GOLD, "[EVs]"));
        lore.add(Text.of(GOLD, "EVs",
                NEW_LINE, "HP: ", pokemonData.getInteger(EV_HP),
                NEW_LINE, "Attack: ", pokemonData.getInteger(EV_ATTACK),
                NEW_LINE, "Defence: ", pokemonData.getInteger(EV_DEFENCE),
                NEW_LINE, "Sp. Attack: ", pokemonData.getInteger(EV_SPECIAL_ATTACK),
                NEW_LINE, "Sp. Defence: ", pokemonData.getInteger(EV_SPECIAL_DEFENCE),
                NEW_LINE, "Speed: ", pokemonData.getInteger(EV_SPEED)
        ));

        lore.add(Text.of(TextColors.LIGHT_PURPLE, "[IVs]"));
        lore.add(Text.of(LIGHT_PURPLE,  "IVs",
                NEW_LINE, "HP: ", pokemonData.getInteger(IV_HP),
                NEW_LINE, "Attack: ", pokemonData.getInteger(IV_ATTACK),
                NEW_LINE, "Defence: ", pokemonData.getInteger(IV_DEFENCE),
                NEW_LINE, "Sp. Attack: ", pokemonData.getInteger(IV_SP_ATT),
                NEW_LINE, "Sp. Defence: ", pokemonData.getInteger(IV_SP_DEF),
                NEW_LINE, "Speed: ", pokemonData.getInteger(IV_SPEED)
        ));

        lore.add(Text.of(TextColors.BLUE, "[Moves]"));
        lore.add(Text.of(
                BLUE, "Moves",
                NEW_LINE, "Move 1: ", (pokemonData.hasKey(PIXELMON_MOVE_ID+"0") ? AttackBase.getAttackBase(pokemonData.getInteger(PIXELMON_MOVE_ID+"0")).get().getLocalizedName() : "None"),
                NEW_LINE, "Move 2: ", (pokemonData.hasKey(PIXELMON_MOVE_ID+"1") ? AttackBase.getAttackBase(pokemonData.getInteger(PIXELMON_MOVE_ID+"1")).get().getLocalizedName() : "None"),
                NEW_LINE, "Move 3: ", (pokemonData.hasKey(PIXELMON_MOVE_ID+"2") ? AttackBase.getAttackBase(pokemonData.getInteger(PIXELMON_MOVE_ID+"2")).get().getLocalizedName() : "None"),
                NEW_LINE, "Move 4: ", (pokemonData.hasKey(PIXELMON_MOVE_ID+"3") ? AttackBase.getAttackBase(pokemonData.getInteger(PIXELMON_MOVE_ID+"3")).get().getLocalizedName() : "None")
        ));
        */

        return lore;
    }

    public static Text getHoverText(Pokemon pokemonData) {
        //★ = black star = \u2605

        Text.Builder statBuilder = Text.builder();
        statBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"Stats",DARK_GRAY,BOLD,"]",RESET));

        String itemText = getHeldItemName(pokemonData);

        Text statHover = Text.of(DARK_GREEN, UNDERLINE, pokemonData.getDisplayName(),
                (pokemonData.isShiny() ? Text.of(YELLOW, "\u2605") : Text.EMPTY),
                NEW_LINE, AQUA, "Level: ", pokemonData.getLevel(),
                NEW_LINE, YELLOW, "Nature: ", pokemonData.getNature().toString(),
                NEW_LINE, GREEN, "Growth: ", pokemonData.getGrowth().toString(),
                NEW_LINE, GOLD, "Ability: ", (pokemonData.getAbilitySlot() != 2 ? GOLD : GRAY), pokemonData.getAbility().getName(),
                NEW_LINE, DARK_PURPLE, "OT: ", pokemonData.getOriginalTrainer(),
                NEW_LINE, RED, "Item: ", itemText,
                NEW_LINE, LIGHT_PURPLE, "Pokeball:", pokemonData.getCaughtBall().name()
        );
        statBuilder.onHover(TextActions.showText(statHover));

        Text.Builder evBuilder = Text.builder();
        evBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"EVs",DARK_GRAY,BOLD,"]",NONE));
        Text evHover = Text.of(GREEN, "EVs",
                NEW_LINE, GREEN, "HP: ", LIGHT_PURPLE, pokemonData.getEVs().get(StatsType.HP),
                NEW_LINE, GREEN, "Attack: ", LIGHT_PURPLE,pokemonData.getEVs().get(StatsType.Attack),
                NEW_LINE, GREEN, "Defence: ", LIGHT_PURPLE,pokemonData.getEVs().get(StatsType.Defence),
                NEW_LINE, GREEN, "Sp. Attack: ", LIGHT_PURPLE,pokemonData.getEVs().get(StatsType.SpecialAttack),
                NEW_LINE, GREEN, "Sp. Defence: ", LIGHT_PURPLE,pokemonData.getEVs().get(StatsType.SpecialDefence),
                NEW_LINE, GREEN, "Speed: ", LIGHT_PURPLE,pokemonData.getEVs().get(StatsType.Speed)
        );
        evBuilder.onHover(TextActions.showText(evHover));

        Text.Builder ivBuilder = Text.builder();
        ivBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"IVs",DARK_GRAY,BOLD,"]",NONE, RESET));
        Text ivHover = Text.of(GREEN, "IVs",
                NEW_LINE, GREEN, "HP: ", LIGHT_PURPLE,pokemonData.getIVs().get(StatsType.HP),
                NEW_LINE, GREEN, "Attack: ", LIGHT_PURPLE, pokemonData.getIVs().get(StatsType.Attack),
                NEW_LINE, GREEN, "Defence: ", LIGHT_PURPLE, pokemonData.getIVs().get(StatsType.Defence),
                NEW_LINE, GREEN, "Sp. Attack: ", LIGHT_PURPLE, pokemonData.getIVs().get(StatsType.SpecialAttack),
                NEW_LINE, GREEN, "Sp. Defence: ", LIGHT_PURPLE, pokemonData.getIVs().get(StatsType.SpecialDefence),
                NEW_LINE, GREEN, "Speed: ", LIGHT_PURPLE, pokemonData.getIVs().get(StatsType.Speed)
        );
        ivBuilder.onHover(TextActions.showText(ivHover));

        Text.Builder movesBuilder = Text.builder();
        movesBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"Moves",DARK_GRAY,BOLD,"]",NONE));
        Text movesHover = Text.of(
                GREEN, "Moves",
                NEW_LINE, "Move 1: ", LIGHT_PURPLE, pokemonData.getMoveset().get(0) != null ? pokemonData.getMoveset().get(0).toString() : "None",
                NEW_LINE, GREEN, "Move 2: ", LIGHT_PURPLE, pokemonData.getMoveset().get(1) != null ? pokemonData.getMoveset().get(1).toString() : "None",
                NEW_LINE, GREEN, "Move 3: ", LIGHT_PURPLE, pokemonData.getMoveset().get(2) != null ? pokemonData.getMoveset().get(2).toString() : "None",
                NEW_LINE, GREEN, "Move 4: ", LIGHT_PURPLE, pokemonData.getMoveset().get(3) != null ? pokemonData.getMoveset().get(3).toString() : "None"
        );
        movesBuilder.onHover(TextActions.showText(movesHover));

        return Text.of(DARK_GRAY,BOLD,"[",NONE,RESET,GREEN, pokemonData.getSpecies().name,DARK_GRAY,BOLD,"]",NONE,RESET, " ", statBuilder.build(), " ", evBuilder.build(), " ", ivBuilder.build(), " ", movesBuilder.build());
    }

}