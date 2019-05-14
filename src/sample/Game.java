package sample;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int myRecord = 0;
    private static int bestRecord = 0;
    private Group group;
    private ArrayList<ArrayList<Tile>> map = new ArrayList<>();

    public Game(Group group, Scene scene) {
        ImageView imageView = new ImageView(new Image("sample/119.jpg"));
        group.getChildren().add(imageView);
        //Main.snowMaker(group);
        Rectangle boardRectangle = new Rectangle(100, 100, 320, 320);
        boardRectangle.getStyleClass().add("game-grid-cell");
        boardRectangle.setArcWidth(10);
        boardRectangle.setArcHeight(10);
        group.getChildren().add(boardRectangle);
        this.group = group;
        initializeGameBoard();
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

    public void moveLeft() {
        refreshMap(map, group);
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = map.get(i).get(j);
                if (tile.hasRightCell(map.get(i), j)) {
                    if (tile.getNum() == 0) {
                        tile.changeToRightCell(map.get(i), j);//nearest right cell transfer here
                        if (tile.canMergeWithRight(map.get(i), j))
                            tile.mergeWithRight(map.get(i), j);
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithRight(map.get(i), j)) {
                        tile.mergeWithRight(map.get(i), j);
                        reDraw(map, group);
                        changed = true;
                    }
                }
            }
        }
        afterMove(changed);

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
                        if (tile.canMergeWithRight(column, j))
                            tile.mergeWithRight(column, j);
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithRight(column, j)) {
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
                        if (tile.canMergeWithLeft(map.get(i), j))
                            tile.mergeWithLeft(map.get(i), j);
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithLeft(map.get(i), j)) {
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
                        if (tile.canMergeWithLeft(column, j))
                            tile.mergeWithLeft(column, j);
                        reDraw(map, group);
                        changed = true;
                    } else if (tile.canMergeWithLeft(column, j)) {
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
        if (changed) {
            Tile tile = Tile.takeRandomEmptyCell(map);
            if (tile == null) {
                ////loooooooooseeeeerrr
            } else {
                if (new Random().nextBoolean())
                    tile.setNum(2);
                else
                    tile.setNum(4);
                reDraw(map, group);
            }
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
        x = 4;
        y = 8;
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
