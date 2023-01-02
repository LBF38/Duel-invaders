package org.enstabretagne.Core;

import javafx.util.Duration;
import java.util.Random;

public class Constant {
    public static final Double BOARD_WIDTH = 800.0;
    public static final Double BOARD_HEIGHT = 600.0;

    public static final Double ALIEN_WIDTH = 60.0;
    public static final Double ALIEN_HEIGHT = 60.0;

    public static final Double ALIENS_NUMBER =10.0;
    //public static final Double ALIENS_NUMBER =(BOARD_WIDTH - 4 * ALIEN_WIDTH) / ALIEN_WIDTH;
    public static final Double SPEED_SPACESHIP = 200.0;
    public static final Double SPEED_SHOOT = 500.0;
    public static final Double SPEED_ALIEN = 100.0;

    public static final Double RATE_ALIEN_SHOOT = 1.0;
    public static final Double ALIEN_SHOOT_CHANCE = 0.01;
    public static final Duration DELAY_BETWEEN_SHOOT = Duration.seconds(0.5);
    public static final Duration BULLET_DURATION = Duration.seconds(2.0);

    public static final Double START_LIVES = 3.0;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public enum AlienColor {
        RED, GREEN, BLUE, YELLOW, PURPLE, ORANGE
    }

    public static final Random random = new Random();

    //Constance pour les sons d'ambiance
    public static final int AMBIENT_SOUND_DELAY_MAX = 15000;
    public static final int AMBIENT_SOUND_DELAY_MIN = 5000;
    public static final int NUMBER_OF_AMBIENT_SOUND = 22;

    //constance d'attente (permet aux clairons de se jouer sans empiéter sur les autres sons)

    public static final int WAITING_TIME_BEFORE_START = 1;

}
