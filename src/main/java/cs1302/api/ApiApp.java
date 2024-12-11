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
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;


/**
 * This class handles the main application logic for the ApiApp.
 * It communicates with two external API's to fetch recipe and nutritional data.
 * This application provides a UI where users can search for recipes and view
 * relevant information such as ingredients, nutritional details, and images.
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

    /** Private boolean which keeps track whether user can make API calls. */
    private boolean rateLimitTracker = true;

    /**
     * Helper method that builds and displays any relevant alerts to the user.
     *
     * @param message The string which is taken as a param when method is called and displays on UI.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rate Limit Reached");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    } // showAlert

    /**
     * Helper method which handles button event handling.
     */
    private void buttonHandlers() {
        getRecipes.setOnAction(e -> {
            photoContainer.getChildren().clear(); // clears all results from previous search
            if (rateLimitTracker) {
                String searchTermContent = searchTerm.getText();
                System.out.println(searchTermContent);
                rateLimitTracker = false;
                getRecipes.setDisable(true); // disables button
                try {
                    recipeData(searchTermContent);
                    showAlert("You have reached the API rate limit. Please wait atleast 60 "
                              + "seconds before searching again.");
                    new Timeline(new KeyFrame(Duration.seconds(60), f -> {
                        getRecipes.setDisable(false);
                        rateLimitTracker = true;
                    })).play();
                } catch (IOException | InterruptedException ex) {
                    System.out.println("There was an error calling the recipeData method.");
                } // try-catch to handle exceptions when calling the recipeData method.
            } else {
                System.out.println("Somethings wrong with the button. ");
            } // if-else
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
     * Fetches the recipe data for a search term provided by user using the Recipe Search API.
     * Constructs query with provided search term and sends an HTTP request to retrieve
     * images, name of cuisine, and an ingredient list. Response is then parsed and returned
     * as a RecipeResponse object.
     *
     * @param term The search term used to request recipes containing the term.
     * @return RecipeResponse object containing image, recipe name, and ingredient list data.
     * @throws IOException if an I/O error occurs during request.
     * @throws InterruptedException if thread is interrupted while awaiting response.
     */
    public RecipeResponse recipeData(String term) throws IOException, InterruptedException {
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

        /** Test to ensure request returned good status code. */
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        } // if

        /** Parse the JSON response and handles objects + formats objects for scene. */
        RecipeResponse recipeResponse = GSON.fromJson(response.body(), RecipeResponse.class);
        System.out.println("Parsed Recipes: " + recipeResponse.getHits().size());
        for (Hit hit : recipeResponse.getHits()) {
            Recipe recipe = hit.getRecipe();
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
        } // for
        content.setContent(photoContainer);
        return recipeResponse;
    } // recipeData

    /**
     * Fetches the nutritional data for a list of ingredients using the Nutritional Analysis API.
     * Constructs query with provided ingredient list and sends an HTTP request to retrieve
     * the nutritional information. Response is then parsed and returned as a NutritionResponse
     * object.
     *
     * @param ingredientLines The list of ingredients in the recipe.
     * @return NutritionResponse object containing nutritional data for ingredients.
     * @throws IOException if an I/O error occurs during request.
     * @throws InterruptedException if thread is interrupted while awaiting response.
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

        /** Parse the JSON response to make it into storable data. */
        NutritionResponse nutritionResponse = GSON.fromJson(response.body(),
                                                            NutritionResponse.class);
        return nutritionResponse;
    } // nutritionData

    /**
     * Formats the nutritional response data into a list of strings suitable for displaying
     * in the UI. This method extracts key nutritional information such as calories, total
     * nutrients, daily values. It formats them into a list of strings for display.
     *
     * @param nutritionResponse The NutritionResponse object containing the nutritional data.
     * @return A list of strings representing the formatted nutritional data.
     */
    private List<String> formattedNutritionResponse(NutritionResponse nutritionResponse) {
        List<String> nutritionInfo = new ArrayList<>();
        nutritionInfo.add("Calories: " + nutritionResponse.getCalories());
        nutritionInfo.add("Total Weight: " + nutritionResponse.getTotalWeight() + "g");
        nutritionInfo.add("CO2 Emissions: " + nutritionResponse.getTotalCO2Emissions());
        nutritionInfo.add("\nTotal Nutrients: ");
        if (nutritionResponse.getTotalNutrients() != null) {
            for (Map.Entry<String, NutritionResponse.Nutrient> entry : nutritionResponse
                     .getTotalNutrients().entrySet()) {
                NutritionResponse.Nutrient nutrient = entry.getValue();
                nutritionInfo.add(String.format("%s: %.2f %s", nutrient.getLabel(),
                                                nutrient.getQuantity(),
                                                nutrient.getUnit()));
            }
        } else {
            nutritionInfo.add("No nutrient data available.");
        } // if-else

        nutritionInfo.add("\nTotal DailyValues: ");
        if (nutritionResponse.getTotalDaily() != null) {
            for (Map.Entry<String, NutritionResponse.Nutrient> entry : nutritionResponse
                     .getTotalDaily().entrySet()) {
                NutritionResponse.Nutrient nutrient = entry.getValue();
                nutritionInfo.add(String.format("%s: %.2f ", nutrient.getLabel(),
                                                nutrient.getQuantity()));
            }
        } else {
            nutritionInfo.add("No daily value data available.");
        } // if-else

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
        contentText.getChildren().add(new Text("Thank you for viewing my app! :D"
                                               + "\n \nThe following is an Application that uses"
                                      + "\ntwo Edamam API's. The first, Recipe Search API returns"
                                      + "\nrecipes based on the search term you provide. "
                                      + "\nIt returns the image of the dish, in addition it"
                                      + "\nprovides the list of ingredients for each recipe."
                                      + "\n \nThe second, Nutritional Analysis API returns "
                                      + "\nmore detailed nutritional facts not provided by"
                                      + "\nthe Recipe Search API. It provides Calories,"
                                      + "\nTotal Weight, CO2 Emissions, Total Nutrients,"
                                      + "\nTotal Daily Values, and much more!"));
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
        scene = new Scene(root, 1000, 710);


        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
