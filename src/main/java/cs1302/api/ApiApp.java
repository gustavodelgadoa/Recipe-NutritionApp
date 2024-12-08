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

    /** API_KEYS.  */
    String recipeSearchAPI_KEY = "ba92181528c43589f71bc35246a436c8";
    String nutritionalAnalysisAPI_KEY = "f1941193cc909c5970e94cedb87ab025";


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

    /** The container which stores the different app navigation menus. */
    HBox navigationLayer;
    Button homeButton;
    Button recipeButton;
    Button exitButton;



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
