package ru.asofrygin.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    public GetProductsServlet() {
        super();
        helper = new ServletHelper("jdbc:sqlite:test.db");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        helper.connectAndExecute(stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            response.getWriter().println("<html><body>");

            while (rs.next()) {
                String name = rs.getString("name");
                int price   = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");

            rs.close();
        });

        helper.finishOkResponse(response);
    }

    private final ServletHelper helper;
}