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
        final ResponseBuilder writer = new ResponseBuilder(response);
        helper.connectAndExecute(stmt -> {
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            writer.addBodyTags();

            while (rs.next()) {
                String name = rs.getString("name");
                int price   = rs.getInt("price");
                writer.println(name + "\t" + price + "</br>");
            }
            writer.closeBodyTags();
            rs.close();
        });

        writer.finishOkResponse();
    }

    private final ServletHelper helper;
}
