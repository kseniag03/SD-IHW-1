/**
 * Class of the field cell
 */
public class Cell {
    /** Flag if cell is located in the corner of the field */
    private final boolean isCorner;
    /** Flag if cell is located on the side of the field */
    private final boolean isEdge;
    /**
     * Cell color:
     * 0 - black, 1 - white,
     * 2 - empty,
     * 3 - black-possible-move, 4 - white-possible-move
     * */
    private byte color;

    /**
     * Constructor of the cell
     * @param point coordinates of the cell
     * @param maxSize max size of the game board
     */
    public Cell(Point point, int maxSize) {
        color = 2;
        isCorner = point.x() == 1 && point.y() == 1
                || point.x() == 1 && point.y() == maxSize
                || point.x() == maxSize && point.y() == 1
                || point.x() == maxSize && point.y() == maxSize;
        isEdge = point.x() == 1 || point.x() == maxSize || point.y() == 1 || point.y() == maxSize;
    }

    /**
     * Get cell's color
     * @return cell's color
     */
    public byte getColor() {
        return color;
    }

    /**
     * Get closed cell's value (calculations are based on the cell's location)
     * @return value of current closed cell
     */
    public double getClosedCellValue() {
        if (isCorner) {
            return 0;
        } else if (isEdge) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Get cell's value (calculations are based on the cell's location)
     * @return value of current cell
     */
    public double getCellValue() {
        if (isCorner) {
            return 0.8;
        } else if (isEdge) {
            return 0.4;
        } else {
            return 0;
        }
    }

    /**
     * Repaint the cell
     * @param color new color
     */
    public void setColor(byte color) {
        this.color = color;
    }

    /**
     * Override toString method
     * @return string representation of the cell
     */
    @Override
    public String toString() {
        return switch (color) {
            case 0 -> " ○ ";
            case 1 -> " ● ";
            case 3 -> " □ ";
            case 4 -> " ■ ";
            default -> "   ";
        };
    }
}
