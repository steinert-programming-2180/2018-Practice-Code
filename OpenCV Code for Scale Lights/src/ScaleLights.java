import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
	static {
		System.load("C:/Users/Steinert Robotics/Downloads/opencv/build/java/x64/opencv_java340.dll");
	}
	
	static VideoCapture camera = new VideoCapture(0);
	
	static Mat frame = new Mat();
	static Mat hsv = new Mat();
	static Mat medianBlur = new Mat();
	static Mat threshold = new Mat();
	static Mat erode = new Mat();
	static Mat dilate = new Mat();
	static Mat element = new Mat();
//	Mat dest = new Mat();
	
	static Size s = new Size();
	
	static double morphSize = 2;
	
	static Moments p = new Moments();
	
	// uncomment when using red LED light
	static Scalar lowerRed = new Scalar(95, 0, 230); // 2nd parameter (saturation) doesn't matter; old V: 220
	static Scalar upperRed = new Scalar(180, 240, 240); // 2nd parameter (saturation) doesn't matter
	
	static List<MatOfPoint> contours = new ArrayList<>();
	static List<Moments> mu = new ArrayList<>();
	
	static long period = 1000L; // code is run every 100 ms
	static long delay = 0L; // code starts immediately
	
	public static void main(String[] args) {
		
//		hsv = Imgcodecs.imread("ledred.png");
		
		TimerTask lightDetection = new TimerTask() {

			@Override
			public void run() {
				
				if (camera.isOpened()) {
					camera.read(frame);
				}
				
				Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_RGB2HSV);				
				Imgproc.medianBlur(hsv, medianBlur, 11);				
				element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4*morphSize + 1, (2*morphSize)+1), new Point(morphSize, morphSize));
				Imgproc.erode(medianBlur, erode, element);
				Imgproc.dilate(erode, dilate, element);				
				Imgproc.erode(dilate, erode, element);
				Imgproc.dilate(erode, dilate, element);
				Core.inRange(dilate, lowerRed, upperRed, threshold);
				
				// Find contours
				Imgproc.findContours(threshold, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

				// Draw contours in dest Mat
				Imgproc.drawContours(dilate, contours, -1, new Scalar(255, 255, 255));
				Imgproc.drawContours(frame, contours, -1, new Scalar(255, 255, 255));
							
				if (contours.size() > 0) {
					mu.add(0, Imgproc.moments(contours.get(0), false));
					p = mu.get(0);
					int x = (int) (p.get_m10() / p.get_m00());
					int y = (int) (p.get_m01() / p.get_m00());
					System.out.println("X: " + x + "\nY: " + y);
					Imgproc.circle(frame, new Point(x, y), 4, new Scalar(255,49,0,255));
				}
				
				s = frame.size();
				
				Imgproc.line(frame, new Point(0, s.height / 2), new Point(s.width, s.height / 2), new Scalar(255, 255, 255), 2);
				
				if (mu.size() != 0) { // check if nothing is detected
					if (((int) (mu.get(0).get_m01() / mu.get(0).get_m00()) <= s.height / 2) && !((int) (mu.get(0).get_m01() / mu.get(0).get_m00()) == 0)) {
						
						Imgproc.putText(frame, "Detected: Above line", new Point(10, s.height - 50), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 4);
					} else if (((int) (mu.get(0).get_m01() / mu.get(0).get_m00()) > s.height / 2) && !((int) (mu.get(0).get_m01() / mu.get(0).get_m00()) == 0)) {
						
						Imgproc.putText(frame, "Detected: Below line", new Point(10, s.height - 50), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 0, 0), 4);
					} else if (((int) (mu.get(0).get_m01() / mu.get(0).get_m00()) == 0) && ((int) (p.get_m10() / p.get_m00()) == 0)) {
						
						Imgproc.putText(frame, "In frame, not detected", new Point(10, s.height - 50), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 4);
					}
				} else {
					Imgproc.putText(frame, "No light in frame", new Point(10, s.height - 50), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 4);
				}
				
				displayImage(Mat2BufferedImage(frame), dilate);
				
				
//				displayImage(Mat2BufferedImage(frame));
				
				contours.clear(); // delete all current contours to prepare for next iteration
				mu.clear(); // delete all current moments to prepare for next iteration
			}
			
		};
		
		Timer timer = new Timer("Timer");
		if (camera.isOpened()) {
			timer.scheduleAtFixedRate(lightDetection, delay, period);
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
		JFrame frame = new JFrame();
	    ImageIcon icon = new ImageIcon(img2);
//	    JFrame frame = new JFrame();
	    
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
