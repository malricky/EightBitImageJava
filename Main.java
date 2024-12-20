import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String args[]) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        String img_path = "images/200MP.jpg";
        int num_threads = Runtime.getRuntime().availableProcessors();
        int device_id = 1;

        if (args.length > 0 && args.length % 2 == 0) {
            for (int i = 0; i < args.length; i += 2) {
                switch (args[i]) {
                    case "-i":
                        img_path = args[i + 1];
                        break;
                    case "-t":
                        num_threads = Integer.parseInt(args[i + 1]);
                        break;
                    case "-d":
                        device_id = Integer.parseInt(args[i + 1]);
                        break;
                    default:
                        break;
                }
            }
        }

        File img_file = new File(img_path);
        BufferedImage image = ImageIO.read(img_file);
        int img_width = image.getWidth();
        int img_height = image.getHeight();

        byte[] rgb_matrix = new byte[img_width * img_height * 3];
        int size = (img_width * img_height) / num_threads;

        Runnable task = new EightBitImage(image, img_width, img_height, rgb_matrix, size,device_id);

        ExecutorService executor = Executors.newFixedThreadPool(num_threads);

        for (int i = 0; i < num_threads; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        System.out.println("Pixel (0,0): (R,G,B) = ("+(rgb_matrix[0] & 0xFF) + "," + (rgb_matrix[1] & 0xFF) + "," + (rgb_matrix[2] & 0xFF)+")");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Total execution time: " + duration + " ms");
    }
}
