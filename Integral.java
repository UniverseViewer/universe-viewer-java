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
 * This class is used to compute integrals.
 * This one is very precise but takes time.
 */
final class Integral
{
    public static double poly(double a)
    {
        double value = Environment.getLambda() * Math.pow(a, 4) - Environment.getKappa() * a * a + Environment.getOmega() * a + Environment.getAlpha();
        return value;
    }
    
    /**
     * This method contains the function to integrate
     * @param x
     * @return
     */
    public static double funcToIntegrate(double x)
    {
        double value = 1.0d / Math.sqrt(poly(x));
        return value;
    }
    
    /**
     * This method compute an integral
     * @param limitA interval min
     * @param limitB interval max
     * @param stepH step
     * @return integral result
     */
    public static double integrate(double limitA, double limitB, double stepH)
    {
        double val = 0;
        for(double temp=limitA+stepH; temp<limitB; temp+=stepH)
            val += funcToIntegrate(temp);
        val = val*stepH;
        return ((stepH/2)*(funcToIntegrate(limitA) + funcToIntegrate(limitB)) + val);
    }
}
