package fischer.mandelbrot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Mandelbrot
{	
	/**
	 * Returns an integer hue between 0 and 255.
	 * @param m potential function as defined in wikipedia
	 * @return the hue
	 */
	public static float getHue(double m)
	{
		return (float)(0.3 * Math.log(m));
	}
	
	/**
	 * Returns a smoothly-colored image of the Mandelbrot set
	 * @param bottomLeftX the bottom left corner location of the set to be drawn, x coordinate
	 * @param bottomLeftY the bottom left corner location of the set to be drawn, y coordinate
	 * @param unitsPerPixel how wide each pixel in the BufferedImage will be
	 * @param width how many pixels wide the returned BufferedImage will be
	 * @param height how many pixels tall the returned BufferedImage will be
	 * @param maxSteps how many iterations to perform before giving up
	 * @param progressListener an optional progressListener to call to tell it
	 * how close it is to being done, to update a progress bar. Leave null if
	 * you don't want to use this.
	 * @return a BufferedImage of the Mandelbrot set.
	 */
	public static BufferedImage getImage(double bottomLeftX, double bottomLeftY, double unitsPerPixel, int width, int height, int maxSteps, ProgressListener progressListener)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int black = 0b11111111_00000000_00000000_00000000;
		double a, b;
		// start at the bottom left; go to the right then up
		for (int y = height - 1; y >= 0; y--)
		{
			for (int x = 0; x < width; x++)
			{
				// compute the coordinates of the point we're going to see
				// whether or not it's in the set
				a = bottomLeftX + x * unitsPerPixel;
				b = bottomLeftY + (height - 1 - y) * unitsPerPixel;
				
				double potential = Mandelbrot.potentialFunction(a, b, maxSteps);
				if (potential < 0) // if it is in the set
				{
					image.setRGB(x, y, black);
				} else
				{
					// if it diverges, color it based on how quickly it diverges
					image.setRGB(x, y, Color.HSBtoRGB((float)(0.5 * Math.log(potential)), 1, 1));
					
				}
			}
			if (progressListener != null)
			{
				progressListener.onProgressChanged((int)((height - 1 - y) / ((double)height) * 100));
				//System.out.println("called onProgressChanged with progress=" + ((int)((height - 1 - y) / ((double)height) * 100)) + "and y=" + y);
			}
		}
		System.out.println("returned image: (" + width + ", " + height + ")");
		return image;
	}
	
	/**
	 * A multithreaded version of getImage.
	 * 
	 * Returns a smoothly-colored image of the Mandelbrot set
     * @param bottomLeftX the bottom left corner location of the set to be drawn, x coordinate
     * @param bottomLeftY the bottom left corner location of the set to be drawn, y coordinate
     * @param unitsPerPixel how wide each pixel in the BufferedImage will be
     * @param imageSizeX how many pixels wide the returned BufferedImage will be
     * @param imageSizeY how many pixels tall the returned BufferedImage will be
     * @param maxSteps how many iterations to perform before giving up
     * @param progressListener an optional progressListener to call to tell it
     * how close it is to being done, to update a progress bar. Leave null if
     * you don't want to use this.
     * @param numThreads how many threads to use to render the Mandelbrot set.
     * @return a BufferedImage of the Mandelbrot set.
	 */
	public static BufferedImage getImageMultiThreaded(double bottomLeftX, double bottomLeftY, double unitsPerPixel, int imageSizeX, int imageSizeY, int maxSteps, ProgressListener progressListener, int numThreads)
    {
        BufferedImage image = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.TYPE_INT_RGB);
        AtomicIntegerArray rows = new AtomicIntegerArray(imageSizeY);
        ExecutorService es = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++)
        {
            es.execute(new MandelbrotThread(bottomLeftX, bottomLeftY, unitsPerPixel, maxSteps, imageSizeX, imageSizeY, rows, progressListener, image));
        }
        es.shutdown();
        try
        {
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); // there should be a way to do this with no timeout
        } catch (InterruptedException e)
        {
            // shouldn't happen
            e.printStackTrace();
        }
        return image;
    }
	
	/**
	 * Reports how many steps until (a+bi) diverges under the sequence that determines the Mandelbrot set
	 * Not used anywhere in current version.
	 * @param a real part of the input
	 * @param b imaginary part of the input
	 * @param maxSteps number of steps of the sequence to check until giving up
	 * @return
	 */
	public static int stepsUntilDiverge(double a, double b, int maxSteps)
	{
		double aNew=a, bNew=b, aTemp, bTemp;
		for (int i = 0; i <= maxSteps; i++)
		{
			if (aNew*aNew+bNew*bNew > 4.0) // if the norm becomes greater than 2
			{
				return i;
			}
			aTemp = aNew*aNew-bNew*bNew+a;
			bTemp = 2*aNew*bNew+b;
			aNew=aTemp;
			bNew=bTemp;
		}
		return -1;
	}
	
	/**
	 * A continuous potential function that allows for smooth coloring
	 * @param a real part of the input
	 * @param b imaginary part of the input
	 * @param maxSteps number of steps of the sequence to check until giving up
	 * @return the potential function
	 */
	public static double potentialFunction(double a, double b, int maxSteps)
	{
		double aNew=a, bNew=b, aTemp, bTemp;
		for (int i = 0; i <= maxSteps; i++)
		{
			if (aNew*aNew+bNew*bNew > 50) // if the norm squared becomes greater than the escape radius of 50.
				// 50 is chosen arbitrarily; the only requirement is that it be significantly greater than the
				// norm of the points we're testing so that around the escape radius, we have approximately:
				//  (z_n)^2 + c ≈ (z_n)^2
			{
				return i + 1 - Math.log(Math.log( (aNew*aNew+bNew*bNew) ) / 2 / Math.log(2) ) / Math.log(2.0);
			}
			aTemp = aNew*aNew-bNew*bNew+a;
			bTemp = 2*aNew*bNew+b;
			aNew=aTemp;
			bNew=bTemp;
		}
		return -1;
	}
}
