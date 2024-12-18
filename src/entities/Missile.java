package entities;

import panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Missile {
    int missleX;
    int missleY;
    int speed;
    private BufferedImage missleImage;
    private final GamePanel gamePanel;
    private final Player player;

    public Missile(GamePanel gamePanel, Player player) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.speed = 8;

        this.missleX = player.getPlayerX();
        this.missleY = player.getPlayerY()- (gamePanel.getTileSize()/2);


        try {
            missleImage = ImageIO.read(new File("resources/missile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Missile(GamePanel gamePanel, Player player, int x, int y) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.speed = 8;

        this.missleX = player.getPlayerX() + x;
        this.missleY = player.getPlayerY() - (gamePanel.getTileSize()/2) + y;


        try {
            missleImage = ImageIO.read(new File("resources/missile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        missleY -= speed;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(missleImage, missleX, missleY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

    public int getMissleX() {
        return missleX;
    }
    public int getMissleY() {
        return missleY;
    }

}
