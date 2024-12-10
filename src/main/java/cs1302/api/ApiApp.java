package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.net.URL;
import java.net.URI;
import java.io.IOException;
import javafx.scene.control.TextArea;
import java.util.List;
import java.util.ArrayList;


/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** The HTTP Client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2) // uses HTTP protocol v2
        .followRedirects(HttpClient.Redirect.NORMAL) // always redirects, except from HTTPS to HTTP
        .build(); // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting() // enables pretty printning
        .create(); // builds and returns Gson object

    /** API_KEYS, and URI's. */
    public static final String RECIPESEARCHAPI_KEY = "ba92181528c43589f71bc35246a436c8";
    public static final String NUTRITIONANALYSISAPI_KEY = "f1941193cc909c5970e94cedb87ab025";
    public static final String RECIPE_API = "https://api.edamam.com/api/recipes/v2";
    public static final String NUTRITION_API = "https://api.edamam.com/api/nutrition-data";


    /** Instance variables for stage, scene, and root. */
    Stage stage;
    Scene scene;
    VBox root;

    /** The first container which contains a search field and the search button. */
    HBox controlLayer;
    TextField searchTerm;
    Button getRecipes;

    /** The second container contains the scrollPane which displays content. */
    HBox contentLayer;
    ScrollPane content;
    TextFlow contentText;
    VBox photoContainer;


    /** The container which stores the different app navigation menus. */
    HBox navigationLayer;
    Button homeButton;
    Button recipeButton;
    Button exitButton;

    /** A default image that loads when application starts or when home button is selected. */
    private static final String CUISINE_IMG = "resources/ApiAppHomePhoto.jpeg";

    /**
     * Helper method which handles button event handling.
     */
    private void buttonHandlers() {
        getRecipes.setOnAction(e -> {
                String searchTermContent = searchTerm.getText();
                System.out.println(searchTermContent);
                try {
                    recipeData(searchTermContent);
                    //nutritionData(recipe.getIngredientLines()); DELETE IF CALL DOESNT WORK
                } catch (IOException | InterruptedException ex) {
                    System.out.println("There was an error calling the recipeData method.");
                } // try-catch to handle exceptions when calling the recipeData method.
            });
        homeButton.setOnAction(e -> System.out.println("Home button was clicked."));
        recipeButton.setOnAction(e -> {
                System.out.println("Recipes button was clicked.");
            });
        exitButton.setOnAction(e -> {
                System.out.println("Exit button was clicked.");
                System.exit(0);
            });
    } // buttonHandlers

    /**
     * Sends request to the Recipe Search API to search for food items.
     *
     * // find somewhere in the app to param in the textfield to call this method.
     * // deleted "static" in method declaration. Testing before changing call in button handle.
     *
     *
     */
    public RecipeResponse recipeData(String term) throws
        IOException, InterruptedException {

        /** Form URI. */
        String query = String.format(
            "?type=public&q=%s&app_id=fcfe9a37&app_key=",
            URLEncoder.encode(term, StandardCharsets.UTF_8)
        ); // query
        String uri = RECIPE_API + query + RECIPESEARCHAPI_KEY;

        /** Build the Http Request. */
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();

        /** Send Http Request + Recieve response in form of string. */
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());

        /** Test to ensure reques returned good status code. */
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        } // if

        /** Parse the JSON response to make it into storable data. */
        RecipeResponse recipeResponse = GSON.fromJson(response.body(), RecipeResponse.class);
        System.out.println("Parsed Recipes: " + recipeResponse.getHits().size());

        /** Test to visually isnpect response and ensure request is successful. */
        //System.out.println("Raw Response: ");
        //System.out.println(response.body()); // Prints raw response body
        //return response.body(); // returns raw JSON response
        for (Hit hit : recipeResponse.getHits()) {
            Recipe recipe = hit.getRecipe();
            System.out.println("Recipe Name: " + recipe.getLabel());
            System.out.println("Ingredient list: " + recipe.getIngredientLines());
            //System.out.println("Cuisine Type: " + recipe.getCuisineType());
            //System.out.println("Image URL: " + recipe.getImage());
            nutritionData(recipe.getIngredientLines());
            System.out.println("------------------------------------");
            ImageView imageView = new ImageView();
            try {
                Image image = new Image(recipe.getImage(), 200, 0, true, true);
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            } // try-catch for image loading onto VBox.
            Label nameLabel = new Label(recipe.getLabel());
            TextArea ingredientsTextArea = new TextArea(String.join("\n",
                                                                    recipe.getIngredientLines()));
            ingredientsTextArea.setWrapText(true);
            ingredientsTextArea.setEditable(false);
            ingredientsTextArea.setMaxWidth(200);

            NutritionResponse nutritionResponse = nutritionData(recipe.getIngredientLines());
            TextArea nutritionalTextArea;
            nutritionalTextArea = new TextArea(String.join("\n",
                                formattedNutritionResponse(nutritionResponse)));

            nutritionalTextArea.setWrapText(true);
            nutritionalTextArea.setEditable(false);
            nutritionalTextArea.setMaxWidth(200);

            VBox imageAndLabelBox = new VBox(imageView, nameLabel);
            imageAndLabelBox.setAlignment(Pos.CENTER);

            HBox recipeBox = new HBox(imageAndLabelBox, ingredientsTextArea, nutritionalTextArea);
            recipeBox.setAlignment(Pos.CENTER_LEFT);
            recipeBox.setSpacing(20);


            photoContainer.getChildren().add(recipeBox); // exchanged imageView to recipeBox
            //nutritionData(recipe.getIngredientLines()); PLACED HIGHER SO IN RIGHT PLACE
        } // for
        content.setContent(photoContainer);
        return recipeResponse;
    } // recipeData

    /**
     * Sends request to the Nutrition analsysis API.
     *
     *
     */
    public NutritionResponse nutritionData(List<String> ingredientLines) throws
        IOException, InterruptedException {

        /** Form URI. */
        StringBuilder query2 = new StringBuilder(
            String.format(
                "%s?app_id=f4bdac2c&app_key=%s&nutrition-type=cooking", NUTRITION_API,
                NUTRITIONANALYSISAPI_KEY)
        );


        for (String ingredient : ingredientLines) {
            String encodedIngredient = URLEncoder.encode(ingredient, StandardCharsets.UTF_8);
            query2.append("&ingr=").append(encodedIngredient);
        } // for
        System.out.println(query2); // for testing purposes

        /** Build the Http Request. */
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(query2.toString()))
            .build();

        /** Send Http Request + Recieve response in form of string. */
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());

        /** Test to ensure request returned good status code. */
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        } // if

        // used to test raw JSON response
        //System.out.println("Raw JSON nutrition response: ");
        //System.out.println(response.body());

        /** Parse the JSON response to make it into storable data. */
        NutritionResponse nutritionResponse = GSON.fromJson(response.body(),
                                                            NutritionResponse.class);
        System.out.println("Parsed Nutrition: \n Total Calories: "
                           + nutritionResponse.getCalories()
                           + "\n Total Fat: " + nutritionResponse.getTotalNutrients().get("FAT").
                           getQuantity()
                           + "g");

        /** Test to visually inspect response and ensure request is successful. */
        return nutritionResponse;
    } // nutritionData

    /**
     *
     *
     */
    private List<String> formattedNutritionResponse(NutritionResponse nutritionResponse) {
        List<String> nutritionInfo = new ArrayList<>();
        nutritionInfo.add("Calories: " + nutritionResponse.getCalories());
        nutritionInfo.add("Total Weight: " + nutritionResponse.getTotalWeight() + "g");
        return nutritionInfo;
    } // formattedNutritionResponse





    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        System.out.println("Creating an instance of the ApiApp Application. ");
        this.stage = null;
        this.scene = null;
        this.root = new VBox(4); // sets up the VBox spacing

        /** Constructs the components and objects. The HBox's set spacing = 4, prompt text is set
         * for the searchfield so users can input relevant search terms.*/
        controlLayer = new HBox(4);
        searchTerm = new TextField();
        searchTerm.setPromptText("What would you like to cook?");
        getRecipes = new Button("Search!");
        contentLayer = new HBox(4);
        content = new ScrollPane();
        contentText = new TextFlow();
        photoContainer = new VBox();
        photoContainer.setSpacing(10);
        navigationLayer = new HBox(4);
        homeButton = new Button("Home");
        recipeButton = new Button("Recipes");
        exitButton = new Button("Exit");
    } // ApiApp


    /** {@inheritDoc} */
    @Override
    public void init() {
        System.out.println("Executing the init method. ");

        /** Initialization of the expected scene structure. */
        root.setPadding(new Insets(3)); // sets padding around borders.
        root.getChildren().addAll(controlLayer, contentLayer, navigationLayer);

        /** Add controlLayer children to scene. */
        controlLayer.getChildren().addAll(searchTerm, getRecipes);

        /** Add contentLayer children to scene. */
        contentLayer.getChildren().addAll(content, contentText);
        contentText.getChildren().add(new Text("Thank you for viewing my app! :D"));
        content.setContent(contentText);

        /** Add navigationLayer children to scene. */
        navigationLayer.getChildren().addAll(homeButton, recipeButton, exitButton);

        /** Adjusting components and objects. */
        HBox.setHgrow(searchTerm, Priority.ALWAYS);
        HBox.setHgrow(content, Priority.ALWAYS);
        navigationLayer.setAlignment(Pos.CENTER);
        VBox.setVgrow(contentLayer, Priority.ALWAYS);
        buttonHandlers();
        //homePhoto(content); commented out since picture is not loading.


    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        System.out.println("Executing the start method. ");
        this.stage = stage;

        /** Excluded code was preset for display purposes. */
        // demonstrate how to load local asset using "file:resources/"
        //Image bannerImage = new Image("file:resources/ApiAppHomePhoto.jpeg");
        //ImageView banner = new ImageView(bannerImage);
        //banner.setPreserveRatio(true);
        //banner.setFitWidth(640);

        // some labels to display information
        //Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        scene = new Scene(root, 360, 640);


        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
