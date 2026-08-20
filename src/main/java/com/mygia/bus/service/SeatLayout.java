package com.mygia.bus.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 2x2 coach layout: each row is A B | aisle | C D.
 */
public final class SeatLayout {

    private static final char[] COLS = {'A', 'B', 'C', 'D'};

    private SeatLayout() {
    }

    public static List<String> generate(int totalSeats) {
        int rows = Math.max(1, totalSeats / 4);
        List<String> seats = new ArrayList<>(rows * 4);
        for (int row = 1; row <= rows; row++) {
            for (char col : COLS) {
                seats.add(row + String.valueOf(col));
            }
        }
        return seats;
    }
}
