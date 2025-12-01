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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;
import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.*;

/**
 * Projection renderer class, using OpenGL
 */
public class ViewerCanvas implements GLEventListener, MouseListener, MouseWheelListener, MouseMotionListener
{    
    private GLCanvas canvas;    // OpenGL canvas
    
    // OpenGL canvas size
    public static final int SIZE_X = 500;
    public static final int SIZE_Y = 500;
    
    // variables used for zooming on viewer canvas
    private float zoom;
    private int mouseX, mouseY;
    private double xMin, xMax, yMin, yMax;
    private double posX, posY;
    private boolean zoomChanged;
    
    // mouse selection
    private double selectX1, selectY1, selectX2, selectY2;
    private boolean selecting;
    private boolean selectionEnabled;
    
    // viewing mode
    private int mode;
    public final int SKY_MODE = 1;
    public final int UNIVERSE_MODE = 0;
    
    // references marks
    private boolean showReferencesMarks;
    
    /**
     * Constructor, initializing OpenGL viewer canvas
     */
    public ViewerCanvas()
    {
            GLCapabilities caps = new GLCapabilities();
            caps.setDoubleBuffered(true);
            caps.setHardwareAccelerated(true);

            // canvas settings
            canvas = new GLCanvas(caps);
            canvas.addGLEventListener(this);
            canvas.addMouseListener(this);
            canvas.addMouseWheelListener(this);
            canvas.addMouseMotionListener(this);
            canvas.setSize(SIZE_X, SIZE_Y);
            
            // zoomAndMove initialisation
            zoom = 1;
            mouseX = 0;
            mouseY = 0;
            xMin = -1;
            yMin = -1;
            xMax = 1;
            yMax = 1;
            posX = 0;
            posY = 0;
            zoomChanged = false;
            
            // selection initialisation
            selecting = false;
            selectionEnabled = false;
            
            // default mode
            mode = UNIVERSE_MODE;
            
            // by default, show references marks
            showReferencesMarks = true;

            // automatic refresh
            //Animator anim = new Animator(canvas);
            //anim.start();
    }
    
    
    // ----------
    //  ACCESSORS
    // ----------

    /**
     * Get OpenGL viewer canvas
     * @return viewer canvas
     */
    public GLCanvas getCanvas() {return canvas; }
    
    /**
     * Set mode: UNIVERSE_MODE or SKY_MODE
     * @param choosen mode
     * @throws java.lang.Exception
     */
    public void setMode(int m) throws Exception
    {
        if(m == UNIVERSE_MODE)  // canvas default bounds for universe mode
        {
            xMin = -1;
            yMin = -1;
            xMax = 1;
            yMax = 1;
            posX = 0;
            posY = 0;
        }
        else if(m == SKY_MODE)  // canvas default bounds for sky mode
        {
            xMin = 0;
            yMin = -1;
            xMax = 2;
            yMax = 1;
            posX = -1;
            posY = 0;
        }
        else
            throw new Exception("Only SKY_MODE and UNIVERSE_MODE are allowed!");
        
        // reset zoomAndMove
        zoom = 1;
        mode = m;
    }
    
    /**
     * Used to show or hide references marks
     * @param s
     */
    public void setShowReferencesMarks(boolean s) {showReferencesMarks = s;}
    
    /**
     * Update (refresh) OpenGL viewer canvas display
     */
    public void updateCanvas() { canvas.display(); }
    
    /**
     * Enable or disable selection mode.
     * If selection is disabled, move mode is enabled.
     * @param s
     */
    public void enableSelectionMode(boolean s) { selectionEnabled = s; }
    
    
    // -----
    //  MISC
    // -----
    
    /**
     * Used to draw a circle in the OpenGL canvas
     * @param gl
     * @param x, center position X
     * @param y, center position Y
     * @param circle radius (radians)
     */
    private void drawCircle(GL gl, double x, double y, double radius)
    {
        double step = Math.PI/100; // drawing precision
        double angle = (2*Math.PI) + step;    // arc circle angle (radians)
        double vectX1 = x;
        double vectY1 = y + radius;
        double vectX, vectY;
        
        gl.glBegin(GL.GL_LINE_STRIP);
            gl.glColor3d(0, 0, 255);
            for(double i=0; i<=angle; i+=step)
            {
                vectX = x + (radius * Math.sin(i));
                vectY = y + (radius * Math.cos(i));
                gl.glVertex2d(vectX1, vectY1);
                vectY1 = vectY;
                vectX1 = vectX;
            }
        gl.glEnd();
    }
    
    /**
     * Used to draw an hyperbole in OpenGL canvas
     * @param gl
     */
    private void drawHyperbole(GL gl)
    {
        gl.glBegin(GL.GL_LINE_STRIP);
            gl.glColor3d(0, 0, 255);
            for(double x=1; x<Environment.getAscensionMax(); x+=0.01d)
                gl.glVertex2d(x, Math.sqrt(x*x-1));
        gl.glEnd();
        gl.glBegin(GL.GL_LINE_STRIP);
            gl.glColor3d(0, 0, 255);
            for(double x=1; x<Environment.getAscensionMax(); x+=0.01d)
                gl.glVertex2d(x, -Math.sqrt(x*x-1));
        gl.glEnd();
    }
    
    /**
     * Draw the mouse selection in OpenGL canvas
     * @param gl
     */
    private void drawSelection(GL gl)
    {
        double x1 = selectX1, x2 = selectX2, y1 = selectY1, y2 = selectY2;
        
        // corrections
        if(mode == SKY_MODE)
        {
            x1 *= (Math.PI);
            x2 *= (Math.PI);
            y1 *= (Math.PI / 2.0d);
            y2 *= (Math.PI / 2.0d);
        }
        else if((Environment.getKappa() < 0) && (Environment.getView() <= 3))
        {
            x1 += 2;
            x2 += 2;
        }
        
        if(selecting == true)
        {
            gl.glBegin(GL.GL_LINE_LOOP);
                gl.glColor3d(0, 255, 0);
                gl.glVertex2d(x1, y1);
                gl.glVertex2d(x1, y2);
                gl.glVertex2d(x2, y2);
                gl.glVertex2d(x2, y1);
            gl.glEnd();
        }
    }
    
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    /**
     * Compute canvas mouse cursor pointed location
     * @param axis: if =X_AXIS, return X location, if =Y_AXIS, return Y location, else return NaN
     * @return X or Y location
     */
    private double mouseToCanvasLocation(int axis)
    {
        if(axis == X_AXIS)
        {
            double u = (2.0d * (double)mouseX - (double)SIZE_X) / (double)SIZE_X;   // mouse X position to canvas X axis coefficient
            double x0 = (xMax - xMin) / 2 + xMin;   // X axis canvas center
            return x0 + u * (x0 - xMin);    // canvas X position
        }
        else if(axis == Y_AXIS)
        {
            double v = -(2.0d * (double)mouseY - (double)SIZE_Y) / (double)SIZE_Y;   // mouse Y position to canvas Y axis coefficient
            double y0 = (yMax - yMin) / 2 + yMin;   // Y axis canvas center
            return y0 + v * (y0 - yMin);    // canvas Y position
        }
        else
            return 1.0 / 0.0d;  // NaN
    }
    
    /**
     * Zoom in/out openGL viewer canvas, mouse position is the center of the zoomAndMove
     * @param gl
     */
    private void zoomAndMove(GL gl)
    {
        if(zoom != 1.0d)
        {
            if(zoomChanged == true)
            {
                // mouse position in canvas
                double x = mouseToCanvasLocation(X_AXIS);
                double y = mouseToCanvasLocation(Y_AXIS);

                // temporary canvas bounds (the mouse cursor pointed location becomes the center)
                xMin = x - 1 / zoom;
                yMin = y - 1 / zoom;
                xMax = x + 1 / zoom;
                yMax = y + 1 / zoom;
                
                // mouse position in canvas after zoomAndMove (according to temporary xMin, yMin, xMax, yMax values)
                double x2 = mouseToCanvasLocation(X_AXIS);
                double y2 = mouseToCanvasLocation(Y_AXIS);
                
                // new center, the mouse pointed location hasn't moved (it is the zoomAndMove center !not the canvas center!)
                posX = -(x - (x2 - x));
                posY = -(y - (y2 - y));
                
                // new canvas bounds
                xMin = -posX - 1 / zoom;
                yMin = -posY - 1 / zoom;
                xMax = -posX + 1 / zoom;
                yMax = -posY + 1 / zoom;
                
                zoomChanged = false;
            }
            
            gl.glScalef(zoom, zoom, 1); // zooming
        }
        gl.glTranslated(posX, posY, 0); // moving
    }
    
    /**
     * Method called by mouseReleased (see mouseReleased method)
     * @param q quasars Vector
     * @param i quasar index in Vector
     * @return 1 if quasar is selected, else 0
     */
    private int selectQuasar(Vector<Quasar> q, int i)
    {
        // if some quasars are already selected and if multiple selection is disabled
        if((Quasar.getSelectedCount() != 0) && !Quasar.isMultipleSelectionEnabled())
            if(q.get(i).isSelected() == true)   // ... selecting only already selected quasars
            {
                q.get(i).setSelected(true);
                return 1;
            }
            else    // quasar not already selected, not in new selection
            {
                q.get(i).setSelected(false);
                return 0;
            }
        else    // no quasars selected, or multiple selection enabled
        {
            q.get(i).setSelected(true);
            return 1;
        }
    }


    // ---------------------------------------------------------------------------------------------
    // Methods defined by GLEventListener, MouseListener, MouseWheelListener and MouseMotionListener
    // ---------------------------------------------------------------------------------------------

    /**
     * Initializing OpenGL (called by OpenGL itself)
     * @param drawable
     */
    public void init(GLAutoDrawable drawable)
    {
            GL gl = drawable.getGL();

            gl.glClearColor(0, 0, 0, 0);
    }

    /**
     * OpenGL refresh display method (called by OpenGL itself)
     * @param drawable
     */
    public void display(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        
        // clear viewer
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);   // drawing
        gl.glLoadIdentity();    // initializing matrix (with entity matrix)
        
        if(Environment.isSomethingToShow())   // something to draw?
        {
            // zooming and moving, if necessary
            zoomAndMove(gl);
            
            if(mode == UNIVERSE_MODE)
            {
                if((Environment.getKappa() < 0) && (Environment.getView() <= 3))
                {
                    // moving
                    gl.glTranslated(-2.0d, 0, 0);
                    if(showReferencesMarks)
                        drawHyperbole(gl);
                }
                else
                    if(showReferencesMarks)
                        drawCircle(gl, 0, 0, 1.0d);
                
                // drawing quasars
                gl.glBegin(GL.GL_POINTS);
                    Vector<Quasar> q = Environment.getQuasars();
                    for(int i=0; i<q.size(); i++)
                    {
                        if(q.get(i).isSelected() == true)
                            gl.glColor3ub((byte)255, (byte)255, (byte)255); // quasar selected color
                        else
                            gl.glColor3ub((byte)255, (byte)0, (byte)0); // quasar not selected color
                        gl.glVertex2d(q.get(i).getx(), q.get(i).gety());
                    }
                gl.glEnd();
            }
            else    // SKY_MODE
            {
                gl.glScaled(1.0d/Math.PI, 2.0d/Math.PI, 1); // changing scale

                // drawing axis
                if(showReferencesMarks)
                {
                    gl.glBegin(GL.GL_LINES);
                        gl.glColor3ub((byte)255, (byte)255, (byte)255);
                        // x axis
                        gl.glVertex2d(0, 0);
                        gl.glVertex2d(2*Math.PI, 0);
                        // y axis
                        gl.glVertex2d(0, -Math.PI/2);
                        gl.glVertex2d(0, Math.PI/2);
                    gl.glEnd();
                }

                // drawing quasars
                gl.glBegin(GL.GL_POINTS);
                    Vector<Quasar> q = Environment.getQuasars();
                    for(int i=0; i<q.size(); i++)
                    {
                        if(q.get(i).isSelected() == true)
                            gl.glColor3ub((byte)255, (byte)255, (byte)255);
                        else
                            gl.glColor3ub((byte)251, (byte)255, (byte)0);
                        gl.glVertex2d(q.get(i).getAscension(), q.get(i).getDeclination());
                    }
                gl.glEnd();
            }
            
            if(selectionEnabled)
                drawSelection(gl);  // draw selection if necessary
        }

        gl.glFlush();
        
        /*gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3d(1.0, 0, 0);
        gl.glVertex3d(0, 0, 0);

        gl.glColor3d(0, 1.0, 0);
        gl.glVertex3d(0.25, 0.5, 0.5);

        gl.glColor3d(0, 0, 1.0);
        gl.glVertex3d(1.0, 1.0, 1.0);
        gl.glEnd();

        gl.glRotated(0.1, 1, 0, 0);*/
    }
    
    /**
     * Used to handle mouse wheel actions (zoomAndMove in, zoomAndMove out) (called by OpenGL itself)
     * @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if(Environment.isSomethingToShow())
        {
            // new zoomAndMove value is divided by 100 (slower zoomAndMove), and multiplied by previous zoomAndMove value (regular zoomAndMove speed)
            zoom += -e.getUnitsToScroll() / 100.0 * zoom; // setting new zoomAndMove value
            mouseX = e.getX();  // get mouse position X
            mouseY = e.getY();  // Y
            zoomChanged = true;
            updateCanvas(); // refresh display
        }
    }
    
    /**
     * Reset zoomAndMove on mouse click
     * @param e
     */
    public void mouseClicked(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON3)
        {
            try
            {
                setMode(mode);  // reset view
            }
            catch(Exception ex) { }

            updateCanvas(); // refresh display
        }
    }
    
    /**
     * Called when starting a selection or a move
     * @param e
     */
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(Environment.isSomethingToShow() == true)
            {
                mouseX = e.getX();
                mouseY = e.getY();
                selectX1 = mouseToCanvasLocation(X_AXIS);
                selectY1 = mouseToCanvasLocation(Y_AXIS);
                if(selectionEnabled)
                    selecting = true;
            }
        }
    }
    
    /**
     * Called when a selection has been made, selecting content
     * @param e
     */
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if((Environment.isSomethingToShow() == true) && selectionEnabled)
            {
                mouseX = e.getX();
                mouseY = e.getY();
                selectX2 = mouseToCanvasLocation(X_AXIS);
                selectY2 = mouseToCanvasLocation(Y_AXIS);
                
                // reverse x1,x2 or y1,y2 if necessary
                if(selectX1 > selectX2)
                {
                    double tmp = selectX1;
                    selectX1 = selectX2;
                    selectX2 = tmp;
                }
                if(selectY1 > selectY2)
                {
                    double tmp = selectY1;
                    selectY1 = selectY2;
                    selectY2 = tmp;
                }

                // now, selecting quasars
                int nbSelected = 0;
                Vector<Quasar> q = Environment.getQuasars();
                // if quasar is in selected zone
                if(mode == UNIVERSE_MODE)
                {
                    // corrections
                    if((Environment.getKappa() < 0) && (Environment.getView() <= 3))
                    {
                        selectX1 += 2;
                        selectX2 += 2;
                    }
                    
                    for(int i=0; i<q.size(); i++)
                    {
                        if((q.get(i).getx() > selectX1) && (q.get(i).getx() < selectX2) && (q.get(i).gety() > selectY1) && (q.get(i).gety() < selectY2))
                            nbSelected += selectQuasar(q, i);
                        else    // quasar not in selected zone
                            if(Quasar.isMultipleSelectionEnabled() == false)    // if multiple selection is disabled
                                q.get(i).setSelected(false);
                            else
                                if(q.get(i).isSelected())
                                    nbSelected++;
                    }
                }
                else    // SKY_MODE
                {
                    // corrections
                    selectX1 *= (Math.PI);
                    selectX2 *= (Math.PI);
                    selectY1 *= (Math.PI / 2.0d);
                    selectY2 *= (Math.PI / 2.0d);
                    
                    for(int i=0; i<q.size(); i++)
                    {
                        if((q.get(i).getAscension() > selectX1) && (q.get(i).getAscension() < selectX2) && (q.get(i).getDeclination() > selectY1) && (q.get(i).getDeclination() < selectY2))
                            nbSelected += selectQuasar(q, i);
                        else    // quasar not in selected zone
                            if(Quasar.isMultipleSelectionEnabled() == false)    // if multiple selection is disabled
                                q.get(i).setSelected(false);
                            else
                                if(q.get(i).isSelected())
                                    nbSelected++;
                    }
                }
                Quasar.setSelectedCount(nbSelected);
                
                selecting = false;
                updateCanvas(); // updating viewer canvas
                Environment.getMainWindow().updateSelection();
            }
        }
    }
    
    public void mouseDragged(MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        selectX2 = mouseToCanvasLocation(X_AXIS);
        selectY2 = mouseToCanvasLocation(Y_AXIS);

        if(selectionEnabled)
            updateCanvas(); // updating viewer canvas (to draw selection square)
        else    // moving mode
        {
            double offsetX, offsetY;
            if(mode == UNIVERSE_MODE)
            {
                offsetX = (selectX2 - selectX1) / 2;
                offsetY = (selectY2 - selectY1) / 2;
            }
            else    // SKY_MODE
            {
                offsetX = (selectX2 - selectX1) / Math.PI;
                offsetY = (selectY2 - selectY1) / (Math.PI / 2.0d);
            }
            
            posX += offsetX;
            posY += offsetY;
            xMin -= offsetX;
            yMin -= offsetY;
            xMax -= offsetX;
            yMax -= offsetY;
            selectX1 = selectX2;
            selectY1 = selectY2;
            updateCanvas(); // updating viewer canvas (to move)
        }
    }
    
    
    // UNUSED
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { }
    public void mouseEntered(MouseEvent arg0) { }
    public void mouseExited(MouseEvent arg0) { }
    public void mouseMoved(MouseEvent arg0) { }
}