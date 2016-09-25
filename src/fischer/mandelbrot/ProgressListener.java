package fischer.mandelbrot;

public interface ProgressListener
{
	/**
	 * Called when the progress changed
	 * @param progress an integer between 0 and 100 inclusive signifying the percentage completed
	 */
	public void onProgressChanged(int progress);
}
