package dragespillet;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class DrageSpillet extends BasicGame {

    private TiledMap map;
    private Camera camera;
    private Player player;
    private Image background;
    private Controller controller;
    private Keyboard keyboard;

    public DrageSpillet(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        map = new TiledMap("maps/game.tmx", "maps");
        camera = new Camera(gc.getWidth(), gc.getHeight(), map);
        controller = new Controller();
        gc.getInput().addControllerListener(controller);
        keyboard = new Keyboard();
        player = new Player(camera, keyboard, controller);
        player.init(gc);
        background = new Image("textures/background.jpg");
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        keyboard.update(gc, i);
        controller.update(gc, i);
        player.update(gc, i);
        camera.centerOn(player.position.getRect());
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        background.draw(0, 0, gc.getWidth(), gc.getHeight());
        camera.drawMap();
        camera.translateGraphics(g);
        player.render(g);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new DrageSpillet("DrageSpillet"));
            appgc.setDisplayMode(1024, 768, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(DrageSpillet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
