package hexlet.code.controller;

import hexlet.code.dto.BasePage;

import java.util.Collections;
import io.javalin.http.Context;

public final class RootController {
    public static void index(Context ctx) {
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
