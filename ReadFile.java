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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class used to read the quasars registry file and store data in a Vector.
 */
final class ReadFile
{
	private static BufferedReader data_file = null;
	private static String rd_line;

	/**
	 * Read text registry file, and parse data in a vector
	 * @return quasars vector
	 */
	public static void read_txt_file(String filename)
	{
		Vector<Quasar> quasars = new Vector<Quasar>();	// new vector
		// opening file
		try
		{
			data_file = new BufferedReader(new FileReader(filename));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Error while opening registry file!");
		}
		
		// reading
		try
		{
			while ((rd_line = data_file.readLine()) != null)
			{
				Quasar q = new Quasar();
				StringTokenizer st = new StringTokenizer(rd_line);
				for(int i=0; i<3; i++)
				{
					String temp = st.nextToken();
					double d = Double.parseDouble(temp);
					switch(i)	// parsing values
					{
						case 0: 
                                                    q.setAscension(d); 
                                                    if(d > Environment.getAscensionMax())
                                                        Environment.setAscensionMax(d);
                                                    break;
						case 1: q.setDeclination(d); break;
						case 2: q.setRedshift(d); break;
						default: break;
					}
                                }
                                quasars.add(q);
			}
		}
		catch (IOException e)
		{
			System.out.println("Error while reading registry file!");
		}

		// closing file
		try
		{
			data_file.close();
		}
		catch (IOException e)
		{
			System.out.println("Error while closing registry file!");
		}
		
		Environment.setQuasars(quasars);
                Quasar.setSelectedCount(0); // re-initializing selection
	}
}
