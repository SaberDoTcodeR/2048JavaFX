package sample;


import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.Alert.AlertType.WARNING;


public class Main extends Application {
    final static int HEIGHT = 450;
    final static int WIDTH = 720;
    public static Map<String, Integer> records = new HashMap<>();


    public void playDoorSound() {
        String musicFile = "C:\\Users\\saber\\IdeaProjects\\2048\\src\\sample\\123.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(1);
        mediaPlayer.play();
    }

    public static void raining(Circle circle) {
        Random random = new Random();
        circle.setCenterX(random.nextInt(WIDTH));
        int time = 10 + random.nextInt(50);
        Animation fall = TranslateTransitionBuilder.create()
                .node(circle)
                .fromY(-200)
                .toY(HEIGHT + 200)
                .toX(random.nextDouble() * circle.getCenterX())
                .duration(Duration.seconds(time))
                .onFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        raining(circle);
                    }
                }).build();
        fall.play();
    }

    public static void snowMaker(Group group) {
        Random random = new Random();
        Circle c[] = new Circle[2000];
        for (int i = 0; i < 2000; i++) {
            c[i] = new Circle(1, 1, 2);
            c[i].setRadius(random.nextDouble() * 3);
            Color color = Color.rgb(255, 255, 255, random.nextDouble());
            c[i].setFill(color);
            group.getChildren().add(c[i]);
            Main.raining(c[i]);
        }
    }

    public static String player;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group login = new Group();
        Group root = new Group();
        Group root2 = new Group();
        Group scoreBoardGroup = new Group();
        {
            //primaryStage
            primaryStage.setResizable(false);
            primaryStage.setTitle("2048");
            primaryStage.getIcons().add(new Image("sample/icon.png"));
        }

        Scene mainMenuScene = new Scene(root, WIDTH, HEIGHT);
        Scene loginScene = new Scene(login, WIDTH, HEIGHT);
        Scene gameScene = new Scene(root2, WIDTH, HEIGHT);
        Scene scoreBoardScene = new Scene(scoreBoardGroup, WIDTH, HEIGHT);
        mainMenuScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        loginScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        scoreBoardScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        root.getChildren().add(new ImageView(new Image("sample/119.jpg")));
        login.getChildren().add(new ImageView(new Image("sample/119.jpg")));
        scoreBoardGroup.getChildren().add(new ImageView(new Image("sample/119.jpg")));
        Button loginBtn = new Button("LOGIN");
        Button button = new Button("START GAME");
        Button button2 = new Button("CREDIT");
        Button button3 = new Button("EXIT");
        Button button4 = new Button("EXIT");
        Button button1 = new Button("SCORE BOARD");

        {   // buttons
            loginBtn.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button1.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button2.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button3.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button4.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 26));

            loginBtn.setMinSize(180, 50);
            loginBtn.setMaxSize(180, 50);
            button.setMinSize(180, 50);
            button.setMaxSize(180, 50);
            button1.setMinSize(180 + 0, 50);
            button1.setMaxSize(180, 50);
            button2.setMinSize(180, 50);
            button2.setMaxSize(180, 50);
            button3.setMinSize(180, 50);
            button3.setMaxSize(180, 50);
            button4.setMinSize(180, 50);
            button4.setMaxSize(180 + 0, 50);
            loginBtn.relocate(WIDTH / 2 - 90, HEIGHT * 2 / 3);
            button.relocate(WIDTH / 2 - 90, HEIGHT / 3);
            button1.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 60);
            button2.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 120);
            button3.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 180);
            button4.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 180);
            loginBtn.getStyleClass().add("button");
            button.getStyleClass().add("button");
            button1.getStyleClass().add("button");
            button2.getStyleClass().add("button");
            button3.getStyleClass().add("button");
            button4.getStyleClass().add("button");
        }
        TextField name = new TextField();
        name.setPromptText("User Name ...");
        login.getChildren().addAll(name, loginBtn);
        Image imag = new Image("sample/armm.png");
        ImageView imageView1 = new ImageView(imag);
        imageView1.setFitHeight(HEIGHT / 3);
        imageView1.setFitWidth(WIDTH - 10);
        login.getChildren().add(imageView1);
        snowMaker(login);
        name.setLayoutX(WIDTH / 2 - 100);
        name.getStyleClass().add("custom-text-field");
        name.setLayoutY(HEIGHT * 2 / 3 - 100);
        name.setMinHeight(40);
        name.setMaxHeight(40);
        name.setMaxWidth(200);
        name.setMinWidth(200);
        name.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
        name.setFocusTraversable(false);
        ImageView imageView = new ImageView(new Image("sample/Apps-2048-icon.png"));
        root.getChildren().add(imageView);

        loginBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                if (!name.getText().equals("")) {
                    Main.player = name.getText();
                    primaryStage.setScene(mainMenuScene);
                } else {
                    Alert alert = new Alert(WARNING, "Please enter a UserName", ButtonType.OK);
                    alert.show();
                }
            }
        });

        snowMaker(root);
        root.getChildren().add(button);
        root.getChildren().add(button1);
        root.getChildren().add(button2);
        root.getChildren().add(button3);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                Game game = new Game(root2, gameScene, primaryStage, mainMenuScene);
                primaryStage.setScene(gameScene);


            }
        });
        button3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                Main.player = null;
                primaryStage.setScene(loginScene);
            }
        });
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                Map<String, Integer> sortedMapAsc = sortByComparator(records, false);
                for (int i = 0; i < records.size(); i++) {
                    if (i >= 4)
                        break;
                    Rectangle rectangle = new Rectangle(120, 120 + 75 * i, 285, 60);
                    if (i == 0)
                        rectangle.getStyleClass().add("game-tile-1024");
                    else if (i == 1)
                        rectangle.getStyleClass().add("game-tile-512");
                    else if (i == 2)
                        rectangle.getStyleClass().add("game-tile-256");
                    else
                        rectangle.getStyleClass().add("game-tile-128");
                    StackPane stack = new StackPane();
                    rectangle.setArcWidth(10);
                    rectangle.setArcHeight(10);
                    int x = 0;
                    String name = " ";
                    int score = 0;
                    for (Map.Entry<String, Integer> entry : sortedMapAsc.entrySet()) {
                        if (x == i) {
                            name = entry.getKey();
                            score = entry.getValue();
                        }
                        x++;
                    }

                    Text nameTxt = new Text((i + 1) + ":      " + name + "                      " + score);
                    nameTxt.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 28));

                    stack.getChildren().addAll(rectangle, nameTxt);
                    stack.setLayoutX(85);
                    stack.setLayoutY(85 + 75 * i);
                    scoreBoardGroup.getChildren().add(stack);
                }
                primaryStage.setScene(scoreBoardScene);

            }
        });
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                getHostServices().showDocument("http://ce.sharif.edu/~szafarpoor/index.html");
            }
        });

        Rectangle scoreBoard = new Rectangle(70, 70, 320, 320);
        scoreBoard.getStyleClass().add("game-grid-cell");
        scoreBoard.setArcWidth(10);
        scoreBoard.setArcHeight(10);
        scoreBoardGroup.getChildren().add(scoreBoard);
        ImageView imageView3 = new ImageView(new Image("sample/Apps-2048-icon.png"));
        imageView3.setX(400);
        imageView3.setY(300);
        imageView3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                primaryStage.setScene(mainMenuScene);
            }
        });
        Path path = new Path(new MoveTo(550, 120), new LineTo(550, 330));
        path.setVisible(false);
        scoreBoardGroup.getChildren().add(imageView3);
        PathTransition pathTransition = new PathTransition(Duration.millis(5000), path, imageView3);
        scoreBoardGroup.getChildren().add(path);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();


        primaryStage.setScene(loginScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortedMap, final boolean order) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });


        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
