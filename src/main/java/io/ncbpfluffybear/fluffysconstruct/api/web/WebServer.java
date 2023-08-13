package io.ncbpfluffybear.fluffysconstruct.api.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.List;

public class WebServer {

    private HttpServer server;

    public WebServer() {
    }

    public void start(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(FCPlugin.getInstance().getServer().getIp(), port), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();

        WebUtils.copyWebpages(); // Save default webpages
    }

    private static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String request = exchange.getRequestURI().toASCIIString().substring(1); // ASCII string replaces "%20" with " " for example. subtring removes first "/"
            byte[] bytes = WebUtils.getResponse(request);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream out = exchange.getResponseBody();
            out.write(bytes);
            out.close();
        }
    }

    public HttpServer getServer() {
        return server;
    }
}