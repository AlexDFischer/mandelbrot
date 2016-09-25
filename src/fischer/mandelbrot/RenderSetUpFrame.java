package fischer.mandelbrot;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RenderSetUpFrame extends JFrame
{	
	// strings
	public static String ex1Text = "example 1", ex2Text = "example 2", ex3Text = "example 3", saveText = "save as PNG image";
	
	public boolean rendering = false;
	
	private static final long serialVersionUID = -5857765071953950846L;
	
	// graphical components
	
	private JLabel titleLabel = new JLabel("Set Up Render");
	
	private JLabel centerCoordLabel = new JLabel("Center Coordinate: ");
	private JTextField centerCoordxTextField = new JTextField();
	private JLabel centerCommaLabel = new JLabel(",");
	private JTextField centerCoordyTextField = new JTextField();
	
	private JLabel unitsPerPixelLabel = new JLabel("Units per pixel: ");
	private JTextField unitsPerPixelTextField = new JTextField();
	
	private JLabel maxStepsLabel = new JLabel("Max steps: ");
	private JTextField maxStepsTextField = new JTextField("1000");
	
	private JButton example1Button = new JButton(ex1Text);
	private JButton example2Button = new JButton(ex2Text);
	private JButton example3Button = new JButton(ex3Text);
	
	private JLabel imageSizeLabel = new JLabel("Image size: ");
	private JTextField imageSizexTextField = new JTextField();
	private JLabel imageSizeCommaLabel = new JLabel(",");
	private JTextField imageSizeyTextField = new JTextField();
	
	private JButton saveButton = new JButton(saveText);
	private JProgressBar progressBar = new JProgressBar();
	
	private JLabel timeLabel = new JLabel();
	
	private JFileChooser fileChooser = new JFileChooser();
	
	public RenderSetUpFrame()
	{
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel panel = new JPanel();
		BoxLayout mainLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(mainLayout);
		
		// title label
		panel.add(titleLabel);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// center coordinate things
		JPanel centerPanel = new JPanel();
		BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.LINE_AXIS);
		centerPanel.setLayout(centerLayout);
		centerPanel.add(centerCoordLabel);
		centerPanel.add(centerCoordxTextField);
		centerPanel.add(centerCommaLabel);
		centerPanel.add(centerCoordyTextField);
		centerCoordxTextField.setMaximumSize(new Dimension(100, 20));
		centerCoordyTextField.setMaximumSize(new Dimension(100, 20));
		panel.add(centerPanel);
		centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// units per pixel things
		JPanel unitsPerPixelPanel = new JPanel();
		BoxLayout unitsPerPixelLayout = new BoxLayout(unitsPerPixelPanel, BoxLayout.LINE_AXIS);
		unitsPerPixelPanel.setLayout(unitsPerPixelLayout);
		unitsPerPixelPanel.add(unitsPerPixelLabel);
		unitsPerPixelPanel.add(unitsPerPixelTextField);
		unitsPerPixelTextField.setMaximumSize(new Dimension(100, 20));
		panel.add(unitsPerPixelPanel);
		unitsPerPixelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// max steps things
		JPanel maxStepsPanel = new JPanel();
		BoxLayout maxStepsLayout = new BoxLayout(maxStepsPanel, BoxLayout.LINE_AXIS);
		maxStepsPanel.setLayout(maxStepsLayout);
		maxStepsPanel.add(maxStepsLabel);
		maxStepsPanel.add(maxStepsTextField);
		maxStepsTextField.setMaximumSize(new Dimension(100, 20));
		panel.add(maxStepsPanel);
		maxStepsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// example buttons
		panel.add(example1Button);
		example1Button.addActionListener(new ButtonListener());
		panel.add(example2Button);
		example2Button.addActionListener(new ButtonListener());
		panel.add(example3Button);
		example3Button.addActionListener(new ButtonListener());
		
		// center coordinate things
		JPanel imageSizePanel = new JPanel();
		BoxLayout imageSizeLayout = new BoxLayout(imageSizePanel, BoxLayout.LINE_AXIS);
		imageSizePanel.setLayout(imageSizeLayout);
		imageSizePanel.add(imageSizeLabel);
		imageSizePanel.add(imageSizexTextField);
		imageSizePanel.add(imageSizeCommaLabel);
		imageSizePanel.add(imageSizeyTextField);
		imageSizexTextField.setMaximumSize(new Dimension(100, 20));
		imageSizeyTextField.setMaximumSize(new Dimension(100, 20));
		panel.add(imageSizePanel);
		imageSizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// save button
		saveButton.addActionListener(new ButtonListener());
		panel.add(saveButton);
		
		// progress bar
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		
		// time label
		panel.add(timeLabel);
		
		this.setContentPane(panel);
		this.addWindowListener(new RenderWindowListener());
		
		fileChooser.setSelectedFile(new File("mandelbrot.png"));
	}
	
	/**
	 * Checks if all the numeric text fields are valid numbers
	 * @return true if they are valid, false otherwise
	 */
	private boolean validateTextFields()
	{
		try
		{
			new Double(centerCoordxTextField.getText());
			new Double(centerCoordyTextField.getText());
			new Double(unitsPerPixelTextField.getText());
			new Integer(maxStepsTextField.getText());
			new Integer(imageSizexTextField.getText());
			new Integer(imageSizeyTextField.getText());
		} catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}
	
	private class ButtonListener implements java.awt.event.ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (((JButton)(e.getSource())).getText().equals(ex1Text))
			{
				RenderSetUpFrame.this.centerCoordxTextField.setText("-0.6");
				RenderSetUpFrame.this.centerCoordyTextField.setText("0");
				RenderSetUpFrame.this.unitsPerPixelTextField.setText("0.003");
				RenderSetUpFrame.this.imageSizexTextField.setText("1366");
				RenderSetUpFrame.this.imageSizeyTextField.setText("768");
			} else if (((JButton)(e.getSource())).getText().equals(ex2Text))
			{
				RenderSetUpFrame.this.centerCoordxTextField.setText("-0.98");
				RenderSetUpFrame.this.centerCoordyTextField.setText("0.32");
				RenderSetUpFrame.this.unitsPerPixelTextField.setText("0.0002");
				RenderSetUpFrame.this.imageSizexTextField.setText("1366");
				RenderSetUpFrame.this.imageSizeyTextField.setText("768");
			} else if (((JButton)(e.getSource())).getText().equals(ex3Text)) // this took 119762 milliseconds first time, 115970 milliseconds second time
			{
				RenderSetUpFrame.this.centerCoordxTextField.setText("-0.7");
				RenderSetUpFrame.this.centerCoordyTextField.setText("-0.355");
				RenderSetUpFrame.this.unitsPerPixelTextField.setText("0.00001");
				RenderSetUpFrame.this.imageSizexTextField.setText("13660");
				RenderSetUpFrame.this.imageSizeyTextField.setText("7680");
				/*
				 * Date for how long example 3 took to render:
				 * Trial no. | Rendering time (ms)
				 *         1 | 119762
				 *         2 | 115970
				 *         3 | 117453
				 *         4 | 116313
				 *         5 | 114786
				 *       avg | 116857
				 */
			} else if (((JButton)(e.getSource())).getText().equals(saveText))
			{
				if (!validateTextFields())
				{
					JOptionPane.showMessageDialog(RenderSetUpFrame.this, "Error: not all fields are proper numbers");
					return;
				}
				int result = RenderSetUpFrame.this.fileChooser.showSaveDialog(RenderSetUpFrame.this);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					progressBar.setValue(0);
					if (!fileChooser.getSelectedFile().getPath().toUpperCase().endsWith(".PNG")) // if it's not a PNG file
					{
						fileChooser.setSelectedFile(new File(fileChooser.getSelectedFile().getPath() + ".png")); // make sure it has a PNG extension
					}
					File file = fileChooser.getSelectedFile();
					
					// calculate bottom left and bottom y coordinates
					double centerX = new Double(centerCoordxTextField.getText());
					double centerY = new Double(centerCoordyTextField.getText());
					double unitsPerPixel = new Double(unitsPerPixelTextField.getText());
					int maxSteps = new Integer(maxStepsTextField.getText());
					int imageSizeX = new Integer(imageSizexTextField.getText());
					int imageSizeY = new Integer(imageSizeyTextField.getText());
					double bottomLeftX = centerX - unitsPerPixel * imageSizeX / 2;
					double bottomLeftY = centerY - unitsPerPixel * imageSizeY / 2;
					Thread thread = new Thread()
							{
								public void run()
								{
									rendering = true;
									timeLabel.setText("currently rendering");
									long t1 = System.currentTimeMillis();
									BufferedImage img = Mandelbrot.getImage(bottomLeftX, bottomLeftY, unitsPerPixel, imageSizeX, imageSizeY, maxSteps, new CustomProgressListener());
									long t2 = System.currentTimeMillis();
									timeLabel.setText("Done rendering: took " + (t2 - t1) + " milliseconds. Writing image to file...");
									progressBar.setValue(100);
									
									// now that we have img, write it to a file
									try {
									    ImageIO.write(img, "png", file);
									} catch (IOException e) {
									    JOptionPane.showMessageDialog(RenderSetUpFrame.this, "ERROR: unable to write image to file.\n" + e.getMessage());
									}
									timeLabel.setText("Done rendering: took " + (t2 - t1) + " milliseconds. Wrote image to file.");
									rendering = false;
								}
							};
					thread.start(); // run this in a separate thread so the GUI doesn't freeze up as it renders
				}
			}
		}
	}
	
	private class CustomProgressListener implements ProgressListener
	{
		@Override
		public void onProgressChanged(int progress)
		{
			RenderSetUpFrame.this.progressBar.setValue(progress);
		}
	}
	
	private class RenderWindowListener implements java.awt.event.WindowListener
	{
		@Override public void windowActivated(WindowEvent arg0) { }
		@Override public void windowClosed(WindowEvent e) { }
		@Override
		public void windowClosing(WindowEvent e)
		{
			if (rendering)
			{
				int result = JOptionPane.showConfirmDialog(RenderSetUpFrame.this, "Image is currently rendering; exit anyways?", "close window?", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
				{
					System.exit(0);
				} else if (result == JOptionPane.NO_OPTION)
				{
					return;
				}
			}
			System.exit(0);
			
		}
		@Override public void windowDeactivated(WindowEvent e) { }
		@Override public void windowDeiconified(WindowEvent e) { }
		@Override public void windowIconified(WindowEvent e) { }
		@Override public void windowOpened(WindowEvent e) { }
	}
}
