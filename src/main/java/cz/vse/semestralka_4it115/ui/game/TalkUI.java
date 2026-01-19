package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.serializer.SerHandler;

import java.util.List;
import java.util.Map;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Manages dialogue interactions between the player and NPCs, loading and saving conversation data.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-06-05
 */
public class TalkUI {

    /**
     * Initiates a conversation session with the specified NPC, processing dialogue until completion.
     *
     * @param osoba the identifier (name or index) of the NPC to talk to
     * @throws Exception if the NPC cannot be found or an error occurs during conversation
     */
    public static void start(String osoba) throws Exception {
        List<Person> otherPeople = GH.game.getCurrentRoom().getOtherPeople();

        Person target = findTalkablePerson(osoba, otherPeople);
        if (target == null) {
            throw new Exception("Osoba '" + osoba + "' nenalezena v místnosti.");
        }

        Map<TalkablePeople, String> conversations =
                SerHandler.loadConversationsMap("resources\\conversations.ser");

        TalkablePeople tp = getTalkableEnum(target);
        if (tp == null) {
            throw new Exception("Nelze určit enum TalkablePeople pro osobu '"
                    + target.getName() + "'.");
        }

        if (!conversations.containsKey(tp)) {
            throw new Exception("Pro osobu '" + target.getName()
                    + "' není definován žádný dialog.");
        }

        System.out.println(conversations.get(tp) + "\n");
    }

    /**
     * Helper method to locate a Person in the current room by index or exact name.
     *
     * @param osoba       the name or index of the person to find
     * @param otherPeople the list of Person objects available in the room
     * @return the matching Person, or null if not found
     */
    private static Person findTalkablePerson(String osoba, List<Person> otherPeople) {
        try {
            int index = Integer.parseInt(osoba);
            if (index > 0 && index <= otherPeople.size()) {
                return otherPeople.get(index - 1);
            }
        } catch (NumberFormatException ignored) {

        }

        for (Person p : otherPeople) {
            if (p.toString().equalsIgnoreCase(osoba)) {
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
    private static TalkablePeople getTalkableEnum(Person p) {
        String rawName = p.getName();
        // TODO: Dodělat!

        try {
            //return TalkablePeople.valueOf(key);
        } catch (IllegalArgumentException e) {
            // Pokud hodnota neexistuje v enumu, vrátíme null
            return null;
        }
        return null;
    }
}
