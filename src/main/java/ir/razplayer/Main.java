package ir.razplayer;

import ir.razplayer.view.RazPlayerViewController;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ir.razplayer.model.MediaItem;
import ir.razplayer.view.MediaPlayerViewController;
import ir.razplayer.view.PlayListViewController;

import java.io.IOException;


/**
 * Loads, initializes and displays the application and its corresponding views.
 * Assigns controller access. Stores and returns the
 * {@code ObservableList<MediaItem>} playList and the observable IntegerProperty
 * current.
 *
 * @authors Alex Hage and Hossein Abdollahipour
 *
 */
public class Main extends Application {

    /**
     * The main stage of the application.
     */
    private Stage primaryStage;

    /**
     * The main layout as BorderPane.
     */
    private BorderPane rootLayout;

    /**
     * The observable list of MediaItem objects. Effectively, this is the media playlist.
     */
    private ObservableList<MediaItem> playList = FXCollections.observableArrayList();

    /**
     * The currently playing media Integer-flag.
     */
    private IntegerProperty current;

    /**
     * The Media Player Name.
     */
    private final String mediaPlayerName = "Raz Media Player";

    /**
     * The Version of Media Player.
     */
    private final String versionNumber = "v1.0.4";

    @Override
    public void start(Stage primaryStage) {

        this.current =new SimpleIntegerProperty(0);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(mediaPlayerName + " " + versionNumber);

        //Attach the icon to the stage/window
        Image icon = new Image("file:resources/images/logo.png");
        this.primaryStage.getIcons().add(icon);

        initRootLayout();
        showRazPlayerView();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout()
    {
        try
        {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Shows the media player inside the root layout.
     */
    public void showRazPlayerView()
    {
        try{
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RazPlayerView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            RazPlayerViewController controller = loader.getController();
            controller.setMain(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Shows the media player inside the root layout.
     */
    public void showMediaPlayerView()
    {
        try{
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("mediaPlayerView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            MediaPlayerViewController controller = loader.getController();
            controller.setMain(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Opens the playlist view in a new modal popup window.
     */
    public void showPlayListView()
    {
        try
        {
            // Load the fxml file and create a new stage for the playlist popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("PlayListView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage playListStage = new Stage();

            playListStage.setTitle(mediaPlayerName + " ::: Playlist");
            playListStage.initModality(Modality.WINDOW_MODAL);
            playListStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            playListStage.setScene(scene);

            // Set the person into the controller.
            PlayListViewController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(playListStage);

            // Show the dialog and wait until the user closes it
            playListStage.showAndWait();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns the primary stage.
     *
     * @return primaryStage the Stage to return.
     */
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    /**
     * Retrns the currently playing item's playlist index as IntegerProperty.
     *
     * @return current the IntegerProperty to return.
     */
    public IntegerProperty getCurrent()
    {
        return current;
    }

    /**
     * Sets the index value of the currently playing item.
     *
     * @param current
     *            the int value to set.
     */
    public void setCurrent(int current)
    {
        this.current.set(current);
    }

    /**
     * Returns the playlist as an observable list of MediaItems.
     *
     * @return playList the {@code ObservableList<MediaItem>} to return.
     */
    public ObservableList<MediaItem> getPlayList()
    {
        return playList;
    }

    /**
     * The main method. Ignored on proper deployment.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}