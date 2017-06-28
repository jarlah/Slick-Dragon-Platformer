package dragespillet;

import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class Controller implements ControllerListener {

    private final static int A_BUTTON = 1;
    private final static int X_BUTTON = 2;
    
    private boolean active = false;
    private boolean left = false;
    private boolean right = false;
    private boolean jumping = false;
    private boolean grabbing = false;
    
    public void update(GameContainer gc, int delta) {
        if (gc.getInput().isKeyPressed(Input.KEY_C)) {
            this.active = true;
        }
    }

    @Override
    public void controllerLeftPressed(int controller) {
        this.left = true;
    }

    @Override
    public void controllerLeftReleased(int controller) {
        this.left = false;
    }

    @Override
    public void controllerRightPressed(int controller) {
        this.right = true;
    }

    @Override
    public void controllerRightReleased(int controller) {
        this.right = false;
    }

    @Override
    public void controllerUpPressed(int controller) {
    }

    @Override
    public void controllerUpReleased(int controller) {
    }

    @Override
    public void controllerDownPressed(int controller) {
    }

    @Override
    public void controllerDownReleased(int controller) {
    }

    @Override
    public void controllerButtonPressed(int controller, int button) {
        if (button == A_BUTTON) {
            this.jumping = true;
        }
        if (button == X_BUTTON) {
            this.grabbing = true;
        }
    }

    @Override
    public void controllerButtonReleased(int controller, int button) {
        if (button == A_BUTTON) {
            this.jumping = false;
        }
        if (button == X_BUTTON) {
            this.grabbing = false;
        }
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public boolean isAcceptingInput() {
        return active;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isJumping() {
        return jumping;
    }

    public boolean isGrabbing() {
        return grabbing;
    }
}
