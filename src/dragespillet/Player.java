package dragespillet;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public class Player {

    public Position position = new Position(0f, 0f, 60, 60);

    public float walkingSpeed = 0.002f;
    public float jumpingSpeed = 0.003f;
    public float fallingSpeed = 0.003f;

    private final Camera camera;
    private final Controller controller;
    private final Keyboard keyboard;

    private boolean left = false;
    private boolean right = false;
    private boolean falling = false;
    private boolean jumping = false;
    private int jumpTimer;

    private Animation currentAnimation, idleAnimation, walkingAnimation, fallingAnimation;
    private Animation grabbingAnimation;
    private boolean grabbing;

    public Player(Camera camera, Keyboard keyboard, Controller controller) {
        this.camera = camera;
        this.controller = controller;
        this.keyboard = keyboard;
    }

    void init(GameContainer gc) throws SlickException {
        Image sprites = new Image("sprites/playersprites.gif");
        SpriteSheet sheet = new SpriteSheet(sprites, 30, 30);
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
        SpriteSheet sheetGrabbing = new SpriteSheet(sprites, 60, 30);
        Image[] grabbingImgs = new Image[]{
            sheetGrabbing.getSubImage(0, 6),
            sheetGrabbing.getSubImage(1, 6),
            sheetGrabbing.getSubImage(2, 6),
            sheetGrabbing.getSubImage(3, 6),
            sheetGrabbing.getSubImage(4, 6)
        };
        this.grabbingAnimation = new Animation(grabbingImgs, 70, false);
        this.currentAnimation = this.idleAnimation;
    }

    public void update(GameContainer gc, int delta) {
        this.jumping = this.jumping || keyboard.isJumping() || this.controller.isJumping();
        this.right = keyboard.isRight() || this.controller.isRight();
        this.left = keyboard.isLeft() || this.controller.isLeft();
        this.grabbing = keyboard.isGrabbing() || controller.isGrabbing();
        if (jumping || falling) {
            this.currentAnimation = this.fallingAnimation;
        } else if (right || left) {
            this.currentAnimation = this.walkingAnimation;
        } else if (grabbing) {
            this.currentAnimation =  this.grabbingAnimation;
        } else {
            this.currentAnimation = this.idleAnimation;
        }
        this.currentAnimation.update(delta);
        checkCollision(delta);
    }

    public void checkCollision(int delta) {
        float newXPos = this.position.x, newYPos = this.position.y;
        if (this.right) {
            newXPos += this.walkingSpeed * delta;
            if (newXPos > this.camera.mapWidth || this.camera.checkCollision(this.position.copy(newXPos, newYPos).getRect())) {
                newXPos = this.position.x;
            }
        }
        if (this.left) {
            newXPos -= this.walkingSpeed * delta;
            if (newXPos < 0 || this.camera.checkCollision(this.position.copy(newXPos, newYPos).getRect())) {
                newXPos = this.position.x;
            }
        }
        if (this.jumping && this.jumpTimer <= 400) {
            newYPos -= this.jumpingSpeed * delta;
            this.jumpTimer += delta;
            if (newYPos < 0 || this.camera.checkCollision(this.position.copy(newXPos, newYPos).getRect())) {
                newYPos = this.position.y;
                this.jumping = false;
                this.jumpTimer = 0;
            }
        } else {
            this.jumping = false;
            this.jumpTimer = 0;
        }
        if (!this.jumping && !this.camera.checkCollision(this.position.copy(newXPos, newYPos + (this.fallingSpeed * delta)).getRect())) {
            newYPos += this.fallingSpeed * delta;
            this.falling = true;
        } else {
            this.falling = false;
        }
        this.position.setX(newXPos);
        this.position.setY(newYPos);
    }

    void render(Graphics g) {
        Rectangle rect = this.position.getRect();
        final Image currentFrame = this.currentAnimation.getCurrentFrame();
        currentFrame.getFlippedCopy(this.left, false).draw(rect.getX(), rect.getY(), currentFrame.getWidth() * 2, rect.getHeight());
    }
}
