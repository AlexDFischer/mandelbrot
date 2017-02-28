package fischer.mandelbrot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class MandelbrotThread extends Thread
{
    private final double
        unitsPerPixel,
        bottomLeftX,
        bottomLeftY;
    private final int
        maxSteps,
        imageSizeX,
        imageSizeY,
        black = 0b11111111_00000000_00000000_00000000;
    /**
     * represents rows available for rendering
     * 1 = has been rendered
     * 0 = hasn't been rendered
     */
    private final AtomicIntegerArray rows;
    private final ProgressListener progressListener;
    private final BufferedImage image;
    
    public MandelbrotThread(double bottomLeftX, double bottomLeftY, double unitsPerPixel,
                            int maxSteps, int imageSizeX, int imageSizeY,
                            AtomicIntegerArray rows, ProgressListener progressListener, BufferedImage image)
    {
        this.unitsPerPixel = unitsPerPixel;
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
        this.maxSteps = maxSteps;
        this.imageSizeX = imageSizeX;
        this.imageSizeY = imageSizeY;
        this.rows = rows;
        this.progressListener = progressListener;
        this.image = image;
    }
    
    @Override
    public void run()
    {
        int currentRow = 0;
        while (currentRow < imageSizeY)
        {
            
            if (rows.compareAndSet(currentRow, 0, 1))
            {
                // we can render currentRow
                rows.set(currentRow, 1);
                double rePart, imPart = bottomLeftY + currentRow * unitsPerPixel;
                for (int x = 0; x < imageSizeX; x++)
                {
                    rePart = bottomLeftX + x * unitsPerPixel;
                    double potential = Mandelbrot.potentialFunction(rePart, imPart, maxSteps);
                    if (potential < 0) // if it is in the set
                    {
                        image.setRGB(x, imageSizeY - 1 - currentRow, black);
                    } else
                    {
                        // if it diverges, color it based on how quickly it diverges
                        image.setRGB(x, imageSizeY - 1 - currentRow, Color.HSBtoRGB(Mandelbrot.getHue(potential), 1, 1));
                    }
                }
                currentRow++;
                if (progressListener != null)
                {
                    progressListener.onProgressChanged((int)((double)currentRow / imageSizeY * 100));    
                }
            } else
            {
                currentRow++;
            }
        }
    }
}
