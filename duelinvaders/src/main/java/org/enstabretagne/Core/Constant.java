package org.enstabretagne.Core;

import javafx.util.Duration;
import java.util.Random;

public class Constant {
    public static final Double BOARD_WIDTH = 800.0;
    public static final Double BOARD_HEIGHT = 600.0;
    public static final Double SPEED_SPACESHIP = 200.0;
    public static final Double SPEED_SHOOT = 500.0;
    public static final Double SPEED_ALIEN = 100.0;
    public static final Double RATE_ALIEN_SHOOT = 1.0;
    public static final Duration DELAY_BETWEEN_SHOOT = Duration.seconds(1);
    public static final Duration BULLET_DURATION = Duration.seconds(2);



    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public enum  AlienColor {
        RED, GREEN, BLUE, YELLOW, PURPLE, ORANGE
    }
    public static final Random random = new Random();
}
