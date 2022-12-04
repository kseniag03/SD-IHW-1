import java.util.ArrayList;
import java.util.Stack;

/**
 * Class of the game Field realising Reversi rules
 */
public class Field implements Reversi, Game {
    /** The game field */
    private final Cell[] field = new Cell[BOARD_SIZE * BOARD_SIZE];
    /** Possible moves for current state of the board */
    private final ArrayList<Move> possibleMoves = new ArrayList<>();
    /** The stack of moves for undo */
    private final Stack<Move> updates = new Stack<>();
    /** Possible directions to move */
    private final Point[] DIRECTIONS = {
            new Point(0, -1),   // Up
            new Point(-1, -1),  // Up-Left
            new Point(1, -1),   // Up-Right
            new Point(0, 1),    // Down
            new Point(-1, 1),   // Down-Left
            new Point(1, 1),    // Down-Right
            new Point(-1, 0),   // Left
            new Point(1, 0)     // Right
    };
    /**
     * immutable class of move
     * @param destination cell to move
     * @param recolor list of cells to recolor
     */
    private record Move(Point destination, ArrayList<Cell> recolor) {}
    /** Gameplay mode */
    private GameMode mode = GameMode.PLAYER_VS_COMPUTER;
    /** Gameplay difficulty */
    private DifficultyLevel level = DifficultyLevel.NORMAL;
    /** Current turn (true = black, false = white) */
    private boolean isBlackGo = true;
    /** Max score for black disks (during game session) */
    private int maxBlack = 0;
    /** Max score for white disks (during game session) */
    private int maxWhite = 0;

    /**
     * Constructor of the game field
     */
    public Field() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                field[i + BOARD_SIZE * j] = new Cell(new Point(i + 1, j + 1), BOARD_SIZE);
            }
        }
        setStartPosition();
    }

    /**
     * Set start position of reversi game
     */
    private void setStartPosition() {
        int x = BOARD_SIZE / 2;
        int y = BOARD_SIZE / 2 + 1;
        field[(y - 1) + (x - 1) * BOARD_SIZE].setColor((byte) 0);
        field[(x - 1) + (x - 1) * BOARD_SIZE].setColor((byte) 1);
        field[(x - 1) + (y - 1) * BOARD_SIZE].setColor((byte) 0);
        field[(y - 1) + (y - 1) * BOARD_SIZE].setColor((byte) 1);
        isBlackGo = true;
        updatePossibleMoves();
    }

    /**
     * Update possible moves for current state of the board and save cells to repaint
     */
    private void updatePossibleMoves() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (field[i + BOARD_SIZE * j].getColor() == (isBlackGo ? 0 : 1)) {
                    for (Point dir : DIRECTIONS) {
                        Point destination = null;
                        int iShift = i + dir.x();
                        int jShift = j + dir.y();
                        boolean hasChangedDirection = false;
                        ArrayList<Cell> cells = new ArrayList<>();
                        ArrayList<Cell> addCells = new ArrayList<>();
                        while ((iShift >= 0 && iShift < BOARD_SIZE && jShift >= 0 && jShift < BOARD_SIZE)
                                && field[iShift + BOARD_SIZE * jShift].getColor() == (isBlackGo ? 1 : 0)){
                            hasChangedDirection = true;
                            addCells.add(field[iShift + BOARD_SIZE * jShift]);
                            iShift += dir.x();
                            jShift += dir.y();
                        }
                        if (hasChangedDirection && iShift >= 0 && iShift < BOARD_SIZE && jShift >= 0 && jShift < BOARD_SIZE) {
                            if (field[iShift + BOARD_SIZE * jShift].getColor() == 2) {
                                field[iShift + BOARD_SIZE * jShift].setColor((byte) (isBlackGo ? 3 : 4));
                                destination = new Point(iShift + 1, jShift + 1);
                                cells.addAll(addCells);
                            } else if (field[iShift + BOARD_SIZE * jShift].getColor() == (isBlackGo ? 3 : 4)) {
                                for (Move move : possibleMoves) {
                                    if (move.destination.x() == iShift + 1 && move.destination.y() == jShift + 1) {
                                        move.recolor.addAll(addCells);
                                    }
                                }
                            }
                        }
                        if (destination != null) {
                            Move move = new Move(destination, cells);
                            possibleMoves.add(move);
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if there is possible move for the point
     * @param point cell's coordinates
     */
    private boolean isPossibleMove(Point point) {
        return field[point.x() - 1 + BOARD_SIZE * (point.y() - 1)].getColor() == (isBlackGo ? 3 : 4);
    }

    /**
     * Clear possible moves and repaint possible cells to empty
     */
    private void clearPossibleMoves() {
        possibleMoves.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (field[i + BOARD_SIZE * j].getColor() == 3 || field[i + BOARD_SIZE * j].getColor() == 4) {
                    field[i + BOARD_SIZE * j].setColor((byte) 2);
                }
            }
        }
    }

    /**
     * Get the result value of moving (without evaluation of the rival's move)
     * @param move current move
     */
    private double evaluation(Move move) {
        double result = 0;
        for (Cell cell : move.recolor) {
            result += cell.getClosedCellValue();
        }
        result += field[(move.destination.x() - 1) + BOARD_SIZE * (move.destination.y() - 1)].getCellValue();
        return result;
    }

    /**
     * Get the result value of moving (with evaluation of the rival's move)
     * @param move current move
     */
    private double smartEvaluation(Move move) {
        double result = evaluation(move);
        if (isPossibleMove(move.destination)) {
            move(move.destination);
            double maxRivalResult = 0;
            for (Move rivalMove: possibleMoves) {
                double rivalResult = evaluation(rivalMove);
                if (rivalResult > maxRivalResult) {
                    maxRivalResult = rivalResult;
                }
            }
            cancelMove();
            result -= maxRivalResult;
        }
        return result;
    }

    @Override
    public GameMode getGameMode() {
        return mode;
    }

    @Override
    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }

    @Override
    public void setDifficultyLevel(DifficultyLevel level) {
        this.level = level;
    }

    @Override
    public void resetField() {
        updates.clear();
        possibleMoves.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                field[i + BOARD_SIZE * j].setColor((byte) 2);
            }
        }
        setStartPosition();
    }

    @Override
    public void printField() {
        for (int i = 0; i <= BOARD_SIZE; i++) {
            System.out.print((i < BOARD_SIZE) ? (i + 1) : " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (i == BOARD_SIZE) {
                    System.out.print("  " + (j + 1) + " ");
                    continue;
                }
                System.out.print("|" + (field[j + BOARD_SIZE * i]));
            }
            if (i < BOARD_SIZE) {
                System.out.println();
                System.out.print(" ");
                for (int j = 0; j < BOARD_SIZE * 4; j++) {
                    System.out.print("–");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void printPossibleMoves() {
        System.out.println("Possible moves:");
        for (Move move: possibleMoves) {
            System.out.println(move.destination.x() + " " + move.destination.y());
        }
    }

    @Override
    public void printResults() {
        int black = 0;
        int white = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (field[i + BOARD_SIZE * j].getColor() == 0) {
                    black++;
                } else if (field[i + BOARD_SIZE * j].getColor() == 1) {
                    white++;
                }
            }
        }
        System.out.println("Black score: " + black);
        System.out.println("White score: " + white);
        if (black > maxBlack) {
            maxBlack = black;
        }
        if (white > maxWhite) {
            maxWhite = white;
        }
        System.out.println("Black max score: " + maxBlack);
        System.out.println("White max score: " + maxWhite);
    }

    @Override
    public void move() {
        double maxResult = (level == DifficultyLevel.NORMAL) ? 0 : -1e9;
        Point maxPoint = null;
        ArrayList<Move> possibleMovesCopy = new ArrayList<>(possibleMoves);
        for (Move move: possibleMovesCopy) {
            double result;
            if (level == DifficultyLevel.NORMAL) {
                result = evaluation(move);
            } else {
                result = smartEvaluation(move);
            }
            if (result > maxResult) {
                maxResult = result;
                maxPoint = move.destination;
            }
        }
        if (maxPoint != null) {
            System.out.println("AI moved: " + maxPoint.x() + " " + maxPoint.y());
            move(maxPoint);
        } else {
            System.out.println("No possible moves");
            isBlackGo = !isBlackGo;
            clearPossibleMoves();
            updatePossibleMoves();
        }
    }

    @Override
    public void move(Point point) {
        if (isPossibleMove(point)) {
            field[(point.x() - 1) + BOARD_SIZE * (point.y() - 1)].setColor((byte) (isBlackGo ? 0 : 1));
            for (Move move : possibleMoves) {
                if (move.destination.x() == point.x() && move.destination.y() == point.y()) {
                    for (Cell cell : move.recolor) {
                        //System.out.println("repainted cell: " + cell.getPoint().x() + " " + cell.getPoint().y());
                        cell.setColor((byte) (isBlackGo ? 0 : 1));
                    }
                    System.out.println("moved: (" + move.destination.x() + " " + move.destination.y() + ") ");
                    updates.push(move);
                    break;
                }
            }
            isBlackGo = !isBlackGo;
            clearPossibleMoves();
            updatePossibleMoves();
        } else {
            System.out.println("Impossible move!");
        }
    }

    @Override
    public void cancelMove() {
        if (updates.size() > 0) {
            for (int i = 0; i < ((mode == GameMode.PLAYER_VS_COMPUTER) ? 2 : 1); i++) {
                Move move = updates.pop();
                System.out.println("canceled move: (" + move.destination.x() + " " + move.destination.y() + ") ");
                field[(move.destination.x() - 1) + BOARD_SIZE * (move.destination.y() - 1)].setColor((byte) 2);
                for (Cell cell : move.recolor) {
                    cell.setColor((byte) (isBlackGo ? 0 : 1));
                }
                isBlackGo = !isBlackGo;
            }
            clearPossibleMoves();
            updatePossibleMoves();
        } else {
            System.out.println("No moves to cancel!");
        }
    }

    @Override
    public boolean getTurn() {
        return isBlackGo;
    }

    @Override
    public boolean hasGameEnded() {
        if (possibleMoves.size() > 0) {
            return false;
        } else {
            isBlackGo = !isBlackGo;
            clearPossibleMoves();
            updatePossibleMoves();
            if (possibleMoves.size() > 0) {
                isBlackGo = !isBlackGo;
                clearPossibleMoves();
                updatePossibleMoves();
                return false;
            } else {
                return true;
            }
        }
    }
}
