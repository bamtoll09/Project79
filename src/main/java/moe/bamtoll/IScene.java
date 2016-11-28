package moe.bamtoll;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jaehyun on 2016-11-24.
 */
public abstract class IScene extends JPanel {

    public abstract void Update(float deltaTime);

    public static void PushScene(int index, IScene object, boolean autoSizing)
    {
        object.setLayout(null);
        object.setLocation(0, 0);

        if (autoSizing)
            object.setPreferredSize(new Dimension(ApplicationMain.WIDTH, ApplicationMain.HEIGHT));

        ApplicationMain.node.add(index, object);
    }

}
