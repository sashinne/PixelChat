package co.uk.hhservers.pixelchat;

import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.common.entity.pixelmon.stats.BaseStats;
import com.pixelmongenerations.common.entity.pixelmon.stats.Gender;
import com.pixelmongenerations.common.entity.pixelmon.stats.StatsType;
import com.pixelmongenerations.common.entity.pixelmon.stats.links.NBTLink;
import com.pixelmongenerations.core.enums.EnumGrowth;
import com.pixelmongenerations.core.enums.EnumNature;
import com.pixelmongenerations.core.enums.EnumSpecies;
import com.pixelmongenerations.core.enums.EnumType;
import com.pixelmongenerations.core.enums.items.EnumPokeball;
import com.pixelmongenerations.core.network.PixelmonData;
import com.pixelmongenerations.core.storage.NbtKeys;
import com.pixelmongenerations.core.storage.PlayerStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;

import static org.spongepowered.api.text.Text.NEW_LINE;
import static org.spongepowered.api.text.format.TextColors.*;
import static org.spongepowered.api.text.format.TextStyles.BOLD;

@SuppressWarnings("WeakerAccess")
public class PokeData {

    private static PokeData instance;

    public static PokeData getInstance(){
        return instance;
    }

    public static String getName(PlayerStorage party, Integer slot){
        String name = "pokename";
        name = party.getTeamIncludeEgg().get(slot).getString(NbtKeys.NAME);
        return name;
    }

    public static Boolean isNull(NBTTagCompound slot){
        return slot == null;
    }


    public static boolean isInBattle(EntityPixelmon pixelmon) {
        return pixelmon.battleController != null;
    }

    public static EnumType checkHiddenPowerType(EntityPixelmon pokemonData) {
        //https://bulbapedia.bulbagarden.net/wiki/Hidden_Power_(move)/Calculation
        int f = pokemonData.stats.IVs.get(StatsType.SpecialDefence) % 2;
        int e = pokemonData.stats.IVs.get(StatsType.SpecialAttack) % 2;
        int d = pokemonData.stats.IVs.get(StatsType.Speed) % 2;
        int c = pokemonData.stats.IVs.get(StatsType.Defence) % 2;
        int b = pokemonData.stats.IVs.get(StatsType.Attack) % 2;
        int a = pokemonData.stats.IVs.get(StatsType.HP) % 2;

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

    public static boolean canLearnOtherMove(EntityPixelmon pokemon, String move) {
        return pokemon.baseStats.moves.canLearnMove(move);
    }

    public static boolean canLearnAbility(EntityPixelmon pokemon, String ability) {
        BaseStats baseStats = pokemon.baseStats;
        for (String s : baseStats.abilities) {
            if (s == null || !s.equals(ability)) continue;
            return true;
        }
        return false;
    }

    public static boolean isHiddenAbility(EntityPixelmon pokemon, String ability) {
        BaseStats baseStats = pokemon.baseStats;
        return baseStats.abilities[2] != null && baseStats.abilities[2].equalsIgnoreCase(ability);
    }

    public static String getHeldItemName(NBTLink pokemonData) {
        String itemText;

        ItemStack itemStack = pokemonData.getHeldItemStack();
        //if(itemStack != ItemStack.EMPTY && itemStack.getItem() != ItemTypes.AIR) {//Prevent pixelmon bug that allows item:air
            //noinspection ConstantConditions
            org.spongepowered.api.item.inventory.ItemStack heldItemStack = (org.spongepowered.api.item.inventory.ItemStack) (Object) itemStack;//TODO check format and make less ugh
           itemText = heldItemStack.getType().getTranslation().get();
       // }

        return itemText;
    }

    public static ArrayList<Text> getFullStatTexts(NBTLink pokemonData) {//TODO neaten up
        ArrayList<Text> lore = new ArrayList<>();

        EnumSpecies enumSpecies = pokemonData.getSpecies();

        int ivHP = pokemonData.getStats().IVs.get(StatsType.HP);
        int ivAttack = pokemonData.getStats().IVs.get(StatsType.Attack);
        int ivDefence = pokemonData.getStats().IVs.get(StatsType.Defence);
        int ivSpeed = pokemonData.getStats().IVs.get(StatsType.Speed);
        int ivSpecialAttack = pokemonData.getStats().IVs.get(StatsType.SpecialAttack);
        int ivSpecialDefense = pokemonData.getStats().IVs.get(StatsType.SpecialDefence);
        int totalIvs = ivHP + ivAttack + ivDefence + ivSpeed + ivSpecialAttack + ivSpecialDefense;

        int evHP = pokemonData.getStats().EVs.get(StatsType.HP);
        int evAttack = pokemonData.getStats().EVs.get(StatsType.Attack);
        int evDefence = pokemonData.getStats().EVs.get(StatsType.Defence);
        int evSpeed = pokemonData.getStats().EVs.get(StatsType.Speed);
        int evSpecialAttack = pokemonData.getStats().EVs.get(StatsType.SpecialAttack);
        int evSpecialDefense = pokemonData.getStats().EVs.get(StatsType.SpecialDefence);
        int totalEvs = evHP + evAttack + evDefence + evSpeed + evSpecialAttack + evSpecialDefense;

        EnumNature nature = pokemonData.getNature();
        Gender gender = pokemonData.getGender();
        EnumGrowth growth = pokemonData.getGrowth();
        EnumPokeball caughtBall = pokemonData.getCaughtBall();
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
        lore.add(Text.of(GRAY, "Form: ", YELLOW, (enumSpecies.getNumForms(false) > 0 ? ((Enum)pokemonData.getEntity().getFormEnum()).name() : "N/A")));
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
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(0) != null ? pokemonData.getMoveset().get(0).baseAttack.getLocalizedName() : "None")));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(1) != null ? pokemonData.getMoveset().get(1).baseAttack.getLocalizedName() : "None")));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(2) != null ? pokemonData.getMoveset().get(2).baseAttack.getLocalizedName() : "None")));
        lore.add(Text.of(GRAY,"  - ", (pokemonData.getMoveset().get(3) != null ? pokemonData.getMoveset().get(3).baseAttack.getLocalizedName() : "None")));

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


    public static Text getPartyText(PixelmonData[] partyData) {
        Text.Builder returnText = TextSerializers.FORMATTING_CODE.deserialize("&aSpecies &b~ &dLevel&r").toBuilder();
        for (PixelmonData pixelData : partyData)
        {
            if (pixelData == null) continue;

            returnText.append(TextSerializers.FORMATTING_CODE.deserialize("\n&a" + pixelData.getSpecies().name + "&b ~ &d" + pixelData.lvl));
        }
        return returnText.build();
    }


    //TODO:implement pagination to display individuals when displaying party in chat
   /* public static ArrayList<Text> getPartyPageText(PartyStorage partyData){
        Text.Builder returnText = TextSerializers.FORMATTING_CODE.deserialize("hi").toBuilder();
    }*/

    public static Text getHoverText(NBTLink pokemonData) {
        //★ = black star = \u2605

        Text.Builder statBuilder = Text.builder();
        statBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN, pokemonData.getSpecies().name, DARK_GRAY,BOLD,"]",RESET));

        String itemText = getHeldItemName(pokemonData);


        Text.Builder evBuilder = Text.builder();
        evBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"EVs",DARK_GRAY,BOLD,"]",NONE));
        Text evHover = Text.of(GREEN, "EVs",
                NEW_LINE, GREEN, "HP: ", LIGHT_PURPLE, pokemonData.getStats().EVs.get(StatsType.HP),
                NEW_LINE, GREEN, "Attack: ", LIGHT_PURPLE,pokemonData.getStats().EVs.get(StatsType.Attack),
                NEW_LINE, GREEN, "Defence: ", LIGHT_PURPLE,pokemonData.getStats().EVs.get(StatsType.Defence),
                NEW_LINE, GREEN, "Sp. Attack: ", LIGHT_PURPLE,pokemonData.getStats().EVs.get(StatsType.SpecialAttack),
                NEW_LINE, GREEN, "Sp. Defence: ", LIGHT_PURPLE,pokemonData.getStats().EVs.get(StatsType.SpecialDefence),
                NEW_LINE, GREEN, "Speed: ", LIGHT_PURPLE,pokemonData.getStats().EVs.get(StatsType.Speed)
        );
        evBuilder.onHover(TextActions.showText(evHover));

        Text.Builder ivBuilder = Text.builder();
        ivBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"IVs",DARK_GRAY,BOLD,"]",NONE, RESET));
        Text ivHover = Text.of(GREEN, "IVs",
                NEW_LINE, GREEN, "HP: ", LIGHT_PURPLE,pokemonData.getStats().IVs.get(StatsType.HP),
                NEW_LINE, GREEN, "Attack: ", LIGHT_PURPLE, pokemonData.getStats().IVs.get(StatsType.Attack),
                NEW_LINE, GREEN, "Defence: ", LIGHT_PURPLE, pokemonData.getStats().IVs.get(StatsType.Defence),
                NEW_LINE, GREEN, "Sp. Attack: ", LIGHT_PURPLE, pokemonData.getStats().IVs.get(StatsType.SpecialAttack),
                NEW_LINE, GREEN, "Sp. Defence: ", LIGHT_PURPLE, pokemonData.getStats().IVs.get(StatsType.SpecialDefence),
                NEW_LINE, GREEN, "Speed: ", LIGHT_PURPLE, pokemonData.getStats().IVs.get(StatsType.Speed)
        );
        ivBuilder.onHover(TextActions.showText(ivHover));

        int ivTotal = pokemonData.getStats().IVs.get(StatsType.HP) + pokemonData.getStats().IVs.get(StatsType.Attack) + pokemonData.getStats().IVs.get(StatsType.Defence) + pokemonData.getStats().IVs.get(StatsType.SpecialAttack) + pokemonData.getStats().IVs.get(StatsType.SpecialDefence) + pokemonData.getStats().IVs.get(StatsType.Speed);
        int evTotal = pokemonData.getStats().EVs.get(StatsType.HP) + pokemonData.getStats().EVs.get(StatsType.Attack) + pokemonData.getStats().EVs.get(StatsType.Defence) + pokemonData.getStats().EVs.get(StatsType.SpecialAttack) + pokemonData.getStats().EVs.get(StatsType.SpecialDefence) + pokemonData.getStats().EVs.get(StatsType.Speed);

        Text statHover = Text.of(DARK_GREEN, pokemonData.getNickname(),
                (pokemonData.isShiny() ? Text.of(YELLOW, "\u2605") : Text.EMPTY),
                NEW_LINE, GREEN, "Level: ",LIGHT_PURPLE, pokemonData.getLevel(),
                NEW_LINE, GREEN, "Nature: ",LIGHT_PURPLE, pokemonData.getNature().toString(), DARK_GRAY, " (", GREEN, "+", pokemonData.getNature().increasedStat.name(), DARK_GRAY, "/", RED, "-" , pokemonData.getNature().decreasedStat, DARK_GRAY, ")",
                NEW_LINE, GREEN, "Growth: ",LIGHT_PURPLE, pokemonData.getGrowth().toString(),
                NEW_LINE, GREEN, "Ability: ",LIGHT_PURPLE, (pokemonData.getEntity().abilitySlot != 2 ? GOLD : LIGHT_PURPLE), pokemonData.getAbility().getName(),
                NEW_LINE, GREEN, "OT: ",LIGHT_PURPLE, pokemonData.getOriginalTrainer(),
                NEW_LINE, GREEN, "Item: ",LIGHT_PURPLE, itemText,
                NEW_LINE, GREEN, "Pokeball: ",LIGHT_PURPLE, pokemonData.getCaughtBall().name(),
                NEW_LINE, NEW_LINE, AQUA, "IVs: ", LIGHT_PURPLE, ivTotal, "/186", GREEN, "(", LIGHT_PURPLE, (ivTotal*100/186), "%", GREEN, ")",
                NEW_LINE, RED, "HP: ", pokemonData.getStats().IVs.HP, DARK_GRAY, "/", GOLD, "Atk: ", pokemonData.getStats().IVs.Attack, DARK_GRAY, "/", YELLOW, "Def: ", pokemonData.getStats().IVs.Defence,
                NEW_LINE, BLUE, "SpA: ", pokemonData.getStats().IVs.SpAtt, DARK_GRAY, "/", GREEN, "SpD: ", pokemonData.getStats().IVs.SpDef, DARK_GRAY, "/", LIGHT_PURPLE, "Spe: ", pokemonData.getStats().IVs.Speed,
                NEW_LINE, AQUA, "EVs: ", LIGHT_PURPLE, evTotal, "/510", GREEN, "(", LIGHT_PURPLE, (evTotal*100/510), "%", GREEN, ")",
                NEW_LINE, RED, "HP: ", pokemonData.getStats().EVs.HP, DARK_GRAY, "/", GOLD, "Atk: ", pokemonData.getStats().EVs.Attack, DARK_GRAY, "/", YELLOW, "Def: ", pokemonData.getStats().EVs.Defence,
                NEW_LINE, BLUE, "SpA: ", pokemonData.getStats().EVs.SpecialAttack, DARK_GRAY, "/", GREEN, "SpD: ", pokemonData.getStats().EVs.SpecialDefence, DARK_GRAY, "/", LIGHT_PURPLE, "Spe: ", pokemonData.getStats().EVs.Speed
                );
        statBuilder.onHover(TextActions.showText(statHover));

        Text.Builder movesBuilder = Text.builder();
        movesBuilder.append(Text.of(DARK_GRAY,BOLD,"[",NONE,GREEN,"Moves",DARK_GRAY,BOLD,"]",NONE));
        Text movesHover = Text.of(
                GREEN, "Move 1: ", LIGHT_PURPLE, pokemonData.getMoveset().get(0) != null ? pokemonData.getMoveset().get(0).toString() : "None",
                NEW_LINE, GREEN, "Move 2: ", LIGHT_PURPLE, pokemonData.getMoveset().get(1) != null ? pokemonData.getMoveset().get(1).toString() : "None",
                NEW_LINE, GREEN, "Move 3: ", LIGHT_PURPLE, pokemonData.getMoveset().get(2) != null ? pokemonData.getMoveset().get(2).toString() : "None",
                NEW_LINE, GREEN, "Move 4: ", LIGHT_PURPLE, pokemonData.getMoveset().get(3) != null ? pokemonData.getMoveset().get(3).toString() : "None"
        );
        movesBuilder.onHover(TextActions.showText(movesHover));

        return Text.of(/*DARK_GRAY,BOLD,"[",NONE,RESET,GREEN, pokemonData.getSpecies().name,DARK_GRAY,BOLD,"]",NONE,RESET, " ", */statBuilder.build(), " ", /*evBuilder.build(), " ", ivBuilder.build(), " ", */movesBuilder.build());
    }

}