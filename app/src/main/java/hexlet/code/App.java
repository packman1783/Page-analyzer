package hexlet.code;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.BaseRepository;

import hexlet.code.util.NamedRoutes;
import lombok.extern.slf4j.Slf4j;

import io.javalin.Javalin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.rendering.template.JavalinJte;

import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.io.InputStreamReader;

import java.util.stream.Collectors;

@Slf4j
public class App {
    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    public static Javalin getApp() throws IOException, SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        var sql = readResourceFile("schema.sql");

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        return app;
    }

//Configure DirectoryCodeResolver to load templates from a given route
    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
}
