### Hexlet tests:
[![Actions Status](https://github.com/packman1783/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/packman1783/java-project-72/actions)

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
   
   ![first image](https://i.ibb.co/gjvzmPK/image-1.png)

 - The website will be added to the list of other pages. Click on name of your website.

   ![second image](https://i.ibb.co/ZTPdDDZ/image-2.png)

 - On website's page click on the button "Run check" to start the first check of your site.

   ![third image](https://i.ibb.co/rstPx49/image-3.png)

 - Get results in a table under the button.

   ![fourth image](https://i.ibb.co/PDTw3Bx/image-4.png)
