package render;

public class GlTest {
	public static void main(String[] args) {
		renderer render = new renderer();
		render.initDisplay();
		render.intitShape();
		render.intitShaders();
		render.startLoop();
		
	}
}