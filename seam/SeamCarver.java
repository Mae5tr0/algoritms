import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Topological;

import java.util.Arrays;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.IllegalArgumentException();
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width()) {
            throw new java.lang.IllegalArgumentException();
        }
        if (y < 0 || y >= height()) {
            throw new java.lang.IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }

        int prevColorX = picture.getRGB(x - 1, y);
        int nextColorX = picture.getRGB(x + 1, y);
        int prevColorY = picture.getRGB(x, y - 1);
        int nextColorY = picture.getRGB(x, y + 1);

        return Math.sqrt(delta(prevColorX, nextColorX) + delta(prevColorY, nextColorY));
    }

    private int delta(int prevColor, int nextColor) {
        int rDiff = ((prevColor >> 16) & 0xFF) - ((nextColor >> 16) & 0xFF);
        int gDiff = ((prevColor >> 8) & 0xFF) - ((nextColor >> 8) & 0xFF);
        int bDiff = ((prevColor >> 0) & 0xFF) - ((nextColor >> 0) & 0xFF);

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        Digraph digraph = digraphFromImage();

        // init helper arrays
        double[][] energy = calcEnergy();
        double distTo[][] = new double[width()][height()];
        int edgeTo[][] = new int[width()][height()];

        for (int i = 0; i < width(); i++) {
            Arrays.fill(distTo[i], Double.MAX_VALUE);
            distTo[i][0] = 0.0;
        }

        // calculate minimum path
        Topological topological = new Topological(digraph);

        for (int v : topological.order()) {
            int vertexX = xFromIndex(v);
            int vertexY = yFromIndex(v);

            for (int adjacentVertex : digraph.adj(v)) {
                int adjacentVertexX = xFromIndex(adjacentVertex);
                int adjacentVertexY = yFromIndex(adjacentVertex);

                double energyForAdjacentVertex = distTo[vertexX][vertexY] + energy[adjacentVertexX][adjacentVertexY];
                if (distTo[adjacentVertexX][adjacentVertexY] >= energyForAdjacentVertex) { // ??? incorrect topologic order
                    distTo[adjacentVertexX][adjacentVertexY] = energyForAdjacentVertex;
                    edgeTo[adjacentVertexX][adjacentVertexY] = v;
                }
            }
        }

        // find minimum path
        int position = 0;
        double minValue = Integer.MAX_VALUE;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < minValue) {
                minValue = distTo[i][height() - 1];
                position = i;
            }
        }

        // build seam
        int[] result = new int[height()];
        result[height() - 1] = position;
        int prevEdge = edgeTo[position][height() - 1];

        for (int i = height() - 2; i >= 0; i--) {
            int prevEdgeX = xFromIndex(prevEdge);
            int prevEdgeY = yFromIndex(prevEdge);

            result[i] = prevEdgeX;

            prevEdge = edgeTo[prevEdgeX][prevEdgeY];
        }

        return result;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposePicture();
//        }
        int[] result = findVerticalSeam();
        transposePicture();

        return result;
    }

    private void transposePicture() {
        Picture transposedPicture = new Picture(height(), width());

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                transposedPicture.setRGB(y, x, picture.getRGB(x, y));
            }
        }

        picture = transposedPicture;
    }

    private double[][] calcEnergy() {
        double[][] energy = new double[width()][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }

        return energy;
    }

    private Digraph digraphFromImage() {
        Digraph digraph = new Digraph(width() * height());

        // skip last line for y
        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                //right bottom
                if (x != (width() - 1)) {
                    digraph.addEdge(indexForXY(x, y), indexForXY(x + 1, y + 1));
                }
                //bottom
                digraph.addEdge(indexForXY(x, y), indexForXY(x, y + 1));
                //left bottom
                if (x != 0) {
                    digraph.addEdge(indexForXY(x, y), indexForXY(x - 1, y + 1));
                }
            }
        }

        return digraph;
    }

    private int indexForXY(int x, int y) {
        return y * width() + x;
    }

    private int xFromIndex(int index) {
        return index % width();
    }

    private int yFromIndex(int index) {
        return index / width();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (width() <= 1) {
            throw new java.lang.IllegalArgumentException();
        }
        validateSeam(seam);

        Picture truncatedPicture = new Picture(width() - 1, height());

        for (int y = 0; y < height(); y++) {
            int truncatedPictureX = 0;
            for (int x = 0; x < picture.width(); x++) {
                if (seam[y] == x) {
                    continue;
                }
                truncatedPicture.setRGB(truncatedPictureX, y, picture.getRGB(x, y));
                truncatedPictureX++;
            }
        }

        picture = truncatedPicture;
    }

    private void validateSeam(int[] seam) {
        if (seam.length != height()) {
            throw new java.lang.IllegalArgumentException();
        }
        int prevEntry = -1;
        for (int entry : seam) {
            if (entry < 0 || entry >= width()) {
                throw new java.lang.IllegalArgumentException("Entry is outside range");
            }
            if (prevEntry > -1 && Math.abs(prevEntry - entry) > 1) {
                throw new java.lang.IllegalArgumentException("Invalid distance");
            }
            prevEntry = entry;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) {
            throw new java.lang.IllegalArgumentException();
        }

        transposePicture();
        removeVerticalSeam(seam);
        transposePicture();
    }
}