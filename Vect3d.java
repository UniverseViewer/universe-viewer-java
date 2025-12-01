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
 * This class is standing for a three-dimensional vector
 */
public class Vect3d
{
    private double x;
    private double y;
    private double z;
    
    // --------------------------------
    // VECT3D ACCESSORS
    // --------------------------------
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setZ(double z) { this.z = z; }
    
    /**
     * Computing dot product between this vector and another one
     * @param v, a 3d vector
     * @return scalar product
     */
    public double dotProd3d(Vect3d v) 
    {
        return (x*v.getX()+y*v.getY()+z*v.getZ());
    }
    
    /**
     * Computing Vectorial product between this vector and another one
     * @param v, a 3d vector
     * @return result in a new 3d vector
     */
    public Vect3d vectProd3d(Vect3d v)
    {
        Vect3d r = new Vect3d();
        r.x = (y*v.getZ() - z*v.getY());
        r.y = (z*v.getX() - x*v.getZ());
        r.z = (x*v.getY() - y*v.getX());
        return r;
    }

    public double norm() 
    {
        return Math.sqrt(x*x+y*y+z*z);
    }
}