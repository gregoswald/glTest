package render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

public class Shader {
	private int vsId = 0;
	private int fsId = 0;
	private int sProg = 0;
	
	public Shader(String vFile, String fFile) {
		int errorCheckValue = GL11.glGetError();
		vsId = this.loadShaderData(vFile, GL20.GL_VERTEX_SHADER);
		fsId = this.loadShaderData(fFile, GL20.GL_FRAGMENT_SHADER);
		
		sProg = GL20.glCreateProgram();
		GL20.glAttachShader(sProg, vsId);
		GL20.glAttachShader(sProg, fsId);
		GL20.glBindAttribLocation(sProg, 0, "in_Position");
		GL20.glBindAttribLocation(sProg, 1, "in_Color");
		GL20.glLinkProgram(sProg);
		GL20.glValidateProgram(sProg);
		
		errorCheckValue = GL11.glGetError();
		if (errorCheckValue != GL11.GL_NO_ERROR) {
			System.out.println("ERROR - Could not create the shaders:" + GLU.gluErrorString(errorCheckValue));
			System.exit(-1);
		}
	}
	
	private int loadShaderData(String file, int type) {
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

	public int prog() {
		return sProg;
	}

	public int vShad() {
		return vsId;
	}

	public int fShad() {
		return fsId;
	}
}
