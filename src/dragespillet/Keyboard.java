/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dragespillet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Keyboard {

    private boolean right;
    private boolean left;
    private boolean up;
    private boolean down;
    private boolean jumping;

    public void update(GameContainer gc, int delta) {
        this.right = gc.getInput().isKeyDown(Input.KEY_RIGHT);
        this.left = gc.getInput().isKeyDown(Input.KEY_LEFT);
        this.up = gc.getInput().isKeyDown(Input.KEY_UP);
        this.down = gc.getInput().isKeyDown(Input.KEY_DOWN);
        this.jumping = gc.getInput().isKeyPressed(Input.KEY_SPACE);
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isJumping() {
        return jumping;
    }
}
