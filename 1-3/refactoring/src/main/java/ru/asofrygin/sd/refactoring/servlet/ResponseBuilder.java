package ru.asofrygin.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseBuilder {
    private final HttpServletResponse response;

    public ResponseBuilder(HttpServletResponse response) {
        this.response = response;
    }

    public void addBodyTags() throws IOException {
        response.getWriter().println("<html><body>");
    }

    public void closeBodyTags() throws IOException {
        response.getWriter().println("</body></html>");
    }

    public void finishOkResponse() {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void println(String toPrint) throws IOException {
        response.getWriter().println(toPrint);
    }
}
