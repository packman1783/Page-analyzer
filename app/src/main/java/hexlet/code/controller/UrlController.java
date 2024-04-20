package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.Date;
import java.util.Collections;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public final class UrlController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var checks = UrlCheckRepository.getAllLastChecks();
        var page = new UrlsPage(urls, checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("url/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        var check = UrlCheckRepository.getEntitiesById(id);
        var page = new UrlPage(url, check);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("url/show.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParamAsClass("url", String.class)
                .get()
                .toLowerCase()
                .trim();

        String normalUrl;

        try {
            URL parsedUrl = new URI(inputUrl).toURL();
            normalUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getAuthority();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            ctx.sessionAttribute("flash", "Incorrect URL");
            ctx.sessionAttribute("flashType", "warning");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        if (UrlRepository.isExist(normalUrl)) {
            ctx.sessionAttribute("flash", "This page is already exist");
            ctx.sessionAttribute("flashType", "info");
            ctx.redirect(NamedRoutes.urlsPath());
        } else {
            var url = new Url(normalUrl, new Timestamp(new Date().getTime()));
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Page added successfully");
            ctx.sessionAttribute("flashType", "success");
            ctx.redirect(NamedRoutes.urlsPath());
        }
    }
}
