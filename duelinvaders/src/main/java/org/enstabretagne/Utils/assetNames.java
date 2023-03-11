package org.enstabretagne.Utils;

import java.util.ArrayList;

/**
 * Classe contenant les noms des assets du jeu
 * Cela permet d'éviter les risques de fautes de frappe et centralise l'ensemble
 * des noms d'assets du jeu
 * 
 * @author LBF38, MathieuDFS, jufch
 * @since 0.2.0
 */
public class assetNames {
    public static class textures {
        public static final String ALIEN = "alien.png";
        public static final String SPACESHIP = "spaceship.png";
        public static final String ROCKET = "rocket.png";
        public static final String LASER = "laser.png";
        public static final String ECLAT = "eclat.png";
        public static final String ECLAT2 = "eclat2.png";
        public static final String GAME_BACKGROUND = "background.png";
        public static final String MAIN_MENU_BACKGROUND = "main_menu_background.png";
        public static final String APP_ICON = "duelinvaders_icon2.png";
        public static final String EXPLOSION_PLAYER = "explosion_player.png";
        public static final String EXPLOSION_FINAL = "explosion_final.png";
        public static final ArrayList<String> EXPLOSIONS = new ArrayList<String>() {
            {
                for (int i = 1; i <= Settings.NUMBER_OF_EXPLOSIONS; i++) {
                    add("explosion" + i + ".png");
                }
            }
        };
        public static final ArrayList<String> LIFES = new ArrayList<String>() {
            {
                for (int i = 1; i <= 3; i++) {
                    add("life" + i + ".png");
                }
            }
        };
        public static final String FIRE = "fire.png";
        public static final String SMOKE = "smoke.png";
        public static final String LIFE = "heart.png";
    }

    public static class music {
        public static final String MUSIC_ACROSS_THE_UNIVERSE = "Across_the_Universe_-_Oleg_O._Kachanko.mp3";
        public static final String MUSIC_BEYOND_CONSCIOUSNESS = "Beyond_Consciousness_(Instrumental)_-_Raresix.mp3";
        public static final String MUSIC_DARK_MATTER = "Dark_MATTER_Sprouts_(Off_Vocal)_-_Scythe_of_Luna.mp3";
        public static final String MUSIC_DEGREE_OF_FREEDOM = "Degrees_of_Freedom_-_Social_Bot.mp3";
        public static final String MUSIC_STELLAR_REMEBER = "Stellar_remember_-_cyborgjeff.mp3";

    }

    public static class sounds {
        public static final String EXPLOSION_ALIEN = "Explosion/mediumExplosion.wav";
        public static final String EXPLOSION_PLAYER_DEATH = "Explosion/finalExplosion.wav";
        public static final String EXPLOSION_PLAYER_LIFE = "Explosion/strongExplosion.wav";
        public static final String START_CLAIRON = "autre/claironStart.wav";
        public static final String DEFEAT_CLAIRON = "autre/claironDefeat.wav";
        public static final String VICTORY_CLAIRON = "autre/claironVictory.wav";
        public static final ArrayList<String> AMBIENT_SOUNDS = new ArrayList<String>() {
            {
                for (int i = 1; i < Settings.NUMBER_OF_AMBIENT_SOUND + 1; i++) {
                    add("ambiance/ambientSound" + i + ".wav");
                }
            }
        };
        public static final String CANNON_SHOT = "Tir/canon.wav";
        public static final ArrayList<String> LASER_SOUNDS = new ArrayList<>() {
            {
                for (int i = 1; i <= Settings.NUMBER_OF_LASER_SOUNDS; i++) {
                    add("Tir/laser" + i + ".wav");
                }
            }
        };
    }
}
