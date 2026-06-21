package mrg.speedometer.util;

public enum UIRelateTypes {
    LEFT_UP(0, 0),
    LEFT_CENTER(0, 1),
    LEFT_DOWN(0, 2),
    CENTER_UP(1, 0),
    CENTER_CENTER(1, 1),
    CENTER_DOWN(1, 2),
    RIGHT_UP(2, 0),
    RIGHT_CENTER(2, 1),
    RIGHT_DOWN(2, 2);

    private UIRelateTypes(int h, int v) {
        horizontal = h;
        vertical = v;
    }

    private final int horizontal;
    private final int vertical;

    public int getVertical() { return vertical; }
    public int getHorizontal() { return horizontal; }
}
