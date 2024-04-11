### Hexlet tests and linter status:
[![Actions Status](https://github.com/packman1783/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/packman1783/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/a3f323ecd3b6c41d92c0/maintainability)](https://codeclimate.com/github/packman1783/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/a3f323ecd3b6c41d92c0/test_coverage)](https://codeclimate.com/github/packman1783/java-project-72/test_coverage)

## Description:
Page Analyzer is a site based on the Javalin framework that analyses specified pages for SEO suitability. The request returns details:
 - response status code
 - page title
 - HTML H1 block (if present)
 - HTML meta description block (if present)

Here we practice the basic principles of building modern sites on MVC-architecture: 
 - working with routing 
 - request handlers and templating 
 - interaction with the database via ORM

You can familiarise yourself with the possibilities of the site here on Render.com [page-analyzer](https://java-project-72-jf6n.onrender.com)

## Use:
for example: 
```
gradle run
```

By default, the server starts at http://localhost:7070.

 - On the empty field of the home page print name of the website you want to check and click "Check".

![img_5.png](img_5.png)

 - The website will be added to the list of other pages. Click on name of your website.

![img_1.png](img_1.png)

 - On website's page click on the button "Run check" to start the first check of your site. And get results of the checking in a table under the button.

![img_12.png](img_12.png)