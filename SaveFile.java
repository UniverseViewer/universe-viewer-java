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
 *@author FONTAINE Julie
 * @author ABATI Mathieu
 */


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;


final class SaveFile {

    private static PrintWriter writer;
    
    public static void open(String filename) 
    {
        try 
        {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
        }
        catch(Exception e) {}
    }
    
    public static void close() 
    {
        writer.close();
    }
    
    public static void write_txt_file(String wt_line) 
    {
        
        try 
        {
            writer.println(wt_line);
        }
        catch(Exception e) {}
        
        
    }
    
}
