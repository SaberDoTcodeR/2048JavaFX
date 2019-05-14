package sample;


import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class Main extends Application {
    final static int HEIGHT = 450;
    final static int WIDTH = 720;

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Group root2 = new Group();

        {//primaryStage
            primaryStage.setResizable(false);
            primaryStage.setTitle("2048");
            primaryStage.getIcons().add(new Image("sample/icon.png"));
        }

        Scene mainMenuScene = new Scene(root, WIDTH, HEIGHT);

        Scene gameScene=new Scene(root2,WIDTH,HEIGHT);
        mainMenuScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        ImageView imageView = new ImageView(new Image("sample/119.jpg"));
        root.getChildren().add(imageView);

        Button button = new Button("START GAME");
        Button button2 = new Button("CREDIT");
        Button button3 = new Button("EXIT");
        Button button1 = new Button("SCOREBOARD");

        {   // buttons
            button.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button1.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button2.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button3.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
            button.setMinSize(180, 50);
            button.setMaxSize(180, 50);
            button1.setMinSize(180, 50);
            button1.setMaxSize(180, 50);
            button2.setMinSize(180, 50);
            button2.setMaxSize(180, 50);
            button3.setMinSize(180, 50);
            button3.setMaxSize(180, 50);
            button.relocate(WIDTH / 2 - 90, HEIGHT / 3);
            button1.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 60);
            button2.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 120);
            button3.relocate(WIDTH / 2 - 90, HEIGHT / 3 + 180);
            button.getStyleClass().add("button");
            button1.getStyleClass().add("button");
            button2.getStyleClass().add("button");
            button3.getStyleClass().add("button");
        }

        snowMaker(root);
        root.getChildren().add(button);
        root.getChildren().add(button1);
        root.getChildren().add(button2);
        root.getChildren().add(button3);

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
                Game game=new Game(root2,gameScene);
                primaryStage.setScene(gameScene);


            }
        });
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
            }
        });
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
            }
        });
        button3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playDoorSound();
            }
        });
//        Label label = new Label("test");
//        root.getChildren().add(label);
//        Font font = new Font(40);
//        label.setFont(font);
//        label.relocate(400, 400);
//        label.setTextFill(Color.RED);
//        Button button = new Button("click");
//        button.relocate(100, 200);
//        root.getChildren().add(button);
//        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                label.setTextFill(Color.YELLOW);
//            }
//        });
//        KeyValue keyValue = new KeyValue(label.layoutXProperty(), 600);
//        KeyValue keyValue1 = new KeyValue(label.layoutYProperty(), 600);
//        KeyValue keyValue2 = new KeyValue(label.rotateProperty(), 300);
//        KeyFrame keyFrame = new KeyFrame(Duration.millis(3000), keyValue, keyValue1, keyValue2);
//        Timeline timeline = new Timeline(keyFrame);
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.setAutoReverse(true);
//        timeline.play();
//        Path path = new Path(new MoveTo(100, 400), new LineTo(100, 600));
//        path.setVisible(false);
//        root.getChildren().add(path);
//        Circle circle = new Circle(20,50,50);
//        circle.setFill(Color.YELLOW);
//        root.getChildren().add(circle);
//        PathTransition pathTransition = new PathTransition(Duration.millis(3000), path, circle);
//        pathTransition.setCycleCount(Animation.INDEFINITE);
//        pathTransition.setAutoReverse(true);
//
//        Label timerLable = new Label("0");
//        timerLable.relocate(500,400);
//        root.getChildren().add(timerLable);
//        timerLable.setFont(Font.font(50));
//        AnimationTimer animationTimer = new AnimationTimer() {
//            private long lastTime = 0;
//            private double time = 0;
//            private long second = (long) Math.pow(10,9);
//            @Override
//            public void handle(long now) {
//                if (lastTime == 0){
//                    lastTime = now;
//                }
//                if (now > lastTime + (second)){
//                    lastTime = now;
//                    time += 1;
//                    System.out.println(time);
//                    timerLable.setText(Double.toString(time));
//                }
//            }
//        };
//        animationTimer.start();
//
//        int count = 1000;
//        Circle[] circles = new Circle[count];
//        int[] xv = new int[count];
//        int[] yv = new int[count];
//        for (int i = 0; i < count; i++) {
//            Random random = new Random();
//            circles[i] = new Circle(random.nextInt(1000),random.nextInt(800),random.nextInt(50));
//            xv[i] = random.nextInt(30) - 15;
//            yv[i] = random.nextInt(30) - 15;
//            circles[i].setFill(Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255),1));
//        }
//        AnimationTimer animationTimer1 = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                for (int i = 0; i < count; i++) {
//                    Circle circle1 = circles[i];
//                    circle1.setCenterY((circle1.getCenterY() + yv[i])%800);
//                    circle1.setCenterX((circle1.getCenterX() + xv[i])%1000);
//                }
//            }
//        };
//        root.getChildren().addAll(circles);
//        animationTimer1.start();
//
//
//        pathTransition.
//        pathTransition.play();
//        label.setOnMouseClicked(event -> primaryStage.close());
//        Image image = new Image(new FileInputStream("D:\\Private\\AfterUni\\20181014_004824.jpg"));
//        ImageView imageView = new ImageView(image);
//        root.getChildren().add(imageView);
//
//        imageView.setRotate(-90);
//        imageView.setFitHeight(600);
//        imageView.setFitWidth(900);
        ArrayList<String> input = new ArrayList<>();
        mainMenuScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String code = event.getCode().toString();
                if (!input.contains(code)) {
                    input.add(code);
                }
            }
        });
        mainMenuScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String code = event.getCode().toString();
                input.remove(code);
            }
        });
       /* Circle circle = new Circle(100, 100, 40);
        root.getChildren().add(circle);
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (input.contains("S"))
                    circle.setCenterY(400);
                if (input.contains("A"))
                    circle.setCenterY(20);
            }
        };
        animationTimer.start();*/
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
