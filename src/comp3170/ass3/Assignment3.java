package comp3170.ass3;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import org.joml.Matrix4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.InputManager;
import comp3170.ass3.sceneobjects.Plane;
import comp3170.ass3.sceneobjects.SceneObject;

public class Assignment3 extends JFrame implements GLEventListener {

	final private static float TAU = (float) (Math.PI * 2);
	final private static float GROUND_SIZE = 40f;
	
	private GLCanvas canvas;
	private Animator animator;
	private long oldTime;
	private InputManager input;
	
	private int screenWidth = 800;
	private int screenHeight = 800;
	private SceneObject root;

	public Assignment3() {
		super("Assignment 3");
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		add(canvas);
		
		animator = new Animator(canvas);
		animator.start();
		oldTime = System.currentTimeMillis();				
		input = new InputManager(canvas);
		
		setSize(screenWidth, screenHeight);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	

	}

	@Override
	public void init(GLAutoDrawable arg0) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// TODO: Initialise GL here
		
		createScene();
	}

	private void createScene() {
		root = new SceneObject();		
		
		Plane ground = new Plane(GROUND_SIZE);
		ground.setParent(root);
		ground.getMatrix().translate(-GROUND_SIZE/2,0,-GROUND_SIZE/2);

		// TODO: complete your scene here
	}
	
	private float random(float min, float max) {
		return (float) (min + Math.random() * (max - min));
	}

	private void update() {
		long time = System.currentTimeMillis();
		float dt = (time - oldTime) / 1000.0f;
		oldTime = time;

		// TODO: update the scene here
		
		input.clear();
	}

	private Matrix4f mvpMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();	
	private Matrix4f projectionMatrix = new Matrix4f();	
	
	private static final float CAMERA_WIDTH = 50;
	private static final float CAMERA_HEIGHT = 50;
	private static final float CAMERA_NEAR = 1;
	private static final float CAMERA_FAR = 10;
	
	@Override
	public void display(GLAutoDrawable arg0) {
		update();
		
		GL4 gl = (GL4) GLContext.getCurrentGL();		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
				
		// orthographic camera looking down on plane
		// TODO: replace this with your own camera
				
		viewMatrix.identity().translate(0,5,0).rotateX(-TAU/4); // camera Z points away from scene
		viewMatrix.invert();
		
		projectionMatrix.setOrtho(
				-CAMERA_WIDTH/2, CAMERA_WIDTH/2,
				-CAMERA_HEIGHT/2, CAMERA_HEIGHT/2,
				CAMERA_NEAR, CAMERA_FAR);
		
		// model-view-projection (MVP) matrix is the product of the three matrices.
		// M_mvp = M_projection * M_view * M_model
		
		// start by multiplying M_projection * M_view then descend the scene graph 
		// multiplying model matrices on the right   
		
		mvpMatrix.set(projectionMatrix).mul(viewMatrix);
		
		root.draw(mvpMatrix);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		new Assignment3();
	}
}
