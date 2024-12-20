import java.awt.image.BufferedImage;
import java.io.IOException;

public class EightBitImage implements Runnable {

    private final BufferedImage image;
    private final byte[] rgb_matrix;
    private final int img_width, img_height;
    private static int threadIdx = 0;
    private final int size;
    private int device_id;

    static {
        System.loadLibrary("image_processor");
    }

    private native void getRGBMatrix(byte[] rgbMatrix,int[] pixelMatrix,int size,int device_id);

    public EightBitImage(BufferedImage image, int img_width, int img_height, byte[] rgb_matrix, int size,int device_id) throws IOException {
        this.image = image;
        this.img_width = img_width;
        this.img_height = img_height;
        this.rgb_matrix = rgb_matrix;
        this.size = size;
        this.device_id = device_id;
    }

    private void generateMatrix(int start, int size) {
        int pixelMatrix[] = new int[size];
        byte tmp_rgb_matrix[] = new byte[size * 3];
        
        for (int i = start, j = 0; i < start + size; i++, j++) {
            if (i >= img_width * img_height) break;

            int row = i / img_width;
            int col = i % img_width;

            pixelMatrix[j] = this.image.getRGB(col, row);
        }

        getRGBMatrix(tmp_rgb_matrix, pixelMatrix, size, device_id);

        for (int i = 0; i < size; i++) {
            int index = start + i;
            if (index >= img_width * img_height) break;

            this.rgb_matrix[index * 3] = tmp_rgb_matrix[i * 3];      
            this.rgb_matrix[index * 3 + 1] = tmp_rgb_matrix[i * 3 + 1]; 
            this.rgb_matrix[index * 3 + 2] = tmp_rgb_matrix[i * 3 + 2]; 
        }
    }


    @Override
    public void run() {
        int idx = threadIdx++;

        int currentPos = size * idx;

        generateMatrix(currentPos, size);
    }
}
