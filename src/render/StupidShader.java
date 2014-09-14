package render;

public class StupidShader extends Shader {
	private static final String vertexFile = "src/shaders/StupidVertexShader";
	private static final String fragmentFile = "src/shaders/StupidFragmentShader";
	
	public StupidShader() {
		super(vertexFile, fragmentFile);
	}
	

}
