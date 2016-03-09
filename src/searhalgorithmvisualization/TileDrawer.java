/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searhalgorithmvisualization;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Transformation2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.ColorFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class TileDrawer {

    private final G2D g2d;
    private final ColorFactory cf;

    public TileDrawer(G2D g2d, ColorFactory cf) {
        this.g2d = g2d;
        this.cf = cf;
    }

    public void draw(Canvas canvas, Tile tile) {
        Transformation2D parent = canvas.getTransformation();
        Transformation2D t = g2d.translate(tile.getXPos(), tile.getYPos());
        Transformation2D c = g2d.combine(parent, t);
        canvas.setTransformation(c);

        canvas.setColor(cf.getBlack());
        canvas.drawFilledRectangle(g2d.newPoint2D(0.5, 0.5), 1, 1);

        canvas.setColor(cf.getWhite());
        if (tile.isVisited()) {
            canvas.setColor(cf.getYellow());
        }
        if (tile.isCurrentPath()) {
            canvas.setColor(cf.getGreen());
        }
        if (tile.isStartPoint()) {
            canvas.setColor(cf.getRed());
        }
        if (tile.isEndPoint()) {
            canvas.setColor(cf.getBlue());
        }
        if(tile.isWall()) canvas.setColor(cf.getBlack());
        canvas.drawFilledRectangle(g2d.newPoint2D(0.5, 0.5), 0.95, 0.95);

        if (tile.isStartPoint()) {
            canvas.setColor(cf.getBlack());
            canvas.drawText(g2d.newPoint2D(0.5, 0.5), "Start", 0.5, true, true);
        } else if (tile.isEndPoint()) {
            canvas.setColor(cf.getWhite());
            canvas.drawText(g2d.newPoint2D(0.5, 0.5), "End", 0.5, true, true);
        } else if (!tile.isWall()){
            canvas.setColor(cf.getBlue());
            canvas.drawText(g2d.newPoint2D(0.5, 0.7), format(tile.getF(), 5), 0.3, true, true);
            canvas.drawText(g2d.newPoint2D(0.05, 0.0), "" + tile.getG(), 0.2, false, false);
            canvas.drawText(g2d.newPoint2D(0.80, 0.0), format(tile.getH(), 5), 0.2, true, false);
        }
        canvas.setTransformation(parent);

    }

    private String format(Double d, int ciffers) {
//        if (d>100) return d.toString().substring(0, 5);
//        else if (d>10) return d.toString().substring(0, 4);
//        else if(d>1) return d.toString().substring(0, 3);
//        else return d.toString().substring(0,3);
        if (d.toString().length() > ciffers) {
            return d.toString().substring(0, ciffers);
        }
        return d.toString();
    }

}
