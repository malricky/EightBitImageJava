import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EightBitImage implements Runnable{

    private BufferedImage image;
    private byte rgb_matrix[][][];
    private int img_width, img_height;
    private int pixelMatrix[][];
    private static int threadIdx = 0;
    private int num_threads;
    private int size;

    public EightBitImage(BufferedImage image,int img_width, int img_height,byte rgb_matrix[][][], int num_threads, int size) throws IOException{
        this.image = image;
        this.img_width = img_width;
        this.img_height = img_height;
        this.rgb_matrix = rgb_matrix;
        this.pixelMatrix = new int[img_width][img_height];
        this.num_threads = num_threads;
        this.size = size;
    }

    private void generatePixelMatrix(int r,int c,int size){
        
        for(int row = r;(row < this.img_width) && (size > 0);row++){
            for(int col = c;(col< this.img_height) && (size > 0);col++,size--){
                this.pixelMatrix[row][col] = this.image.getRGB(row,col);
            }
        }
    }

    private int[][] getPixelMatrix(){
        return this.pixelMatrix;
    }

    private void generateRGBMatrix(int r,int c,int size){

        for(int row = r;(row < this.img_width) && (size > 0);row++){
            for(int col = c;(col < this.img_height) && (size > 0);col++,size--){
                this.rgb_matrix[row][col][0] = (byte) (this.pixelMatrix[row][col] >> 16);
                this.rgb_matrix[row][col][1] = (byte) (this.pixelMatrix[row][col] >> 8 & 0xFF);
                this.rgb_matrix[row][col][2] = (byte) (this.pixelMatrix[row][col] & 0xFF);
            }
        }
    }

    private byte[][][] getRGBMatrix(){
        return this.rgb_matrix;
    }

    @Override
    public void run(){

        int idx = threadIdx++;

        int currentPos = size*idx;
        int row = (currentPos / this.img_width);
        int col = (currentPos % this.img_width);

        generatePixelMatrix(row,col,size);
        generateRGBMatrix(row,col,size);

    }
    
}
