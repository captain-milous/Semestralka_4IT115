package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;

import java.util.List;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Manages the shop interface, allowing the player to interact with peaceful NPCs to buy or sell items.
 *
 * @author Miloš Tesař
 * @version BETA
 * @since 2025-04-03
 */
public class ShopUI {
    private static List<Person> otherPeople;

    /**
     * Begins a shopping session with the specified dealer, handling item transactions until the player exits or an error occurs.
     *
     * @param osoba the identifier (name or index) of the dealer to interact with
     * @throws Exception if the dealer cannot be found or an error occurs during the shopping session
     */
    public static void start(String osoba) throws Exception {
        otherPeople = GH.game.getCurrentRoom().getOtherPeople();
        Person player = GH.game.getPlayer();
        Person dealer = findDealer(osoba);

        if (dealer != null) {
            while (true) {
                if (!dealer.getInventory().getItems().isEmpty()) {
                    if (player.getMoney() > 0) {
                        //dodělat obchodování
                        System.out.println("Zatím nelze nakupovat. Příkaz pro nákup je předpřipraven pro další verzi.");
                        break;
                    } else {
                        throw new Exception("Nemáš peníze.");
                    }
                } else {
                    throw new Exception("Daná osoba nemá žádné položky k nákupu.");
                }
            }
        } else {
            throw new Exception("Obchodník nenalezen.");
        }

    }

    /**
     * Searches for a suitable dealer in the current room by index or name, ensuring the dealer is available and alive.
     *
     * @param osoba the identifier (name or index) of the dealer to find
     * @return the Person object representing the dealer, or null if not found
     * @throws Exception if the chosen dealer is dead or not peaceful
     */
    private static Person findDealer(String osoba) throws Exception {
        Person dealer = null;
        int index = 0;
        try {
            index = Integer.parseInt(osoba);
        } catch (Exception e) {
            index = -1;
        }
        if (index > 0 && index <= otherPeople.size()) {
            try {
                index -= 1;
                if (otherPeople.get(index).isPeaceful() || otherPeople.get(index).isAlive()) {
                    dealer = otherPeople.get(index);
                    return dealer;
                } else {
                    throw new Exception(otherPeople.get(index).getName() + " je buď mrtvý nebo není mírumilovný.");
                }
            } catch (Exception e) {
                throw new Exception("Nepovedlo se načíst protivníka " + otherPeople.get(index).getName() + ": " + e.getMessage());
            }

        } else if (index == -1) {
            for (Person p : otherPeople) {
                String tName = p.getName().toLowerCase();
                if (osoba.toLowerCase().startsWith(tName)) {
                    try {
                        if (p.isPeaceful() || p.isAlive()) {
                            dealer = p;
                            return dealer;
                        } else {
                            throw new Exception(p.getName() + " je buď mrtvý nebo není mírumilovný.");
                        }
                    } catch (Exception e) {
                        throw new Exception("Nepovedlo se načíst protivníka " + p.getName());
                    }
                }
            }
        } else {
            throw new Exception("Nevalidní příkaz.");
        }

        return dealer;
    }

    /**
     * Facilitates the transaction process with the dealer, allowing the player to buy or sell items and updating inventories.
     *
     * @param dealer the Person object representing the dealer
     * @return the same dealer after transactions
     * @throws Exception if a transaction fails or is invalid
     */
    private static Person shop(Person dealer) throws Exception {
        Person player = GH.game.getPlayer();

        return dealer;
    }
}
