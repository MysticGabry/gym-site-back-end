package org.mystic.gymsite;

import org.junit.jupiter.api.Test;
import org.mystic.gymsite.entities.Product;
import org.mystic.gymsite.entities.User;
import org.mystic.gymsite.repositories.ProductRepository;
import org.mystic.gymsite.repositories.UserRepository;
import org.mystic.gymsite.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductConcurrencyTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testConcurrentOrders() throws InterruptedException {

        long productId = 2L;
        int initialStock = 10;
        int threadCount = 20;

        // Reset stock
        Product p = productRepository.findById(productId).orElseThrow();
        p.setStock(initialStock);
        productRepository.save(p);
        
        final User testUser = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Utente ID=1 non trovato"));

        System.out.println("===============================================");
        System.out.println("🚀 TEST CONCORRENZA AVVIATO");
        System.out.println("Prodotto ID: " + productId);
        System.out.println("Utente ID: " + testUser.getId());
        System.out.println("Stock iniziale: " + initialStock);
        System.out.println("Thread totali: " + threadCount);
        System.out.println("===============================================");

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successful = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {

            int threadIndex = i;

            new Thread(() -> {
                String threadName = "Thread-" + threadIndex;
                System.out.println(" --->  " + threadName + " avviato");

                try {
                    Map<Long, Integer> items = Map.of(productId, 1);
                    orderService.createOrder(testUser, items);

                    successful.incrementAndGet();
                    System.out.println("   ✔️  " + threadName + " ha ACQUISTATO correttamente");

                } catch (Exception e) {
                    failed.incrementAndGet();
                    System.out.println("   ❌  " + threadName + " NON è riuscito ad acquistare ("
                            + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
                }

                System.out.println(" ⬅️  " + threadName + " terminato");
                latch.countDown();
            }).start();
        }

        latch.await();

        Product updated = productRepository.findById(productId).orElseThrow();

        System.out.println("===============================================");
        System.out.println("🏁 RISULTATI FINALI");
        System.out.println("Thread riusciti:       " + successful.get());
        System.out.println("Thread falliti:        " + failed.get());
        System.out.println("Stock finale prodotto: " + updated.getStock());
        System.out.println("===============================================");

        assertEquals(initialStock, successful.get(), "Numero di acquisti riusciti errato");
        assertEquals(0, updated.getStock(), "Stock finale errato");
    }
}
