package dk.stonemountain.demo.demofx.authentication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Webserver {
    private static final Logger log = LoggerFactory.getLogger(Webserver.class);

    private final Consumer<HttpExchange> requestHandler;

    public Webserver(Consumer<HttpExchange> handler) {
        this.requestHandler = handler;
    }

	private final ExecutorService exec = Executors.newFixedThreadPool(10, r -> {
        Thread t = new Thread(r);
        t.setName("webserver thread");
        t.setDaemon(true);
        log.info("Webserver thread created");
        return t;
    });
    private HttpServer server = null;

    public int start() throws IOException {
        if (server != null) {
            return server.getAddress().getPort();
        } else {
            var adr = new InetSocketAddress("localhost", 0);
            server = HttpServer.create(adr, 0);
            server.setExecutor(exec);
            HttpContext context = server.createContext("/authenticate");
            context.setHandler(new CallbackHandler(requestHandler));
            server.start();
            var port = server.getAddress().getPort();
            log.info("Webserver using port {}", port);
            return port;
        }
    }

    public void stop() {
        if (server != null) {
            log.info("Webserver stopping");
            server.stop(0);
            server = null;
        }
    }

    public void callResponse(HttpExchange exchange, String contentType, String message, int statusCode) {
        exchange.getResponseHeaders().add("Content-Type", contentType);
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(statusCode, message.getBytes().length);
            os.write(message.getBytes());
        } catch (IOException e) {
            log.error("Failed to response callback", e);
        }
    }

    public static class CallbackHandler implements HttpHandler {
        private final Consumer<HttpExchange> requestHandler;

        public CallbackHandler(Consumer<HttpExchange> requestHandler) {
            log.info("Webserver callback handler instantiated");
            this.requestHandler = requestHandler;
        }

        @Override
        public void handle(HttpExchange exchange) {
            log.info("callback invoked");
            try {
                log.info("Handling incoming request: {}", exchange.getRequestMethod(), exchange.getRequestURI().toString());
            } catch (Exception e){
                e.printStackTrace();
            }
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                requestHandler.accept(exchange);
            }
        }
    }
}
