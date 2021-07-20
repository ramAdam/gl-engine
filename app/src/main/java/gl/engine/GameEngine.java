package gl.engine;

public class GameEngine implements Runnable {

    private final Window window;
    private final Timer timer;

    private final IGameLogic gameLogic;
    private static final int TARGET_UPS = 30;
    private static final int TARGET_FPS = 75;

    private final MouseInput mouseInput;

    public GameEngine(String title, int width, int height, boolean vSync, IGameLogic gameLogic) {
        window = new Window(title, width, height, vSync);
        timer = new Timer();
        this.gameLogic = gameLogic;
        this.mouseInput = new MouseInput();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }

    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init(this.window);
        gameLogic.init(this.window);
    }

    protected void gameLoop() {
       
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    protected void render() {
        this.gameLogic.render(this.window);
        this.window.update();
    }

    protected void update(float interval) {
        this.gameLogic.update(interval, this.mouseInput);
    }

    protected void input() {
        this.mouseInput.input(this.window);
        this.gameLogic.input(this.window, mouseInput);
    }

    protected void cleanup() {
        gameLogic.cleanup();

    }

}
