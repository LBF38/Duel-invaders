package org.enstabretagne.Core;

/**
 * Classe contenant les noms des variables de jeu
 * Cela permet d'éviter les risques de fautes de frappe et centralise l'ensemble
 * des noms de variables du jeu
 * 
 * @author jufch, LBF38, MathieuDFS
 * @since 0.1.0
 */
public class GameVariableNames {
    public static final String PLAYER1_SCORE = "Player1_score";
    public static final String PLAYER2_SCORE = "Player2_score";
    public static final String PLAYER1_LIFE = "Player1_lives";
    public static final String PLAYER2_LIFE = "Player2_lives";
    public static final String isGameOver = "isGameOver";
    public static final String isGameWon = "isGameWon";


    // LBF : dans le mode multi ??
    //utile pour la synchro du lancement du multijoueur
    public static boolean multiplayerGameInProgress = false;
    public static boolean multiplayerGameWaiting = false;
}
