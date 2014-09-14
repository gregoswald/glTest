package render;


import org.lwjgl.opengl.GL11;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {
	
	
	public void render(int vaoID, int vCount){
		GL11.glClearColor(0.6f,0.6f,0.6f,0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES,0, vCount);;
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);//unbind
	}
	
	

}