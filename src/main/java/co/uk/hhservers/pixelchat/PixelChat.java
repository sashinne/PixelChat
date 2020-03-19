package co.uk.hhservers.pixelchat;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import java.util.UUID;

@Plugin(
        id = "chatpixel",
        name = "PixelChat",
        description = "PixelChat allows you to display your Pixelmon in chat",
        url = "http://hhservers.co.uk",
        authors = {
                "blvxr"
        }
)
public class PixelChat {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    @Listener
    public void onChatMessage(MessageChannelEvent.Chat event, @Getter("getSource") Player player) {
        String msg = event.getFormatter().getBody().toText().toPlain();
        logger.info(msg);
        if (msg.toLowerCase().startsWith("@poke")) {
            UUID uuid = player.getUniqueId();
            PartyStorage party = Pixelmon.storageManager.getParty(uuid);
            Character slot = msg.charAt(msg.length() - 1);
            Integer slotInt = Integer.parseInt(slot.toString());
            Pokemon pokemon = party.get(slotInt);
            String pokeName = pokemon.getDisplayName();
            //Integer pokeLevel = pokemon.getLevel();
            //EVStore evStore = pokemon.getEVs();
            //IVStore ivStore = pokemon.getIVs();
            //String itemTypeID = "pixelmon:pixelmon_sprite";
            //Optional<ItemType> opItem = Sponge.getRegistry().getType(ItemType.class, itemTypeID);
            //ItemStackSnapshot itemSnapshot = opItem.get().getTemplate();
            //ItemStack realSprite = itemSnapshot.createStack();
            //Text EVTemplate = TextSerializers.FORMATTING_CODE.deserialize("&aAttack:&d " + evStore.attack + "\n&aDefence:&d " + evStore.defence + "\n&aSpeed:&d " + evStore.speed + "\n&aSpAtk:&d " + evStore.specialAttack + "\n&aSpDef:&d " + evStore.specialDefence);
            //Text evs = Text.builder("&l&8[&r&aEVs&r&l&8]&r").onHover(TextActions.showText(EVTemplate)).build();
            //Text IVTemplate = TextSerializers.FORMATTING_CODE.deserialize("&aAttack:&d " + ivStore.attack + "\n&aDefence:&d " + ivStore.defence + "\n&aSpeed:&d " + ivStore.speed + "\n&aSpAtk:&d " + ivStore.specialAttack + "\n&aSpDef:&d " + ivStore.specialDefence);
            //Text ivs = Text.builder("[").color(TextColors.DARK_GRAY).append(Text.builder("IVs").color(TextColors.GREEN).build()).append(Text.builder("]").color(TextColors.DARK_GRAY).build()).onHover(TextActions.showText(IVTemplate)).build();
            //Text evs = Text.builder("[").color(TextColors.DARK_GRAY).append(Text.builder("EVs").color(TextColors.GREEN).build()).append(Text.builder("]").color(TextColors.DARK_GRAY).build()).onHover(TextActions.showText(EVTemplate)).build();
            event.setCancelled(true);
            World world = player.getWorld();
            /*Text messageathon = TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&a" + pokeName + "&l&8]&r").toBuilder()
                    .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize("&aGender:&d " + pokemon.getGender().toString()
                            + "\n&aLevel:&d " + pokeLevel.toString()
                            + "\n&aAbility:&d " + pokemon.getAbilityName()
                            + "\n&aNature:&d " + pokemon.getNature().name()
                            + "\n&aHappiness:&d " + pokemon.getFriendship())))
                    .append(evs)
                    .append(ivs)
                    .build();*/
            Text wat = PokeData.getHoverText(pokemon);
            Text finalmessage = Text.builder("[").color(TextColors.DARK_GRAY)
                    .append(Text.builder(player.getName()).color(TextColors.LIGHT_PURPLE).build())
                    .append(Text.builder("] ").color(TextColors.DARK_GRAY).build())
                    .append(Text.builder("has shared their Pokémon: ").color(TextColors.AQUA).onHover(TextActions.showText(Text.builder("Type @pokeX, replacing X with a slot number to display your Poké in chat.").color(TextColors.LIGHT_PURPLE).build())).build())
                    .append(Text.of(Text.NEW_LINE))
                    .append(wat).build();
            MessageChannel.TO_PLAYERS.send(finalmessage);
        }
        if (msg.toLowerCase().startsWith("@party")) {

            UUID uuid = player.getUniqueId();
            PartyStorage playerParty = Pixelmon.storageManager.getParty(uuid);
            Text hhmessage = Text.of("hi");
            logger.info((String.valueOf(playerParty.getAll().length)));
        }
    }
}
