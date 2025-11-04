package org.example.Amazon;

import org.example.Amazon.Cost.PriceRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AmazonUnitTest {

    @Test
    @DisplayName("Unit: Amazon uses rules and calls getItems exactly once")
    void testRuleApplicationAndCartInteraction() {
        // Mock cart
        ShoppingCart cart = Mockito.mock(ShoppingCart.class);
        List<Item> fakeList = List.of(new Item(null, "X", 1, 10.0));
        when(cart.getItems()).thenReturn(fakeList);

        // Fake rule returning fixed cost
        PriceRule rule = items -> 50.0;

        Amazon store = new Amazon(cart, List.of(rule));

        double result = store.calculate();

        assertEquals(50.0, result);

        // Verify behavior
        verify(cart, times(1)).getItems();
    }
}