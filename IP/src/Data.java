import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that stores the input data.
 */
class Data {

    private static String outputFilename;
    private BufferedImage inputImage;
    private BufferedImage[] inputImages;

    /**
     * Constructor that is called when a single file is
     * selected. It reads the file and stores the image.
     * @param input the input file
     * @param output the name of the output file
     */
    public Data(File input, String output) {
        try {
            outputFilename = output;
            inputImage = ImageIO.read(input);
            if(inputImage == null)
                System.out.println("Unable to read file");
            inputImages = new BufferedImage[1];
            inputImages[0] = inputImage;
        } catch(IllegalArgumentException e1) {
            System.out.println("Invalid filename !!!");
        } catch (IOException e2) {
            System.out.println("Error reading file !!!");
        }
    }

    /**
     * Constructor that is called when multiple files are
     * selected. It reads those file and stores the images.
     * @param files the array of input files
     * @param output the name of the output file
     */
    public Data(File[] files, String output) {
        try{
            inputImages = new BufferedImage[files.length];
            outputFilename = output;
            for(int i=0; i<files.length; ++i) {
                inputImages[i] = ImageIO.read(files[i]);
                if(inputImages[i] == null)
                    System.out.println("Unable to read file");
            }
            inputImage = inputImages[0];
            System.out.println(inputImages.length);
        } catch(IllegalArgumentException e1) {
            System.out.println("Invalid filename !!!");
        } catch (IOException e2) {
            System.out.println("Error reading file !!!");
        }
    }

    /**
     * Method to get the input image.
     * @return the stored image
     */
    public BufferedImage getInputImage() { return inputImage; }

    /**
     * Method to get the array of input images.
     * @return the stored set of images
     */
    public BufferedImage[] getInputImages() { return inputImages; }

    /**
     * Prints the properties of input image
     */
    public void printInputImgDetails() { System.out.println(inputImage.toString()); }

    /**
     * Writes an image to a file.
     * @param outputImage the image to be written
     */
    public static void writeImageFile(BufferedImage outputImage) {
        try {
            ImageIO.write(
                    outputImage,
                    outputFilename.substring(outputFilename.lastIndexOf('.')+1),
                    new File(outputFilename)
            );
        } catch (IOException e) {
            System.out.println("Error during write !!!");
        }
    }
}