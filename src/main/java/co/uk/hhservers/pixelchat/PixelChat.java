package co.uk.hhservers.pixelchat;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStats;
import com.pixelmonmod.pixelmon.storage.playerData.PlayerData;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import net.minecraft.entity.player.*;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.transform.SimpleTextFormatter;
import org.spongepowered.api.world.World;

import java.lang.reflect.Array;
import java.util.Optional;
import java.util.UUID;

@Plugin(
        id = "pixelchat",
        name = "Pixelchat",
        description = "b",
        url = "b",
        authors = {
                "b"
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
        String message = event.getFormatter().getBody().toText().toPlain();
        logger.info("This should work:"+event.getFormatter().getBody().toText().toPlain());
        //String[] hi = message.split(" ");
        logger.info(message);
        if (message.toLowerCase().startsWith("@poke")) {
            UUID uuid = player.getUniqueId();
            PartyStorage party = Pixelmon.storageManager.getParty(uuid);
            Character slot = message.charAt(message.length() - 1);
            Integer slotInt = Integer.parseInt(slot.toString());
            Pokemon pokemon = party.get(slotInt);
            String pokeName = pokemon.getDisplayName();
            Integer pokeLevel = pokemon.getLevel();
            EVStore evStore = pokemon.getEVs();
            IVStore ivStore = pokemon.getIVs();
            String itemTypeID = "pixelmon:pixelmon_sprite";
            Optional<ItemType> opItem = Sponge.getRegistry().getType(ItemType.class, itemTypeID);
            ItemStackSnapshot itemSnapshot = opItem.get().getTemplate();
            ItemStack realSprite = itemSnapshot.createStack();
            Text EVTemplate = Text.of("Attack: " + evStore.attack + "\nDefence: " + evStore.defence + "\nSpeed: " + evStore.speed + "\nSpAtk: " + evStore.specialAttack + "\nSpDef: " + evStore.specialDefence);
            //Text evs = Text.builder("&l&8[&r&aEVs&r&l&8]&r").onHover(TextActions.showText(EVTemplate)).build();
            Text IVTemplate = Text.of("Attack: " + ivStore.attack + "\nDefence: " + ivStore.defence + "\nSpeed: " + ivStore.speed + "\nSpAtk: " + ivStore.specialAttack + "\nSpDef: " + ivStore.specialDefence);
            Text ivs = Text.builder("[").color(TextColors.DARK_GRAY).append(Text.builder("IVs").color(TextColors.GREEN).build()).append(Text.builder("]").color(TextColors.DARK_GRAY).build()).onHover(TextActions.showText(IVTemplate)).build();
            Text evs = Text.builder("[").color(TextColors.DARK_GRAY).append(Text.builder("EVs").color(TextColors.GREEN).build()).append(Text.builder("]").color(TextColors.DARK_GRAY).build()).onHover(TextActions.showText(EVTemplate)).build();
            event.setCancelled(true);
            World world = player.getWorld();
            Text messageathon = TextSerializers.FORMATTING_CODE.deserialize("&l&8[&r&a" + pokeName + "&l&8]&r").toBuilder()
                    .onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize("&aName:&r " + pokeName + "\n&aLevel:&r " + pokeLevel.toString() + "\n")))
                    //.onHover(TextActions.showItem(itemSnapshot))
                    .append(evs)
                    .append(ivs)
                    .build();
            Text finalmessage = Text.builder("[").color(TextColors.DARK_GRAY)
                    .append(Text.builder(player.getName()).color(TextColors.LIGHT_PURPLE).build())
                    .append(Text.builder("] ").color(TextColors.DARK_GRAY).build())
                    .append(Text.builder("has shared their Pokémon: ").color(TextColors.AQUA).onHover(TextActions.showText(Text.builder("Type @pokeX, replacing X with a slot number to display your Poké in chat.").color(TextColors.LIGHT_PURPLE).build())).build())
                    .append(messageathon).build();
            MessageChannel.TO_PLAYERS.send(finalmessage);
            //player.sendMessage(messageathon);
        }
    }
}
