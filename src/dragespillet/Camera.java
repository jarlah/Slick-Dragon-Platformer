/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragespillet;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

public class Camera {

    /**
     * the map used for our scene
     */
    protected TiledMap map;

    /**
     * the number of tiles in x-direction (width)
     */
    protected int numTilesX;

    /**
     * the number of tiles in y-direction (height)
     */
    protected int numTilesY;

    /**
     * the height of the map in pixel
     */
    protected int mapHeight;

    /**
     * the width of the map in pixel
     */
    protected int mapWidth;

    /**
     * the width of one tile of the map in pixel
     */
    protected int tileWidth;

    /**
     * the height of one tile of the map in pixel
     */
    protected int tileHeight;

    /**
     * the game size
     */
    private final int gameWidth;
    private final int gameHeight;

    /**
     * the x-position of our "camera" in pixel
     */
    protected float cameraX;

    /**
     * the y-position of our "camera" in pixel
     */
    protected float cameraY;

    private final boolean[][] blocked;
    private final List<Rectangle> blocks;

    /**
     * Create a new camera
     *
     * @param gameWidth
     * @param gameHeight
     * @param map the TiledMap used for the current scene
     */
    public Camera(int gameWidth, int gameHeight, TiledMap map) {
        this.map = map;

        this.numTilesX = map.getWidth();
        this.numTilesY = map.getHeight();

        this.tileWidth = map.getTileWidth();
        this.tileHeight = map.getTileHeight();

        this.mapWidth = this.numTilesX * this.tileWidth;
        this.mapHeight = this.numTilesY * this.tileHeight;

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        int collisionLayer = map.getLayerIndex("Collision");
        blocked = new boolean[map.getWidth()][map.getHeight()];
        blocks = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int id = map.getTileId(x, y, collisionLayer);
                if (id != 0) {
                    blocked[x][y] = true;
                    blocks.add(new Rectangle((float) x * tileWidth, (float) y * tileHeight, tileWidth, tileHeight));
                }
            }
        }

    }

    /**
     * "locks" the camera on the given coordinates. The camera tries to keep the
     * location in it's center.
     *
     * @param x the real x-coordinate (in pixel) which should be centered on the
     * screen
     * @param y the real y-coordinate (in pixel) which should be centered on the
     * screen
     */
    public void centerOn(float x, float y) {
        cameraX = getCameraX(x);
        cameraY = getCameraY(y);
    }

    public boolean checkCollision(Rectangle shape) {
        boolean collision = false;
        for (Rectangle ret : blocks) {
            if (ret.intersects(shape)) {
                collision = true;
            }
        }
        return collision;
    }

    private float getCameraX(float playerX) {
        float newX = playerX - gameWidth / 2;

        if (newX < 0) {
            newX = 0;
        }
        if (newX + gameWidth > mapWidth) {
            newX = mapWidth - gameWidth;
        }

        return newX;
    }

    private float getCameraY(float playerY) {
        float newY = playerY - gameHeight / 2;

        if (newY < 0) {
            newY = 0;
        }
        if (newY + gameHeight > mapHeight) {
            newY = mapHeight - gameHeight;
        }

        return newY;
    }

    /**
     * "locks" the camera on the center of the given Rectangle. The camera tries
     * to keep the location in it's center.
     *
     * @param x the x-coordinate (in pixel) of the top-left corner of the
     * rectangle
     * @param y the y-coordinate (in pixel) of the top-left corner of the
     * rectangle
     * @param height the height (in pixel) of the rectangle
     * @param width the width (in pixel) of the rectangle
     */
    public void centerOn(float x, float y, float height, float width) {
        this.centerOn(x + width / 2, y + height / 2);
    }

    /**
     * "locks the camera on the center of the given Shape. The camera tries to
     * keep the location in it's center.
     *
     * @param shape the Shape which should be centered on the screen
     */
    public void centerOn(Shape shape) {
        this.centerOn(shape.getCenterX(), shape.getCenterY());
    }

    /**
     * draws the part of the map which is currently focussed by the camera on
     * the screen
     */
    public void drawMap() {
        this.drawMap(0, 0);
    }

    /**
     * draws the part of the map which is currently focussed by the camera on
     * the screen.<br>
     * You need to draw something over the offset, to prevent the edge of the
     * map to be displayed below it<br>
     * Has to be called before Camera.translateGraphics() !
     *
     * @param offsetX the x-coordinate (in pixel) where the camera should start
     * drawing the map at
     * @param offsetY the y-coordinate (in pixel) where the camera should start
     * drawing the map at
     */
    public void drawMap(int offsetX, int offsetY) {
        //calculate the offset to the next tile (needed by TiledMap.render())
        int tileOffsetX = (int) -(cameraX % tileWidth);
        int tileOffsetY = (int) -(cameraY % tileHeight);

        //calculate the index of the leftmost tile that is being displayed
        int tileIndexX = (int) (cameraX / tileWidth);
        int tileIndexY = (int) (cameraY / tileHeight);

        //finally draw the section of the map on the screen
        map.render(
                tileOffsetX + offsetX,
                tileOffsetY + offsetY,
                tileIndexX,
                tileIndexY,
                (gameWidth - tileOffsetX) / tileWidth + 1,
                (gameHeight - tileOffsetY) / tileHeight + 1);
    }

    /**
     * Translates the Graphics-context to the coordinates of the map - now
     * everything can be drawn with it's NATURAL coordinates.
     *
     * @param g
     */
    public void translateGraphics(Graphics g) {
        g.translate(-cameraX, -cameraY);
    }

    /**
     * Reverses the Graphics-translation of Camera.translatesGraphics(). Call
     * this before drawing HUD-elements or the like
     *
     * @param g
     */
    public void untranslateGraphics(Graphics g) {
        g.translate(cameraX, cameraY);
    }

}
