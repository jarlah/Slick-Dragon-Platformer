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

    public float walkingSpeed = 2f;
    public float jumpingSpeed = 6f;
    public float fallingSpeed = 4f;

    private final Camera camera;
    private final Controller controller;
    private final Keyboard keyboard;

    private boolean left = false;
    private boolean right = false;
    private boolean falling = false;
    private boolean jumping = false;
    private float jumpTimer = 0;

    private Animation currentAnimation, idleAnimation, walkingAnimation, fallingAnimation;

    public Player(Camera camera, Keyboard keyboard, Controller controller) {
        this.camera = camera;
        this.controller = controller;
        this.keyboard = keyboard;
    }

    void init(GameContainer gc) throws SlickException {
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
        boolean wasJumping = this.jumping;
        this.jumping = wasJumping || keyboard.isJumping() || this.controller.isJumping();
        if (!wasJumping && jumping && this.jumpTimer == 0) {
            this.jumpTimer = 20f;
            System.out.println("jumping");
        }
        this.right = keyboard.isRight() || this.controller.isRight();
        this.left = keyboard.isLeft() || this.controller.isLeft();
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
}
