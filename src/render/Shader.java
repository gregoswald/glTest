package render;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Shader {
	private int shaderProgram;
	private int vShaderID;
	private int fShaderID;
	public Shader(String vFile, String fFile){
		vShaderID = parseShaderFile(vFile, GL20.GL_VERTEX_SHADER);
		fShaderID = parseShaderFile(fFile, GL20.GL_FRAGMENT_SHADER);
		shaderProgram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgram, vShaderID);
		GL20.glAttachShader(shaderProgram, fShaderID);
		GL20.glBindAttribLocation(shaderProgram, 0, "in_position");
		GL20.glLinkProgram(shaderProgram);
		GL20.glValidateProgram(shaderProgram);
	}
	
	public void start(){
		GL20.glUseProgram(shaderProgram);
	}
	public void stop(){
		GL20.glUseProgram(0);
	}
	public void clearShaders(){
		stop();
		GL20.glDetachShader(shaderProgram, fShaderID);
		GL20.glDetachShader(shaderProgram, vShaderID);
		GL20.glDeleteShader(fShaderID);
		GL20.glDeleteShader(vShaderID);
		GL20.glDeleteProgram(shaderProgram);
	}
	private static int parseShaderFile(String file, int type){
		StringBuilder shader = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String nextLine;
			while((nextLine = reader.readLine())!=null){
				shader.append(nextLine).append("\n");
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
		GL20.glShaderSource(shaderID, shader);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
		System.out.println("..Done.");
		return shaderID;
	}
	
}
