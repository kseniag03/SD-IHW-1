public class Disk {
    private byte color; // 0 - black, 1 - white, 2 - empty, ○ ● □ ■ - x, 3 - black possible move, 4 - white possible move
    private final Point point;

    private boolean isCorner = false;
    private boolean isEdge = false;

    boolean isSelected;

    public Disk(Point point) {
        color = 2;
        this.point = point;
        isCorner = point.x() == 1 && point.y() == 1
                || point.x() == 1 && point.y() == Field.BOARD_SIZE
                || point.x() == Field.BOARD_SIZE && point.y() == 1
                || point.x() == Field.BOARD_SIZE && point.y() == Field.BOARD_SIZE;
        isEdge = point.x() == 1 || point.x() == Field.BOARD_SIZE || point.y() == 1 || point.y() == Field.BOARD_SIZE;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public byte getColor() {
        return color;
    }

    public Point getPoint() {
        return point;
    }

    public double getClosedCellValue() {
        if (isCorner) {
            return 0;
        } else if (isEdge) {
            return 2;
        } else {
            return 1;
        }
    }

    public double getCellValue() {
        if (isCorner) {
            return 0.8;
        } else if (isEdge) {
            return 0.4;
        } else {
            return 0;
        }
    }

    public void setColor(byte color) {
        this.color = color;
    }

    @Override
    public String toString() {

        return switch (color) {
            case 0 -> " ○ ";
            case 1 -> " ● ";
            case 2 -> "   ";
            case 3 -> " □ ";
            case 4 -> " ■ ";
            default -> " x";
        };

        // return "Cell{" + "color=" + color + ", point=" + point + '}';
    }

    private void evaluation(Point point) { // not evaluation...
        /*
        if (point.getX() == 1 || point.getX() == 8) {
            if (point.getY() == 1 || point.getY() == 8) {
                isCorner = true;
            } else {
                isEdge = true;
            }
        } else if (point.getY() == 1 || point.getY() == 8) {
            isEdge = true;
        }
        */

    }
}
