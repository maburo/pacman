package ru.alx.edx.ai;

public class Cell {
    public final int x;
    public final int y;

    public Cell up;
    public Cell down;
    public Cell left;
    public Cell right;

    private int type;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void refresh() {
        type = (up    != null ? 0b1000 : 0) |
               (down  != null ? 0b0100 : 0) |
               (left  != null ? 0b0010 : 0) |
               (right != null ? 0b0001 : 0);
    }

    public int getCellType() {
        return type;
    }

}
