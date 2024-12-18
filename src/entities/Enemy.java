package entities;

import panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Enemy {
    private final int enemyX;
    private int enemyY;
    private final int speed;
    private BufferedImage enemyImage;
    private final GamePanel gamePanel;

    public Enemy(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        Random rand = new Random();
        speed = rand.nextInt(1,4);
        enemyX = rand.nextInt(gamePanel.getTileSize(), gamePanel.getScreenWidth()-gamePanel.getTileSize());
        enemyY = gamePanel.getTileSize();

        try {
            enemyImage = ImageIO.read(new File("resources/enemy1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        enemyY += speed;
    }
    public void draw(Graphics2D g2) {
        g2.drawImage(enemyImage, enemyX, enemyY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

    public int getX() {
        return enemyX;
    }
    public int getY() {
        return enemyY;
    }
}
