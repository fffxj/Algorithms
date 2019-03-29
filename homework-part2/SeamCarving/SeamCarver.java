import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final double BORDER_ENERGY = 1000;
    private Picture picture;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(this.picture);
    }

    // width of current picture
    public int width() {
        return this.width;
    }
    
    // height of current picture
    public int height() {
        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1)
            throw new IllegalArgumentException();
        if (y < 0 || y > height - 1)
            throw new IllegalArgumentException();

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return BORDER_ENERGY;
        }

        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);

        int xR = left.getRed() - right.getRed();
        int xG = left.getGreen() - right.getGreen();
        int xB = left.getBlue() - right.getBlue();
        int xGradSquare = xR * xR + xG * xG + xB * xB;

        Color up = picture.get(x, y - 1);
        Color down = picture.get(x, y + 1);
        int yR = up.getRed() - down.getRed();
        int yG = up.getGreen() - down.getGreen();
        int yB = up.getBlue() - down.getBlue();
        int yGradSquare = yR * yR + yG * yG + yB * yB;

        return Math.sqrt(xGradSquare + yGradSquare);
    }

    // find vertical seam given by the energy matrix
    private int[] findVerticalSeam(double[][] energy, int h, int w) {
        int[] seam = new int[h];
        int[][] vertexTo = new int[h][w];
        double[][] energyTo = new double[h][w];

        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                energyTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int j = 0; j < w; ++j) {
            energyTo[0][j] = BORDER_ENERGY;
        }

        for (int i = 0; i < h - 1; ++i) {
            for (int j = 1; j < w - 1; ++j) {
                if (energyTo[i+1][j-1] > energyTo[i][j] + energy[i+1][j-1]) {
                    energyTo[i+1][j-1] = energyTo[i][j] + energy[i+1][j-1];
                    vertexTo[i+1][j-1] = j;
                }
                if (energyTo[i+1][j] > energyTo[i][j] + energy[i+1][j]) {
                    energyTo[i+1][j] = energyTo[i][j] + energy[i+1][j];
                    vertexTo[i+1][j] = j;
                }
                if (energyTo[i+1][j+1] > energyTo[i][j] + energy[i+1][j+1]) {
                    energyTo[i+1][j+1] = energyTo[i][j] + energy[i+1][j+1];
                    vertexTo[i+1][j+1] = j;
                }
            }
        }

        double dist = Double.POSITIVE_INFINITY;
        int j = 0;
        for (int x = 0; x < w; ++x) {
            if (energyTo[h-1][x] < dist) {
                dist = energyTo[h-1][x];
                j = x;
            }
        }
            
        for (int i = h - 1; i >= 0; --i) {
            seam[i] = j;
            j = vertexTo[i][j];
        }

        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] energy = new double[width][height];
        for (int y = 0; y < width; ++y) {
            for (int x = 0; x < height; ++x) {
                energy[y][x] = energy(y, x);
            }
        }
        return findVerticalSeam(energy, width, height);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                energy[y][x] = energy(x, y);
            }
        }
        return findVerticalSeam(energy, height, width);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != width)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; ++i) {
            if (seam[i] < 0 || seam[i] > height - 1)
                throw new IllegalArgumentException();
            if (i >= 1 && Math.abs(seam[i] - seam[i-1]) > 1)
                throw new IllegalArgumentException();
        }
        if (height <= 1)
            throw new IllegalArgumentException();

        Picture newPicture = new Picture(width, height - 1);
        for (int j = 0; j < width; ++j) {
            for (int i = 0; i < height; ++i) {
                if (i < seam[j])
                    newPicture.set(j, i, picture.get(j, i));
                else if (i > seam[j])
                    newPicture.set(j, i - 1, picture.get(j, i));
            }
        }
        picture = newPicture;
        height = picture.height();
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != height)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; ++i) {
            if (seam[i] < 0 || seam[i] > width - 1)
                throw new IllegalArgumentException();
            if (i >= 1 && Math.abs(seam[i] - seam[i-1]) > 1)
                throw new IllegalArgumentException();
        }
        if (width <= 1)
            throw new IllegalArgumentException();

        Picture newPicture = new Picture(width - 1, height);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (j < seam[i])
                    newPicture.set(j, i, picture.get(j, i));
                else if (j > seam[i])
                    newPicture.set(j - 1, i, picture.get(j, i));
            }
        }
        picture = newPicture;
        width = picture.width();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // test code
    }
}
