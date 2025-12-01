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

/**
 * Quasar class structure, and all its accessors
 */
public class Quasar
{
    // quasaes common variables (static)
    private static int selectedCount = 0;   // number of selected quasars
    private static boolean multipleSelection = true;    // true if multiple selection is enabled, false if combo selection is enabled
    
    // quasar caracteristics
    private double	ascension;
    private double	declination;
    private double	redshift;
    private double	angular_distance;
    private double	magnitude;
    
    // quasar position
    Vect4d pos;
    
    // quasar projection
    double x, y;
    
    // true if this quasar selected in viewer canvas
    boolean selected = false;

    // -----------------------
    // QUASAR COMMON ACCESSORS
    // -----------------------
    
    public static int getSelectedCount() { return selectedCount; }
    public static void setSelectedCount(int nb) { selectedCount = nb; }
    
    public static boolean isMultipleSelectionEnabled() { return multipleSelection; }
    public static void setMultipleSelection(boolean s) { multipleSelection = s; }
    
    // --------------------------------
    // QUASAR CARACTERISTICS ACCESSORS
    // --------------------------------
    
    public double getAscension() { return ascension; }
    public void setAscension(double ascension) { this.ascension = ascension; }

    public double getDeclination() { return declination; }
    public void setDeclination(double declination) { this.declination = declination; }

    public double getRedshift() { return redshift; }
    public void setRedshift(double redshift) { this.redshift = redshift; }

    public double getAngularDist() { return angular_distance; }
    public void setAngularDist(double ad) { this.angular_distance = ad; }

    public double getMagnitude() { return magnitude; }
    public void setMagnitude(double magnitude) { this.magnitude = magnitude; }

    // ----------------------------------------
    // QUASAR POSITION AND PROJECTION ACCESSORS
    // ----------------------------------------
    
    public Vect4d getPos() { return pos; }
    public void setPos(Vect4d v) { pos = v; }
    
    public double getx() { return x; }
    public double gety() { return y; }
    public void setx(double x) { this.x = x; }
    public void sety(double y) { this.y = y; }
    
    // --------------
    // MISC ACCESSORS
    // --------------
    
    public boolean isSelected() { return selected; }
    public void setSelected(boolean s) { selected = s; }
}
