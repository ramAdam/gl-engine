package gl.game;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import org.joml.Matrix4f;

import gl.engine.Utils;
import gl.engine.Window;
import gl.engine.graph.Mesh;
import gl.engine.graph.ShaderProgram;

public class Renderer {

  
    private ShaderProgram shaderProgram;
    private Matrix4f projectionMatrix;
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    private final String PROJ_MATRIX_NAME = "projectionMatrix";



    public Renderer() {
    }

    public void init(Window window) throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

        float aspectRatio = (float)window.getWidth()/window.getHeight();
        projectionMatrix = new Matrix4f().perspective(Renderer.FOV, aspectRatio, Renderer.Z_NEAR, Renderer.Z_FAR);
        shaderProgram.createUniform(PROJ_MATRIX_NAME);

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();
        shaderProgram.setUniform(PROJ_MATRIX_NAME, projectionMatrix);

        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        shaderProgram.unbind();

    }

    public void cleanup() {

        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

    }
}
