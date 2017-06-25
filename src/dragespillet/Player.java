package dragespillet;

import org.newdawn.slick.Animation;
import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Player implements ControllerListener {

    public Position position;

    public float walkingSpeed;
    public float jumpingSpeed;
    public float fallingSpeed;

    private final Camera camera;
    private final static int A_BUTTON = 1;
    private boolean controller = false;
    private boolean controller_left;
    private boolean controller_right;

    private boolean left;
    private boolean right;
    private boolean falling;
    private boolean jumping;
    private float jumpTimer = 0;

    private Animation currentAnimation, idleAnimation, walkingAnimation, fallingAnimation;

    public Player(Camera camera) {
        this.position = new Position(0f, 0f, 60, 60);
        this.walkingSpeed = 2f;
        this.fallingSpeed = 4f;
        this.jumpingSpeed = 6f;
        this.camera = camera;
    }

    void init(GameContainer gc) throws SlickException {
        gc.getInput().addControllerListener(this);
        SpriteSheet sheet = new SpriteSheet("sprites/playersprites.gif", 30, 30);
        Image[] idle = new Image[]{
            sheet.getSubImage(0, 0),
            sheet.getSubImage(1, 0)
        };
        this.idleAnimation = new Animation(idle, 400, false);
        Image[] walking = new Image[]{
            sheet.getSubImage(0, 1),
            sheet.getSubImage(1, 1),
            sheet.getSubImage(2, 1),
            sheet.getSubImage(3, 1),
            sheet.getSubImage(4, 1),
            sheet.getSubImage(5, 1),
            sheet.getSubImage(6, 1),
            sheet.getSubImage(7, 1)
        };
        this.walkingAnimation = new Animation(walking, 40, false);
        Image[] fallingSequence = new Image[]{
            sheet.getSubImage(0, 3),
            sheet.getSubImage(1, 3)
        };
        this.fallingAnimation = new Animation(fallingSequence, 200, false);
        this.currentAnimation = this.idleAnimation;

    }

    public void update(GameContainer gc, int delta) {
        if (gc.getInput().isKeyPressed(Input.KEY_C)) {
            this.controller = true;
        }
        if (!jumping && gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            this.jumping = true;
            this.jumpTimer = 20f;
        }
        this.right = gc.getInput().isKeyDown(Input.KEY_RIGHT) || this.controller_right;
        this.left = gc.getInput().isKeyDown(Input.KEY_LEFT) || this.controller_left;
        if (jumping || falling) {
            this.currentAnimation = this.fallingAnimation;
        } else if (right || left) {
            this.currentAnimation = this.walkingAnimation;
        } else {
            this.currentAnimation = this.idleAnimation;
        }
        this.currentAnimation.update(delta);
        checkCollision(delta);
    }

    public void checkCollision(int delta) {
        float newXPos = this.position.x, newYPos = this.position.y;
        if (this.right) {
            newXPos += this.walkingSpeed * delta / 1000f;
            if (newXPos > this.camera.mapWidth || this.camera.checkCollision(this.position.copy(newXPos, newYPos).getRect())) {
                newXPos = this.position.x;
            }
        }
        if (this.left) {
            newXPos -= this.walkingSpeed * delta / 1000f;
            if (newXPos < 0 || this.camera.checkCollision(this.position.copy(newXPos, newYPos).getRect())) {
                newXPos = this.position.x;
            }
        }
        if (this.jumping) {
            newYPos -= this.jumpingSpeed * delta / 1000f;
            if (newYPos < 0 || this.camera.checkCollision(this.position.copy(newXPos, newYPos).getRect())) {
                newYPos = this.position.y;
                this.jumpTimer = 0;
            } else {
                this.jumpTimer--;
            }
            if (this.jumpTimer <= 0) {
                this.jumping = false;
            }
        }
        if (!this.jumping && !this.camera.checkCollision(this.position.copy(newXPos, newYPos + (this.fallingSpeed * delta / 1000f)).getRect())) {
            newYPos += this.fallingSpeed * delta / 1000f;
            this.falling = true;
        } else {
            this.falling = false;
        }
        this.position.setX(newXPos);
        this.position.setY(newYPos);
    }

    void render(Graphics g) {
        Rectangle rect = this.position.getRect();
        this.currentAnimation.getCurrentFrame().getFlippedCopy(this.left, false).draw(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    @Override
    public void controllerLeftPressed(int controller) {
        this.controller_left = true;
    }

    @Override
    public void controllerLeftReleased(int controller) {
        this.controller_left = false;
    }

    @Override
    public void controllerRightPressed(int controller) {
        this.controller_right = true;
    }

    @Override
    public void controllerRightReleased(int controller) {
        this.controller_right = false;
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
        if (!jumping && button == A_BUTTON) {
            this.jumping = true;
            this.jumpTimer = 20f;
        }
    }

    @Override
    public void controllerButtonReleased(int controller, int button) {
    }

    @Override
    public void setInput(Input input) {
    }

    @Override
    public boolean isAcceptingInput() {
        return controller;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }
}
