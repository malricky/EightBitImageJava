import java.awt.image.BufferedImage;
import java.io.IOException;

public class EightBitImage implements Runnable {

    private final BufferedImage image;
    private final byte[] rgb_matrix;
    private final int img_width, img_height;
    private static int threadIdx = 0;
    private static float progress = 20.0f;
    private final int size;
    private final int device_id;
    private final float contrib;

    static {
        System.loadLibrary("image_processor");
    }

    private native void getRGBMatrix(byte[] rgbMatrix,int[] pixelMatrix,int size,int device_id);

    public EightBitImage(BufferedImage image, int img_width, int img_height, byte[] rgb_matrix, int size,int device_id,float contrib) throws IOException {
        this.image = image;
        this.img_width = img_width;
        this.img_height = img_height;
        this.rgb_matrix = rgb_matrix;
        this.size = size;
        this.device_id = device_id;
        this.contrib = contrib;
    }

    private synchronized void updateProgress(){
        System.out.print("\rProgress: " + Math.min(EightBitImage.progress, 100) + "% Complete [Extracting RGB From Image]");
        System.out.flush(); 
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

        EightBitImage.progress += Math.ceil(contrib/3);
        updateProgress();

        getRGBMatrix(tmp_rgb_matrix, pixelMatrix, size, device_id);

        EightBitImage.progress += Math.ceil(contrib/3);
        updateProgress();

        for (int i = 0; i < size; i++) {
            int index = start + i;
            if (index >= img_width * img_height) break;

            this.rgb_matrix[index * 3] = tmp_rgb_matrix[i * 3];      
            this.rgb_matrix[index * 3 + 1] = tmp_rgb_matrix[i * 3 + 1]; 
            this.rgb_matrix[index * 3 + 2] = tmp_rgb_matrix[i * 3 + 2]; 
        }

        EightBitImage.progress += Math.ceil(contrib/3);
        updateProgress();
    }

    @Override
    public void run() {
        int idx = threadIdx++;

        int currentPos = size * idx;

        generateMatrix(currentPos, size);
    }
}
