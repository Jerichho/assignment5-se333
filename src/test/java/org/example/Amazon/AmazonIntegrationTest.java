package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmazonIntegrationTest {

    @Test
    @DisplayName("Integration: Calculates total with real ShoppingCartAdaptor + all price rules")
    void testFullPriceCalculation() {
        Database db = new Database();
        ShoppingCart cart = new ShoppingCartAdaptor(db);

        Amazon store = new Amazon(cart,
                List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics()));

        // Add items
        store.addToCart(new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000));
        store.addToCart(new Item(ItemType.ELECTRONIC, "Phone", 1, 200));


        double total = store.calculate();

        assertEquals(1212.50, total, 0.001);
    }
}