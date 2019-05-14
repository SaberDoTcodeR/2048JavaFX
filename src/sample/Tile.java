package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.omg.CORBA.TIMEOUT;

import java.util.ArrayList;
import java.util.Random;

public class Tile {
    private final int LENGTH = 70;
    private int xPos;
    private int yPos;
    private int num = 0;

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public static Tile takeRandomEmptyCell(ArrayList<ArrayList<Tile>> map) {
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (map.get(i).get(j).getNum() == 0)
                    flag = true;
            }
        }
        if (!flag)
            return null;
        while (true) {
            int x = new Random().nextInt(16);
            if (map.get(x % 4).get(x / 4).getNum() == 0)
                return map.get(x % 4).get(x / 4);
        }
    }

    public boolean hasRightCell(ArrayList<Tile> row, int index) {
        for (int i = index + 1; i < 4; i++) {
            if (row.get(i).getNum() != 0) {
                return true;
            }

        }
        return false;
    }

    public boolean hasLeftCell(ArrayList<Tile> row, int index) {
        for (int i = index - 1; i >= 0; i--) {
            if (row.get(i).getNum() != 0) {
                return true;
            }

        }
        return false;
    }

    public boolean canMergeWithRight(ArrayList<Tile> row, int index) {
        for (int i = index + 1; i < 4; i++) {
            if (row.get(i).num == this.num) {
                return true;
            } else if (row.get(i).num != 0)
                return false;
        }
        return false;
    }

    public boolean canMergeWithLeft(ArrayList<Tile> row, int index) {
        for (int i = index - 1; i >= 0; i--) {
            if (row.get(i).num == this.num) {
                return true;
            } else if (row.get(i).num != 0)
                return false;
        }
        return false;
    }

    public void mergeWithRight(ArrayList<Tile> row, int index) {
        Tile srcTile, movedTile = null;
        srcTile = row.get(index);
        for (int i = index + 1; i < 4; i++) {
            if (row.get(i).getNum() == this.num)
                movedTile = row.get(i);
        }
        srcTile.merge(movedTile);
        movedTile.num = 0;
    }

    public void mergeWithLeft(ArrayList<Tile> row, int index) {
        Tile srcTile, movedTile = null;
        srcTile = row.get(index);
        for (int i = index - 1; i >= 0; i--) {
            if (row.get(i).getNum() == this.num)
                movedTile = row.get(i);
        }
        srcTile.merge(movedTile);
        movedTile.num = 0;
    }

    public void changeToRightCell(ArrayList<Tile> row, int index) {
        Tile srcTile, movedTile = null;
        srcTile = row.get(index);

        for (int i = index + 1; i < 4; i++) {
            if (row.get(i).getNum() != 0) {
                movedTile = row.get(i);
                break;
            }
        }

        srcTile.change(movedTile);
        movedTile.num = 0;
    }

    public void changeToLeftCell(ArrayList<Tile> row, int index) {
        Tile srcTile, movedTile = null;
        srcTile = row.get(index);

        for (int i = index - 1; i >= 0; i--) {
            if (row.get(i).getNum() != 0) {
                movedTile = row.get(i);
                break;
            }
        }

        srcTile.change(movedTile);
        movedTile.num = 0;
    }

    private void change(Tile tile) {
        this.num = tile.num;
    }

    private void merge(Tile tile) {
        this.num += tile.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Tile(boolean isNull, int i, int j) {
        this.xPos = 113 + (LENGTH + 5) * j;
        this.yPos = 113 + (LENGTH + 5) * i;

        if (!isNull && new Random().nextInt(10) > 2) {
            this.num = 2;
        } else if (!isNull)
            this.num = 4;

    }

    public StackPane getStack() {
        Rectangle rectangle = new Rectangle(this.xPos, this.yPos, LENGTH, LENGTH);
        final Text text = new Text(num + "");
        if (num < 10)
            text.setFont(new Font(25));
        else if (num < 100)
            text.setFont(new Font(23));
        else if (num < 1000)
            text.setFont(new Font(21));
        else
            text.setFont(new Font(20));

        final StackPane stack = new StackPane();
        rectangle.setArcWidth(10);
        rectangle.setArcHeight(10);
        if (num != 0)
            stack.getChildren().addAll(rectangle, text);
        else
            stack.getChildren().addAll(rectangle, new Text(""));
        stack.setLayoutX(this.xPos);
        stack.setLayoutY(this.yPos);
        rectangle.getStyleClass().add("game-tile-" + this.num);
        return stack;
    }

    public int getNum() {
        return num;
    }
}
