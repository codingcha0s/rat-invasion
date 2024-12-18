package panels;

import entities.Enemy;
import entities.Player;
import handlers.EnemyHandler;
import handlers.KeyHandler;
import handlers.MissileHandler;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // PANEL SIZE:
    private final int originalTileSize = 16; //16x16 -> characters
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale; // 3 * 16 = 48px per Tile
    private final int maxScreenCol = 16;
    private final int maxScreenRow = 18;//12;
    private final int screenWidth = tileSize * maxScreenCol; //768
    private final int screenHeight = tileSize * maxScreenRow; //576

    private String gameTitle = "Rat Invasion";
    private int gameScore = -1;
    public boolean gameStarted = false;
    public boolean gameOver = false;

    // GAME THREAD
    private final Thread gameThread;

    private final KeyHandler keyHandler = new KeyHandler(this);
    private final MissileHandler missileHandler = new MissileHandler(this);
    private final EnemyHandler enemyHandler = new EnemyHandler(this);
    private final Player player = new Player(this, keyHandler, missileHandler);

    // GAME PANEL CONSTRUCTOR
    public GamePanel(JFrame window) {
        window.setTitle(gameTitle);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.setLayout(null);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        int FPS = 60;
        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            // Update game logic
            update();

            // Check for game start or restart conditions
            if (!gameStarted && keyHandler.rDown) {
                // Start the game
                gameStarted = true;
                gameOver = false; // Ensure Game Over state is reset
                gameScore = 0;    // Reset score if necessary
                missileHandler.fireMode = 0;
                missileHandler.clearAll();
                enemyHandler.spawn(1); // Spawn the first enemy
                keyHandler.rDown = false; // Reset the key state
            } else if (gameStarted && gameOver && keyHandler.rDown) {
                // Restart the game
                gameOver = false;
                gameScore = 0;               // Reset score
                missileHandler.fireMode = 0; // Reset fire mode
                missileHandler.clearAll();
                enemyHandler.clearAll();        // Clear existing enemies
                enemyHandler.spawn(1);       // Spawn initial enemy
                keyHandler.rDown = false;    // Reset the key state
            }

            // Repaint the screen
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (!gameOver) {
            enemyHandler.reachedBottom();
            missileHandler.update();
            missileHandler.checkHit(enemyHandler);
            enemyHandler.update();
            player.update();
            if (gameScore == 100) {
                missileHandler.fireMode = 1;
            } else if (gameScore >= 200) {
                missileHandler.fireMode = 2;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (!gameStarted) {
            // Display the start screen
            paintStartScreen(g2);
        } else {
            if (gameOver) {
                // Display the Game Over screen
                paintGameOver(g2);
            } else {
                // Gameplay rendering
                paintScore(g2);
                missileHandler.draw(g2);
                enemyHandler.draw(g2);
                player.draw(g2);
            }
        }

        g2.dispose();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    private void paintScore(Graphics g2) {
        // Set up the font and color for the score
        g2.setFont(new Font("Courier New", Font.BOLD, 30)); // Slightly larger font for better readability
        g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black background for contrast

        // Background rectangle for the score text
        String scoreText = "Score: " + gameScore;
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(scoreText);
        int textHeight = fm.getHeight();

        int padding = 10; // Padding around the text
        int backgroundX = this.tileSize - padding;
        int backgroundY = this.tileSize - textHeight;
        int backgroundWidth = textWidth + padding * 2;
        int backgroundHeight = textHeight + padding;

        g2.fillRect(backgroundX, backgroundY, backgroundWidth, backgroundHeight);

        // Draw the score text over the background
        g2.setColor(Color.WHITE);
        g2.drawString(scoreText, this.tileSize, this.tileSize);
    }
    private void paintStartScreen(Graphics g) {
        // Fill the background with a solid color
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

        // Title: "Game Title"
        g.setFont(new Font("Courier New", Font.BOLD, 60));
        g.setColor(Color.RED);
        String title = gameTitle; // Change to your game's title
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (screenWidth - titleWidth) / 2;
        int titleY = screenHeight / 3; // Top third of the screen
        g.drawString(title, titleX, titleY);

        // Subtitle: "Press 'R' to Start"
        g.setFont(new Font("Courier New", Font.PLAIN, 30));
        g.setColor(Color.WHITE);
        String subtitle = "Press 'R' to Start";
        FontMetrics subFm = g.getFontMetrics();
        int subWidth = subFm.stringWidth(subtitle);
        int subX = (screenWidth - subWidth) / 2;
        int subY = titleY + 50; // Below the title
        g.drawString(subtitle, subX, subY);

        // Additional instructions or tips
        g.setFont(new Font("Courier New", Font.ITALIC, 20));
        String instructions = "Use Arrow Keys to Move | Collect Coins to Score";
        int instructionsWidth = g.getFontMetrics().stringWidth(instructions);
        int instructionsX = (screenWidth - instructionsWidth) / 2;
        int instructionsY = subY + 50; // Below the subtitle
        g.drawString(instructions, instructionsX, instructionsY);
    }

    private void paintGameOver(Graphics g) {
        // Create a semi-transparent black overlay
        g.setColor(new Color(0, 0, 0, 150)); // RGB with alpha for transparency
        g.fillRect(0, 0, screenWidth, screenHeight);

        // Set up the font for the "Game Over" title
        g.setFont(new Font("Courier New", Font.BOLD, 48));
        g.setColor(Color.RED);

        // Measure and center "Game Over"
        String gameOverText = "Game Over";
        FontMetrics fm = g.getFontMetrics();
        int gameOverWidth = fm.stringWidth(gameOverText);
        int gameOverX = (screenWidth - gameOverWidth) / 2;
        int gameOverY = screenHeight / 3; // Top third of the screen

        g.drawString(gameOverText, gameOverX, gameOverY);

        // Display the player's score
        g.setFont(new Font("Courier New", Font.PLAIN, 30));
        g.setColor(Color.WHITE);
        String scoreText = "Your Score: " + gameScore;
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        int scoreX = (screenWidth - scoreWidth) / 2;
        int scoreY = gameOverY + 60; // Below "Game Over"
        g.drawString(scoreText, scoreX, scoreY);

        // Add a restart or exit instruction
        g.setFont(new Font("Courier New", Font.ITALIC, 20));
        String restartText = "Press 'R' to Restart or 'Q' to Quit";
        int restartWidth = g.getFontMetrics().stringWidth(restartText);
        int restartX = (screenWidth - restartWidth) / 2;
        int restartY = scoreY + 60; // Below the score

        g.drawString(restartText, restartX, restartY);
    }


    public void setScore(int score) {
        gameScore += score;
    }

    public int getScore() {
        return gameScore;
    }
}
