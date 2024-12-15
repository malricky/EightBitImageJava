import java.awt.image.BufferedImage;
import java.io.IOException;

public class EightBitImage implements Runnable {

    private BufferedImage image;
    private byte[] rgb_matrix;
    private int[] pixelMatrix;
    private int img_width, img_height;
    private static int threadIdx = 0;
    private int num_threads;
    private int size;

    public EightBitImage(BufferedImage image, int img_width, int img_height, byte[] rgb_matrix, int[] pixelMatrix, int num_threads, int size) throws IOException {
        this.image = image;
        this.img_width = img_width;
        this.img_height = img_height;
        this.rgb_matrix = rgb_matrix;
        this.pixelMatrix = pixelMatrix;
        this.num_threads = num_threads;
        this.size = size;
    }

    private void generateMatrix(int start, int size) {
        for (int i = start; i < start + size; i++) {
            if (i >= img_width * img_height) break;

            int row = i / img_width;
            int col = i % img_width;

            int pixel = this.image.getRGB(col, row);
            int index = row * this.img_width + col;

            this.pixelMatrix[index] = pixel;

            this.rgb_matrix[index * 3] = (byte) (pixel >> 16);
            this.rgb_matrix[index * 3 + 1] = (byte) (pixel >> 8 & 0xFF);
            this.rgb_matrix[index * 3 + 2] = (byte) (pixel & 0xFF);
        }
    }

    @Override
    public void run() {
        int idx = threadIdx++;

        int currentPos = size * idx;

        generateMatrix(currentPos, size);
    }

    public byte[] getRGBMatrix() {
        return this.rgb_matrix;
    }

    public int[] getPixelMatrix() {
        return this.pixelMatrix;
    }
}
