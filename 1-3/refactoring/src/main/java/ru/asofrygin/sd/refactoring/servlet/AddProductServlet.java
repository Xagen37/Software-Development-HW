package ru.asofrygin.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    public AddProductServlet() {
        super();
        helper = new ServletHelper("jdbc:sqlite:test.db");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final ResponseBuilder writer = new ResponseBuilder(response);
        List<String> parameters = helper.getFromRequest(request, List.of("name", "price"));
        String sql = "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\""
                    + parameters.get(0)
                    + "\","
                    + Long.parseLong(parameters.get(1))
                    + ")";

        helper.connectAndExecute(stmt -> stmt.executeUpdate(sql));

        writer.finishOkResponse();
        writer.println("OK");
    }

    private final ServletHelper helper;
}
