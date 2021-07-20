package gl.game;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;


import org.joml.Matrix4f;

import gl.engine.GameItem;
import gl.engine.Utils;
import gl.engine.Window;
import gl.engine.graph.ShaderProgram;
import gl.engine.graph.Transformation;

public class Renderer {

  
    private ShaderProgram shaderProgram;
    private Transformation tranformation;
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    private final String PROJ_MATRIX_NAME = "projectionMatrix";
    private final String WORLD_MAT_NAME = "worldMatrix";
    private final String TEXTURE_SAMPLER = "texture_sampler";

    public Renderer() {
        this.tranformation = new Transformation();
    }

    public void init(Window window) throws Exception {

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

      
        shaderProgram.createUniform(PROJ_MATRIX_NAME);
        shaderProgram.createUniform(WORLD_MAT_NAME);
        shaderProgram.createUniform(TEXTURE_SAMPLER);

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        shaderProgram.bind();
        
        Matrix4f projectionMatrix = tranformation.getProjectionMatrix(Renderer.FOV, window.getWidth(), window.getWidth(), Renderer.Z_NEAR, Renderer.Z_FAR);
        shaderProgram.setUniform(PROJ_MATRIX_NAME, projectionMatrix);

        shaderProgram.setUniform(TEXTURE_SAMPLER, 0);

        for(GameItem gameItem: gameItems){
            Matrix4f worldMatrix = tranformation.getWorldMatrix(gameItem.getPosition(), gameItem.getRotation(), gameItem.getScale());
            shaderProgram.setUniform(WORLD_MAT_NAME, worldMatrix);
            gameItem.getMesh().render();
        }


        shaderProgram.unbind();

    }

    public void cleanup() {

        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

    }
}
