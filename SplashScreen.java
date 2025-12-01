/*
Copyright © 2008 FONTAINE Julie
Copyright © 2008 ABATI Mathieu
 
This file is part of Universe Viewer.

Universe Viewer is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Universe Viewer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Universe Viewer; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/** 
 * @author FONTAINE Julie
 * @author ABATI Mathieu
 */

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * This class is used to display the starting splash screen
 */
public class SplashScreen extends JWindow
{
    private final int showDelay;
    
    /**
     * Create a new splash screen
     * @param img, ImageIcon to show
     * @param delay before closing splash screen
     */
    public SplashScreen(ImageIcon img, int delay)
    {
        super(new Frame());
        showDelay = delay;
        
        // creating image label
        JLabel l = new JLabel(img);
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        
        // window centered and resized to image size
        setSize(img.getImage().getWidth(null), img.getImage().getHeight(null));
        setLocationRelativeTo(Environment.getMainWindow());
        
        Runnable waitRunner = new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(showDelay);    // waiting
                    
                    // delay elapsed, close splash screen
                    setVisible(false);
                    dispose();
                }
                catch (Exception e) {}
            }
        };
        
        // show splash screen
        setVisible(true);
        
        // launch thread to close splash screen after delay
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }
}
