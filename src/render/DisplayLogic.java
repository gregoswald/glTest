package render;

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

public class DisplayLogic {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int fps = 60;
	
	public static void main(String[] args) {
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs);
			//Display.create();
			Display.setTitle("glTest");
			GL11.glViewport(0,0,Display.getWidth(),Display.getHeight());
		} catch (LWJGLException e) {
			e.printStackTrace();//do something more usefull here.
		}
		GL11.glViewport(0,0,Display.getWidth(),Display.getHeight());
		
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
		
		int[] indices = {
				0,1,3,
				2,3,0
		};
		IntBuffer iBuffer = BufferUtils.createIntBuffer(indices.length);
		iBuffer.put(indices);
		iBuffer.flip();
		
		
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		 
		
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		 
		
		int vbocId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		int vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		Shader shader = new Shader();
		
		while(!Display.isCloseRequested()){
			
			GL11.glClearColor(0.6f,0.6f,0.6f,0);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			GL20.glUseProgram(shader.shaderProg());
			GL30.glBindVertexArray(vaoId);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
			GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0);
			
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
			GL20.glUseProgram(0);
			Display.sync(fps);
			Display.update();
		}
		//shader.clearShaders();
		
		Display.destroy();
	}

}
