import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

/**
 * Class that contains the method for noise removal.
 */
class NoiseRemoval {

    /**
     * Performs noise removal by averaging multiple images.
     * @param images the input image set
     * @return the average image of the input set of images
     */
    public static BufferedImage removeNoise(BufferedImage[] images) {

        int n = images.length;
        for(int i=1; i<n; ++i) {
            if ((images[i].getWidth() != images[i-1].getWidth()) ||
                    (images[i].getHeight() != images[i-1].getHeight()) ||
                    (images[i].getType() != images[i-1].getType()))
                return images[0];
        }

        BufferedImage outputImage = new BufferedImage(
                images[0].getWidth(),
                images[0].getHeight(),
                images[0].getType()
//                BufferedImage.TYPE_BYTE_GRAY
        );

        int width = outputImage.getWidth();
        int height = outputImage.getHeight();
        int numComponents = images[0].getColorModel().getNumComponents();
        WritableRaster inputRasters[] = new WritableRaster[n];
        WritableRaster outputRaster = outputImage.getRaster();
        for(int i=0; i<n; ++i)
            inputRasters[i] = images[i].getRaster();

        for(int i=0; i<width; ++i) {
            for (int j = 0; j < height; ++j) {
                double[] sum = new double[numComponents];
                for (int k = 0; k < n; ++k) {
                    double[] pixel = inputRasters[k].getPixel(i, j, new double[numComponents]);
                    for (int c = 0; c < numComponents; ++c)
                        sum[c] += pixel[c];
                }
                double[] avg = new double[numComponents];
                for (int c = 0; c < numComponents; ++c)
                    avg[c] = sum[c]/n;
                outputRaster.setPixel(i, j, avg);
            }
        }

        outputImage.setData(outputRaster);

        Data.writeImageFile(outputImage);
        return outputImage;
    }

    public static BufferedImage addNoise(BufferedImage inputImage, int n) {
        int numComponents = inputImage.getColorModel().getNumComponents();
        WritableRaster inputRaster = inputImage.getRaster();
        BufferedImage[] images = new BufferedImage[n];

        for(int k=0;k<n;++k) {
            BufferedImage outputImage = new BufferedImage(
                    inputImage.getWidth(),
                    inputImage.getHeight(),
                inputImage.getType()
//                    BufferedImage.TYPE_BYTE_GRAY
            );
            Random random = new Random();
            WritableRaster outputRaster = outputImage.getRaster();
            for (int i = 0; i < inputImage.getWidth(); ++i)
                for (int j = 0; j < inputImage.getHeight(); ++j) {
                    int[] pixel = inputRaster.getPixel(i, j, new int[numComponents]);
                    int flag = random.nextInt(6);
                    if (flag % 2 == 0) {
                        if (pixel[0] < 50)
                            pixel[0] = 0;
                        else if (pixel[0] > 205)
                            pixel[0] = 255;
                    }
                    outputRaster.setPixel(i, j, pixel);
                }
            outputImage.setData(outputRaster);
            Data.writeImageFile(outputImage, Integer.toString(k+1) + ".jpg");
            images[k] = outputImage;
        }

        return removeNoise(images);
    }

}