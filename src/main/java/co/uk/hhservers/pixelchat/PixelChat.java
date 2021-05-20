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
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
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

    private PokeData pokeData = new PokeData();

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }

    @Listener
    public void onChatMessage(MessageChannelEvent.Chat event, @Getter("getSource") Player player) {
        String msg = event.getFormatter().getBody().toText().toPlain();
        logger.info(msg);
        if (msg.toLowerCase().startsWith("@poke")) {
            event.setCancelled(true);
            UUID uuid = player.getUniqueId();
            PartyStorage party = Pixelmon.storageManager.getParty(uuid);
            Character slot = msg.charAt(msg.length() - 1);
            Integer slotInt = Integer.parseInt(slot.toString())-1;
            if(!PokeData.isNull(party.get(slotInt))) {
                Pokemon pokemon = party.get(slotInt);

                Text wat = PokeData.getHoverText(pokemon);
                Text finalmessage = Text.builder("[").color(TextColors.DARK_GRAY)
                        .append(Text.builder(player.getName()).color(TextColors.LIGHT_PURPLE).build())
                        .append(Text.builder("] ").color(TextColors.DARK_GRAY).build())
                        .append(Text.builder(": ").color(TextColors.AQUA).onHover(TextActions.showText(Text.builder("Type @pokeX, replacing X with a slot number to display your Poke in chat.").color(TextColors.LIGHT_PURPLE).build())).build())
                        .append(wat).build();
                MessageChannel.TO_PLAYERS.send(finalmessage);
            } else {player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&aPixel&bChat&l&8]&r &bYou do not have a Pokemon in this slot!"));}
        }
        if (msg.toLowerCase().startsWith("!party")) {
            event.setCancelled(true);
            UUID uuid = player.getUniqueId();
            PartyStorage playerParty = Pixelmon.storageManager.getParty(uuid);
            Text partyMessage = Text.builder("[").color(TextColors.DARK_GRAY)
                    .append(Text.builder(player.getName()).color(TextColors.LIGHT_PURPLE).build())
                    .append(Text.builder("] ").color(TextColors.DARK_GRAY).build())
                    .append(Text.builder(": ").color(TextColors.AQUA).onHover(TextActions.showText(Text.builder("Type @party to display your Party in chat.").color(TextColors.LIGHT_PURPLE).build())).build())
                    .build();
            Text hoverMessage = Text.builder()
                    .append(partyMessage)
                    .append(TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&aPARTY&r&l&8]&r"))
                    .onHover(TextActions.showText(PokeData.getPartyText(playerParty)))
                    .build();
            MessageChannel.TO_PLAYERS.send(hoverMessage);
            //MessageChannel.TO_PLAYERS.send(PokeData.getPartyText(playerParty));
        }
    }
}
