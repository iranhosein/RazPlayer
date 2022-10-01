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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
    private AnchorPane playerWindow;

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
    private HBox playListSection;

    @FXML
    private HBox speedSection;

    @FXML
    private HBox section0_25;

    @FXML
    private HBox section0_50;

    @FXML
    private HBox section0_75;

    @FXML
    private HBox section1_00;

    @FXML
    private HBox section1_25;

    @FXML
    private HBox section1_50;

    @FXML
    private HBox section1_75;

    @FXML
    private HBox section2_00;

    @FXML
    private Label timeNowLabel;

    @FXML
    private Slider volSlider;

    @FXML
    private AnchorPane userControls;

    @FXML
    private VBox settingControls;

    @FXML
    private VBox speedControls;

    @FXML
    private AnchorPane spectrumBox;

    @FXML
    private Label rateLabel;

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
     * The Setting UI visibility flag. Initialized to <i>false</i> locally.
     */
    private boolean showSetting;

    /**
     * The Speed's panel UI visibility flag. Initialized to <i>false</i> locally.
     */
    private boolean showSpeed;

    /**
     * The music flag. Initialized to <i>true</i> locally.
     */
    private boolean music;

    /**
     * Reference to the main application.
     */
    private Main main;

    /**
     * The speed of media.
     */
    private double rate;

    /**
     * The Timeline for use as a delay timer.
     */
    private Timeline timeLine;

    /**
     * The Address source of images.
     */
    private final String imageResources = String.valueOf(Main.class.getResource("images"));

    /**
     * The image address of playbtn.
     */
    private final String playBtnShape = String.valueOf(Main.class.getResource("images/playbtn.png"));

    /**
     * The image address of pausebtn.
     */
    private final String pauseBtnShape = String.valueOf(Main.class.getResource("images/pausebtn.png"));

    /**
     * The image address of mutebtn.
     */
    private final String muteBtnShape = String.valueOf(Main.class.getResource("images/mutebtn.png"));

    /**
     * The image address of volBtn.
     */
    private final String volBtnShape = String.valueOf(Main.class.getResource("images/volbtn.png"));

    /**
     * The image address of halfVolBtn.
     */
    private final String halfVolBtnShape = String.valueOf(Main.class.getResource("images/halfvolbtn.png"));

    /**
     * The image address of speedBtn.
     */
    private final String speedBtnShape = String.valueOf(Main.class.getResource("images/speedbtn.png"));

    /**
     * The image address of arrowBtn.
     */
    private final String arrowBtnShape = String.valueOf(Main.class.getResource("images/arrowbtn.png"));

    /**
     * The image address of pListBtn.
     */
    private final String pListBtnShape = String.valueOf(Main.class.getResource("images/plistbtn.png"));

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
        if (rate == 0.0)
            rate = 1.0;


        //Adding tooltips
        addBtn.setTooltip(new Tooltip("Open...(O)"));
        playBtn.setTooltip(new Tooltip("Play/Pause(K)"));
        backBtn.setTooltip(new Tooltip("5 Sec Back(J)"));
        nextBtn.setTooltip(new Tooltip("5 Sec Next(L)"));
        volBtn.setTooltip(new Tooltip("Toggle Mute(M)"));
        volSlider.setTooltip(new Tooltip("Volume"));
        settingBtn.setTooltip(new Tooltip("Setting(S)"));
        fScreenBtn.setTooltip(new Tooltip("Toggle Fullscreen(F)"));

        // set keyboard's event for player window
        playerWindow.addEventFilter(KeyEvent.KEY_PRESSED, keyRequestHandler());

        // set mouse's event for progress bar
        progBar.setOnMouseClicked(progBarMouseListener());
        progBar.setOnTouchPressed(progBarTouchListener());
        progBar.setOnMouseDragged(progBarMouseListener());
        progBar.setOnMouseMoved(progBarMouseListener());
        progBar.setOnTouchMoved(progBarTouchListener());

        // set mouse's event for user controls
        userControls.setOnMouseEntered(uIMouseInOutListener());
        userControls.setOnTouchPressed(uITouchInOutListener());
        userControls.setOnMouseExited(uIMouseInOutListener());
        userControls.setOnTouchReleased(uITouchInOutListener());

        // set visibility of setting controls
        settingControls.setVisible(HIDE_UI);

        // set visibility of speed controls
        speedControls.setVisible(HIDE_UI);

        Image arrowImage = new Image(arrowBtnShape);
        ImageView arrowImg = new ImageView(arrowImage);
        arrowImg.setFitWidth(9);
        arrowImg.setFitHeight(9);

        ImageView arrowImg2 = new ImageView(arrowImage);
        arrowImg2.setFitHeight(9);
        arrowImg2.setFitWidth(9);

        Image image = new Image(speedBtnShape);
        ImageView img = new ImageView(image);
        img.setFitWidth(13);
        img.setFitHeight(13);
        speedSection.getChildren().add(img);

        Label speedLabel = new Label("Playback Speed");
        speedLabel.setStyle("-fx-font-size: 9; -fx-padding: 0 10 0 0");
        speedSection.getChildren().add(speedLabel);

        this.rateLabel = new Label(Double.toString(this.rate));
        rateLabel.setStyle("-fx-font-size: 10;");
        speedSection.getChildren().add(rateLabel);

        speedSection.getChildren().add(arrowImg);

        speedSection.setOnMouseClicked(speedSectionMouseListener());
        speedSection.setOnTouchPressed(speedSectionTouchListener());

        image = new Image(pListBtnShape);
        img = new ImageView(image);
        img.setFitHeight(13);
        img.setFitWidth(13);
        playListSection.getChildren().add(img);

        Label pListLabel = new Label("playList");
        pListLabel.setStyle("-fx-font-size: 9; -fx-padding: 0 61 0 0");
        playListSection.getChildren().add(pListLabel);

        playListSection.getChildren().add(arrowImg2);

        playListSection.setOnMouseClicked(playListSectionMouseListener());
        playListSection.setOnTouchPressed(playListSectionTouchListener());

        Label label0_25 = new Label("0.25");
        label0_25.setStyle("-fx-font-size: 9");
        section0_25.getChildren().add(label0_25);
        section0_25.setOnMouseClicked(rateMediaMouseListener(0.25));
        section0_25.setOnTouchPressed(rateMediaTouchListener(0.25));

        Label label0_50 = new Label("0.50");
        label0_50.setStyle("-fx-font-size: 9");
        section0_50.getChildren().add(label0_50);
        section0_50.setOnMouseClicked(rateMediaMouseListener(0.50));
        section0_50.setOnTouchPressed(rateMediaTouchListener(0.50));

        Label label0_75 = new Label("0.75");
        label0_75.setStyle("-fx-font-size: 9");
        section0_75.getChildren().add(label0_75);
        section0_75.setOnMouseClicked(rateMediaMouseListener(0.75));
        section0_75.setOnTouchPressed(rateMediaTouchListener(0.75));

        Label label1_00 = new Label("1.00");
        label1_00.setStyle("-fx-font-size: 9");
        section1_00.getChildren().add(label1_00);
        section1_00.setOnMouseClicked(rateMediaMouseListener(1.00));
        section1_00.setOnTouchPressed(rateMediaTouchListener(1.00));

        Label label1_25 = new Label("1.25");
        label1_25.setStyle("-fx-font-size: 9");
        section1_25.getChildren().add(label1_25);
        section1_25.setOnMouseClicked(rateMediaMouseListener(1.25));
        section1_25.setOnTouchPressed(rateMediaTouchListener(1.25));

        Label label1_50 = new Label("1.50");
        label1_50.setStyle("-fx-font-size: 9");
        section1_50.getChildren().add(label1_50);
        section1_50.setOnMouseClicked(rateMediaMouseListener(1.50));
        section1_50.setOnTouchPressed(rateMediaTouchListener(1.50));

        Label label1_75 = new Label("1.75");
        label1_75.setStyle("-fx-font-size: 9");
        section1_75.getChildren().add(label1_75);
        section1_75.setOnMouseClicked(rateMediaMouseListener(1.75));
        section1_75.setOnTouchPressed(rateMediaTouchListener(1.75));

        Label label2_00 = new Label("2.00");
        label2_00.setStyle("-fx-font-size: 9");
        section2_00.getChildren().add(label2_00);
        section2_00.setOnMouseClicked(rateMediaMouseListener(2.00));
        section2_00.setOnTouchPressed(rateMediaTouchListener(2.00));

        // set volume slider
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
     * Handles the speed of media with give number of speed as double
     * rate of media value.
     */
    private void setRateMedia(double rate) {
        if (mediaPlayer != null) {
            mediaPlayer.setRate(rate);
            this.rate = rate;
            rateLabel.setText(Double.toString(rate));
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
     * Handles the <i>SettingBtn</i> button click. Each click reverses the
     * current show setting menu status.
     */
    @FXML
    public void settingRequestHandler() {
        if (showSetting) {
            settingControls.setVisible(HIDE_UI);
            showSetting = HIDE_UI;
        } else if (showSpeed) {
            speedControls.setVisible(HIDE_UI);
            showSpeed = HIDE_UI;
        } else {
            settingControls.setVisible(SHOW_UI);
            showSetting = SHOW_UI;
        }
    }

    /**
     * Handles the speedSection click. When click, setting menu will close
     * and speed menu will open.
     */
    public void speedHandler() {
        settingControls.setVisible(HIDE_UI);
        showSetting = HIDE_UI;
        speedControls.setVisible(SHOW_UI);
        showSpeed = SHOW_UI;
    }

    /**
     * Handles the playListSection click. When click, setting menu will close
     * and playList window will open.
     */
    public void playListHandler() {
        settingControls.setVisible(HIDE_UI);
        showSetting = HIDE_UI;
        main.showPlayListView();
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
                volBtn.setStyle("-fx-graphic: url(" + muteBtnShape + "); -fx-padding: 2 4 2 4;");
            }
            //Otherwise, demute.
            else {
                //Set the appropriate volume button icon on return, based on current volume.
                if (volSlider.getValue() > 0.6) {
                    volBtn.setStyle("-fx-graphic: url(" + volBtnShape + "); -fx-padding: 2 4 2 4;");
                } else if (volSlider.getValue() > 0) {
                    volBtn.setStyle("-fx-graphic: url(" + halfVolBtnShape + "); -fx-padding: 2 4 2 4;");
                }
            }
            mediaPlayer.muteProperty().set(!muted);
            muted = !muted;
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
            mediaPlayer.seek(Duration.ZERO); //////////////////////////////////////////////////////
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.setFitWidth(main.getPrimaryStage().getScene().getWidth());
            setRateMedia(rate);           ////////////////////////////////////////////////////////

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
                    if (current == playList.size()) {
                        current = 0;
                        playRequestHandler();
                    }
                    main.getCurrent().set(current);
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
        if (!music && !showSetting && !showSpeed) {
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
                        progBar.setProgress(/*1.0**/ mediaPlayer.getCurrentTime().toMillis()
                        / mediaPlayer.getTotalDuration().toMillis());


                        timeNowLabel.setText(ConversionUtils.convertTimeInSeconds((int) newValue.toSeconds()) + " / " +
                                ConversionUtils.convertTimeInSeconds((int) mediaPlayer.getTotalDuration().toSeconds()));
            }
        };
        return progressChangeListener;
    }

    /**
     * Listens for left mouse button click action on the speed section. Reacts
     * by calling settingRequestHandler.
     *
     * @return {@code EventHandler<MouseEvent>}
     */
    private EventHandler<MouseEvent> speedSectionMouseListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mediaPlayer == null) {
                    event.consume();
                } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    speedHandler();
                }
            }
        };
    }

    /**
     * Listens for touch action on the speed section. Reacts
     * by calling settingRequestHandler.
     *
     * @return {@code EventHandler<TouchEvent>}
     */
    private EventHandler<TouchEvent> speedSectionTouchListener() {
        return new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                if (mediaPlayer == null) {
                    event.consume();
                } else if (event.getEventType() == TouchEvent.TOUCH_PRESSED) {
                    speedHandler();
                }
            }
        };
    }

    /**
     * Listens for left mouse button click action on the playList section. Reacts
     * by calling settingRequestHandler.
     *
     * @return {@code EventHandler<MouseEvent>}
     */
    private EventHandler<MouseEvent> playListSectionMouseListener() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    playListHandler();
                }
            }
        };
    }

    /**
     * Listens for touch action on the playList section. Reacts
     * by calling settingRequestHandler.
     *
     * @return {@code EventHandler<TouchEvent>}
     */
    private EventHandler<TouchEvent> playListSectionTouchListener() {
        return new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                if (event.getEventType() == TouchEvent.TOUCH_MOVED) {
                    playListHandler();
                }
            }
        };
    }

    /**
     * Listens for left mouse button click action on the number of speed section. Reacts
     * by calling settingRequestHandler.
     *
     * @return {@code EventHandler<MouseEvent>}
     */
    private EventHandler<MouseEvent> rateMediaMouseListener(double rate) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType() == MouseEvent.MOUSE_DRAGGED
                        || event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    setRateMedia(rate);
                    speedControls.setVisible(HIDE_UI);
                    showSpeed = HIDE_UI;
                    settingControls.setVisible(SHOW_UI);
                    showSetting = SHOW_UI;
                }
            }
        };
    }

    /**
     * Listens for touch action on the number of speed section. Reacts
     * by calling settingRequestHandler.
     *
     * @return {@code EventHandler<TouchEvent>}
     */
    private EventHandler<TouchEvent> rateMediaTouchListener(double rate) {
        return new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                if (event.getEventType() == TouchEvent.TOUCH_PRESSED) {
                    setRateMedia(rate);
                    speedControls.setVisible(HIDE_UI);
                    showSpeed = HIDE_UI;
                    settingControls.setVisible(SHOW_UI);
                    showSetting = SHOW_UI;
                }
            }
        };
    }

    /**
     * Listens for keyboard press action. Reacts by calling RequestHandler button.
     * @return {@code EventHandler<KeyEvent>}
     */
    private EventHandler<KeyEvent> keyRequestHandler() {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.O){
                    openRequestHandler();
                } else if (keyCode == KeyCode.K || keyCode == KeyCode.SPACE) {
                    playRequestHandler();
                } else if (keyCode == KeyCode.J || keyCode == KeyCode.LEFT || keyCode == KeyCode.KP_LEFT){
                    backRequestHandler();
                } else if (keyCode == KeyCode.L || keyCode == KeyCode.RIGHT || keyCode == KeyCode.KP_RIGHT){
                    nextRequestHandler();
                } else if (keyCode == KeyCode.M){
                    muteRequestHandler();
                } else if (keyCode == KeyCode.S){
                    settingRequestHandler();
                } else if (keyCode == KeyCode.F){
                    fullScreenRequestHandler();
                }
            }
        };
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
     * Listens for thouch or move action on the progress bar. Reacts
     * by updating the media position index.
     *
     * @return {@code EventHandler<TouchEvent>}
     */
    private EventHandler<TouchEvent> progBarTouchListener() {
        return new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                if (mediaPlayer == null) {
                    event.consume();
                } else if (event.getEventType() == TouchEvent.TOUCH_MOVED
                        || event.getEventType() == TouchEvent.TOUCH_PRESSED) {
                    mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(
                            event.getTouchPoint().getX() / progBar.getWidth()));

                    //Console printout for easier testing.
                    System.out.println("Setting media progress to "
                            + (int) ((event.getTouchPoint().getX()+5) / progBar.getWidth() * 100)
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
     * Listens for mouse entering and exiting the UI container. Toggles UI
     * visibility accordingly using <i>toggleUI()</i>
     *
     * @return {@code EventHandler<TouchEvent>}
     */
    private EventHandler<TouchEvent> uITouchInOutListener() {
        return new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                if (mediaPlayer == null) {
                    event.consume();
                } else if (event.getEventType() == TouchEvent.TOUCH_PRESSED) {
                    if (timeLine != null) {
                        timeLine.stop();
                    }
                    if (!showUI) {
                        toggleUI(SHOW_UI);
                    }
                } else if (event.getEventType() == TouchEvent.TOUCH_RELEASED) {
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