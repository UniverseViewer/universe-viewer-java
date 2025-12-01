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

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 * Universe Viewer main class
 */
final class UniverseViewer
{
    /**
     * Compute a number round with defined decimals
     * @param d number to round
     * @param n precision
     * @return d rounded with n decimals
     */
    public static double floor(double d, int n)
    {
        double p = Math.pow(10.0, n);
        return Math.floor((d*p)+0.5) / p;
    }
    
    /**
     * Create a new ImageIcon from an image file
     * @param path
     * @return
     */
    public static final ImageIcon createImage(String path)
    {
	java.net.URL imgURL = UniverseViewer.class.getResource(path);
	if(imgURL != null)
		return new ImageIcon(imgURL);
        else
        {
            System.err.println("couldn't find file: "+path);
            return null;
	}
    }
    
    /**
     * Main method, initializes Environment, show starting window and main window
     * @param args
     */
    public static void main(String[] args)
    {
        // initializations
        Environment.initEnvironment();
        
        // show splash screen, close after 2s
        new SplashScreen(createImage("icons/starting.jpg"), 3000);
        
        // creating main window
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                SelectionManagerWindow selectionManager = new SelectionManagerWindow();
                MainWindow window = new MainWindow(selectionManager);
                window.setVisible(true);
            }
        });
    }
}
