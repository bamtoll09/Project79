package moe.bamtoll;

import com.sun.javafx.font.directwrite.RECT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Jaehyun on 2016-11-28.
 */
public class GameStart extends IScene {

    JLabel bgLabel;
    ImageIcon bg_normal;
    ImageIcon bg_mouse;
    RECT playButton;
    Point buttonPos;

    boolean onMouse = false;

    public GameStart() {
        bg_normal = new ImageIcon(getClass().getClassLoader().getResource("image/start_normal.png"));
        bg_mouse = new ImageIcon(getClass().getClassLoader().getResource("image/start_mouse.png"));

        bgLabel = new JLabel();
        bgLabel.setLocation(0, 0);
        bgLabel.setSize(400, 600);
        SetOnMouse(false);

        playButton = new RECT();
        SetRect(playButton, 300, 100);

        buttonPos = new Point(35, 170);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                if (OverLapped(e.getPoint(), playButton, buttonPos)) {
                    SetOnMouse(true);
                } else {
                    SetOnMouse(false);
                }
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (onMouse) {
                    ApplicationMain.INSTANCE.SceneChange(1);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        add(bgLabel);
    }

    @Override
    public void Update(float deltaTime) {
        //System.out.println(onMouse);


    }

    public void SetOnMouse(boolean tr) {

        if (tr) {
            bgLabel.setIcon(bg_mouse);
        } else {
            bgLabel.setIcon(bg_normal);
        }
        onMouse = tr;

    }

    public void SetRect(RECT rect, int width, int height) {
        rect.left = 0;
        rect.top = 0;
        rect.right = width;
        rect.bottom = height;
    }

    public boolean OverLapped(Point mouse, RECT rect, Point rectPos) {
        int left = rectPos.x + rect.left;
        int right = rectPos.x + rect.right;
        int top = rectPos.y + rect.top;
        int bottom = rectPos.y + rect.bottom;

        if (mouse.x > left && mouse.x < right && mouse.y > top && mouse.y < bottom) {
            return true;
        }
        return false;
    }
}
