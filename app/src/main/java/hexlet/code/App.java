package hexlet.code;

import lombok.extern.slf4j.Slf4j;

import io.javalin.Javalin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Slf4j
public class App {
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }
}
