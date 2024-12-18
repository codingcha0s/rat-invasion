package handlers;

import entities.Enemy;
import panels.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EnemyHandler {
    private final GamePanel gamePanel;
    final ArrayList<Enemy> enemies = new ArrayList<>();
    private final Random rand = new Random();

    public EnemyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void add(Enemy enemy) {
        enemies.add(enemy);
    }

    public void update() {
        System.out.println("Active Enemies: " + enemies.size());
        for (Enemy enemy : enemies) {
            enemy.update();
        }
    }

    public void draw(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }
    }

    public void clearAll() {
        enemies.clear();
    }

    public void spawn(int amount) {
        for (int i = 0; i < rand.nextInt(1,amount+1); i++) {
            add(new Enemy(gamePanel));
        }
    }

    public void reachedBottom() {
        for (Enemy enemy : enemies) {
            // System.out.println("EnemyY is: "+enemy.getY()+" calculated Bottom is: "+ (gamePanel.getHeight()-gamePanel.getTileSize()-10));
            if (enemy.getY() > gamePanel.getHeight()-gamePanel.getTileSize()-10) {
                gamePanel.gameOver = true;
            }
        }
    }
}
