# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> This app allows users to search for recipes by querying the Edamam Recipe Search API.
> Users can view recipe information including images, recipe labels,  and ingredient lists.
> The app integrates with the Edamam Nutrition Analysis API to provide a comprehensive breakdown
> of the nutritional content for the ingredients in each recipe. The two API's are connected
> to enrich the recipe details with dynamic nutritional analysis.
> https://github.com/gustavodelgadoa/cs1302-api-app

TODO WRITE / REPLACE

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://api.edamam.com/search?q=chicken&app_id=fcfe9a37&app_key=ba92181528c43589f71bc35246a436c8
```

> Rate Limit: 10 calls per minute

### API 2

```
https://api.edamam.com/api/nutrition-data?app_id=f4bdac2c&app_key=f1941193cc909c5970e94cedb87ab025&nutrition-type=cooking&ingr=1%20chicken%20breast
```

> Rate Limit: 20 calls per minute

## Part 2: New

> One exciting aspect of building this application was learning how to combine data from miltiple
> API's to create a seamless User Experience. Understanding how to handle API rate limits was very
> exciting as well.

## Part 3: Retrospect

> I would first design the App's architecture to better account for API limitations.
> Planning these constraints I could have streamlined the logic in the application for handling
> errors and delays.
