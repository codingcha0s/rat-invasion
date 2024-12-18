package handlers;

import panels.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean leftDown, rightDown, fireMissle, rDown;

    private final GamePanel gamePanel;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftDown = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightDown = true;
        } else if (key == KeyEvent.VK_SPACE) {
            fireMissle = true;
        } else if (key == KeyEvent.VK_R) {
            rDown = true;
        }

        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            leftDown = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightDown = false;
        } else if (key == KeyEvent.VK_SPACE) {
            fireMissle = false;
        } else if (key == KeyEvent.VK_R) {
            rDown = false;
        }
    }
}
