package moe.bamtoll;

import javax.swing.*;

/**
 * Created by Jaehyun on 2016-11-25.
 */
public class Block extends JButton {

    public boolean active = false;
    public static boolean pressed = false;
    public int score = 300;
    public float aliveTime = 0.0f;
    public ImageIcon mole;
    static ImageIcon YS;


    public void Update(float deltaTime) {

        if (active) {
            aliveTime += deltaTime;
        }

        if (aliveTime > 1.0f) {
            SetActive(false);
        }

    }

    public void SetActive(boolean tf) {
        if (tf) {
            if ((int)(Math.random()*10) == 0) {
                setIcon(YS);
                score = 1000;
            }
            else {
                setIcon(mole);
                score = 300;
            }
            active = true;
        } else {
            setIcon(null);
            active = false;
            aliveTime = 0.0f;
        }
    }
}
