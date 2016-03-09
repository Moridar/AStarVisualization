/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searhalgorithmvisualization;

/**
 *
 * @author Tobias Grundtvig
 */
public class Tile {

    private final int xPos;
    private final int yPos;
    private Tile parent = null;
    private double H;
    private boolean startPoint = false;
    private boolean endPoint = false;
    private boolean visited = false;
    private boolean currentPath = false;
    private boolean wall = false;

    public Tile(int xPos, int yPos, int value) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    public void toogleWall(){
        wall = !wall;
    }
    
    public boolean isWall(){
        return wall;
    }
    
    public void setCurrentPath(){
        currentPath = true;
        if(parent != null) parent.setCurrentPath();
    }
    
    public void clearCurrentPath(){
        currentPath = false;
        if(parent != null) parent.clearCurrentPath();
    }
    
    public boolean isCurrentPath(){
        return currentPath;
    }
    public boolean isStartPoint() {
        return startPoint;
    }

    public void toogleStartPoint() {
        startPoint = !startPoint;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public double getG() {
        if(parent == null) return 0;
        return 1 + parent.getG();
    }

    public Tile getParent() {
        return parent;
    }

    public void setParent(Tile parent) {
        this.parent = parent;
    }

    public double getH() {
        return H;
    }

    public void setH(double H) {
        this.H = H;
    }

    public boolean isEndPoint() {
        return endPoint;
    }

    public void toogleEndPoint() {
        endPoint = !endPoint;
    }

    public boolean isVisited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    public double getF() {
//        if(isCurrentPath()) return getG();
        return H + getG()*0.9;
    }
}
