package moe.bamtoll;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Jaehyun on 2016-11-24.
 */
public class GameMain extends IScene {

    Color[] colors = {
            Color.BLACK, Color.BLUE, Color.CYAN,
            Color.DARK_GRAY, Color.GREEN, Color.GRAY,
            Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
            Color.PINK, Color.RED, Color.WHITE, Color.YELLOW
    };

    final String TIME = "TIME : ";
    final String SCORE = "SCORE : ";

    static Font notoFont;

    Block[] blocks = new Block[9];
    JLabel timeLabel;
    JLabel scoreLabel;
    JButton pauseButton;
    JOptionPane scoreOptionPane;

    AudioInputStream bgm;
    AudioInputStream punch;

    int score = 0;
    int reduceScore = 50;
    int minusScore = 10;
    float GameTime = 15.0f;
    float spawnTime = (int)(Math.random()*10)%3*0.1f + 0.7f;
    float spawnTimer = 0.0f;
    float minusTime = 0.1f;

    public GameMain() {
        try {
            notoFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResource("font/NotoSans-Regular.ttf").openStream());
            bgm = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("sound/bgm.wav"));
            punch = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("sound/punch.wav"));
            ApplicationMain.INSTANCE.addAudio(bgm);
            ApplicationMain.INSTANCE.addAudio(punch);
            ApplicationMain.audioClip.get(1).addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        Block.pressed = false;
                        //ApplicationMain.audioClip.get(1).close();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        setBackground(colors[(int)(Math.random()*colors.length)]);

        timeLabel = new JLabel();
        timeLabel.setLocation(25, 30);
        timeLabel.setText(TIME + (int)GameTime);
        //timeLabel.setFont(notoFont.deriveFont(Font.PLAIN, 36));
        timeLabel.setFont(notoFont.deriveFont(Font.PLAIN, 36));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setSize(300, 36);

        scoreLabel = new JLabel();
        scoreLabel.setLocation(25, 84);
        scoreLabel.setText(SCORE + score);
        //scoreLabel.setFont(notoFont.deriveFont(Font.PLAIN, 36));
        scoreLabel.setFont(notoFont.deriveFont(Font.PLAIN, 36));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setSize(300, 36);

        pauseButton = new JButton("||");
        pauseButton.setLocation(335, 30);
        pauseButton.setFont(notoFont.deriveFont(Font.PLAIN, 36));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBackground(null);
        pauseButton.setSize(40, 40);
        pauseButton.setMargin(new Insets(0, 0, 0, 0));
        pauseButton.setBorder(null);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ApplicationMain.gamePaused) {
                    ((JButton)e.getSource()).setText("||");
                    ApplicationMain.gamePaused = false;
                    ApplicationMain.audioClip.get(0).start();
                } else {
                    ((JButton)e.getSource()).setText(">");
                    ApplicationMain.gamePaused = true;
                    ApplicationMain.audioClip.get(0).stop();
                }
            }
        });

        scoreOptionPane = new JOptionPane();

        for (int i=0; i<blocks.length; ++i) {
            blocks[i] = new Block();
            try {
                Image img = ImageIO.read(getClass().getClassLoader().getResource("image/mole.gif"));
                blocks[i].mole = new ImageIcon(img);
            } catch (IOException ex) {}
            blocks[i].setMargin(new Insets(0, 0, 0, 0));
            blocks[i].setBackground(Color.WHITE);
            blocks[i].SetActive(false);
            blocks[i].setBorder(null);
            blocks[i].setSize(100, 100);
            blocks[i].setLocation(
                    i % 3 * 100 + 25 * (i % 3 + 1),
                    200 + i / 3 * 100 + 25 * (i / 3 + 1)
            );

            //System.out.println(blocks[i].getLocation());
            blocks[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!ApplicationMain.gamePaused && !ApplicationMain.gameOver) {
                        if (((Block) e.getSource()).active) {
                            score += ((Block) e.getSource()).score - ((Block) e.getSource()).aliveTime / minusTime * minusScore;
                            scoreLabel.setText(SCORE + score);
                            ((Block) e.getSource()).SetActive(false);
                        } else {
                            score -= reduceScore;
                            scoreLabel.setText(SCORE + score);
                        }

                        if (!Block.pressed) {
                            Block.pressed = true;
                            ApplicationMain.INSTANCE.playAudio(1);
                        } else {
                            ApplicationMain.audioClip.get(1).stop();
                            //ApplicationMain.audioClip.get(1).close();
                            ApplicationMain.INSTANCE.playAudio(1);
                        }
                    }
                }
            });
            add(blocks[i]);
        }

        add(timeLabel);
        add(scoreLabel);
        add(pauseButton);

        ApplicationMain.INSTANCE.add(scoreOptionPane);

        ApplicationMain.INSTANCE.playAudio(0);

        Block.YS = new ImageIcon(getClass().getClassLoader().getResource("image/easterEgg.png"));
    }

    @Override
    public void Update(float deltaTime) {

        if (spawnTimer >= spawnTime) {

            int random = (int)(Math.random() * 10) % 9;
            while (blocks[random].active) {
                random = (int)(Math.random( )* 10) % 9;
            }
            Active(random);

            spawnTime = (int)(Math.random() * 10) % 3 * 0.1f + 0.7f;
            spawnTimer = 0.0f;
        }

        for (int i=0; i<blocks.length; ++i) {
            blocks[i].Update(deltaTime);
        }

        spawnTimer += deltaTime;
        ReduceGameTime(deltaTime);

        // why window is get minimum size
        if (ApplicationMain.INITIALLIZED) {
            if (ApplicationMain.INSTANCE.getSize().getWidth() < 400)
                ApplicationMain.INSTANCE.pack();

            ApplicationMain.INITIALLIZED = false;
        }
    }

    void Active(int index) {
        if (!blocks[index].active) {
            blocks[index].SetActive(true);
        }
    }

    void AllInActive() {
        for (int i=0; i<blocks.length; ++i) {
            if (!blocks[i].active)
                blocks[i].SetActive(false);
        }
    }

    void ReduceGameTime(float deltaTime) {
        GameTime -= deltaTime;
        timeLabel.setText(TIME + (int)GameTime);

        if (GameTime < 0.000f) {
            ApplicationMain.INSTANCE.gamePaused = true;
            ApplicationMain.audioClip.get(0).stop();
            AllInActive();
            //scoreOptionPane.setMessage("당신의 점수는 " + score + "입니다!");
            //scoreOptionPane.createDialog(ApplicationMain.INSTANCE, "종료").show();
            int checkScore = JOptionPane.showConfirmDialog(
                    this,
                    "당신의 점수는 " + score + "입니다!",
                    "종료",
                    JOptionPane.DEFAULT_OPTION);

            if (checkScore == JOptionPane.CLOSED_OPTION || checkScore == JOptionPane.OK_OPTION) {
                System.out.println("FINISH");
                Object checkName = JOptionPane.showInputDialog(this, "이름을 입력해주세요.", "중료", JOptionPane.QUESTION_MESSAGE, null, null, "NaN");

                if (checkName != null)
                    Rank.Insert(checkName.toString(), score);

                ApplicationMain.INSTANCE.SceneChange(2);
            }
        }
    }
}
