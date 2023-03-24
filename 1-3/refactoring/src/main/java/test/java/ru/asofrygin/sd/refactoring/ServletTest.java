package test.java.ru.asofrygin.sd.refactoring;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.*;
import ru.asofrygin.sd.refactoring.Main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author asofrygin
 */
class ServletTest {

    public static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @BeforeAll
    public static void init() throws InterruptedException {
        executor.submit(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(1000);
    }

    @AfterAll
    public static void tearDown() {
        executor.shutdown();
    }

    @AfterEach
    public void cleanup() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            final Statement s = conn.createStatement();
            s.executeUpdate("DELETE FROM PRODUCT WHERE 1=1");
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void empty(){
        // only cleanup
    }

    private String send(String Uri) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Uri)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private List<String> parseHtml(String html) {
        return Jsoup.parse(html).body().textNodes().stream()
                .map(textNode -> textNode.text().trim())
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());
    }

    private class Item {
        final String name;
        final int price;

        private Item(String name, int price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Item) {
                return name.equals(((Item) obj).name) && price == ((Item) obj).price;
            }
            return false;
        }
    }

    private List<Item> parseItems(List<String> items) {
        List<Item> parsed = new ArrayList<>();
        for (String item: items) {
            final String[] splitted = item.split(" ");
            parsed.add(new Item(splitted[0], Integer.parseInt(splitted[1])));
        }
        return parsed;
    }

    @Test
    public void testAddProduct() throws IOException, InterruptedException {
        send("http://localhost:8081/add-product?name=item1&price=123");
    }

    @Test
    public void testAddAndGetProduct() throws IOException, InterruptedException {
        send("http://localhost:8081/add-product?name=item2&price=2");
        send("http://localhost:8081/add-product?name=item3&price=3");
        String response = send("http://localhost:8081/get-products");
        List<Item> items = parseItems(parseHtml(response));
        List<Item> expected = Arrays.asList(new Item("item2", 2), new Item("item3", 3));
        Assertions.assertTrue(expected.containsAll(items) && items.containsAll(expected));
    }

}