package cz.vse.semestralka_4it115.ui.game;

import cz.vse.semestralka_4it115.logic.entity.Person;
import cz.vse.semestralka_4it115.logic.item.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopUITest {

    @Test
    void transferFailsWhenBuyerHasInsufficientMoney() throws Exception {
        Person seller = new Person();
        seller.setName("Seller");
        seller.setMoney(0);

        Person buyer = new Person();
        buyer.setName("Buyer");
        buyer.setMoney(10);

        Item item = new Item("Mapa", 15, 1.0);
        seller.addItemToInventory(item);

        Exception ex = assertThrows(Exception.class, () -> ShopUI.transferItemWithMoney(seller, buyer, item));
        assertTrue(ex.getMessage().contains("nema dost penez"));

        assertTrue(seller.getInventory().getItems().contains(item));
        assertFalse(buyer.getInventory().getItems().contains(item));
        assertEquals(0, seller.getMoney());
        assertEquals(10, buyer.getMoney());
    }

    @Test
    void transferMovesItemAndMoney() throws Exception {
        Person seller = new Person();
        seller.setName("Seller");
        seller.setMoney(5);

        Person buyer = new Person();
        buyer.setName("Buyer");
        buyer.setMoney(30);

        Item item = new Item("Klic", 8, 0.1);
        seller.addItemToInventory(item);

        ShopUI.transferItemWithMoney(seller, buyer, item);

        assertFalse(seller.getInventory().getItems().contains(item));
        assertTrue(buyer.getInventory().getItems().contains(item));
        assertEquals(13, seller.getMoney());
        assertEquals(22, buyer.getMoney());
    }

    @Test
    void findDealerByNameAcceptsPeacefulAlivePerson() throws Exception {
        Person dealer = new Person();
        dealer.setName("Strazny");
        dealer.setIsPeaceful(true);
        dealer.setIsAlive(true);

        Person enemy = new Person();
        enemy.setName("Bandita");
        enemy.setIsPeaceful(false);
        enemy.setIsAlive(true);

        Person found = ShopUI.findDealer("straz", List.of(enemy, dealer));
        assertNotNull(found);
        assertEquals("Strazny", found.getName());
    }
}
