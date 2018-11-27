import Jama.LUDecomposition;
import Jama.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Class that contains methods for zooming ang shrinking operation.
 */
class ZoomAndShrink {
    /**
     * Performs zooming of an image.
     * @param inputImage the input image
     * @param sx the x-axis scaling factor
     * @param sy the y-axis scaling factor
     * @return the zoomed image
     */
    public static BufferedImage zoom(BufferedImage inputImage, double sx, double sy) {
        BufferedImage outputImage = new BufferedImage (
                (int) (inputImage.getWidth() * sx),
                (int) (inputImage.getHeight() * sy),
                inputImage.getType());

        WritableRaster inputRaster = inputImage.getRaster();
        WritableRaster outputRaster = outputImage.getRaster();
        int width = outputImage.getWidth();
        int height = outputImage.getHeight();
        int numComponents = inputImage.getColorModel().getNumComponents();
        for(int i=0; i<width; ++i)
            for(int j=0;j<height; ++j) {
                int[] pixel = inputRaster.getPixel((int) (i/sx), (int) (j/sy), new int[numComponents]);
                outputRaster.setPixel(i, j, pixel);
            }
        outputImage.setData(outputRaster);
        Data.writeImageFile(outputImage);

        return outputImage;
    }

    /**
     * Performs shrinking of an image.
     * @param inputImage the input image
     * @param sx the x-axis scaling factor
     * @param sy the y-axis scaling factor
     * @return the shrunk image
     */
    public static BufferedImage shrink(BufferedImage inputImage, double sx, double sy) {
        BufferedImage outputImage = new BufferedImage (
                (int) (inputImage.getWidth() * sx),
                (int) (inputImage.getHeight() * sy),
                inputImage.getType()
        );

        WritableRaster inputRaster = inputImage.getRaster();
        WritableRaster outputRaster = outputImage.getRaster();
        int width = outputImage.getWidth();
        int height = outputImage.getHeight();
        int numComponents = inputImage.getColorModel().getNumComponents();
        for(int i=0; i<width; ++i)
            for(int j=0;j<height; ++j) {
//                int[] pixel = inputRaster.getPixel((int) (i/sx), (int) (j/sy), new int[numComponents]);
                double[] pixel = inputRaster.getPixel((int) (i/sx), (int) (j/sy), new double[numComponents]);
                outputRaster.setPixel(i, j, pixel);
            }
        outputImage.setData(outputRaster);
        Data.writeImageFile(outputImage);
        return outputImage;
    }

    public static BufferedImage zoom2(BufferedImage inputImage, double sx, double sy) {
        BufferedImage outputImage = zoom(inputImage,sx,sy);
        int numComponents = outputImage.getColorModel().getNumComponents();

        WritableRaster outputRaster = outputImage.getRaster();
        for(int i=1;i<outputImage.getWidth()-1;++i)
            for(int j=1;j<outputImage.getHeight()-1;++j) {
                int x1 = i-1; int y1 = j-1; int x2 = i+1; int y2 = j+1;
                double f11 = outputRaster.getPixel(x1, y1, new float[numComponents])[0];
                double f12 = outputRaster.getPixel(x1, y2, new float[numComponents])[0];
                double f21 = outputRaster.getPixel(x2, y1, new float[numComponents])[0];
                double f22 = outputRaster.getPixel(x2, y2, new float[numComponents])[0];
                double[][] values = {{1,x1,y1,x1*y1}, {1,x1,y2,x1*y2}, {1, x2,y1,x2*y1}, {1,x2,y2,x2*y2}};
                double[][] rhs = {{f11}, {f12}, {f21}, {f22}};
                Matrix a = new Matrix(values);
                Matrix b = new Matrix(rhs);
                LUDecomposition luDecomposition = new LUDecomposition(a);
                Matrix x = luDecomposition.solve(b);
                double[] pixel = new double[numComponents];
                pixel[0] = x.get(0,0) + x.get(1,0)*i + x.get(2,0)*j + x.get(3,0)*i*j;
                outputRaster.setPixel(i,j,pixel);
            }

        outputImage.setData(outputRaster);
        Data.writeImageFile(outputImage, "o1.jpg");
        return outputImage;
    }
}