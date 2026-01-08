package com.quiptmc.core.server;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * An abstract base class for Quipt servlets, extending HttpServlet.
 */
public abstract class QuiptServlet extends HttpServlet {

    /**
     * The QuiptServer instance associated with this servlet.
     */
    private final QuiptServer server;

    /**
     * Constructs a new QuiptServlet with the specified server.
     *
     * @param server the QuiptServer instance to associate with this servlet
     */
    public QuiptServlet(QuiptServer server) {
        this.server = server;
    }

    /**
     * Returns the QuiptServer instance associated with this servlet.
     *
     * @return the QuiptServer instance associated with this servlet
     */
    public QuiptServer server() {
        return server;
    }

    /**
     * Handles HTTP GET requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    /**
     * Handles HTTP DELETE requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the DELETE request
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    /**
     * Handles HTTP HEAD requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the HEAD request
     */
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    /**
     * Returns the ServletConfig object for this servlet.
     *
     * @return the ServletConfig object for this servlet
     */
    @Override
    public ServletConfig getServletConfig() {
        return super.getServletConfig();
    }

    /**
     * Handles HTTP OPTIONS requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the OPTIONS request
     */
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    /**
     * Handles HTTP POST requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    /**
     * Handles HTTP PUT requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the PUT request
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    /**
     * Handles HTTP TRACE requests.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the TRACE request
     */
    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

    /**
     * Receives standard HTTP requests from the public service method
     * and dispatches them to the doXXX methods defined in this class.
     *
     * @param req the HttpServletRequest object that contains the request the client made to the servlet
     * @param resp the HttpServletResponse object that contains the response the servlet returns to the client
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the request
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}