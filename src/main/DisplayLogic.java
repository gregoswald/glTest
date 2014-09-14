package main;

import java.nio.FloatBuffer;

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
import render.Renderer;
import render.StupidShader;

public class DisplayLogic {
	private static final int WIDTH = 320;
	private static final int HEIGHT = 200;
	private static final int fps = 60;
	
	public static void main(String[] args) {
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs);
			//Display.create();
			Display.setTitle("glTest");
			GL11.glViewport(0,0,Display.getHeight(),Display.getWidth());
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
		int[] indices = {
				0,1,3,
				2,3,0
		};
		//todo idices buffer
		
		
		
		int vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		int vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,vBuffer,GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(vaoId, 3,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		Renderer renderer = new Renderer();
		StupidShader shader = new StupidShader();
		//shader.bindAttributes();
		while(!Display.isCloseRequested()){
			shader.start();
			renderer.render(vaoId,vertices.length);
			shader.stop();   
			Display.sync(fps);
			Display.update();
		}
		//shader.clearShaders();
		
		Display.destroy();
	}

}
