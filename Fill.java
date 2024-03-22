import java.awt.Color;

public class Fill {
    public void floodFill(int[][] screen, int x, int y, Color originalColor, Color newColor) {
        int m = screen.length;
        int n = screen[0].length;

        if (x < 0 || x >= m || y < 0 || y >= n || screen[x][y] != originalColor.getRGB() || originalColor.getRGB() == newColor.getRGB()) {
            return;
        }

            screen[x][y] = newColor.getRGB();

            floodFill(screen, x + 1, y, originalColor, newColor);
            floodFill(screen, x - 1, y, originalColor, newColor);
            floodFill(screen, x, y + 1, originalColor, newColor);
            floodFill(screen, x, y - 1, originalColor, newColor);
    }
}
