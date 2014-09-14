package render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class renderer {
	private  final int WIDTH = 800;
	private  final int HEIGHT = 600;
	private  final int fps = 60;
	private int vaoId = 0;
	private int vboId = 0;
	private int vbocId = 0;
	private int vboiId = 0;
	private int iCount;
	private static final String vertexFile = "src/shaders/VertexShader.glsl";
	private static final String fragmentFile = "src/shaders/FragmentShader.glsl";
	private int shaderProg = 0;
	private int vShaderID;
	private int fShaderID;

	public void startLoop(){
		
	while(!Display.isCloseRequested()){
		
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL20.glUseProgram(shaderProg);
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, iCount, GL11.GL_UNSIGNED_INT, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
		Display.sync(fps);
		Display.update();
	}
	
	Display.destroy();
}
public void initDisplay(){
ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
try {
	Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
	Display.create(new PixelFormat(), attribs);
	//Display.create();
	Display.setTitle("glTest");
	GL11.glViewport(0,0,Display.getWidth(),Display.getHeight());
} catch (LWJGLException e) {
	e.printStackTrace();//do something more usefull here.
	System.exit(-1);
}
GL11.glClearColor(0.6f,0.6f,0.6f,0);
GL11.glViewport(0,0,Display.getWidth(),Display.getHeight());

}

public void intitShape(){
float[] vertices = {
		-0.5f,0.5f,0,1f
	    -0.5f,-0.5f,0,1f,
		0.5f,-0.5f,0,1f,
		0.5f,0.5f,0,1f
};

FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertices.length);
vBuffer.put(vertices);
vBuffer.flip();//switch to read.

float[] colors = {
		1f, 0f, 0f, 1f,
		0f, 1f, 0f, 1f,
		0f, 0f, 1f, 1f,
		1f, 1f, 1f, 1f,
		};

FloatBuffer cBuffer = BufferUtils.createFloatBuffer(colors.length);
cBuffer.put(colors);
cBuffer.flip();

byte[] indices = {
		0,1,2,
		2,3,0
};
ByteBuffer iBuffer = BufferUtils.createByteBuffer(indices.length);
iBuffer.put(indices);
iBuffer.flip();
iCount = indices.length;

vaoId = GL30.glGenVertexArrays();
GL30.glBindVertexArray(vaoId);

vboId = GL15.glGenBuffers();
GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vBuffer, GL15.GL_STATIC_DRAW);
GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

vbocId = GL15.glGenBuffers();
GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cBuffer, GL15.GL_STATIC_DRAW);
GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

GL30.glBindVertexArray(0);

vboiId = GL15.glGenBuffers();
GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);
GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
}

public void intitShaders(){
	int errorCheckValue = GL11.glGetError();
	vShaderID = parseShaderFile(vertexFile, GL20.GL_VERTEX_SHADER);
	fShaderID = parseShaderFile(fragmentFile, GL20.GL_FRAGMENT_SHADER);
	shaderProg = GL20.glCreateProgram();
	GL20.glAttachShader(shaderProg, vShaderID);
	GL20.glAttachShader(shaderProg, fShaderID);
	GL20.glBindAttribLocation(shaderProg, 0, "in_Position");
	GL20.glBindAttribLocation(shaderProg, 1, "in_Color");
	GL20.glLinkProgram(shaderProg);
	GL20.glValidateProgram(shaderProg);
	errorCheckValue = GL11.glGetError();
	if (errorCheckValue != GL11.GL_NO_ERROR) {
		System.out.println("ERROR - Could not create the shaders:" + GLU.gluErrorString(errorCheckValue));
		System.exit(-1);
	}
}

public int parseShaderFile(String file, int type){
	StringBuilder sCode = new StringBuilder();
	try {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String nextLine;
		while((nextLine = reader.readLine())!=null){
			sCode.append(nextLine).append("\n");
		}
	reader.close();
	
	} 
	catch (IOException e) {
		e.printStackTrace();//TODO do something more usefull here
	}
	int shaderID = GL20.glCreateShader(type);
	if(type == GL20.GL_FRAGMENT_SHADER){
		System.out.println("Compiling Fragment Shader..");
	}
	else{
		System.out.println("Compiling Vertex Shader..");
	}
	System.out.print(sCode);
	GL20.glShaderSource(shaderID, sCode);
	GL20.glCompileShader(shaderID);
	if(GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
		System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
		System.exit(-1);
	}
	System.out.println("..Done.");
	return shaderID;
}

}
