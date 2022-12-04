/**
 * The functionality of the console game
 */
public interface Game {
    /** The game field bounds (square board), min size = 2 (for working game...) */
    int BOARD_SIZE = 8;

    /** Gameplay mode (default = PLAYER_VS_COMPUTER) */
    enum GameMode {
        PLAYER_VS_COMPUTER,
        PLAYER_VS_PLAYER,
        COMPUTER_VS_COMPUTER
    }

    /** Gameplay difficulty (default = NORMAL) */
    enum DifficultyLevel {
        NORMAL,
        ADVANCED
    }

    /** Get gameplay mode */
    GameMode getGameMode();

    /** Set gameplay mode */
    void setGameMode(GameMode mode);

    /** Set gameplay difficulty */
    void setDifficultyLevel(DifficultyLevel level);

    /** Reset the game field (to start new game) */
    void resetField();

    /** Print current state of the game field */
    void printField();

    /** Print current results of the game party */
    void printResults();
}
