package co.uk.hhservers.pixelchat;

import com.google.inject.Inject;
import com.pixelmongenerations.common.entity.pixelmon.stats.links.NBTLink;
import com.pixelmongenerations.core.network.PixelmonData;
import com.pixelmongenerations.core.storage.PixelmonStorage;
import com.pixelmongenerations.core.storage.PlayerStorage;
import net.minecraft.nbt.NBTTagCompound;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

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
            PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(uuid).get();
            char slot = msg.charAt(msg.length() - 1);
            int slotInt = Integer.parseInt(Character.toString(slot))-1;
            NBTTagCompound pokemon = party.getTeamIncludeEgg().get(slotInt);
            NBTLink link = new NBTLink(pokemon);
            event.setCancelled(true);
            Text wat = PokeData.getHoverText(link);
            Text finalmessage = Text.builder("[").color(TextColors.DARK_GRAY)
                    .append(Text.builder(player.getName()).color(TextColors.LIGHT_PURPLE).build())
                    .append(Text.builder("] ").color(TextColors.DARK_GRAY).build())
                    .append(Text.builder("has shared their Pokemon: ").color(TextColors.AQUA).onHover(TextActions.showText(Text.builder("Type @pokeX, replacing X with a slot number to display your Poke in chat.").color(TextColors.LIGHT_PURPLE).build())).build())
                    .append(Text.of(Text.NEW_LINE))
                    .append(wat).build();
            MessageChannel.TO_PLAYERS.send(finalmessage);
        }
        if (msg.toLowerCase().startsWith("@party")) {
            event.setCancelled(true);
            UUID uuid = player.getUniqueId();
            PixelmonData[] playerParty = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(uuid).get().convertToData();
            Text partyMessage = Text.builder("[").color(TextColors.DARK_GRAY)
                    .append(Text.builder(player.getName()).color(TextColors.LIGHT_PURPLE).build())
                    .append(Text.builder("] ").color(TextColors.DARK_GRAY).build())
                    .append(Text.builder("has shared their ").color(TextColors.AQUA).onHover(TextActions.showText(Text.builder("Type @party to display your Party in chat.").color(TextColors.LIGHT_PURPLE).build())).build())
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
