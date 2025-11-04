package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * Part 1 – Test BarnesAndNoble using manual test doubles (no Mockito here)
 */
class BarnesAndNobleTest {

    /**
     * Fake BookDatabase for controlled test data
     */
    static class FakeBookDatabase implements BookDatabase {
        private final Book stored;

        FakeBookDatabase(Book stored) {
            this.stored = stored;
        }

        @Override
        public Book findByISBN(String ISBN) {
            return stored;
        }
    }

    /**
     * Fake BuyBookProcess so we do not alter external state
     */
    static class FakeBuyBookProcess implements BuyBookProcess {
        int calledTimes = 0;
        Book lastBook = null;
        int lastAmount = 0;

        @Override
        public void buyBook(Book book, int amount) {
            calledTimes++;
            lastBook = book;
            lastAmount = amount;
        }
    }

    @Test
    @DisplayName("spec: Computes total price correctly for items in cart")
    void testPriceComputation() {

        // Arrange
        Book book = new Book("111", 10, 5);
        FakeBookDatabase db = new FakeBookDatabase(book);
        FakeBuyBookProcess process = new FakeBuyBookProcess();
        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> cart = Map.of("111", 3);

        // Act
        PurchaseSummary summary = store.getPriceForCart(cart);

        // Assert
        assertNotNull(summary);
        assertEquals(30, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());

        assertEquals(1, process.calledTimes);
        assertEquals(book, process.lastBook);
        assertEquals(3, process.lastAmount);
    }

    @Test
    @DisplayName("Struct: Records unavailable amounts when request exceeds stock")
    void testUnavailableWhenQuantityTooHigh() {

        // Arrange
        Book book = new Book("222", 12, 2);
        FakeBookDatabase db = new FakeBookDatabase(book);
        FakeBuyBookProcess process = new FakeBuyBookProcess();
        BarnesAndNoble store = new BarnesAndNoble(db, process);

        Map<String, Integer> cart = Map.of("222", 5);

        // Act
        PurchaseSummary summary = store.getPriceForCart(cart);

        // Assert
        assertEquals(2 * 12, summary.getTotalPrice());
        assertFalse(summary.getUnavailable().isEmpty());
        assertEquals(3, summary.getUnavailable().get(book));
        assertEquals(1, process.calledTimes);
    }
}