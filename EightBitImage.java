import java.awt.image.BufferedImage;
import java.io.IOException;

public class EightBitImage implements Runnable {

    private final BufferedImage image;
    private final byte[] rgb_matrix;
    private final int img_width, img_height;
    private static int threadIdx = 0;
    private final int size;

    public EightBitImage(BufferedImage image, int img_width, int img_height, byte[] rgb_matrix, int size) throws IOException {
        this.image = image;
        this.img_width = img_width;
        this.img_height = img_height;
        this.rgb_matrix = rgb_matrix;
        this.size = size;
    }

    private void generateMatrix(int start, int size) {
        for (int i = start; i < start + size; i++) {
            if (i >= img_width * img_height) break;

            int row = i / img_width;
            int col = i % img_width;

            int pixel = this.image.getRGB(col, row);
            int index = row * this.img_width + col;

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
}
