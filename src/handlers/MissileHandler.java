package handlers;

import entities.Enemy;
import entities.Missile;
import entities.Player;
import panels.GamePanel;

import java.awt.*;
import java.util.ArrayList;

public class MissileHandler {
    private final ArrayList<Missile> missiles = new ArrayList<>();
    private final GamePanel gamePanel;
    public int fireMode = 0;

    public long lastMissile = System.currentTimeMillis();

    public MissileHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    public void add(Missile missile) {
        missiles.add(missile);
    }

    public void update() {
        System.out.println("Active Missiles: " + missiles.size());
        for (int i = missiles.size() - 1; i >= 0; i--) {
            Missile missile = missiles.get(i);
            missile.update();
            if (missile.getMissleY() < 0) {
                missiles.remove(i);
            }
        }

        /*
         * AINT WORKING :( , cant remove items while iterating directly through ArrayList, throws Exception
        System.out.println("Active Missiles: "+ missiles.size());
        for (Missile missile : missiles) {
            missile.update();
            if (missile.getMissleY() < 0) {
                missiles.remove(missile);
            }
        }
        */
    }

    public void draw(Graphics2D g2) {
        for (Missile missile : missiles) {
            missile.draw(g2);
        }
    }

    public void clearAll() {
        missiles.clear();
    }

    public void checkHit(EnemyHandler enemyHandler) {
        for (int i = missiles.size() - 1; i >= 0; i--) {
            Missile missile = missiles.get(i);
            for (int j = enemyHandler.enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemyHandler.enemies.get(j);

                if (missile.getMissleX() < enemy.getX() + gamePanel.getTileSize() &&
                        missile.getMissleX() + gamePanel.getTileSize() > enemy.getX() &&
                        missile.getMissleY() < enemy.getY() + gamePanel.getTileSize() &&
                        missile.getMissleY() + gamePanel.getTileSize() > enemy.getY()) {
                    // Collision
                    missiles.remove(i); // Remove the missile
                    enemyHandler.enemies.remove(j); // Remove the enemy
                    gamePanel.setScore(1);

                    // Spawn new Enemies
                    int maxEnemies = 100 + (gamePanel.getScore() / 100);
                    if (enemyHandler.enemies.size() <= maxEnemies) {
                        enemyHandler.spawn(2);
                    }

                    // break as items been removed
                    break;
                }
            }
        }
    }
}
