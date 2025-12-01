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

import java.util.Vector;

/**
 * The Environment class contains values, constants, and others public variables.
 * Some calculations with these values are made here.
 */
final class Environment
{
    // quasars related data
    private static Vector<Quasar> quasars;
    private static double ascension_max;
    
    // cosmological constants
    private static double lambda;
    private static double omega;
    private static double kappa;
    private static double alpha;
    
    // basis projection vectors
    private static int view;
    private static double userRA1;
    private static double userDec1;
    private static double userBeta;
    private static Vect4d E0 = new Vect4d();
    private static Vect4d E1 = new Vect4d();
    private static Vect4d E2 = new Vect4d();
    private static Vect4d E3 = new Vect4d();
    
    // misc
    private static ViewerCanvas openGLViewerCanvas;
    private static boolean precisionEnabled;
    private static boolean comovingSpace;
    private static boolean somethingToShow;
    private static MainWindow mainWin;
    
    /**
     * Initializes environment datas
     */
    public static void initEnvironment()
    {
        quasars = null;
        ascension_max = 0;
        
        // cosmological constants
        try
        {
            setCosmoConsts(1.2d, 0.2d, 0.40005d, 0.00005d);
        }
        catch(Exception ex)
        {
            System.err.println("Cosmological constants are incorrect!");
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        
        // creating a new OpenGL viewer canvas
        openGLViewerCanvas = new ViewerCanvas();
        
        // by default, the calculations are faster but are less precise
        precisionEnabled = false;
        
        // projection defaults
        view = 1;
        userRA1 = 0.0d; // units : hours : 0 to 24
        userDec1 = 0.0d; // units : degrees : -90 to 90
        userBeta = 0.0d; // units : hours : 0 to 24
        
        // misc
        comovingSpace = false;      // user wants to see reference space
        somethingToShow = false;    // nothing to draw in viewer canvas at this time
    }
    
    
    // ------------------------------
    // QUASARS RELATED DATA ACCESSORS
    // ------------------------------
    
     /**
     * Get quasars Vector (list of loaded quasars)
     * @return quasars Vector
     */
    public static Vector<Quasar> getQuasars() { return quasars; }
    /**
     * Set quasars Vector (list of quasars)
     * @param q (new quasars Vector)
     */
    public static void setQuasars(Vector<Quasar> q) { quasars = q; }
    
    /**
     * Get the max ascension from the used registry's quasars
     * @return ascension max value
     */
    public static double getAscensionMax() { return ascension_max; }
    
    /**
     * Set the max ascension value
     * @param am
     */
    public static void setAscensionMax(double am) { ascension_max = am; }
    
    // --------------------------------
    // COSMOLOGICAL CONSTANTS ACCESSORS
    // --------------------------------
    
    /**
     * Set cosmological constants values
     * Constraint: lambda - kappa + omega + alpha = 1
     * Constraint: (27/4) * lambda * omega² > k^3
     * Constraint: omega > 0
     * @param newlambda
     * @param newomega
     * @param newkappa
     * @param newalpha
     */
    public static void setCosmoConsts(double newlambda, double newomega, double newkappa, double newalpha) throws Exception
    {
        if(UniverseViewer.floor(newlambda - newkappa + newomega + newalpha, 5) != 1.0d)
            throw new Exception("Constraint broken:\nlambda - kappa + omega + alpha = 1.0 not verified!");
        if(newomega < 0)
            throw new Exception("Constraint broken:\nomega > 0 not verified!");
        if( !(((27/4) * newlambda * newomega * newomega) > (newkappa * newkappa * newkappa)) )
        {
            throw new Exception("Constraint broken:\n(27/4) * lambda * omega² > kappa^3 not verified!");
        }
        if(!comovingSpace && newkappa == 0)
            throw new Exception("kappa cannot be equal to zero if comovingSpace is not checked!");
            
        lambda = newlambda;
        omega = newomega;
        kappa = newkappa;
        alpha = newalpha;
    }
    
    /**
     * Get lambda cosmological constant
     * @return lambda value (double)
     */
    public static double getLambda() { return lambda; }
    /**
     * Get omega cosmological constant
     * @return omega value (double)
     */
    public static double getOmega() { return omega; }
    /**
     * Get kappa cosmological constant
     * @return kappa value (double)
     */
    public static double getKappa() { return kappa; }
    /**
     * Get alpha cosmological constant
     * @return alpha value (double)
     */
    public static double getAlpha() { return alpha; }
    
    
    // --------------------
    // PROJECTION ACCESSORS
    // --------------------
    
    /* Ra1, Dec1 and Beta are set by user */
    /**
     * Get Ra1 (radians) value
     * @return Ra1
     */ 
    public static double getUserRa1Rad() 
    {
        return userRA1;
    }
    /**
     * Get Dec1 (radians) value
     * @return Dec1 (radian)
     */
    public static double getUserDec1Rad() 
    {
        return userDec1;
    }
    /**
     * Get Beta (radians) value
     * @return Beta (radians)
     */
    public static double getUserBetaRad() 
    {
        return userBeta;
    }
    /**
     * Get Dec1 (degrees) value
     * @return Dec1 (degrees)
     */
    public static double getUserDec1Deg() 
    {
        return 180*userDec1/Math.PI;
    }
    /**
     * Get Beta (degrees) value
     * @return Beta (degrees)
     */
    public static double getUserBetaHours()
    {
        return 12*userBeta/Math.PI;
    }
    /**
     * Set Ra1 value (set by user)
     * @param RA1
     */
    public static void setUserRa1(double RA1) 
    {
        userRA1 = (Math.PI/12)*RA1;
    }
    /**
     * Set Dec1 value (set by user)
     * @param Dec1
     */
    public static void setUserDec1(double Dec1) 
    {
        userDec1 = (Math.PI/180)*Dec1;
    }
    /**
     * Set Beta value (set by user)
     * @param Beta
     */
    public static void setUserBeta(double Beta)
    {
        userBeta = (Math.PI/12)*Beta;
    }
    
    /**
     * Computing projections vectors
     */
    public static void setProjVects()
    {
        Vect3d P1 = new Vect3d();
        
        P1.setX(Math.cos(getUserRa1Rad())*Math.cos(getUserDec1Rad()));
        P1.setY(Math.sin(getUserRa1Rad())*Math.cos(getUserDec1Rad()));
        P1.setZ(Math.sin(getUserDec1Rad()));

        Vect3d eta1 = new Vect3d();
        Vect3d eta2 = new Vect3d();
        
        if(Math.abs(P1.getX() - 1) > 1e-5) // 1e-5 = epsilon = 2PI/(24*60*60) => epsilon = une seconde d'arc
        {
            Vect3d i = new Vect3d();
            i.setX(1);
            i.setY(0);
            i.setZ(0);
            Vect3d temp = P1.vectProd3d(i);
            double norme = temp.norm();
            eta1.setX(temp.getX()/norme);
            eta1.setY(temp.getY()/norme);
            eta1.setZ(temp.getZ()/norme);
        }
        else //if(Math.abs(P1.getY() - 1) > 1e-5) 
        {
            Vect3d j = new Vect3d();
            j.setX(0);
            j.setY(1);
            j.setZ(0);
            Vect3d temp = P1.vectProd3d(j);
            double norme = temp.norm();
            eta1.setX(temp.getX()/norme);
            eta1.setY(temp.getY()/norme);
            eta1.setZ(temp.getZ()/norme);
        }
        
        eta2 = P1.vectProd3d(eta1);

        Vect3d P2 = new Vect3d();
        P2.setX(Math.cos(getUserBetaRad())*eta1.getX()+Math.sin(getUserBetaRad())*eta2.getX());
        P2.setY(Math.cos(getUserBetaRad())*eta1.getY()+Math.sin(getUserBetaRad())*eta2.getY());
        P2.setZ(Math.cos(getUserBetaRad())*eta1.getZ()+Math.sin(getUserBetaRad())*eta2.getZ());

        Vect3d P3 = new Vect3d();
        P3 = P1.vectProd3d(P2);
        
        // Projection vectors :
        E0.setX(0.0d);
        E0.setY(0.0d);
        E0.setZ(0.0d);
        E0.setT(1.0d);

        E1.setX(P1.getX());
        E1.setY(P1.getY());
        E1.setZ(P1.getZ());
        E1.setT(0.0d);

        E2.setX(P2.getX());
        E2.setY(P2.getY());
        E2.setZ(P2.getZ());
        E2.setT(0.0d);

        E3.setX(P3.getX());
        E3.setY(P3.getY());
        E3.setZ(P3.getZ());
        E3.setT(0.0d);
    }
    
    /*
     * Return projection vectors
     */
    public static Vect4d getProjVectE0() { return E0; }
    public static Vect4d getProjVectE1() { return E1; }
    public static Vect4d getProjVectE2() { return E2; }
    public static Vect4d getProjVectE3() { return E3; }
    
    /**
     * Return current view
     * @return view
     */
    public static int getView() { return view; }
    
    /**
     * Set current view (current view is set by user)
     * @param v
     * @throws java.lang.Exception
     */
    public static void setView(int v) throws Exception
    {
        if((v<1) || (v>6))
            throw new Exception("View number incorrect, must be {1,2,3,4,5,6}");
        view = v;
    }
    
    
    // --------------
    // MISC ACCESSORS
    // --------------
    
    /**
     * Get OpenGL viewer canvas
     * @return OpenGL canvas
     */
    public static ViewerCanvas getopenGLViewerCanvas() { return openGLViewerCanvas; }
    
    /**
     * Enable / Disable precision (takes more time)
     * @param prec
     */
    public static void enablePrecision(boolean prec) { precisionEnabled = prec; }
    
    /**
     * Enable / Disable comoving space
     * Comoving space can't be disabled if kappa = 0
     * @param com
     */
    public static void comovingSpace(boolean com) throws Exception
    {
        if(com == false)
        {
            if(kappa == 0)
                    throw new Exception("Cant disable comoving space option: Kappa = 0!");
        }
        comovingSpace = com;
    }
    
    public static boolean isSomethingToShow() { return somethingToShow; }
    
    public static void setMainWindow(MainWindow window) { mainWin = window; }
    
    public static MainWindow getMainWindow() { return mainWin; }
    
    
    // ------------
    // CALCULATIONS
    // ------------
    
    /** Update the environment.
     * @param flag, indicate what to update (UPDATE_ALL, UPDATE_VIEW, UPDATE_VIEWER)
     */
    public static void update(int flag) throws Exception
    {
        if((flag != UPDATE_ALL) && (flag != UPDATE_VIEW) && (flag != UPDATE_VIEWER))
            throw new Exception("Update accepted values are: UPDATE_ALL, UPDATE_VIEW, UPDATE_VIEWER");

        if(flag == UPDATE_ALL)
        {
            Environment.calcQuasarsAngularDist();
            Environment.calcQuasarsPos();
            Environment.calcQuasarsProj();
            Environment.getopenGLViewerCanvas().updateCanvas();
        }
        if(flag == UPDATE_VIEW)
        {
            Environment.calcQuasarsProj();
            Environment.getopenGLViewerCanvas().updateCanvas();
        }
        if(flag == UPDATE_VIEWER)
            Environment.getopenGLViewerCanvas().updateCanvas();
    }
    // update constants
    public static final int UPDATE_ALL = 1; // update all (quasars calculations, view and projections, viewer canvas
    public static final int UPDATE_VIEW = 2;    // update view and projections, viewer canvas
    public static final int UPDATE_VIEWER = 3;   // update viewer canvas
    
    /**
     * Computing the comoving distance for the quasar i
     * @param i, quasar index
     * @return value of comoving distance
     */
    private static double comovingDist(int i)
    {
        if(precisionEnabled)
            return Integral.integrate(1.0d / (1.0d + quasars.get(i).getRedshift()), 1.0d, 0.01d); // takes more time but precise
        else
            return Integral2.integrate(1.0d / (1.0d + quasars.get(i).getRedshift()), 1.0d, 6); // faster but less precise
    }
    /**
     * Computing the angular distance for all quasars, using comoving distance
     * @return false if no quasars are loaded
     */
    public static boolean calcQuasarsAngularDist()
    {
        double sqrt_kappa;
        if(kappa != 0) // angular distance is inexistant for kappa = 0
        {
            if(quasars != null)
            {
                if(kappa < 0.0d)
                    sqrt_kappa = Math.sqrt(-kappa);
                else    // kappa > 0
                    sqrt_kappa = Math.sqrt(kappa);

                for(int i=0; i<quasars.size(); i++)
                    quasars.get(i).setAngularDist(sqrt_kappa * comovingDist(i));

                return true;    // success
            }
            else
                return false;   // fail
        }
        else
            return false;
    }
    
    /**
     * Computing position for all quasars, using angular distance
     * @return false if no quasars are loaded
     */
    public static boolean calcQuasarsPos()
    {
        if(quasars != null)
        {
            if(!comovingSpace)
            {
                if(kappa < 0.0d)    
                    for(int i=0; i<quasars.size(); i++)
                    {
                        Vect4d vect = new Vect4d();
                        vect.setX(Math.sinh(quasars.get(i).getAngularDist()) * Math.cos(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setY(Math.sinh(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setZ(Math.sinh(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getDeclination()));
                        vect.setT(Math.cosh(quasars.get(i).getAngularDist()));
                        quasars.get(i).setPos(vect);
                    }
                else if(kappa > 0.0d)
                    for(int i=0; i<quasars.size(); i++)
                    {
                        Vect4d vect = new Vect4d();
                        vect.setX(Math.sin(quasars.get(i).getAngularDist()) * Math.cos(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setY(Math.sin(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setZ(Math.sin(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getDeclination()));
                        vect.setT(Math.cos(quasars.get(i).getAngularDist()));
                        quasars.get(i).setPos(vect);
                    }
                return true;    // success
            }
            else { // comoving space
                if(kappa < 0.0d)    
                    for(int i=0; i<quasars.size(); i++)
                    {
                        Vect4d vect = new Vect4d();
                        vect.setX((1/Math.sqrt(-kappa))*Math.sinh(quasars.get(i).getAngularDist()) * Math.cos(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setY((1/Math.sqrt(-kappa))*Math.sinh(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setZ((1/Math.sqrt(-kappa))*Math.sinh(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getDeclination()));
                        vect.setT((1/Math.sqrt(-kappa))*Math.cosh(quasars.get(i).getAngularDist()));
                        quasars.get(i).setPos(vect);
                    }
                else if(kappa > 0.0d)
                    for(int i=0; i<quasars.size(); i++)
                    {
                        Vect4d vect = new Vect4d();
                        vect.setX((1/Math.sqrt(kappa))*Math.sin(quasars.get(i).getAngularDist()) * Math.cos(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setY((1/Math.sqrt(kappa))*Math.sin(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setZ((1/Math.sqrt(kappa))*Math.sin(quasars.get(i).getAngularDist()) * Math.sin(quasars.get(i).getDeclination()));
                        vect.setT((1/Math.sqrt(kappa))*Math.cos(quasars.get(i).getAngularDist()));
                        quasars.get(i).setPos(vect);
                    }
                else // kappa = 0
                    for(int i=0; i<quasars.size(); i++)
                    {
                        double cd = comovingDist(i);
                        Vect4d vect = new Vect4d();
                        vect.setX(cd * Math.cos(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setY(cd * Math.sin(quasars.get(i).getAscension()) * Math.cos(quasars.get(i).getDeclination()));
                        vect.setZ(cd * Math.sin(quasars.get(i).getDeclination()));
                        vect.setT(0);
                        quasars.get(i).setPos(vect);
                    }
                return true;    // success
            }
        }
        else         
            return false;   // fail
  }
    
    /**
     * Computing 2D projection for all quasars
     */
    public static void calcQuasarsProj()
    {
        Environment.setProjVects();
        switch(view)
        {
            case 1: // E0 E1
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E0));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E1));
                }
            break;

            case 2: // E0 E2
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E0));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E2));
                }
            break;

            case 3: // E0 E3
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E0));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E3));
                }
            break;

            case 4: // E1 E2
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E1));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E2));
                }
            break;

            case 5: // E1 E3
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E1));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E3));
                }
            break;

            case 6: // E2 E3
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E2));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E3));
                }
            break;

            default : // E0 E1
                for(int i=0; i<quasars.size(); i++)
                {
                    quasars.get(i).setx(quasars.get(i).getPos().dotProd4d(E0));
                    quasars.get(i).sety(quasars.get(i).getPos().dotProd4d(E1));
                }
            break;
        }
        
        somethingToShow = true; // now, there is something to draw in viewer canvas
    }
}
