package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;

import java.text.Normalizer;
import java.util.List;

import static cz.vse.semestralka_4it115.ui.game.GameUI.GH;

/**
 * Manages trading between player and peaceful NPCs.
 */
public class ShopUI {
    /**
     * Starts console trading loop with selected dealer.
     */
    public static void start(String osoba) throws Exception {
        List<Person> otherPeople = GH.game.getCurrentRoom().getOtherPeople();
        Person player = GH.game.getPlayer();
        Person dealer = findDealer(osoba, otherPeople);

        while (true) {
            printTradeState(player, dealer);
            System.out.println("Prikazy: koupit <id>, prodat <id>, konec");
            System.out.print("> ");
            String line = GameUI.sc.nextLine();
            if (line == null) {
                continue;
            }
            String input = line.trim().toLowerCase();
            if (input.equals("konec")) {
                System.out.println("Obchod ukoncen.");
                return;
            }

            String[] tokens = input.split("\\s+");
            if (tokens.length != 2) {
                System.out.println("Neplatny prikaz. Pouzijte: koupit <id>, prodat <id>, konec");
                continue;
            }

            int itemIndex;
            try {
                itemIndex = Integer.parseInt(tokens[1]);
            } catch (NumberFormatException e) {
                System.out.println("ID polozky musi byt cele cislo.");
                continue;
            }

            if (itemIndex <= 0) {
                System.out.println("ID polozky musi byt kladne cislo.");
                continue;
            }

            try {
                if (tokens[0].equals("koupit")) {
                    buyByIndex(player, dealer, itemIndex);
                } else if (tokens[0].equals("prodat")) {
                    sellByIndex(player, dealer, itemIndex);
                } else {
                    System.out.println("Neplatny prikaz. Pouzijte: koupit <id>, prodat <id>, konec");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Finds a peaceful and alive dealer by index or name prefix.
     */
    public static Person findDealer(String osoba, List<Person> otherPeople) throws Exception {
        int index;
        try {
            index = Integer.parseInt(osoba);
        } catch (NumberFormatException ignored) {
            index = -1;
        }

        if (index > 0 && index <= otherPeople.size()) {
            Person candidate = otherPeople.get(index - 1);
            validateDealer(candidate);
            return candidate;
        }

        if (index == -1) {
            String normalizedInput = normalize(osoba);
            for (Person p : otherPeople) {
                if (normalize(p.getName()).startsWith(normalizedInput)) {
                    validateDealer(p);
                    return p;
                }
            }
            throw new Exception("Obchodnik nenalezen.");
        }

        throw new Exception("Nevalidni prikaz.");
    }

    /**
     * Buys one dealer item by index.
     */
    public static void buyByIndex(Person player, Person dealer, int itemIndex) throws Exception {
        List<Item> dealerItems = dealer.getInventory().getItems();
        if (dealerItems.isEmpty()) {
            throw new Exception("Obchodnik nema zadne polozky k prodeji.");
        }
        if (itemIndex > dealerItems.size()) {
            throw new Exception("Neplatne ID polozky u obchodnika.");
        }
        Item item = dealerItems.get(itemIndex - 1);
        transferItemWithMoney(dealer, player, item);
        System.out.println("Koupil jsi: " + item.getName() + " za " + item.getValue() + " korun.");
    }

    /**
     * Sells one player item by index.
     */
    public static void sellByIndex(Person player, Person dealer, int itemIndex) throws Exception {
        List<Item> playerItems = player.getInventory().getItems();
        if (playerItems.isEmpty()) {
            throw new Exception("Nemate zadne polozky k prodeji.");
        }
        if (itemIndex > playerItems.size()) {
            throw new Exception("Neplatne ID polozky v batohu.");
        }
        Item item = playerItems.get(itemIndex - 1);
        transferItemWithMoney(player, dealer, item);
        System.out.println("Prodal jsi: " + item.getName() + " za " + item.getValue() + " korun.");
    }

    /**
     * Transfers item from seller to buyer and moves money to seller.
     */
    public static void transferItemWithMoney(Person seller, Person buyer, Item item) throws Exception {
        if (seller == null || buyer == null || item == null) {
            throw new Exception("Neplatna transakce.");
        }
        if (!seller.getInventory().getItems().contains(item)) {
            throw new Exception("Prodavajici danou polozku nevlastni.");
        }

        int price = item.getValue();
        if (buyer.getMoney() < price) {
            throw new Exception(buyer.getName() + " nema dost penez na tuto polozku.");
        }

        buyer.addItemToInventory(item);
        seller.dropItemFromInventory(item);
        buyer.setMoney(buyer.getMoney() - price);
        seller.setMoney(seller.getMoney() + price);
    }

    private static void printTradeState(Person player, Person dealer) {
        System.out.println("=== Obchod s: " + dealer.getName() + " ===");
        System.out.println(player.getName() + " - penize: " + player.getMoney());
        System.out.println("Obchodnik - penize: " + dealer.getMoney());
        System.out.println();
        System.out.println("Polozky obchodnika:");
        printItems(dealer.getInventory().getItems());
        System.out.println("Tvoje polozky:");
        printItems(player.getInventory().getItems());
    }

    private static void printItems(List<Item> items) {
        if (items.isEmpty()) {
            System.out.println("  (zadne)");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.println("  " + (i + 1) + ". " + item.getName() + " (" + item.getValue() + " korun, " + item.getWeight() + " kg)");
        }
    }

    private static void validateDealer(Person dealer) throws Exception {
        if (!dealer.isAlive()) {
            throw new Exception(dealer.getName() + " je mrtvy a nemuze obchodovat.");
        }
        if (!dealer.isPeaceful()) {
            throw new Exception(dealer.getName() + " neni mirumilovny, nelze s nim obchodovat.");
        }
    }

    private static String normalize(String value) {
        String noDiacritics = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noDiacritics.toLowerCase();
    }
}
