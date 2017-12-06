import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class ImageProcessor {

    private String outputFilename;
    private BufferedImage inputImage, outputImage;
    private AffineTransform transform;
    private Graphics2D outputGraphics;

    private void instantiateOutputImage(int width, int height) {
        outputImage = new BufferedImage (
                            width,
                            height,
                            inputImage.getType());
        outputGraphics = outputImage.createGraphics();
    }

    public ImageProcessor(String input, String output) {
        transform = new AffineTransform();
        try {
            inputImage = ImageIO.read(new File(input));
            if(inputImage == null)
                System.out.println("Unable to read file");
            outputFilename = output;
        } catch(IllegalArgumentException e1) {
            System.out.println("Invalid filename !!!");
        } catch (IOException e2) {
            System.out.println("Error reading file !!!");
        }
    }

    public void zoom(double sx, double sy) {
        instantiateOutputImage (
                    (int)(inputImage.getWidth() * sx),
                    (int)(inputImage.getHeight() * sy));

        transform.scale(sx, sy);
        outputGraphics.drawImage(inputImage, transform, null);

        try {
            ImageIO.write(
                outputImage,
                outputFilename.substring(outputFilename.lastIndexOf('.')+1),
                new File(outputFilename));
        } catch (IOException e) {
            System.out.println("Error during write !!!");
        }
    }

    public void shrink(double sx, double sy) {
        instantiateOutputImage (
                (int)(inputImage.getWidth() / sx),
                (int)(inputImage.getHeight() / sy)
        );

        transform.scale(1/sx, 1/sy);
        outputGraphics.drawImage(inputImage, transform, null);

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

    public void smooth() {
        outputImage = new BufferedImage(
                inputImage.getWidth(),
                inputImage.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );

        int k=3;

        WritableRaster inputRaster = inputImage.getRaster();
        WritableRaster outputRaster = outputImage.getRaster();

//        System.out.println(inputRaster.getPixel(0,0,temp)[0]);

        for(int i=0; i<inputImage.getWidth(); ++i) {
            for (int j = 0; j < inputImage.getHeight(); ++j) {

//                for(int c=0; c<pixel.length; ++c)
//                    pixel[c] = 0;
                double[] pixel = new double[outputImage.getColorModel().getNumColorComponents()];

                for (int p = i - k / 2; p <= i + k / 2; ++p) {
                    for (int q = j - k / 2; q <= j + k / 2; ++q) {
                        double[] temp = new double[inputImage.getColorModel().getNumColorComponents()];
                        if (inputRaster.getBounds().contains(p, q))
                            temp = inputRaster.getPixel(p, q, temp);
                        else
                            temp = inputRaster.getPixel(i, j, temp);
//                        if(temp[1]!=0 ||temp[2]!=0)
//                            System.out.println("foo");
                        pixel[0] += temp[0];
//                        if(i==180 && j==95)
//                            System.out.println(""+pixel[0]+","+pixel[1]+","+pixel[2]+"("+p+","+q+")");
                    }
                }

                pixel[0] /= k*k;
//                if(pixel[1]!=0 ||pixel[2]!=0)
//                    System.out.println("foo");
                outputRaster.setPixel(i, j, pixel);
            }
        }

        System.out.println(outputRaster.getPixel(180,95,new double[3])[0]);
        outputImage.setData(outputRaster);

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

    public void setOutputFileName(String filename) {
        outputFilename = filename;
    }

    public void printInputImgDetails() {
        System.out.println(inputImage.toString());
    }

    public void printOutputImgDetails() {
        System.out.println(outputImage.toString());
    }
}

class Main {
    public static void main(String[] args) {
        ImageProcessor ip = new ImageProcessor("input.png", "output.png");
        ip.printInputImgDetails();
        System.out.print(
                "Type \n" +
                "1 --> for zoom \n" +
                "2 --> for shrink \n" +
                "3 --> for average filter \n"
        );
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if(choice == 1) {
            ip.zoom(2, 2);
        } else if(choice == 2) {
            ip.shrink(2, 2);
        } else if(choice == 3) {
            ip.smooth();
        }
        ip.printOutputImgDetails();
    }
}