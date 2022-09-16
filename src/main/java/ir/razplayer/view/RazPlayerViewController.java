package ir.razplayer.view;

import ir.razplayer.Main;
import ir.razplayer.model.MediaItem;
import ir.razplayer.util.ConversionUtils;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Controller for the MediaPlayerView. Contains the UI functionality and
 * media playback logic.
 *
 * @author Alex Hage
 */
public class RazPlayerViewController {

    private static final int HIDE_UI_TIMEOUT = 2500;
    private static final boolean SHOW_UI = true;
    private static final boolean HIDE_UI = false;
    private static final String[] MUSIC = {".MP3", ".WAV"};

    @FXML
    private MediaView mediaView;

    @FXML
    private ProgressBar progBar;

    @FXML
    private Button addBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Button fScreenBtn;

    @FXML
    private Button volBtn;

    @FXML
    private Button settingBtn;

    @FXML
    private Button pListBtn;

    @FXML
    private Button speedBtn;

    @FXML
    private Label timeNowLabel;

    @FXML
    private Label pListLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Slider volSlider;

    @FXML
    private AnchorPane userControls;

    @FXML
    private VBox settingControls;

    @FXML
    private AnchorPane spectrumBox;

    /**
     * A single MediaItem object.
     */
    private MediaItem mediaItem;

    /**
     * The reusable MediaPlayer.
     */
    private MediaPlayer mediaPlayer;

    /**
     * The reusable Media.
     */
    private Media media;

    /**
     * The iterator for the playlist location. Set by Main.
     */
    private int current;

    /**
     * The playback started flag. Initialized to <i>false</i> locally.
     */
    private boolean playing;

    /**
     * The playback paused flag. Initialized to <i>false</i> locally.
     */
    private boolean paused;

    /**
     * The media mute flag. Initialized to <i>false</i> locally.
     */
    private boolean muted;

    /**
     * The UI visibility flag. Initialized to <i>true</i> locally.
     */
    private boolean showUI;

    /**
     * The Setting UI visibility flag. Initialized to <i>true</i> locally.
     */
    private boolean showSetting;

    /**
     * The music flag. Initialized to <i>true</i> locally.
     */
    private boolean music;

    /**
     * Reference to the main application.
     */
    private Main main;

    /**
     * The Timeline for use as a delay timer.
     */
    private Timeline timeLine;

    /**
     * The Address source of images.
     */
    private final String imageResources = "file:resources/images/";

    /**
     * The image address of playbtn.
     */
    private final String playBtnShape = imageResources + "playbtn.png";

    /**
     * The image address of pausebtn.
     */
    private final String pauseBtnShape = imageResources + "pausebtn.png";

    /**
     * The image address of settingbtn.
     */
    private final String settingBtnShape = imageResources + "settingbtn.png";

    /**
     * The image address of mutebtn.
     */
    private final String muteBtnShape = imageResources + "mutebtn.png";

    /**
     * The image address of volbtn.
     */
    private final String volBtnShape = imageResources + "volbtn.png";

    /**
     * The image address of halfVolbtn.
     */
    private final String halfVolBtnShape = imageResources + "halfVolbtn.png";

    /**
     * The default constructor.
     * Called before the <i>initialize()</i> method.
     */
    public RazPlayerViewController() {
//        totalDuration = Duration.ZERO;
//        timeDividerLabel.setText("/");

    } //end ctor

    /**
     * Initializes the controller class. Sets the controller-wise flags, UI
     * tooltips and default values. Is automatically called after the fxml file
     * has been loaded.
     */
    @FXML
    public void initialize() {
        this.playing = false;
        this.paused = false;
        this.muted = false;
        this.showUI = true;
        this.showSetting = false;
        this.music = true;

        //Adding tooltips
        addBtn.setTooltip(new Tooltip("Open..."));
        playBtn.setTooltip(new Tooltip("Play / Pause"));
        backBtn.setTooltip(new Tooltip("5 Sec Back"));
        nextBtn.setTooltip(new Tooltip("5 Sec Next"));
        volBtn.setTooltip(new Tooltip("Toggle Mute"));
        volSlider.setTooltip(new Tooltip("Volume"));
        settingBtn.setTooltip(new Tooltip("Setting"));
        pListBtn.setTooltip(new Tooltip());
        speedBtn.setTooltip(new Tooltip());
        fScreenBtn.setTooltip(new Tooltip("Toggle Fullscreen"));


        progBar.setOnMouseClicked(progBarMouseListener());
        progBar.setOnMouseDragged(progBarMouseListener());
        progBar.setOnMouseMoved(progBarMouseListener());

        userControls.setOnMouseEntered(uIMouseInOutListener());
        userControls.setOnMouseExited(uIMouseInOutListener());

        settingControls.setStyle("visibility: hidden");

        volSlider.setValue(0.5);
        volSlider.valueProperty().addListener(volumeSliderChangedListener());
    }

    /**
     * Handles the <i>Play/Pause</i> button click. Uses the flags <i>playing</i>
     * and <i>paused</i> to initiate, pause or resume media item playback. Sets
     * the <i>paused</i> flag by flipping it.
     */
    @FXML
    public void playRequestHandler() {
        //If not playing anything, play the list.
        if (!playing) {
            playAll();
        }
        //Otherwise, check the paused flag.
        else {
            //If not paused, pause.
            if (!paused) {
                mediaPlayer.pause();
                playBtn.setStyle("-fx-graphic: url(" + playBtnShape + "); -fx-padding: 2 4 2 4;");
            }
            //Otherwise, resume.
            else {
                mediaPlayer.play();
                playBtn.setStyle("-fx-graphic: url(" + pauseBtnShape + "); -fx-padding: 2 4 2 4;");
            }
            this.paused = !paused;
        }
    }

    /**
     * Handles the <i>Playlist</i> button click. Calls to Main to show playlist.
     */
    @FXML
    public void playListRequestHandler()
    {
        main.showPlayListView();
    }

    /**
     * Handles the <i>SpeedUp</i> button click increase 0.1 the speed
     * rate of media value.
     */
    @FXML
    public void speedUpRequestHandler() {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(mediaPlayer.getRate() + 0.1);
        }
    }

    /**
     * Handles the <i>Add</i> button click. Uses <i>FileChooser</i> to populate
     * the playlist. Starts playback if <i>playing</i> flag is set to
     * <i>false</i>.
     */
    @FXML
    public void openRequestHandler() {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(main.getPrimaryStage());
        if (files != null) {
            for (File f : files) {
                mediaItem = new MediaItem(f.toURI());
                mediaItem.setTitle(ConversionUtils.convertToFileName((f.toURI())));
                main.getPlayList().add(mediaItem);

                System.out.println("Added " + f.getName() + " to playlist");
            }
            if (!playing) {
                playAll();

                System.out.println("Playing all items in playlist starting with index #" + current);
            }
        }
    }

    /**
     * Handles the <i>Back</i> button click by setting the appropriate
     * <i>current</i> value.
     */
    @FXML
    public void backRequestHandler() {
        if (mediaPlayer != null /*&& */) {
            double currentDuration = mediaPlayer.getCurrentTime().toMillis() - 5000.0;
            mediaPlayer.seek(Duration.millis(currentDuration));
        }
    }

    /**
     * Handles the <i>Next</i> button click by setting the appropriate
     * <i>current</i> value.
     */
    @FXML
    public void nextRequestHandler() {
        if (mediaPlayer != null) {
            double currentDuration = mediaPlayer.getCurrentTime().toMillis() + 5000.0;
            mediaPlayer.seek(Duration.millis(currentDuration));
        }
    }

    /**
     * Handles the <i>Fullscreen</i> button click. Each click reverses the
     * current fullscreen status of the primary stage.
     */
    @FXML
    public void settingRequestHandler() {
        if (showSetting) {
            settingControls.setStyle("visibility: hidden");
        } else {
            settingControls.setStyle("visibility: visible");
        }
        showSetting = !showSetting;
    }

    /**
     * Handles the <i>Fullscreen</i> button click. Each click reverses the
     * current fullscreen status of the primary stage.
     */
    @FXML
    public void fullScreenRequestHandler() {
        main.getPrimaryStage().setFullScreen(!main.getPrimaryStage().isFullScreen());
    }

    /**
     * Handles the <i>Mute</i> button click. Each click reverses the current
     * status of MediaPlayer's muteProperty.
     */
    @FXML
    public void muteRequestHandler() {
        //If not initialized, nothing happens. Otherwise,
        if (mediaPlayer != null) {
            //If not currently muted, then mute.
            if (!muted) {
                mediaPlayer.muteProperty().set(!muted);
                muted = !muted;
                volBtn.setStyle("-fx-graphic: url(" + muteBtnShape + "); -fx-padding: 2 4 2 4;");
            }
            //Otherwise, demute.
            else {
                mediaPlayer.muteProperty().set(!muted);
                muted = !muted;
                //Set the appropriate volume button icon on return, based on current volume.
                if (volSlider.getValue() > 0.6) {
                    volBtn.setStyle("-fx-graphic: url(" + volBtnShape + "); -fx-padding: 2 4 2 4;");
                } else if (volSlider.getValue() > 0) {
                    volBtn.setStyle("-fx-graphic: url(" + halfVolBtnShape + "); -fx-padding: 2 4 2 4;");
                }
            }
        }
    }

    /**
     * Initializes the MediaPlayer and plays every track in the playlist one
     * after the other, starting from a preset current track. Sets the
     * <i>playing</i> flag to denote that the initial playback has commenced
     * <p>
     * The playing of the next item is achieved via a recursive call. .
     * </p>
     */
    private void playAll() {
        //Get the playlist from Main.
        List<MediaItem> playList = main.getPlayList();

        if (playList.size() != 0) {
            this.current = main.getCurrent().get();
            this.playing = true;
            playBtn.setStyle("-fx-graphic: url(" + pauseBtnShape + "); -fx-padding: 2 4 2 4;");
            media = new Media(playList.get(current).getURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volSlider.getValue());
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setFitWidth(main.getPrimaryStage().getScene().getWidth());

            for (String s : MUSIC) {
                if (s.equalsIgnoreCase(ConversionUtils.convertToFileExtension(playList
                        .get(current).getURI()))) {
                    this.music = true;
                    initSpectroscope();
                    break;
                } else {
                    this.music = false;
                }
            }
            if (!music) {
                toggleUI(HIDE_UI);
            }

            mediaPlayer.play();

            mediaPlayer.currentTimeProperty().addListener(progressChangedListener());

            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                    current++;
                    main.getCurrent().set(current);
                    if (current == playList.size()) {
                        current = 0;
                    }
                    playAll();
                }
            });
        }
    }

    /**
     * Creates a band-spectroscope from an anchor pane and an array of
     * rectangles. The number of rectangles corresponds to the number of audio
     * spectrum bands in media. The graph is inverted and its thickness
     * contingent on amplitude for better look and feel.
     */
    private void initSpectroscope() {
        spectrumBox.getChildren().clear();
        Rectangle[] bars = new Rectangle[mediaPlayer.getAudioSpectrumNumBands()];
        for (int i = 0; i < bars.length; i++) {
            bars[i] = new Rectangle();
            bars[i].setWidth(1);
            bars[i].setHeight(0);
            bars[i].setLayoutX(i + 3);
            spectrumBox.getChildren().add(bars[i]);
        }
        mediaPlayer.setAudioSpectrumListener(new AudioSpectrumListener() {

            @Override
            public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                for (int i = 0; i < bars.length; i++) {
                    bars[i].setLayoutY((spectrumBox.getHeight() / 2) - magnitudes[i]);
                    bars[i].setHeight((magnitudes[i] + 60) / 4);
                    bars[i].setFill(Color.GREEN);
                }
            }

        });
    }

    /**
     * Shows/hides the user interface based on a boolean value. Uses
     * FadeTransition to fade in/out and TimeLine to delay fade out.
     *
     * @param show the boolean. True fades in controls, false fades out.
     */
    private void toggleUI(boolean show) {
        if (!music) {
            if (show) {
                showUI = SHOW_UI;
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), userControls);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                fadeTransition.play();
            } else {
                timeLine = new Timeline(new KeyFrame(
                        Duration.millis(HIDE_UI_TIMEOUT), event ->
                {
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), userControls);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.play();
                    main.getPrimaryStage().getScene().setCursor(Cursor.NONE);
                    showUI = HIDE_UI;
                }));
                timeLine.play();
            }
        }
    }

    /**
     * Invoked by the main app to give it a reference back to itself. Adds
     * listeners contingent on main being set.
     *
     * @param main an instance of the main application.
     */
    public void setMain(Main main) {
        this.main = main;

        //Calling a listener for scene size change
        this.main.getPrimaryStage().getScene().widthProperty().addListener(sceneSizeChangedListener());

        //Listens for changes in current from playlist requests.
        this.main.getCurrent().addListener(currentChangedListener());

        //Listens for mouse movement
        this.main.getPrimaryStage().getScene().setOnMouseMoved(sceneMouseMovedListener());
    }

    /**
     * Listens to changes in Main's <i>current</i>. On change, stops playback of
     * the currently playing item and initiates playback starting with the new
     * index of <i>current</i>.
     *
     * @return {@code ChangeListener<Number>}
     */
    private ChangeListener<Number> currentChangedListener() {
        return new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldSceneWidth, Number newSceneWidth) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                current = main.getCurrent().get();
                playAll();
            }
        };
    }

    /**
     * Listens to changes in Scene size. On change, assigns new values to
     * MediaView's FitWidth property, thus resizing the viewport.
     *
     * @return {@code ChangeListener<Number>}
     */
    private ChangeListener<Number> sceneSizeChangedListener() {
        return new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldSceneWidth, Number newSceneWidth) {
                if (mediaView != null) {
                    mediaView.setFitWidth(newSceneWidth.doubleValue());
                }
            }
        };
    }

    /**
     * Listens to changes in volume Slider position. On change, assigns new
     * values to MediaPlayer's volume property. Cancels mute status and sets the
     * mute flag to <i>false</i>.
     *
     * @return {@code ChangeListener<Number>}
     */
    private ChangeListener<Number> volumeSliderChangedListener() {
        return new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> obervable,
                                Number oldValue, Number newValue) {
                if (mediaPlayer != null) {
                    mediaPlayer.setMute(false);
                    muted = false;
                    mediaPlayer.setVolume(newValue.doubleValue());
                }
                if (newValue.doubleValue() > 0.6) {
                    volBtn.setStyle("-fx-graphic: url(" + volBtnShape + "); -fx-padding: 2 4 2 4;");
                } else if (newValue.doubleValue() == 0) {
                    volBtn.setStyle("-fx-graphic: url(" + muteBtnShape + "); -fx-padding: 2 4 2 4;");
                } else {
                    volBtn.setStyle("-fx-graphic: url(" + halfVolBtnShape + "); -fx-padding: 2 4 2 4;");
                }
            }
        };
    }

    /**
     * Listens to changes in media playback progress. On change, sets the
     * progress bar and the progress clock accordingly.
     *
     * @return {@code ChangeListener<Duration>}
     */
    private ChangeListener<Duration> progressChangedListener() {
        ChangeListener<Duration> progressChangeListener = new ChangeListener<Duration>() {
            @Override
            public void changed(
                    ObservableValue<? extends Duration> observableValue,
                    Duration oldValue, Duration newValue) {
                        progBar.setProgress(1.0
                        * mediaPlayer.getCurrentTime().toMillis()
                        / mediaPlayer.getTotalDuration().toMillis());


                        timeNowLabel.setText(ConversionUtils.convertTimeInSeconds((int) newValue.toSeconds()) + " / " +
                                ConversionUtils.convertTimeInSeconds((int) mediaPlayer.getTotalDuration().toSeconds()));
//                        timeNowLabel.setText(ConversionUtils.convertTimeInSeconds((int) newValue.toSeconds()));
//                        timeExitLabel.setText(ConversionUtils.convertTimeInSeconds((int) mediaPlayer.getTotalDuration().toSeconds()));
            }
        };
        return progressChangeListener;
    }

    /**
     * Listens for left mouse button click or drag action on the progress bar. Reacts
     * by updating the media position index.
     *
     * @return {@code EventHandler<MouseEvent>}
     */
    private EventHandler<MouseEvent> progBarMouseListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mediaPlayer == null) {
                    event.consume();
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED
                        || event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(
                            event.getX() / progBar.getWidth()));

                    //Console printout for easier testing.
                    System.out.println("Setting media progress to "
                            + (int) ((event.getX()+5) / progBar.getWidth() * 100)
                            + " %");
                }
            }
        };
    }

    /**
     * Listens for mouse entering and exiting the UI container. Toggles UI
     * visibility accordingly using <i>toggleUI()</i>
     *
     * @return {@code EventHandler<MouseEvent>}
     */
    private EventHandler<MouseEvent> uIMouseInOutListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mediaPlayer == null) {
                    event.consume();
                } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
                    if (timeLine != null) {
                        timeLine.stop();
                    }
                    if (!showUI) {
                        toggleUI(SHOW_UI);
                    }
                } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
                    if (showUI) {
                        toggleUI(HIDE_UI);
                    }
                }
            }

        };
    }

    /**
     * Listens for mouse movement within the scene. Sets default Cursor on movement.
     *
     * @return {@code EventHandler<MouseEvent>}
     */
    private EventHandler<? super MouseEvent> sceneMouseMovedListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (main.getPrimaryStage().getScene().getCursor() != Cursor.DEFAULT) {
                    main.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
                }
            }
        };
    }
}