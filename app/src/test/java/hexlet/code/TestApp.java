package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Date;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public final class TestApp {
    private static Javalin app;
    private static MockWebServer mockWebServer;
    private static String testUrl;

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        testUrl = mockWebServer.url("/").toString();
        var mockResponse = new MockResponse().setBody(readResourceFile("test.html"));
        mockWebServer.enqueue(mockResponse);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public void beforeEach() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testRootPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Page Analyzer");
        });
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testShowUrl() {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url("http://example.com", new Timestamp(new Date().getTime()));
            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testTheSameUrl() throws SQLException {
        var url = new Url("https://www.example.com", new Timestamp(new Date().getTime()));
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testIncorrectUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=IncorrectUrl";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.getEntities()).hasSize(0);
        });
    }

    @Test
    public void testNotFoundUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("9999999"));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCheckUrl() throws SQLException {
        var url = new Url(testUrl, new Timestamp(new Date().getTime()));
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);

            var urlCheck = UrlCheckRepository.getLastChecks(url.getId()).get();
            var title = urlCheck.getTitle();
            var h1 = urlCheck.getH1();
            var description = urlCheck.getDescription();

            assertThat(title).isEqualTo("title");
            assertThat(h1).isEqualTo("header");
            assertThat(description).isEqualTo("description");
        });
    }
}
