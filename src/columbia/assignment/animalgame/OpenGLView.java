package columbia.assignment.animalgame;


import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.BufferUtil;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class OpenGLView 
{
	JFrame frame;
	OpenGLViewListener GLlistener;
	
	public OpenGLView(GuessModel[] GuessesToLoad)
	{
		frame = new JFrame("Lesson01");
	    GLJPanel gljpanel = new GLJPanel();

	    GLlistener = new OpenGLViewListener(GuessesToLoad);
	    
	    gljpanel.addGLEventListener(GLlistener);
	    frame.add(gljpanel);
	    frame.setSize(800, 600);
	    final Animator animator = new Animator(gljpanel);
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                System.exit(0);
	              }
	            }).start();
	        }
	      });
	    frame.setVisible(true);
	    animator.start();
	}
	
	public void LoadGuessTexture(GuessModel load)
	{
		GLlistener.LoadGuessTexture(load);
	}

}

/**
 * Performs OpenGL Operations
 * @author thomas
 *
 */
class OpenGLViewListener implements GLEventListener
{
	/** Native IO Buffers used in our Vertex Arrays */
	private FloatBuffer vertices;
	private FloatBuffer texCoords;
	private FloatBuffer normals;
	
	/** The default texture to display when asking a question
	 *  Its not an AnimalTexture because... its not an animal. */
	private Texture question_Texture;
	
	/** won't decrease performance because it will only contain a reference anyway */
	private AnimalTexture current_Texture;
	
	/**
	 * The entire array of textures
	 */
	private AnimalTexture Animal_Tex_Array[];
	/** For performance reasons, you don't want to read the entire file at runtime.
	 *  This method is for loading all the textures into memory at start time
	 *  We can pull out the texture we want with a search after when a new
	 *  texture needs to be displayed
	 *  */
	private void LoadAllGuessModelTextures(GuessModel[] guess_array)
	{
		Animal_Tex_Array = new AnimalTexture[guess_array.length];		
		for(int i = 0; i < guess_array.length; i++)
		{
			Texture TEX = loadTexture(guess_array[i].getpathToTexture().toString());
			String name = guess_array[i].getAnimalName();
			Animal_Tex_Array[i] = new AnimalTexture(TEX, name);
		}
	}
	
	private GuessModel[] guess_model_array;
	public OpenGLViewListener(GuessModel[] guess_array)
	{
		Animal_Tex_Array = new AnimalTexture[guess_array.length];
		guess_model_array = guess_array;
	}
	
	
	/**
	 * Posts (sets a boolean to true) a load call into the drawing function
	 * We need to post it because we don't have an OpenGL context in this call
	 * and need it to properly perform this function
	 * Posting it allows us to have runtime flexibility.
	 * Pass null to have it load the default (question_Texture)
	 * @param load
	 */
	public void LoadGuessTexture(GuessModel load)
	{
		System.out.println("GuessModel name: " + load.getAnimalName() + " Path: "
											+ load.getpathToTexture());
		LoadTexturePosted = true;
		LoadGuessModelPosted = load;
	}
	private boolean LoadTexturePosted = false;
	private GuessModel LoadGuessModelPosted = null;
	
	
	
	
	/**
	 * OpenGL drawing function
	 */
	@Override
	public void display(GLAutoDrawable auto_drawable)
	{
		//get the GL context
		final GL gl = auto_drawable.getGL();
		//clear the buffers
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		//load the identity matrix
		gl.glLoadIdentity();
		
		//translations so we can see drawing
		gl.glTranslatef(0, 0, -4);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);

		
		if(LoadTexturePosted == true)
		{
			if(LoadGuessModelPosted == null)
				System.err.println("ERROR: LoadGuessModelPosted is null!");
			current_Texture = AnimalTexture.findAnimalTexture(Animal_Tex_Array,
										LoadGuessModelPosted.getAnimalName());
			LoadTexturePosted = false;
		}
		

		if(current_Texture == null)
			question_Texture.bind();
		else
			current_Texture.bind();
		
		//draw the cube
		gl.glPushMatrix();
			gl.glRotatef(yrotate, 0.0f, 1.0f, 0.0f);
			drawOpenCube(gl);
		gl.glPopMatrix();
		//xrotate += 0.07;
		yrotate += 0.05;
		//zrotate += 0.03;
	}
	private static float xrotate=0, yrotate=0, zrotate=0;

	@Override
	public void displayChanged(GLAutoDrawable auto_drawable, boolean arg1, boolean arg2) {
		
	}

	/**
	 * OpenGL initialization function
	 * Called once, on startup
	 */
	@Override
	public void init(GLAutoDrawable auto_drawable)
	{
		//initialize OpenGL state, load arrays, etc.
		//anything needs to be dont before the drawing calls
		final GL gl = auto_drawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		
		//set the texture mode to decal, so textures are rendered independent of
		//underlying colors
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
		
		initVertexArrays();
		
		gl.glEnable(GL.GL_TEXTURE_2D);

		question_Texture = loadTexture("Data/Pictures/question_mark.png");
		
		
		float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float mat_shininess[] = { 50.0f };
		float light_position[] = { 0.0f, 0.0f, -1.0f, 0.0f };
		
		float LightAmbient[]= { 0.5f, 0.5f, 0.5f, 1.0f };
		float LightDiffuse[]= { 1.0f, 1.0f, 1.0f, 1.0f };
		float LightPosition[]= { 0.0f, 0.0f, 2.0f, 1.0f };
		
		
		//float mat_ambiance[] = { 
		
		
		gl.glEnable(GL.GL_LIGHTING);
		/*
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, 
											FloatArrayToFloatBuffer(mat_specular));
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 
				FloatArrayToFloatBuffer(mat_shininess));
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, 
						FloatArrayToFloatBuffer(light_position));
		*/
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, FloatArrayToFloatBuffer(LightAmbient));
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, FloatArrayToFloatBuffer(LightDiffuse));
		gl.glLightfv(gl.GL_LIGHT0, GL.GL_POSITION, FloatArrayToFloatBuffer(LightPosition));
		
		gl.glEnable(GL.GL_LIGHT0);
		
		//gl.glEnable(GL.GL_COLOR_MATERIAL);
		//gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
		
		
		LoadAllGuessModelTextures(guess_model_array);
	}
	
	private void drawOpenCube(GL gl)
	{
		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, texCoords);
		gl.glNormalPointer(GL.GL_FLOAT, 0, normals);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertices);
		gl.glDrawArrays(GL.GL_QUADS, 0, 24);
	}
	
	private static BufferedImage flipBufferedImageVertical(BufferedImage inBufferedImage)
	{ 
        int tWidth = inBufferedImage.getWidth(); 
        int tHeight = inBufferedImage.getHeight(); 
        BufferedImage tFlippedBufferedImage = new BufferedImage(tWidth, tHeight, inBufferedImage.getType()); 
        Graphics2D tG2D = tFlippedBufferedImage.createGraphics(); 
        tG2D.drawImage(inBufferedImage, 0, 0, tWidth, tHeight, 0, tHeight, tWidth, 0, null); 
        tG2D.dispose(); 
        return tFlippedBufferedImage; 
	} 
	
	/**
	 * Convienance function for loading a texture
	 * @param file
	 * @return Texture object at file
	 */
	private static Texture loadTexture(String file)
	{
		try {
		BufferedImage tBufferedImage = ImageIO.read(new BufferedInputStream(new FileInputStream(file))); 
        return TextureIO.newTexture(flipBufferedImageVertical(tBufferedImage), false);
		}
		catch(Exception e)
		{
			ErrorHandler.handleException(e);
			return null;
		}
	}
	

	/**
	 * OpenGL reshape function
	 * Perspective and view calls go here
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		final GL gl = drawable.getGL();
		final GLU glu = new GLU();
		
		gl.setSwapInterval(1);

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(
				45.0f, 
				(double) width / (double) height, 
				0.1f,
				1000.0f);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * Initializes vertex array native buffers
	 */
	private void initVertexArrays()
	{
		float java_tex_coords[] = new float[] {
				
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				
				
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				
				
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				
				
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f, 
				/*
				1,1,  -1,1,  -1,-1,  1,-1,        // v0-v1-v2-v3
				1,1,  1,-1,  1,-1,  1,1,        // v0-v3-v4-v5
			    1,1,  1,1,  -1,1,  -1,1,        // v0-v5-v6-v1
				-1,1,  -1,1,  -1,-1,  -1,-1,    // v1-v6-v7-v2
				-1,-1,  1,-1,  1,-1,  -1,-1,    // v7-v4-v3-v2
				1,-1,  -1,-1,  -1,1,  1,1   // v4-v7-v6-v5
				*/
		};
		                                  
		
		
		float java_vertices[] = new float[] {
				/*1.0f, 0.0f, -1.0f,
				0.0f, 1.0f, -1.0f,
				0.0f, 0.0f, -1.0f,
				
				1.0f, 0.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				0.0f, 1.0f, -1.0f, */
				// vertex coords array
				1,1,1,  -1,1,1,  -1,-1,1,  1,-1,1,        // v0-v1-v2-v3
				1,1,1,  1,-1,1,  1,-1,-1,  1,1,-1,        // v0-v3-v4-v5
			    1,1,1,  1,1,-1,  -1,1,-1,  -1,1,1,        // v0-v5-v6-v1
				-1,1,1,  -1,1,-1,  -1,-1,-1,  -1,-1,1,    // v1-v6-v7-v2
				-1,-1,-1,  1,-1,-1,  1,-1,1,  -1,-1,1,    // v7-v4-v3-v2
				1,-1,-1,  -1,-1,-1,  -1,1,-1,  1,1,-1   // v4-v7-v6-v5
				
		};
		vertices = BufferUtil.newFloatBuffer(java_vertices.length);
		for(int i = 0; i < java_vertices.length; i++)
		{
			vertices.put(java_vertices[i]);
		}
		vertices.rewind();
		
		texCoords = BufferUtil.newFloatBuffer(java_tex_coords.length);
		for(int i = 0; i < java_tex_coords.length; i++)
		{
			texCoords.put(java_tex_coords[i]);
		}
		texCoords.rewind();
		
		
		// normal array
		float java_normals[] = {0,0,1,  0,0,1,  0,0,1,  0,0,1,             // v0-v1-v2-v3
		                     1,0,0,  1,0,0,  1,0,0, 1,0,0,              // v0-v3-v4-v5
		                     0,1,0,  0,1,0,  0,1,0, 0,1,0,              // v0-v5-v6-v1
		                     -1,0,0,  -1,0,0, -1,0,0,  -1,0,0,          // v1-v6-v7-v2
		                     0,-1,0,  0,-1,0,  0,-1,0,  0,-1,0,         // v7-v4-v3-v2
		                     0,0,-1,  0,0,-1,  0,0,-1,  0,0,-1};        // v4-v7-v6-v5
		normals = FloatArrayToFloatBuffer(java_normals);
	}
	
	private static FloatBuffer FloatArrayToFloatBuffer(float[] array)
	{
		FloatBuffer fb = BufferUtil.newFloatBuffer(array.length);
		for(int i = 0; i < array.length; i++)
		{
			fb.put(array[i]);
		}
		fb.rewind();
		return fb;
	}
}


/**
 * Class encapsulating both an Animal's Texture object and a name to associate it with
 * @author thomas
 *
 */
class AnimalTexture
{
	private Texture texture;
	private String  name;
	AnimalTexture(Texture tex, String Name)
	{
		texture = tex;
		name = Name;
	}
	
	public String getName() { return name; }
	public Texture getTexture() { return texture; }
	
	public void bind()
	{
		texture.bind();
	}
	
	/**
	 * Utility method to find an AnimalTexture object by name in an AnimalTexture array
	 * returns null if no object with that name exists in the array
	 * @param animal_tex_array
	 * @param SEARCH_FOR
	 * @return
	 */
	public static AnimalTexture findAnimalTexture(AnimalTexture[] animal_tex_array,
	                                                      String SEARCH_FOR)
	{
		if(animal_tex_array == null)
			return null;
		
		for(int i = 0; i < animal_tex_array.length; i++)
		{
			if(animal_tex_array[i].getName().equals(SEARCH_FOR))
				return animal_tex_array[i];
		}
		return null;
	}
}