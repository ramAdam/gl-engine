package gl.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import org.joml.Vector2f;
import org.joml.Vector3f;

import gl.engine.GameItem;
import gl.engine.IGameLogic;
import gl.engine.MouseInput;
import gl.engine.Window;
import gl.engine.graph.Camera;
import gl.engine.graph.Mesh;
import gl.engine.graph.OBJLoader;
import gl.engine.graph.Texture;

public class DummyGame implements IGameLogic {

    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Renderer renderer;

    private Mesh mesh;
    private GameItem[] gameItems;

    private final Vector3f cameraInc;


    private Camera camera;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        
        renderer.init(window);

        mesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("textures/grassblock.png");
        mesh.setTexture(texture);

        GameItem gameItem1 = new GameItem(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPosition(0, 0, -2);
   
        gameItems = new GameItem[]{gameItem1};
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
         // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

           // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, this.camera, gameItems);
    }

    @Override
    public void cleanup() {
       renderer.cleanup();
       for(GameItem gameItem: gameItems){
           gameItem.getMesh().cleanUp();
       }
    }

}
