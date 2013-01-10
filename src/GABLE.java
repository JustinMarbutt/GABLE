/*

Author: Justin Marbutt

Purpose: Driver for GABLE (Genetic Algrithmic Breeder of L-grammars in Environments)

to compile: javac GABLE.java
to run: java GABLE [myPlant.gable]

Dependency:
	you will need to download JOGL and add 
	jogl.jar and gluegen-rt.jar to your
	CLASSPATH to compile

*/
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;

// NOTE: If you resize your display list needs to be refilled

public class GABLE extends GLJPanel implements GLEventListener, Runnable, MouseListener, MouseMotionListener, KeyListener
{
	private static final long serialVersionUID = -4287342912798524482L;
	

	private static GrammarInterpreter gi;
	private static double deltaLen;
	private static double deltaAngle;
	private static Grammar grammar;
	private static String plant;
	private int plant1;
	private static GLU glu = new GLU();
	private GLUquadric earth = glu.gluNewQuadric();
	
	public static final float MOVEMENT_SPEED = 0.1f;
	public static final float ROTATION_SPEED = 0.005f;
	
	public static final int BOX_SIZE = 200;
	
	private JFrame frame;
	
	private float[] rot;
	private float[] pos = {0.0f, 1.2f, 4.3f};
	private boolean[] down;
	private int[] mouse;
	private int[] center;
	private boolean[] keys;
	private Robot robot;
	private boolean disregard;
	private Cursor[] cursors;
	
	public GABLE()
	{
		
		rot = new float[3];
		
		mouse = new int[2];
		center = new int[2];
		down = new boolean[3];
		keys = new boolean[256];
		
		disregard = false;
		
		// Make robot makes 2 cursors one to hide the curser when clicked
		// one to replace the cursor at the center when done moving
		try
		{
			robot = new Robot();
			
			cursors = new Cursor[2];
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			cursors[0] = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
			cursors[1] = Cursor.getDefaultCursor();
		}
		catch (AWTException e)
		{
			robot = null;
		}
		
		addGLEventListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setFocusable(true);
		
		frame = new JFrame("GABLE");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setPreferredSize(new Dimension(800, 600));
		frame.getContentPane().add(this);
		frame.pack();
		
		frame.setVisible(true);
		
		Thread thread = new Thread(this);
		thread.run();
	}
	
	public static void main(String[] args)
	{
		FileParser fp = new FileParser(args[0]);
		grammar = fp.getGrammar();
		deltaLen = grammar.getLength();
		deltaAngle = grammar.getAngle();
		plant = grammar.getString();
		gi = new GrammarInterpreter(plant, deltaLen, deltaAngle);
		
		new GABLE();
	}
	
	public void display(GLAutoDrawable glad)
	{
		
		GL gl = glad.getGL();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		//Look around
		gl.glRotatef(rot[0], 1, 0, 0);
		gl.glRotatef(rot[1], 0, 1, 0);
		gl.glRotatef(rot[2], 0, 0, 1);
		//Walk
		gl.glTranslatef(-pos[0], -pos[1], -pos[2]);
		
		gl.glBegin(GL.GL_QUADS);
			gl.glColor3d(0.2d, 0.7d, 0.3d);
			gl.glVertex3d(-30.0d, 0.0d,30.0d);
			gl.glVertex3d(30.0d, 0.0d, 30.0d);
			gl.glVertex3d(30.0d, 0.0d, -30.0d);
			gl.glVertex3d(-30.0d, 0.0d, -30.0d);
		gl.glEnd();
	
	
		gl.glCallList(plant1);
	
		gl.glPushMatrix();
			gl.glColor3d(1.0d,1.0d,0.2d);
			gl.glTranslated(0.0d, 10.2d, 5.0d);
			glu.gluSphere(earth, 0.7d, 100, 100);
			//glu.gluDeleteQuadric(earth);
		gl.glPopMatrix();
		
	}

	public void displayChanged(GLAutoDrawable glad, boolean mc, boolean dc)
	{
	}
	
	public void init(GLAutoDrawable glad)
	{
		GL gl = glad.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_DEPTH_TEST); 
		gl.glDepthFunc(GL.GL_LEQUAL); 
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); 
		gl.glClearColor(0.7f, 0.7f, 1.0f, 1.0f);
		gl.glClearDepth(1.0f);
		
		//Fill display list
		plant1 = gl.glGenLists(1);
		gl.glNewList(plant1, GL.GL_COMPILE);
			gi.drawGrammar(gl,glu);
		gl.glEndList();
	}
	
	public void reshape(GLAutoDrawable glad, int x, int y, int w, int h)
	{
		if (robot != null)
		{
			center[0] = w / 2;
			center[1] = h / 2;
			
			mouse[0] = center[0];
			mouse[1] = center[1];
		}
		
		GL gl = glad.getGL();
		GLU glu = new GLU(); 
		if(h <= 0) h = 1;
		float m = (float)w / (float)h; 
		gl.glMatrixMode(GL.GL_PROJECTION); 
		gl.glLoadIdentity(); 
		glu.gluPerspective(45.0f, m, 1.0f, 100.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	// moving around, in run so it can be in another thread
	public void run()
	{
		while (frame.isVisible())
		{
			long time = System.currentTimeMillis();
			
			if (robot != null)
				if (down[0] || down[2])
					setCursor(cursors[0]);
				else
					setCursor(cursors[1]);
			
			int r = -1;
			if (keys[KeyEvent.VK_W] || (down[0] && down[2]))
				r = 90;
			else if (keys[KeyEvent.VK_A])
				r = 0;
			else if (keys[KeyEvent.VK_S])
				r = 270;
			else if (keys[KeyEvent.VK_D])
				r = 180;
			
			if (r >= 0)
			{
				double a = (rot[1] + r) / 180 * Math.PI;
				pos[2] -= Math.sin(a) * MOVEMENT_SPEED;
				pos[0] -= Math.cos(a) * MOVEMENT_SPEED; 
			}
			
			repaint();
			// Sleep appropriate amount of time
			long dif = System.currentTimeMillis() - time;
			if (dif < 50)
			{
				try { Thread.sleep(50 - dif); }
				catch(InterruptedException e) {}
			}
			else
				System.out.println("Unable to keep up");
		}
		
		
	}
	
	
	
	public void mouseDragged(MouseEvent e)
	{
		if (disregard)
			disregard = false;
		else if (down[0] || down[2])
		{
			rot[1] += (e.getX() - mouse[0]) * ROTATION_SPEED;
			rot[0] += (e.getY() - mouse[1]) * ROTATION_SPEED;
			if (rot[0] < -80) rot[0] = -80;
			if (rot[0] > 80) rot[0] = 80;
			
			if (robot != null)
			{
				if (e.getX() != center[0] || e.getY() != center[1])
				{
					int cx = getLocationOnScreen().x + center[0];
					int cy = getLocationOnScreen().y + center[1];
					
					disregard = true;
					// move mouse back to center
					robot.mouseMove(cx, cy);
				}
			}
			else
			{
				mouse[0] = e.getX();
				mouse[1] = e.getY();
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			down[0] = true;
		else if (e.getButton() == MouseEvent.BUTTON2)
			down[1] = true;
		else if (e.getButton() == MouseEvent.BUTTON3)
			down[2] = true;
		
		if (robot != null)
		{
			if (e.getButton() != MouseEvent.BUTTON2)
			{
				if (e.getX() != center[0] || e.getY() != center[1])
				{
					int cx = getLocationOnScreen().x + center[0];
					int cy = getLocationOnScreen().y + center[1];
					
					disregard = true;
					robot.mouseMove(cx, cy);
				}
			}
		}
		else
		{
			mouse[0] = e.getX();
			mouse[1] = e.getY();
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			down[0] = false;
		else if (e.getButton() == MouseEvent.BUTTON2)
			down[1] = false;
		else if (e.getButton() == MouseEvent.BUTTON3)
			down[2] = false;
	}
	
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}
	
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e) {}
}
