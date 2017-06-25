/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragespillet;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.geom.Rectangle;

public class Position extends Vector2f {

    private final int tileWidth;
    private final int tileHeight;

    public Position(float x, float y, int tileWidth, int tileHeight) {
        super(x, y);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public Position copy(float newX, float newY) {
        return new Position(newX, newY, tileWidth, tileHeight);
    }

    public Rectangle getRect() {
        return new Rectangle(this.x * tileWidth, this.y * tileHeight, tileWidth, tileHeight);
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }
}
