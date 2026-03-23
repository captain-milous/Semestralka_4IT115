package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.serializer.SerHandler;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Manages dialogue interactions between the player and NPCs, loading and saving conversation data.
 *
 * @author Milos Tesar
 * @version BETA
 * @since 2025-06-05
 */
public class TalkUI {
    private static final String CONVERSATIONS_FILE = "resources\\conversations.ser";

    /**
     * Initiates a conversation session with the specified NPC, processing dialogue until completion.
     *
     * @param osoba the identifier (name or index) of the NPC to talk to
     * @throws Exception if the NPC cannot be found or an error occurs during conversation
     */
    public static void start(String osoba) throws Exception {
        if (GH == null || GH.game == null || GH.game.getCurrentRoom() == null) {
            throw new Exception("Herni stav neni inicializovan.");
        }
        List<Person> otherPeople = GH.game.getCurrentRoom().getOtherPeople();

        Person target = findTalkablePerson(osoba, otherPeople);
        if (target == null) {
            throw new Exception("Osoba '" + osoba + "' nenalezena v mistnosti.");
        }
        if (!target.isAlive()) {
            throw new Exception("S mrtvou postavou nelze mluvit.");
        }
        if (!target.isPeaceful()) {
            throw new Exception("S nepratelem nelze mluvit.");
        }

        Map<TalkablePeople, String> conversations = SerHandler.loadConversationsMap(CONVERSATIONS_FILE);
        String conversationText = resolveConversationText(target, conversations);
        System.out.println(conversationText + "\n");
    }

    static String resolveConversationText(Person target, Map<TalkablePeople, String> conversations) {
        TalkablePeople key = getTalkableEnum(target);
        if (key == TalkablePeople.KRAL) {
            return getKingConversation();
        }
        if (key != null) {
            String savedConversation = conversations.get(key);
            if (savedConversation != null && !savedConversation.isBlank()) {
                return savedConversation;
            }
        }
        return getFallbackConversation(target);
    }

    private static String getFallbackConversation(Person target) {
        String normalizedName = normalize(target.getName());

        if (normalizedName.startsWith("kral")) {
            return getKingConversation();
        }
        if (normalizedName.startsWith("hospodsky")) {
            return "Hospodsky: Dobre jidlo a piti ti na ceste muze zachranit zivot.";
        }
        if (normalizedName.startsWith("kovar")) {
            return "Kovar: Kdyz chces lepsi vybavu, stav se u me v kovarne.";
        }
        if (target.getInventory() != null && !target.getInventory().getItems().isEmpty()) {
            return target.getName() + ": Nezdrzuju, pokud neco potrebujes, obchoduj.";
        }
        return target.getName() + ": Ted ti nemam co rict.";
    }

    private static String getKingConversation() {
        return "Kral: Posle, kralovstvi je v nebezpeci.\n"
                + "Na hranicich se shromazduji nepratele a nasi lide ztraceji nadeji.\n"
                + "Potrebuji, abys dorucil kralovskou pecet do druheho hradu driv, nez padne noc.\n"
                + "Jen tak dokazeme sjednotit vojska, uzavrit spojenectvi a zachranit nasi zemi.\n"
                + "Vezmi tento ukol vazne. Budoucnost kralovstvi lezi ve tvych rukou.";
    }

    /**
     * Helper method to locate a Person in the current room by index or exact name.
     *
     * @param osoba       the name or index of the person to find
     * @param otherPeople the list of Person objects available in the room
     * @return the matching Person, or null if not found
     */
    static Person findTalkablePerson(String osoba, List<Person> otherPeople) {
        if (osoba == null || osoba.isBlank()) {
            return null;
        }
        if (otherPeople == null || otherPeople.isEmpty()) {
            return null;
        }

        try {
            int index = Integer.parseInt(osoba);
            if (index > 0 && index <= otherPeople.size()) {
                return otherPeople.get(index - 1);
            }
        } catch (NumberFormatException ignored) {

        }

        for (Person p : otherPeople) {
            if (normalize(p.getName()).startsWith(normalize(osoba))) {
                return p;
            }
        }

        return null;
    }

    /**
     * Converts a Person object to its corresponding TalkablePeople enum value based on the person's name.
     *
     * @param p the Person object to convert
     * @return the matching TalkablePeople enum or null if no match exists
     */
    static TalkablePeople getTalkableEnum(Person p) {
        String key = normalize(p.getName());

        if (key.startsWith("kral")) {
            return TalkablePeople.KRAL;
        }
        if (key.startsWith("radce")) {
            return TalkablePeople.RADCE;
        }
        if (key.startsWith("tajemnik") || key.startsWith("pocestny")) {
            return TalkablePeople.TAJEMNIK;
        }
        if (key.startsWith("strazny") || key.startsWith("straz")) {
            return TalkablePeople.STRAZNY;
        }

        return null;
    }

    /**
     * Normalizes text to lowercase without diacritics for stable matching.
     *
     * @param input source text
     * @return normalized text
     */
    private static String normalize(String input) {
        String noDiacritics = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noDiacritics.toLowerCase();
    }
}
