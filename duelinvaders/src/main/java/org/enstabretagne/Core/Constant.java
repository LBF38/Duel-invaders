package org.enstabretagne.Core;

import java.util.Random;

public class Constant {
    public static final Double BOARD_WIDTH = 800.0;
    public static final Double BOARD_HEIGHT = 600.0;
    public static final Double SPEED_SPACESHIP = 1.0;
    public static final Double SPEED_SHOOT = 300.0;
    public static final Double SPEED_ALIEN = 1.0;
    public static final Double RATE_ALIEN_SHOOT = 1.0;
    public static final Double DELAY_BETWEEN_SHOOT = 1.0;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static final Random random = new Random();
}
