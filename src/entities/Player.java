package entities;

import handlers.KeyHandler;
import handlers.MissileHandler;
import panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private int playerX;
    private final int playerY;
    private final int playerSpeed;
    private BufferedImage playerShip;

    private final GamePanel gamePanel;
    private final KeyHandler keyHandler;
    private final MissileHandler missileHandler;

    public Player(GamePanel gamePanel, KeyHandler keyHandler, MissileHandler missileHandler) {
        playerX = gamePanel.getScreenWidth() / 2 - gamePanel.getTileSize();
        playerY = gamePanel.getScreenHeight() - gamePanel.getTileSize() - 10;
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.missileHandler = missileHandler;
        this.playerSpeed = 10;
        try {
            playerShip = ImageIO.read(new File("resources/ship.png"));
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyHandler.rightDown && playerX < gamePanel.getScreenWidth()-gamePanel.getTileSize()+gamePanel.getTileSize()/2) {
            playerX += playerSpeed;
        }
        if (keyHandler.leftDown && playerX > -gamePanel.getTileSize() / 2) {
            playerX -= playerSpeed;
        }
        if (keyHandler.fireMissle) {
            double interval = 1000.0 / 25; // Interval in milliseconds for 5 missiles per second
            if (System.currentTimeMillis() - missileHandler.lastMissile >= interval) {
                switch (missileHandler.fireMode) {
                    case 0:
                        missileHandler.add(new Missile(gamePanel, this)); // Add a new missile
                        break;
                    case 1:
                        missileHandler.add(new Missile(gamePanel, this, -5, 0)); // Add a new missile
                        missileHandler.add(new Missile(gamePanel, this, 5, 0)); // Add a new missile
                        break;
                    case 2:
                        missileHandler.add(new Missile(gamePanel, this, -7, 0)); // Add a new missile
                        missileHandler.add(new Missile(gamePanel, this, 7, 0)); // Add a new missile
                        missileHandler.add(new Missile(gamePanel, this, 0, -3)); // Add a new missile
                        break;
                }

                missileHandler.lastMissile = System.currentTimeMillis(); // Update the last missile fire time
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(playerShip, playerX, playerY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
    }

    public int getPlayerX() {
        return playerX;
    }
    public int getPlayerY() {
        return playerY;
    }
}
