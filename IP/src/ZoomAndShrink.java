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
}