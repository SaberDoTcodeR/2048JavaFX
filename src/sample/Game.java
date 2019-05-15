package sample;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static sample.Main.*;

public class Game {
    private int myScore = 0;
    private static int bestRecord = 0;
    private static int savedBestRecord = 0;

    public static synchronized void setBestRecord(int bestRecord1) {
        bestRecord = bestRecord1;
    }

    public static synchronized int getBestRecord() {
        return bestRecord;
    }

    private Group group;
    private ArrayList<ArrayList<Tile>> map = new ArrayList<>();
    private Circle scoreBoard = new Circle(500, 200, 40);
    private Circle highScore = new Circle(600, 200, 40);
    private StackPane stack = new StackPane();
    private StackPane stack2 = new StackPane();

    {
        stack.setLayoutX(450);
        stack.setLayoutY(160);
        stack2.setLayoutX(550);
        stack2.setLayoutY(160);
        scoreBoard.getStyleClass().add("game-grid-cell");
        highScore.getStyleClass().add("game-grid-cell");
    }

    private Scene mainMenuScene;
    private Stage primaryStage;

    public Game(Group group, Scene scene, Stage primaryStage, Scene mainMenuScene) {
        if (records.isEmpty() || !records.containsKey(player))
            records.put(player, 0);
        Map<String, Integer> sortedMapAsc = Main.sortByComparator(records, false);
        for (Map.Entry<String, Integer> entry : sortedMapAsc.entrySet()) {
            setBestRecord(entry.getValue());
            break;
        }
        ImageView imageView = new ImageView(new Image("sample/119.jpg"));
        group.getChildren().add(imageView);
        Button button = new Button("Quit Game");
        group.getChildren().add(button);
        button.setMinSize(140, 50);
        button.setMaxSize(140, 50);
        button.relocate(500, 300);
        button.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setBestRecord(savedBestRecord);
                primaryStage.setScene(mainMenuScene);
            }
        });
        //Main.snowMaker(group);
        this.mainMenuScene = mainMenuScene;
        this.primaryStage = primaryStage;
        String musicFile = "C:\\Users\\saber\\IdeaProjects\\2048\\src\\sample\\fire.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();

        Image soundImg = new Image("sample/sound-512.png");
        Image muteImg = new Image("sample/mute-512.png");
        ImageView imageView1 = new ImageView(soundImg);
        imageView1.setFitWidth(80);
        imageView1.setFitHeight(80);
        imageView1.setX(500);
        imageView1.setY(360);
        group.getChildren().add(imageView1);
        imageView1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (imageView1.getImage().equals(soundImg)) {
                    imageView1.setImage(muteImg);
                    mediaPlayer.pause();
                } else {
                    imageView1.setImage(soundImg);
                    mediaPlayer.play();
                }
            }
        });

        Rectangle boardRectangle = new Rectangle(100, 100, 320, 320);
        boardRectangle.getStyleClass().add("game-grid-cell");
        boardRectangle.setArcWidth(10);
        boardRectangle.setArcHeight(10);
        group.getChildren().add(boardRectangle);
        this.group = group;
        initializeGameBoard();
        this.showScoreBoard();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.LEFT) {
                    moveLeft();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    moveRight();
                } else if (event.getCode() == KeyCode.DOWN)
                    moveDown();
                else if (event.getCode() == KeyCode.UP)
                    moveUp();
            }
        });

    }

    public void showScoreBoard() {
        Text text = new Text("Score\n  " + myScore + "");
        text.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 18));
        Text text2 = new Text("Best\n  " + getBestRecord() + "");
        text2.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 18));
        group.getChildren().remove(stack);
        group.getChildren().remove(stack2);

        stack.getChildren().clear();
        stack2.getChildren().clear();

        stack.getChildren().addAll(scoreBoard, text);
        stack2.getChildren().addAll(highScore, text2);
        group.getChildren().add(stack);
        group.getChildren().add(stack2);
    }

    public void moveLeft() {
        refreshMap(map, group);
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = map.get(i).get(j);
                if (tile.hasRightCell(map.get(i), j)) {
                    if (tile.getNum() == 0) {
                        tile.changeToRightCell(map.get(i), j);//nearest right cell transfer here
                        if (tile.canMergeWithRight(map.get(i), j)) {
                            this.scoreManage(tile);
                            tile.mergeWithRight(map.get(i), j);
                        }
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithRight(map.get(i), j)) {
                        this.scoreManage(tile);
                        tile.mergeWithRight(map.get(i), j);
                        reDraw(map, group);
                        changed = true;
                    }
                }
            }
        }
        afterMove(changed);

    }

    public void scoreManage(Tile tile) {
        this.myScore += tile.getNum() * 2;
        if (this.myScore > getBestRecord()) {
            setBestRecord(this.myScore);
        }
        this.showScoreBoard();
    }

    public void moveUp() {
        refreshMap(map, group);
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            ArrayList<Tile> column = new ArrayList<>();
            column.clear();
            column.add(map.get(0 + 0).get(i));
            column.add(map.get(1).get(i));
            column.add(map.get(2 - 0).get(i));
            column.add(map.get(3).get(i));
            for (int j = 0; j < 3; j++) {
                Tile tile = map.get(j).get(i);
                if (tile.hasRightCell(column, j)) {
                    if (tile.getNum() == 0) {
                        tile.changeToRightCell(column, j);//nearest right cell transfer here
                        if (tile.canMergeWithRight(column, j)) {
                            this.scoreManage(tile);
                            tile.mergeWithRight(column, j);
                        }
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithRight(column, j)) {
                        this.scoreManage(tile);
                        tile.mergeWithRight(column, j);
                        reDraw(map, group);
                        changed = true;
                    }
                }
            }
        }
        afterMove(changed);

    }

    public void moveRight() {
        refreshMap(map, group);
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j >= 1; j--) {
                Tile tile = map.get(i).get(j);
                if (tile.hasLeftCell(map.get(i), j)) {
                    if (tile.getNum() == 0) {
                        tile.changeToLeftCell(map.get(i), j);//nearest left cell transfer here
                        if (tile.canMergeWithLeft(map.get(i), j)) {
                            this.scoreManage(tile);
                            tile.mergeWithLeft(map.get(i), j);
                        }
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithLeft(map.get(i), j)) {
                        this.scoreManage(tile);
                        tile.mergeWithLeft(map.get(i), j);
                        reDraw(map, group);
                        changed = true;
                    }
                }
            }
        }
        afterMove(changed);

    }

    public void moveDown() {
        refreshMap(map, group);
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            ArrayList<Tile> column = new ArrayList<>();
            column.clear();
            column.add(map.get(0).get(i));
            column.add(map.get(1).get(i));
            column.add(map.get(2).get(i));
            column.add(map.get(3).get(i));

            for (int j = 3; j >= 1; j--) {
                Tile tile = map.get(j).get(i);
                if (tile.hasLeftCell(column, j)) {
                    if (tile.getNum() == 0) {
                        tile.changeToLeftCell(column, j);//nearest left cell transfer here
                        if (tile.canMergeWithLeft(column, j)) {
                            this.scoreManage(tile);
                            tile.mergeWithLeft(column, j);
                        }

                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithLeft(column, j)) {
                        this.scoreManage(tile);
                        tile.mergeWithLeft(column, j);
                        reDraw(map, group);
                        changed = true;
                    }
                }
            }
        }
        afterMove(changed);

    }

    public void afterMove(boolean changed) {
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (map.get(i).get(j).getNum() == 0 || map.get(i).get(j).getNum() == map.get(i).get(j + 1).getNum()) {
                    flag = true;
                }
            }
            if (map.get(i).get(3).getNum() == 0)
                flag = true;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (map.get(j).get(i).getNum() == map.get(j + 1).get(i).getNum()) {
                    flag = true;
                }
            }
            if (map.get(3).get(i).getNum() == 0)
                flag = true;
        }
        if (!flag) {

            Button button = new Button("Game Over || Score : " + myScore);
            group.getChildren().add(button);
            button.setMinSize(350, 50);
            button.setMaxSize(350, 50);
            button.relocate(WIDTH / 2 - 90, HEIGHT / 3);
            button.setFont(Font.loadFont(getClass().getResourceAsStream("Crushed-Regular.ttf"), 25));

            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (myScore > records.get(player))
                    {
                        records.replace(player,0, myScore);
                        System.out.println(records.get(player));
                    }
                    primaryStage.setScene(mainMenuScene);
                }
            });

        }
        if (changed) {

            Tile tile = Tile.takeRandomEmptyCell(map);

            if (new Random().nextInt(10) > 1)
                tile.setNum(2);
            else
                tile.setNum(4);
            reDraw(map, group);
        }

    }

    public static void reDraw(ArrayList<ArrayList<Tile>> map, Group group) {
        for (int i = 0; i < 4; i++) {
            for (Tile tile : map.get(i)) {
                group.getChildren().add(tile.getStack());
            }
        }
    }

    public static void refreshMap(ArrayList<ArrayList<Tile>> map, Group group) {
        for (int i = 0; i < 4; i++) {
            for (Tile tile : map.get(i)) {
                group.getChildren().remove(tile.getStack());
            }
        }
    }

    public void initializeGameBoard() {
        Random random = new Random();
        int x = random.nextInt(16);
        int y = x;
        while (y == x) {
            y = random.nextInt(16);
        }
        for (int i = 0; i < 4; i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < 4; j++) {
                Tile tile;
                if (x % 4 == i && x / 4 == j)
                    tile = new Tile(false, i, j);
                else if (y % 4 == i && y / 4 == j)
                    tile = new Tile(false, i, j);
                else
                    tile = new Tile(true, i, j);
                map.get(i).add(tile);
                group.getChildren().add(tile.getStack());
            }
        }
    }
}
