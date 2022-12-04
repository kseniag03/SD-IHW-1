import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Main class
 */
public class Main {
    /**
     * Get the input from the user
     */
    private static Point getPoint() {
        System.out.print("Enter the position to move (e.g. '1 2'), 'z' to cancel last move or 'b' to return to menu: ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input = reader.readLine();
            if (input == null) {
                return new Point(-1, -1);
            }
            String[] line = input.split(" ");
            if (line.length == 2) {
                int x = Integer.parseInt(line[0]);
                int y = Integer.parseInt(line[1]);
                if (x <= 0 || y <= 0 || x > Game.BOARD_SIZE || y > Game.BOARD_SIZE) {
                    System.out.println("Position is out of board!");
                    return null;
                }
                return new Point(x, y);
            } else if (line.length == 1 && line[0].equals("z")) {
                return new Point(0, 0);
            } else if (line.length == 1 && line[0].equals("b")) {
                return new Point(-1, -1);
            } else {
                System.out.println("Wrong input format!");
                return null;
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Starting the game (begin new or continue)
     * @param field game field (reversi)
     */
    private static void executeGame(Reversi field) {
        while (!field.hasGameEnded()) {
            ((Game) field).printField();
            field.printPossibleMoves();
            if (((Game) field).getGameMode() == Field.GameMode.PLAYER_VS_COMPUTER && field.getTurn()
                    || ((Game) field).getGameMode() == Field.GameMode.PLAYER_VS_PLAYER) {
                var point = getPoint();
                if (point != null) {
                    if (point.x() == 0 && point.y() == 0) {
                        field.cancelMove();
                        continue;
                    } else if (point.x() == -1 && point.y() == -1) {
                        break;
                    }
                    field.move(point);
                }
            } else {
                field.move();
            }
        }
    }

    /**
     * Create options in main menu
     * @param mainMenu menu to connect options
     * @param field game field (reversi)
     */
    private static void createReversiMenu(Menu mainMenu, Reversi field) {
        mainMenu.addMenu(new MenuTemplate("Start the game") {
            @Override
            public void run() {
                ((Game) field).resetField();
                executeGame(field);
            }
        });
        mainMenu.addMenu(new MenuTemplate("Continue the game") {
            @Override
            public void run() {
                if (field.hasGameEnded()) {
                    System.out.println("The party is over. You can start a new one");
                } else {
                    executeGame(field);
                }
            }
        });
        mainMenu.addMenu(new MenuTemplate("Show the field state") {
            @Override
            public void run() {
                ((Game) field).printField();
            }
        });
        mainMenu.addMenu(new MenuTemplate("Show results") {
            @Override
            public void run() {
                ((Game) field).printResults();
            }
        });
    }

    /**
     * Adding settings to main menu
     * @param mainMenu menu to connect settings
     * @param field game field
     */
    private static void setupReversiSettings(Menu mainMenu, Game field) {
        mainMenu.addMenu(new MenuTemplate("Choose the game mode") {
            @Override
            public void run() {
                Menu gameModeMenu = new Menu("", false);
                gameModeMenu.addMenu(new MenuTemplate("Player vs Computer") {
                    @Override
                    public void run() {
                        field.setGameMode(Field.GameMode.PLAYER_VS_COMPUTER);
                    }
                });
                gameModeMenu.addMenu(new MenuTemplate("Player vs Player") {
                    @Override
                    public void run() {
                        field.setGameMode(Field.GameMode.PLAYER_VS_PLAYER);
                    }
                });
                gameModeMenu.addMenu(new MenuTemplate("Computer vs Computer") {
                    @Override
                    public void run() {
                        field.setGameMode(Field.GameMode.COMPUTER_VS_COMPUTER);
                    }
                });
                gameModeMenu.run();
            }
        });
        mainMenu.addMenu(new MenuTemplate("Choose the difficulty level") {
            @Override
            public void run() {
                Menu difficultyLevelMenu = new Menu("", false);
                difficultyLevelMenu.addMenu(new MenuTemplate("Normal") {
                    @Override
                    public void run() {
                        field.setDifficultyLevel(Field.DifficultyLevel.NORMAL);
                    }
                });
                difficultyLevelMenu.addMenu(new MenuTemplate("Advanced") {
                    @Override
                    public void run() {
                        field.setDifficultyLevel(Field.DifficultyLevel.ADVANCED);
                    }
                });
                difficultyLevelMenu.run();
            }
        });
    }

    /**
     * Main function
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Field field = new Field();
        System.out.println("Welcome to reversi game!\n");
        Menu mainMenu = new Menu("\nSee you again, have a nice day!", true);
        createReversiMenu(mainMenu, field);
        setupReversiSettings(mainMenu, field);
        mainMenu.run();
    }
}
