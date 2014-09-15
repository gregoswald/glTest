package render;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import org.lwjgl.util.glu.GLU;


public class renderer {
		private final int WIDTH = 800;
		private final int HEIGHT = 600;
		private int vaoId = 0;
		private int vboId = 0;
		private int vbocId = 0;
		private int vboiId = 0;
		private int indicesCount = 0;
		private int vsId = 0;
		private int fsId = 0;
		private int shaderProg = 0;
		
		public renderer() {
			
			this.initDisplay();
			this.initShape();
			this.initShaders();
			
			System.out.println("Rendering With following parameters..");
	        System.out.println("vaoId:"+vaoId);
	        System.out.println("vboId:"+vboId);
	        System.out.println("vbocId:"+ vbocId);
	        System.out.println("vboiId:"+ vboiId);
	        System.out.println("iCount:"+ indicesCount);
	        System.out.println("shaderProg:"+ shaderProg);
	        System.out.println("vShaderID:"+ vsId);
	        System.out.println("fShaderID:"+ fsId);
			while (!Display.isCloseRequested()) {
				
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				GL20.glUseProgram(shaderProg);
				GL30.glBindVertexArray(vaoId);
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
				GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
				GL20.glDisableVertexAttribArray(0);
				GL20.glDisableVertexAttribArray(1);
				GL30.glBindVertexArray(0);
				GL20.glUseProgram(0);
				Display.sync(60);
				Display.update();
			}
			
			
			this.cleanUp();
		}

		public void initDisplay() {
			
			try {
				PixelFormat pixelFormat = new PixelFormat();
				ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
					.withForwardCompatible(true)
					.withProfileCore(true);
				
				Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
				Display.setTitle("glTest");
				Display.create(pixelFormat, contextAtrributes);
				GL11.glViewport(0, 0, WIDTH, HEIGHT);
			} catch (LWJGLException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			GL11.glClearColor(0.6f, 0.6f, 0.6f, 0f);
			GL11.glViewport(0, 0, WIDTH, HEIGHT);
		}
		
		public void initShape() {
			
			float[] vertices = {
					-0.5f, 0.5f, 0f, 1f,
					-0.5f, -0.5f, 0f, 1f,
					0.5f, -0.5f, 0f, 1f,
					0.5f, 0.5f, 0f, 1f
			};
			FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
			verticesBuffer.put(vertices);
			verticesBuffer.flip();
			
			float[] colors = {
					0.2f, 0.4f, 0.6f, 0f,//top right
					0f, 0f, 0f, 0f,
					0f, 0f, 0f, 0f,
					0.2f, 0.4f, 0.6f, 0f,//top left
			};
			FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
			colorsBuffer.put(colors);
			colorsBuffer.flip();
			byte[] indices = {
					0, 1, 2,
					2, 3, 0
			};
			indicesCount = indices.length;
			ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
			indicesBuffer.put(indices);
			indicesBuffer.flip();
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			
			vboId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			vbocId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbocId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			
			
			GL30.glBindVertexArray(0);
			
			
			vboiId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		
		private void initShaders() {
			int errorCheckValue = GL11.glGetError();
			vsId = this.loadShaderData("src/shaders/VertexShader.glsl", GL20.GL_VERTEX_SHADER);
			fsId = this.loadShaderData("src/shaders/FragmentShader.glsl", GL20.GL_FRAGMENT_SHADER);
			
			shaderProg = GL20.glCreateProgram();
			GL20.glAttachShader(shaderProg, vsId);
			GL20.glAttachShader(shaderProg, fsId);
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
		public void cleanUp() {
			// Delete the shaders
			GL20.glUseProgram(0);
			GL20.glDetachShader(shaderProg, vsId);
			GL20.glDetachShader(shaderProg, fsId);
			
			GL20.glDeleteShader(vsId);
			GL20.glDeleteShader(fsId);
			GL20.glDeleteProgram(shaderProg);
			
			// Select the VAO
			GL30.glBindVertexArray(vaoId);
			
			// Disable the VBO index from the VAO attributes list
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			
			// Delete the vertex VBO
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL15.glDeleteBuffers(vboId);
			
			// Delete the color VBO
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL15.glDeleteBuffers(vbocId);
			
			// Delete the index VBO
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			GL15.glDeleteBuffers(vboiId);
			
			// Delete the VAO
			GL30.glBindVertexArray(0);
			GL30.glDeleteVertexArrays(vaoId);
			
			Display.destroy();
		}
		
		public int loadShaderData(String file, int type) {
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
