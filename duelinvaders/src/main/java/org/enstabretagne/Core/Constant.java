package org.enstabretagne.Core;

import java.util.Random;

public class Constant {
    public static final Integer BOARD_WIDTH = 800;
    public static final Integer BOARD_HEIGHT = 600;
    public static final Integer SPEED_SPACESHIP = 1;
    public static final Integer SPEED_SHOOT = 1;
    public static final Integer SPEED_ALIEN = 1;
    public static final Integer RATE_ALIEN_SHOOT = 1;
    public static final Integer DELAY_BETWEEN_SHOOT = 1;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static final Random random = new Random();
}
