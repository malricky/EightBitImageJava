import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main{
    public static void main(String args[]) throws IOException, InterruptedException{
        long startTime = System.currentTimeMillis();

        String img_path = "images/5MP.jpg";
        int num_threads = Runtime.getRuntime().availableProcessors();

        if(args.length > 0 && args.length%2 == 0){
            for(int i=0;i<args.length;i+=2){
                if(args[i].equals("-i")){
                    img_path = args[i+1];
                }
                else if(args[i].equals("-t")){
                    num_threads = Integer.parseInt(args[i+1]);
                }
            }
        }

        File img_file = new File(img_path);
        BufferedImage image = ImageIO.read(img_file);
        int img_width = image.getWidth();
        int img_height = image.getHeight();
        byte matrix[][][] = new byte[img_width][img_height][3];
        int size = (img_width*img_height)/num_threads;

        Runnable task = new EightBitImage(image,img_width,img_height,matrix,num_threads,size);
        Thread threads[] = new Thread[num_threads];

        for(int i=0;i<num_threads;i++){
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for(int i=0;i<num_threads;i++){
            threads[i].join();
        }

        System.out.println((matrix[0][0][0] & 0xFF)+";"+(matrix[0][0][1] & 0xFF)+";"+(matrix[0][0][2] & 0xFF));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Execution time: " + duration+" ms");
    }
}