import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

public class ScaleLights {
	
	public static void main(String[] args) {
		System.load("C:/Users/Steinert Robotics/Downloads/opencv/build/java/x64/opencv_java340.dll");
		
		VideoCapture camera = new VideoCapture(0);
		Mat frame = new Mat();
		Mat hsv = new Mat();
		Mat medianBlur = new Mat();
		
		Mat threshold = new Mat();
		
		Mat erode = new Mat();
		
		Mat dilate = new Mat();
		
		Mat element = new Mat();
		
		Mat dest = new Mat();
		
		double morphSize = 2;
		
		// uncomment when using red LED light
		Scalar lowerRed = new Scalar(95, 0, 230); // 2nd parameter (saturation) doesn't matter; old V: 220
		Scalar upperRed = new Scalar(245, 240, 240); // 2nd parameter (saturation) doesn't matter
		
		List<MatOfPoint> contours = new ArrayList<>();
		List<Moments> mu = new ArrayList<>();
		
		Scalar white = new Scalar(255, 255, 255);
		
		hsv = Imgcodecs.imread("ledred.png");
		
		while (true) {
			
			if (camera.isOpened()) {
				camera.read(frame);
			}
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);
			
			Imgproc.medianBlur(hsv, medianBlur, 11);
			
			element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4*morphSize + 1, (2*morphSize)+1), new Point(morphSize, morphSize));
			
			Imgproc.erode(medianBlur, erode, element);
			Imgproc.dilate(erode, dilate, element);
			
			Imgproc.erode(dilate, erode, element);
			Imgproc.dilate(erode, dilate, element);
			
			Core.inRange(dilate, lowerRed, upperRed, threshold);
			
			dest = Mat.zeros(threshold.size(), CvType.CV_8UC3);
			
			// Find contours
			Imgproc.findContours(threshold, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

			// Draw contours in dest Mat
			Imgproc.drawContours(dilate, contours, -1, white);
			
//			Imgproc.adaptiveThreshold(gaussian, threshold, upperRed, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 40);
			
			for (int i = 0; i < contours.size(); i++) {
		        mu.add(i, Imgproc.moments(contours.get(i), false));
		        Moments p = mu.get(i);
		        int x = (int) (p.get_m10() / p.get_m00());
		        int y = (int) (p.get_m01() / p.get_m00());
		        Imgproc.circle(dilate, new Point(x, y), 4, new Scalar(255,49,0,255));
		    }
			
			displayImage(Mat2BufferedImage(dilate), dilate);
			
			
//			displayImage(Mat2BufferedImage(frame));
			
			contours.clear();
		}
	}
	
	public static BufferedImage Mat2BufferedImage(Mat m){
	    int bufferSize = m.channels()*m.cols()*m.rows();
	    byte [] b = new byte[bufferSize];
	    m.get(0,0,b); // get all the pixels
	    BufferedImage image = new BufferedImage(m.cols(), m.rows(), BufferedImage.TYPE_3BYTE_BGR);
	    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    System.arraycopy(b, 0, targetPixels, 0, b.length);
	    return image;
	}

	public static void displayImage(Image img2, Mat m) {
	    ImageIcon icon = new ImageIcon(img2);
	    JFrame frame = new JFrame();
	    
	    frame.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	        }

	        @Override
	        public void keyPressed(KeyEvent e) {
	            System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
	        	Imgcodecs.imwrite("detectcenterofredled.png", m);
	        }

	        @Override
	        public void keyReleased(KeyEvent e) {
	        }
	    });
	    
	    frame.setLayout(new FlowLayout());
	    frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);
	    JLabel lbl = new JLabel();
	    lbl.setIcon(icon);
	    frame.add(lbl);
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
