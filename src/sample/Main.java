package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class Main extends Application {

    private BorderPane border;

    private Stage mainStage;

    private Scene scene;

    private TilePane topButtonsPane;

    private HBox exampleBox;

    private VBox musicBox;

    private Label score;
    private Label updateScore;
    private int runScore = 0;

    private GridPane grid;

    private int rows = 5;
    private int cols = 3;

    private String imgPath = "Images/";
    private String animPath = "Animation/";

    private Image image, animation;

    private final URL song1Resource = getClass().getResource("Sound/04 Give It Away.wav");
    private final URL song2Resource = getClass().getResource("Sound/06 When It Falls.wav");
    private Media media1 = new Media(song1Resource.toString());
    private Media media2 = new Media(song2Resource.toString());
    private MediaPlayer mediaPlayer1 = new MediaPlayer(media1);
    private MediaPlayer mediaPlayer2 = new MediaPlayer(media2);

    private boolean mPlayer1 = true;

    private boolean seeAnimation = false;

    private ArrayList<Card> cards = new ArrayList<Card>();

    private Map<Integer, Card> dealHashMap = new HashMap<Integer, Card>();

    private Map<Integer, Card> sCardHashMap = new HashMap<Integer, Card>();

    private String[] numbers = new String[] { "one", "two", "three" };

    private String[] colors = new String[] { "Red", "Green", "Purple" };

    private String[] shapes = new String[] { "Squiggles", "Ovals", "Triangles" };

    private String[] fills = new String[] { "Fill", "Striped", "Clear" };

    private Button cardsButton, resetButton, examplesButton, pngButton, playButton, stopButton, loopButton, changeButton, playAgain, stopGame;

    private String btnID = null;

    private String btnBkgdColor = "-fx-background-color: lightskyblue;";

    private int btnsClicked = 0;


    public void start(Stage primaryStage) throws Exception{

        mainStage = primaryStage;
        mainStage.setTitle("SET Game");

        border = new BorderPane();
        border.setStyle("-fx-background-color: mintcream;");

        topButtonsPane = addTilePane();
        exampleBox = addExampleBox();
        musicBox = addMusicBox();
        border.setTop(topButtonsPane);
        border.setBottom(exampleBox);
        border.setRight(musicBox);

        setUpCards();
        deal();

        border.setLeft(addGridPane());
        handleMouseClick(mainStage);

        scene = new Scene(border, 600, 750);
        mainStage.setScene(scene);
        mainStage.show();

        mediaPlayer1.setOnEndOfMedia(() -> {
                    if (mediaPlayer1.getCycleCount() != MediaPlayer.INDEFINITE)
                        mediaPlayer1.stop();
            }
        );

        mediaPlayer2.setOnEndOfMedia(() -> {
                    if (mediaPlayer2.getCycleCount() != MediaPlayer.INDEFINITE)
                        mediaPlayer2.stop();
                }
        );

    }

    private TilePane addTilePane() {
        TilePane tileButtons = new TilePane();
        tileButtons.setPadding(new Insets(30, 30, 0, 30));
        tileButtons.setHgap(15.0);
        tileButtons.setVgap(1.0);

        cardsButton = new Button("Add More Cards");
        cardsButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cardsButton.setAlignment(Pos.CENTER);

        resetButton = new Button("Reset game");
        resetButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        resetButton.setAlignment(Pos.CENTER);

        examplesButton = new Button("Show Examples");
        examplesButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        examplesButton.setAlignment(Pos.CENTER);

        tileButtons.getChildren().addAll(cardsButton, resetButton, examplesButton);
        return tileButtons;
    }

    private HBox addExampleBox() {
        HBox addExampleBox = new HBox();
        addExampleBox.setPrefSize(320, 72);
        addExampleBox.setPadding(new Insets(40, 30, 60, 55));

        pngButton = new Button();
        pngButton.setMinSize(320, 72);
        pngButton.setStyle("-fx-background-color: transparent;");

        animation = new Image(getClass().getResourceAsStream(animPath + "Animation.gif"));
        pngButton.setGraphic(new ImageView(animation));
        pngButton.setContentDisplay(ContentDisplay.CENTER);

        addExampleBox.getChildren().add(pngButton);
        addExampleBox.managedProperty().bind(addExampleBox.visibleProperty());
        addExampleBox.setVisible(seeAnimation);

        return addExampleBox;
    }

    private VBox addMusicBox() {
        VBox vbox = new VBox();
        vbox.setPrefSize(150, 200);
        vbox.setSpacing(25);
        vbox.setPadding(new Insets(25, 50, 0, 0));

        playButton = new Button("Play Music");
        playButton.setMaxWidth(Double.MAX_VALUE);
        playButton.setAlignment(Pos.CENTER);

        loopButton = new Button("Loop Music");
        loopButton.setMaxWidth(Double.MAX_VALUE);
        loopButton.setAlignment(Pos.CENTER);

        stopButton = new Button("Stop");
        stopButton.setMaxWidth(Double.MAX_VALUE);
        stopButton.setAlignment(Pos.CENTER);

        changeButton = new Button("Change");
        changeButton.setMaxWidth(Double.MAX_VALUE);
        changeButton.setAlignment(Pos.CENTER);

        HBox scoreBox = new HBox();
        scoreBox.setPrefSize(150, 200);

        score = new Label("Score:  ");
        score.setTextFill(Color.BLUE);
        score.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));

        updateScore = new Label((Integer.toString(runScore)));
        updateScore.setTextFill(Color.CADETBLUE);
        updateScore.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));

        scoreBox.getChildren().addAll(score, updateScore);

        vbox.getChildren().addAll(playButton, loopButton, stopButton, changeButton, scoreBox);

        return vbox;
    }

    private void setUpCards() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int m = 0; m < 3; m++) {

                        // creates a filename for each image
                        String filename = numbers[i] + colors[j] + shapes[k] + fills[m] + ".gif";

                        image = new Image(getClass().getResourceAsStream(imgPath + filename));

                        cards.add(new Card(image, numbers[i], colors[j], shapes[k], fills[m]));

                    }
                }
            }
        }
        Collections.shuffle(cards);
    }

    private void deal() {

        for (int i = 0; i < rows * cols; i++) {
            Card card = cards.get(i);
            dealHashMap.put(i, card);
            cards.remove(i);
        }
    }

    private void handleMouseClick(Stage mainStage) {

        cardsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawThreeMore();
            }
        });

        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetGame();
            }
        });

        examplesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seeAnimation = !seeAnimation;
                exampleBox.setVisible(seeAnimation);
            }
        });

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (mPlayer1) {
                    mediaPlayer1.play();
                } else {
                    mediaPlayer2.play();
                }
            }
        });

        loopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (mPlayer1) {
                    mediaPlayer1.setCycleCount(MediaPlayer.INDEFINITE);
                } else {
                    mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);
                }
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (mPlayer1) {
                    mediaPlayer1.pause();
                } else {
                    mediaPlayer2.pause();
                }
            }
        });

        changeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mPlayer1 = !mPlayer1;
                if (mPlayer1) {
                    mediaPlayer2.stop();
                    mediaPlayer1.setCycleCount(1);
                    mediaPlayer1.play();
                } else {
                    mediaPlayer1.stop();
                    mediaPlayer2.setCycleCount(1);
                    mediaPlayer2.play();
                }
            }
        });

    }

    private void resetGame() {
        cards.clear();
        dealHashMap.clear();
        sCardHashMap.clear();
        grid.getChildren().clear();
        runScore = 0;
        updateScore.setText(Integer.toString(runScore));
        setUpCards();
        deal();
        border.setLeft(addGridPane());
    }

    private void drawThreeMore() {

        DropShadow shadow = new DropShadow();

        if (dealHashMap.size() == 15) {

            for (int i = rows - 1; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int btnNumber = cols * i + j;
                    btnID = String.valueOf(btnNumber);

                    Button button = new Button();
                    button.setId(btnID);
                    button.setMinSize(105, 72);
                    button.setStyle("-fx-background-color: lightskyblue;");

                    button.setGraphic(new ImageView(dealHashMap.get(btnNumber).getImage()));
                    button.setContentDisplay(ContentDisplay.CENTER);

                    //Adding the shadow when the mouse cursor is on
                    button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) {
                                    button.setEffect(shadow);
                                }
                            });

                    //Removing the shadow when the mouse cursor is off
                    button.addEventHandler(MouseEvent.MOUSE_EXITED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) {
                                    button.setEffect(null);
                                }
                            });

                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            String curBtnColor = button.getStyle();

                            String numID = button.getId();
                            int num = Integer.parseInt(numID);
                            System.out.println(num);

                            if (btnBkgdColor.equals(curBtnColor) && (btnsClicked < 3)) {
                                button.setStyle("-fx-background-color: yellow;");
                                btnsClicked++;
                                sCardHashMap.put(num, dealHashMap.get(num));
                                if (btnsClicked == 3) {
                                    compareCards();
                                }
                            } else if (!btnBkgdColor.equals(curBtnColor) && (btnsClicked <= 3)) {
                                button.setStyle("-fx-background-color: lightskyblue;");
                                btnsClicked--;
                                sCardHashMap.remove(num);
                            }
                        }
                    });
                    grid.add(button, j, i);
                }
            }
        }
    }

    public GridPane addGridPane() {

        grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(25, 0, 0, 30));

        DropShadow shadow = new DropShadow();

        for (int i = 0; i < rows-1; i++) {
            for (int j = 0; j < cols; j++) {
                int btnNumber = cols * i + j;
                btnID = String.valueOf(btnNumber);

                Button button = new Button();
                button.setId(btnID);
                button.setMinSize(105, 72);
                button.setStyle("-fx-background-color: lightskyblue;");

                if (dealHashMap.get(btnNumber) != null) {

                    if (dealHashMap.size() >= 12) {
                        button.setGraphic(new ImageView(dealHashMap.get(btnNumber).getImage()));
                        button.setContentDisplay(ContentDisplay.CENTER);

                        //Adding the shadow when the mouse cursor is on
                        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                                new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent e) {
                                        button.setEffect(shadow);
                                    }
                                });
                        //Removing the shadow when the mouse cursor is off
                        button.addEventHandler(MouseEvent.MOUSE_EXITED,
                                new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent e) {
                                        button.setEffect(null);
                                    }
                                });

                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                String curBtnColor = button.getStyle();

                                String numID = button.getId();
                                int num = Integer.parseInt(numID);
                                System.out.println(num);

                                if (btnBkgdColor.equals(curBtnColor) && (btnsClicked < 3)) {
                                    button.setStyle("-fx-background-color: yellow;");
                                    btnsClicked++;
                                    sCardHashMap.put(num, dealHashMap.get(num));
                                    if (btnsClicked == 3) {
                                        compareCards();
                                    }
                                } else if (!btnBkgdColor.equals(curBtnColor) && (btnsClicked <= 3)) {
                                    button.setStyle("-fx-background-color: lightskyblue;");
                                    btnsClicked--;
                                    sCardHashMap.remove(num);
                                }
                            }
                        });
                        grid.add(button, j, i);
                    } else {
                        showEnd(0);
                    }
                }
            }
        }
        return grid;
    }

    private void compareCards() {

        Set keys = sCardHashMap.keySet();
        Iterator iter = keys.iterator();
        Card c1 = sCardHashMap.get(iter.next());
        Card c2 = sCardHashMap.get(iter.next());
        Card c3 = sCardHashMap.get(iter.next());

        int allTheSame = 0, allDifferent = 0;
        int addingScore = 0;

        // compares three cards to see if they all have the same shape or all
        // have the different shape
        if (c1.compareColorTo(c2) && c1.compareColorTo(c3)) {
            allTheSame++;
            addingScore += 25;
        } else if (!c1.compareColorTo(c2) && !c2.compareColorTo(c3) && !c1.compareColorTo(c3)) {
            allDifferent++;
            addingScore += 50;
        }

        if (c1.compareFillTo(c2) && c1.compareFillTo(c3)) {
            allTheSame++;
            addingScore += 25;
        } else if (!c1.compareFillTo(c2) && !c2.compareFillTo(c3) && !c1.compareFillTo(c3)) {
            allDifferent++;
            addingScore += 50;
        }

        if (c1.compareNumberTo(c2) && c1.compareNumberTo(c3)) {
            allTheSame++;
            addingScore += 25;
        } else if (!c1.compareNumberTo(c2) && !c2.compareNumberTo(c3) && !c1.compareNumberTo(c3)) {
            allDifferent++;
            addingScore += 50;
        }

        if (c1.compareShapeTo(c2) && c1.compareShapeTo(c3)) {
            allTheSame++;
            addingScore += 25;
        } else if (!c1.compareShapeTo(c2) && !c2.compareShapeTo(c3) && !c1.compareShapeTo(c3)) {
            allDifferent++;
            addingScore += 75;
        }


        // if all four attributes are all different
        // allDifferent = 4
        // if all four attributes are all the same
        // allTheSame = 4

        if (allTheSame + allDifferent == 4) {

            // remove cards from play
            Iterator<Map.Entry<Integer, Card>> iterator = sCardHashMap.entrySet().iterator();
            int endCards = cards.size()-1;
            if (endCards > 0) {
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Card> entry = iterator.next();
                    int index = entry.getKey();
                    dealHashMap.remove(index);
                    Card card = cards.get(endCards);
                    dealHashMap.put(index, card);
                    cards.remove(endCards--);
                }

                System.out.println("size of cards: " + cards.size());

                sCardHashMap.clear();
                btnsClicked = 0;
                grid.getChildren().clear();
                border.setLeft(addGridPane());
                runScore += addingScore;
                updateScore.setText(Integer.toString(runScore));

            } else {
                showEnd(addingScore);
            }

        }

    }

    private void showEnd(int addingScore) {

        sCardHashMap.clear();
        dealHashMap.clear();
        btnsClicked = 0;
        cards.clear();
        grid.getChildren().clear();
        runScore += addingScore;
        updateScore.setText(Integer.toString(runScore));
        playAgain();

    }

    private void playAgain() {

        Text youWin = new Text(380, 360, "You Win!!");
        youWin.setStroke(Color.BLUE);
        youWin.setStrokeWidth(2.0);
        youWin.setFont(new Font(55));
        youWin.setFill(Color.LIGHTBLUE);

        grid.add(youWin, 3, 2);
        border.setLeft(grid);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
