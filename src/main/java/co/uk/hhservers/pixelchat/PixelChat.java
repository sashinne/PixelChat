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
            Integer slotInt = Integer.parseInt(slot.toString())-1;
            Pokemon pokemon = party.get(slotInt);
            event.setCancelled(true);
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
            //TODO: Set up party formatter in PokeData like Stats formatter
        }
    }
}
