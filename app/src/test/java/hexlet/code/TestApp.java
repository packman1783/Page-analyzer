package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestApp {

    Javalin app;

    @BeforeEach
    public void setUp() throws Exception {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Page Analyzer");
        });
    }


}
