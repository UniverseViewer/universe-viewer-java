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
 * This one is very quick but is less precise.
 */
final class Integral2
{
    private static double largeur;
    
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
    private static double funcToIntegrate(double x)
    {
        double value = 1.0d / Math.sqrt(poly(x));
        return value;
    }
    
    private static double ff(double x, double limitA)
    {
        return funcToIntegrate(limitA + largeur * x);
    }

     /**
     * This method compute an integral
     * @param limitA interval min
     * @param limitB interval max
     * @param n step
     * @return integral result
     */
    public static double integrate(double limitA, double limitB, int n)
    {
        int maximum = 15;
        int j, k, e;
        double s, ee, kk;
        double t[] = new double[maximum];
        
        largeur = limitB - limitA;
        s = 1;
        e = 1;
        t[0] = 0.5d * (ff(0, limitA) + ff(1, limitA));
        for(j = 1; j<n; j++)
        {
            s = 0.5d * s;
            t[j] = 0;
            for(k = 1; k<e; k++)
            {
                t[j] = t[j] + ff(s*(2*k-1), limitA);
            }
            t[j] = s * t[j] + 0.5d * t[j-1];
            e = 2*e;
        }
        ee = 1;
        kk = 1;
        for(j = 1; j<n; j++)
        {
            ee = 4*ee;
            kk = kk * (ee - 1);
            for(k = 0; k<(n-j); k++)
            {
                t[k] = (ee * t[k+1] - t[k]);
            }
        }
        return largeur * t[0] / kk;
    }
}
