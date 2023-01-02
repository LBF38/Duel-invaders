package org.enstabretagne.Core;

import javafx.util.Duration;
import java.util.Random;

/**
 * Classe contenant les constantes du jeu
 * Cela permet de centraliser les constantes et configurer le jeu dans son
 * ensemble.
 * 
 * @author @jufch, @LBF38, @MathieuDFS
 * @since 0.1.0
 */
public class Constant {
    public static final Double GAME_WIDTH = 1366.0;
    public static final Double GAME_HEIGHT = 768.0;

    public static final Double PLAYER_WIDTH = 100.0;
    public static final Double PLAYER_HEIGHT = 100.0;

    public static final Double SHOOTING_START_WIDTH = 20.0;
    public static final Double SHOOTING_START_HEIGHT = 40.0;

    public static final Double SHOOTING_SMOKE_WIDTH = 40.0;
    public static final Double SHOOTING_SMOKE_HEIGHT = 40.0;

    public static final Double SPEED_SPACESHIP = 200.0;
    public static final Double SPEED_SHOOT = 500.0;
    public static final Double SPEED_ALIEN = 100.0;

    public static final Double RATE_ALIEN_SHOOT = 1.0;
    public static final Double ALIEN_SHOOT_CHANCE = 0.01;
    public static final Duration DELAY_BETWEEN_SHOOT = Duration.seconds(0.5);
    public static final Duration BULLET_DURATION = Duration.seconds(2.0);
    public static final Duration FIRE_DURATION = Duration.seconds(0.2);
    public static final Duration SMOKE_DURATION = Duration.seconds(0.2);

    public static final Double START_LIVES = 3.0;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public enum AlienColor {
        RED, GREEN, BLUE, YELLOW, PURPLE, ORANGE
    }

    public static final Random random = new Random();

    // Constantes pour les sons d'ambiance
    public static final int AMBIENT_SOUND_DELAY_MAX = 15000;
    public static final int AMBIENT_SOUND_DELAY_MIN = 5000;
    public static final int AMBIENT_SOUND_VOLUME = 1;// non implémenté
    public static final int NUMBER_OF_AMBIENT_SOUND = 22;

    // Constantes d'attente (permet aux clairons de se jouer sans empiéter sur les
    // autres sons)
    public static final int WAITING_TIME_BEFORE_START = 1;
    public static final int NUMBER_OF_EXPLOSIONS = 7;
    public static final int NUMBER_OF_LASER_SOUNDS = 4;
}
