package render;

public class StupidShader extends Shader {
	private static final String vertexFile = "src/shaders/StupidVertexShader.glsl";
	private static final String fragmentFile = "src/shaders/StupidFragmentShader.glsl";
	
	public StupidShader() {
		super(vertexFile, fragmentFile);
	}
	

}
