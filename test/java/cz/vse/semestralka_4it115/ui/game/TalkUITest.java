package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests NPC alias mapping and conversation text resolution in dialogue logic.
 *
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
class TalkUITest {

    @Test
    void getTalkableEnumSupportsExpectedAliases() {
        Person pocestny = new Person();
        pocestny.setName("Pocestny");
        assertEquals(TalkablePeople.TAJEMNIK, TalkUI.getTalkableEnum(pocestny));

        Person strazny = new Person();
        strazny.setName("Strazny");
        assertEquals(TalkablePeople.STRAZNY, TalkUI.getTalkableEnum(strazny));
    }

    @Test
    void resolveConversationUsesSerializedEntryWhenPresent() {
        Person strazny = new Person();
        strazny.setName("Strazny");

        Map<TalkablePeople, String> conversations = new HashMap<>();
        conversations.put(TalkablePeople.STRAZNY, "Brana je uzavrena.");

        String result = TalkUI.resolveConversationText(strazny, conversations);
        assertEquals("Brana je uzavrena.", result);
    }

    @Test
    void resolveConversationFallsBackForUnknownNpc() {
        Person hospodsky = new Person();
        hospodsky.setName("Hospodsky");

        String result = TalkUI.resolveConversationText(hospodsky, Map.of());
        assertTrue(result.startsWith("Hospodsky:"));
    }

    @Test
    void resolveConversationFallsBackToLongKingDialogWhenMissingSerializedConversation() {
        Person kral = new Person();
        kral.setName("Kral");

        String result = TalkUI.resolveConversationText(kral, Map.of());
        assertTrue(result.contains("kralovstvi je v nebezpeci"));
        assertTrue(result.contains("kralovskou pecet"));
    }

    @Test
    void resolveConversationUsesLongKingDialogEvenWhenSerializedConversationExists() {
        Person kral = new Person();
        kral.setName("Kral");

        Map<TalkablePeople, String> conversations = new HashMap<>();
        conversations.put(TalkablePeople.KRAL, "kratky dialog");

        String result = TalkUI.resolveConversationText(kral, conversations);
        assertTrue(result.contains("Budoucnost kralovstvi lezi ve tvych rukou."));
    }

    @Test
    void findTalkablePersonFindsByIndexAndPrefix() {
        Person strazny = new Person();
        strazny.setName("Strazny");

        Person kovar = new Person();
        kovar.setName("Kovar");

        List<Person> people = List.of(strazny, kovar);

        Person byIndex = TalkUI.findTalkablePerson("1", people);
        assertNotNull(byIndex);
        assertEquals("Strazny", byIndex.getName());

        Person byPrefix = TalkUI.findTalkablePerson("kov", people);
        assertNotNull(byPrefix);
        assertEquals("Kovar", byPrefix.getName());
    }
}
