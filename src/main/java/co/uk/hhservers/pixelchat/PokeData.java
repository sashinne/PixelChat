package co.uk.hhservers.pixelchat;

import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;

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
}
