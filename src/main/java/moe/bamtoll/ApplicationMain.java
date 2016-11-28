package moe.bamtoll;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Jaehyun on 2016-11-24.
 */
public class ApplicationMain extends JFrame {

    public static ApplicationMain INSTANCE;
    public static boolean INITIALLIZED = false;
    static final String TITLE = "두더지 잡기";
    static final int WIDTH = 400;
    static final int HEIGHT = 600;
    static int currentScene = 0;

    static final int FPS = 60;  // number of game update per second
    static final long PERIOD_NSEC = 1000000000L / FPS; // 1 second / 60

    // State of the game
    public static boolean gameOver = false;
    public static boolean gamePaused = false;

    public static Vector<IScene> node = new Vector<>(3);

    public static Vector<Clip> audioClip = new Vector<>();

    public ApplicationMain() {
        INSTANCE = this;

        for (int i=0; i<node.capacity(); ++i) {
            node.add(new IScene() {
                @Override
                public void Update(float deltaTime) {

                }
            });
        }

        Rank.Init("bamtoll.moe", "project79");
        SceneChange(0);

        Thread gameThread = new Thread() {
            // Override run() to provide the running behavior of this thread.
            @Override
            public void run() {
                gameStart();
            }
        };
        gameThread.start();

        setTitle(TITLE);
        setLayout(new BorderLayout());
        setBackground(Color.black);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void gameStart() {

        long beginTime, timeLeft;
        float deltaTime = 1 / 60.000f;

        while (!gameOver) {
            beginTime = System.nanoTime();
            if (!gamePaused) {
                // Update the state and position of all the game objects,
                // detect collisions and provide responses.
                gameUpdate(deltaTime);
                //System.out.println(deltaTime);
            }
            // Refresh the display
            //repaint();
            // Delay timer to provide the necessary delay to meet the target rate
            deltaTime = System.nanoTime() - beginTime;
            timeLeft = (PERIOD_NSEC - (long)deltaTime) / 1000000;  // in milliseconds

            if (timeLeft > 0) {
                try {
                    // Provides the necessary delay and also yields control so that other thread can do work.
                    Thread.sleep(timeLeft);
                } catch (InterruptedException ex) { }
            }

            deltaTime = System.nanoTime() - beginTime;
            deltaTime /= 1000000000.00f;

            //System.out.println(deltaTime);
        }
    }

    private void gameUpdate(float deltaTime) {

        node.get(currentScene).Update(deltaTime);

    }

    public void addAudio(AudioInputStream stream) {
        try {
            audioClip.add(AudioSystem.getClip());
            audioClip.lastElement().open(stream);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playAudio(int index) {
        try {
            //audioClip.get(index).open(streams.get(index));
            audioClip.get(index).setFramePosition(0);
            audioClip.get(index).start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void SceneChange(int index) {
        switch (index) {
            case 0:
                IScene.PushScene(0, new GameStart(), true);
                break;
            case 1:
                IScene.PushScene(1, new GameMain(), true);
                break;
            case 2:
                IScene.PushScene(2, new GameRank(), true);
                break;
        }
        currentScene = index;

        for (int i=0; i<node.size(); ++i) {
            node.get(i).setEnabled(false);
        }
        node.get(currentScene).setEnabled(true);

        setContentPane(node.get(currentScene));
        pack();
        gamePaused = false;
    }

    public static void main(String[] args) {
        new ApplicationMain();
        INITIALLIZED = true;
    }

}
