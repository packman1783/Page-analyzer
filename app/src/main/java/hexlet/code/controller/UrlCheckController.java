package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Date;

public class UrlCheckController {
    public static void create(Context ctx) throws SQLException {
        var inputId = ctx.pathParamAsClass("id", Long.class).get();
        var inputUrl = UrlRepository.find(inputId)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + inputId + " not found"));

        try {
            HttpResponse<String> response = Unirest.get(inputUrl.getName()).asString();

            var statusCode = response.getStatus();

            var doc = Jsoup.parse(response.getBody());
            var title = doc.title();

            var uncheckedH1 = doc.selectFirst("h1");
            var h1 = uncheckedH1 == null ? "" : uncheckedH1.text();

            var uncheckedDescription = doc.selectFirst("meta[name=description]");
            var description = uncheckedDescription == null ? "" : uncheckedDescription.attr("content");

            var createdAt = new Timestamp(new Date().getTime());

            var urlCheck = new UrlCheck(statusCode, title, h1, description, inputId, createdAt);
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute("flash", "Page added successfully");
            ctx.sessionAttribute("flashType", "success");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Incorrect URL");
            ctx.sessionAttribute("flashType", "warning");
        }

        ctx.redirect(NamedRoutes.urlPath(inputId));
    }
}
