package ru.asofrygin.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private enum KeyWord {
        COUNT, MAX, MIN, SUM, VOID;

        public static KeyWord get(String strKeyWord) {
            switch (strKeyWord) {
                case "count": return COUNT;
                case "max": return MAX;
                case "min": return MIN;
                case "sum": return SUM;
                default: return VOID;
            }
        }

        public static final Map<String, KeyWord> getKeyWord = new HashMap<>(Map.of(
                "count", COUNT,
                "max", MAX,
                "min", MIN,
                "sum", SUM
        ));

        public static final EnumMap<KeyWord, String> getQuery = new EnumMap<>(Map.of(
                COUNT, "SELECT COUNT(*) FROM PRODUCT",
                MAX, "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                MIN, "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                SUM, "SELECT SUM(price) FROM PRODUCT"
        ));

        public static final EnumMap<KeyWord, String> getHeader = new EnumMap<>(Map.of(
                COUNT, "Number of products: ",
                MAX, "<h1>Product with max price: </h1>",
                MIN, "<h1>Product with min price: </h1>",
                SUM, "Summary price: "
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        KeyWord key = KeyWord.get(command);
        if (key == KeyWord.VOID) {
            response.getWriter().println("Unknown command: " + command);
            return;
        }

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(KeyWord.getQuery.get(key));

            response.getWriter().println("<html><body>");
            response.getWriter().println(KeyWord.getHeader.get(key));

            switch (KeyWord.get(command)) {
                case COUNT:
                case SUM:
                    if (rs.next()) {
                        response.getWriter().println(rs.getInt(1));
                    }
                    break;
                case MAX:
                case MIN:
                    while (rs.next()) {
                        String  name = rs.getString("name");
                        int price  = rs.getInt("price");
                        response.getWriter().println(name + "\t" + price + "</br>");
                    }
                    break;
            }

            response.getWriter().println("</body></html>");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
