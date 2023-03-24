package ru.asofrygin.sd.refactoring.servlet;

import org.junit.jupiter.api.function.ThrowingConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServletHelper {
    private final String DBUrl;

    public ServletHelper(String DBUrl) {
        this.DBUrl = DBUrl;
    }

    public void connectAndExecute(ThrowingConsumer<Statement> callback) {
        try (Connection c = DriverManager.getConnection(DBUrl)) {
            Statement stmt = c.createStatement();
            callback.accept(stmt);
            stmt.close();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getFromRequest(HttpServletRequest request, List<String> names) {
        List<String> ret = new ArrayList<>();
        names.forEach(name -> ret.add(request.getParameter(name)));
        return ret;
    }

    public void finishOkResponse(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
