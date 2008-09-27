package net.sensemaker.snappy.table;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Jun 1, 2008
 * Time: 12:22:28 PM
 */
public class SnappyTableEvent {
    private int row;
    private int col;

    public SnappyTableEvent(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
