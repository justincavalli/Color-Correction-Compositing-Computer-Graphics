import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class changeImage
{
    // ints to track the RGB values
    private static int red;
    private static int green;
    private static int blue;
    
    public static void main(String[] args)
    {
        for(int i = 0; i < 4; ++i)
        {
            // load images
            BufferedImage Image = null;
            BufferedImage Image2 = null;
            try
            {
                Image = ImageIO.read(new File ("mountainRange.jpg"));
                Image2 = ImageIO.read(new File ("Forest.jpg"));
            } catch (IOException e) {}

            for (int y = 0; y < Image.getHeight(); y++)
            {
                for (int x=0; x < Image.getWidth(); x++)
                {
                    int pixel = Image.getRGB(x,y);

                    // unpacks the 32 bits into the individual R, G, B
                    // channel values which are 8 bits (i.e. o..255)
                    red = (pixel & 0x00ff0000) >> 16;
                    green = (pixel & 0x0000ff00) >> 8;
                    blue = pixel & 0x000000ff;

                    // Generate an inverted image
                    if (i == 0)
                        invertImage();

                    // Generate a grayscale image
                    else if (i == 1)
                        grayScaleImage();

                    // Generate a color correct
                    else if (i == 2)
                        colorCorrect();

                    // Mix at 75%/25%
                    else if (i == 3)
                        mix(Image2, x, y);

                    // Pack the red, green and blue channels back into a 
                    // 32 bit int pixel
                    pixel = (red & 0x000000ff) | ((green << 8) & 0x0000ff00) | ((blue << 16) & 0x00ff0000);

                    Image.setRGB(x, y, pixel);
                }
            }
            try
            {
                // output the generated images
                File outputFile;
                if (i == 0)
                    outputFile = new File("invertedMountainRange.jpg");
                else if (i == 1)
                    outputFile = new File("grayScaleMountainRange.jpg");
                else if (i == 2)
                    outputFile = new File("colorCorrectMountainRange.jpg");
                else 
                    outputFile = new File("mixed.jpg");
                ImageIO.write(Image, "jpg", outputFile);
            } catch (IOException e) {}
        }
    }

    private static void invertImage()
    {
        // inverts each RGB color
        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;
    }

    private static void grayScaleImage()
    {
        // grayscale works by making all RGB values the same
        green = red;
        blue = red;
    }

    private static void colorCorrect()
    {
        // gain of 2 on the red channel
        red *= 2;
        if (red > 255)
            red = 255;
        // offset of 50 on the green channel
        green += 50;
        if (green > 255)
            green = 255;
        // gamma of 2.0 on the blue channel
        blue = (int)Math.pow(blue, 2.0);
        if (blue > 255)
            blue = 255;
    }

    private static void mix(BufferedImage Image2, int x, int y)
    {
        int red2, green2, blue2;
        
        // Gets the pixel of the 2nd image in the same location as the 1st image
        int pixel2 = Image2.getRGB(x,y);

        // Gets RGB for the 2nd image 
        red2 = (pixel2 & 0x00ff0000) >> 16;
        green2 = (pixel2 & 0x0000ff00) >> 8;
        blue2 = pixel2 & 0x000000ff;
        
        red = (int) (0.75 * red) + (int)(0.25 * red2);
        green = (int) (0.75 * green) + (int)(0.25 * green2);
        blue = (int) (0.75 * blue) + (int)(0.25 * blue2);
    }
}
