package moe.bamtoll;

import com.sun.javafx.font.directwrite.RECT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * Created by Jaehyun on 2016-11-28.
 */
public class GameRank extends IScene {

    Vector<RankItem> rankItems = Rank.FindAll();
    JList rankLineList;
    JList rankNameList;
    JList rankScoreList;
    JPanel rankPanel;
    JScrollPane scrollPane;
    JLabel bgLabel;
    ImageIcon bg;

    RECT playButton;
    Point buttonPos;

    public GameRank() {
        rankLineList = new JList();
        /*rankLineList.setLocation(0, 0);
        rankLineList.setSize(50, 500);*/
        rankLineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Vector<Integer> lines = new Vector<>();
        for (int i=0; i<rankItems.size(); ++i) {
            lines.add(i+1);
        }
        rankLineList.setListData(lines);

        rankNameList = new JList();
        /*rankNameList.setLocation(50, 0);
        rankNameList.setSize(100, 500);*/
        rankNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Vector<String> names = new Vector<>();
        for (int i=0; i<rankItems.size(); ++i) {
            names.add(rankItems.get(i).name);
        }
        rankNameList.setListData(names);

        rankScoreList = new JList();
        rankScoreList.setLocation(150, 0);
        rankScoreList.setSize(80, 500);
        rankScoreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Vector<Integer> scores = new Vector<>();
        for (int i=0; i<rankItems.size(); ++i) {
            scores.add(rankItems.get(i).score);
        }
        rankScoreList.setListData(scores);

        rankPanel = new JPanel();
        rankPanel.setLayout(new GridLayout(1, 3));
        /*rankPanel.setLocation(75, 240);
        rankPanel.setSize(230, 500);*/

        rankPanel.add(rankLineList);
        rankPanel.add(rankNameList);
        rankPanel.add(rankScoreList);

        scrollPane = new JScrollPane();
        scrollPane.setLocation(75, 240);
        scrollPane.setSize(250, 300);
        scrollPane.setViewportView(rankPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        bg = new ImageIcon(getClass().getClassLoader().getResource("image/ranking.png"));
        bgLabel = new JLabel(bg);
        bgLabel.setLocation(0, 0);
        bgLabel.setSize(400, 600);

        add(scrollPane);
        add(bgLabel);

        playButton = new RECT();
        SetRect(playButton, 140, 160);

        buttonPos = new Point(125, 20);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (OverLapped(e.getPoint(), playButton, buttonPos)) {
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
    }

    @Override
    public void Update(float deltaTime) {

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
