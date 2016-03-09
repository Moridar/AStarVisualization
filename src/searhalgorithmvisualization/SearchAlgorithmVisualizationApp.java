package searhalgorithmvisualization;

import app2dapi.Platform;
import app2dapi.geometry.G2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;
import app2dapi.input.charinput.CharInputEvent;
import app2dapi.input.keyboard.Key;
import app2dapi.input.keyboard.KeyPressedEvent;
import app2dapi.input.keyboard.KeyReleasedEvent;
import app2dapi.panandzoom2dapp.PanAndZoom2DApp;
import app2dapi.panandzoom2dapp.PanAndZoomAdapter;
import app2dapi.panandzoom2dapp.PanAndZoomInit;
import app2dapi.panandzoom2dapp.PanAndZoomToolKit;
import app2dpcimpl.PCPlatformImpl;
import java.util.ArrayList;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tobias Grundtvig
 */
public class SearchAlgorithmVisualizationApp implements PanAndZoom2DApp {

    private double hudHeight;
    private double hudWidth;
    private int worldWidth = 20;
    private int worldHeight = 10;
    private ColorFactory cf;
    private G2D g2d;
    private Tile[][] tiles;
    private TileDrawer tileDrawer;
    private ArrayList<Tile> openList;
    private ArrayList<Tile> closedList;
    private Tile goal;
    private Tile currTile;
    private boolean autoPlay = false;

    @Override
    public PanAndZoomInit initialize(PanAndZoomToolKit tk, double aspectRatio) {
        openList = new ArrayList<>();
        closedList = new ArrayList<>();
        tiles = new Tile[worldWidth][worldHeight];
        for (int y = 0; y < worldHeight; ++y) {
            for (int x = 0; x < worldWidth; ++x) {
                tiles[x][y] = new Tile(x, y, x + y);
            }
        }
        currTile = tiles[0][0];
        goal = tiles[worldWidth-1][worldHeight-1];
//        randomRocksOnMap(450);
        setMap();
        goal.toogleEndPoint();
        if(goal.isWall()) goal.toogleWall();
        currTile.toogleStartPoint();
        currTile.setH(Double.MAX_VALUE);
        if(currTile.isWall()) currTile.toogleWall();

        this.hudHeight = 1000;
        this.hudWidth = hudHeight * aspectRatio;
        this.cf = tk.cf();
        this.g2d = tk.g2d();
        tileDrawer = new TileDrawer(g2d, cf);
        return new PanAndZoomInit(g2d.origo(),
                g2d.newPoint2D(hudWidth, hudHeight),
                g2d.origo(),
                g2d.newPoint2D(worldWidth, worldHeight),
                g2d.newPoint2D(worldWidth * 0.5, worldHeight * 0.5),
                worldWidth, 1, worldWidth);
    }

    private void randomRocksOnMap(int numberOfRocks){
        Random rnd = new Random();
        for (int i = 0; i < numberOfRocks; i++) {
            Tile t = tiles[rnd.nextInt(worldWidth)][rnd.nextInt(worldHeight)];
            if(t.isWall()) i--;
            else t.toogleWall();
            
            
        }
    }
    private void setMap() {
        currTile = tiles[0][0];
        goal = tiles[15][0];

        for (int i = 0; i < 8; i++) {
            tiles[1][0 + i].toogleWall();
            tiles[5][8 - i].toogleWall();
            tiles[14][0 + i].toogleWall();
            if (4 + i < 10) {
                tiles[15 - i][7].toogleWall();
            }
        }
        tiles[4][8].toogleWall();
        tiles[4][1].toogleWall();
        tiles[3][1].toogleWall();
    }
    
    private void nextAction() {
        if (goal == currTile) {
            
        } else {
            currTile.clearCurrentPath();
            if (!openList.isEmpty()) {
                currTile = openList.get(0);
            }
            for (Tile t : openList) { //Search for tile with lowest F value
                if (currTile.getF() >= t.getF()) {
                    currTile = t;
                }
            }
            currTile.setCurrentPath();
            closedList.add(currTile);
            currTile.visit();
            openList.remove(currTile);
        }
        if (currTile != goal) {
            ArrayList<Tile> adjectTiles = new ArrayList<>();
            if (currTile.getXPos() + 1 < worldWidth) {
                adjectTiles.add(tiles[currTile.getXPos() + 1][currTile.getYPos()]);
            }
            if (currTile.getXPos() - 1 >= 0) {
                adjectTiles.add(tiles[currTile.getXPos() - 1][currTile.getYPos()]);
            }
            if (currTile.getYPos() + 1 < worldHeight) {
                adjectTiles.add(tiles[currTile.getXPos()][currTile.getYPos() + 1]);
            }
            if (currTile.getYPos() - 1 >= 0) {
                adjectTiles.add(tiles[currTile.getXPos()][currTile.getYPos() - 1]);
            }

            for (Tile t : adjectTiles) {
                if (!t.isWall()) {
                    if (!closedList.contains(t)) {
                        if (!openList.contains(t)) {
                            openList.add(calcTile(currTile, t));
                        } else {
                            reCalcTile(currTile, t);
                        }
                    } else {/*Ignore*/

                    }
                }
            }
        }
    }

    @Override
    public boolean showMouseCursor() {
        return true;
    }

    @Override
    public void onMouseMoved(G2D.Point2D mouseHUDPos, G2D.Point2D mouseWorldPos) {

    }

    @Override
    public void onMousePressed(G2D.Point2D mouseHUDPos, G2D.Point2D mouseWorldPos) {

    }

    @Override
    public void onMouseReleased(G2D.Point2D mouseHUDPos, G2D.Point2D mouseWorldPos) {

    }

    @Override
    public void onKeyPressed(KeyPressedEvent e) {
        if(e.getKey() != Key.VK_SPACE) nextAction();
    }

    @Override
    public void onKeyReleased(KeyReleasedEvent e) {
        if(e.getKey() == Key.VK_SPACE) autoPlay = !autoPlay;
        else nextAction();

    }

    private void reCalcTile(Tile cameFrom, Tile t) {
        if (t.getG() > cameFrom.getG() + 1) {
            t.setParent(cameFrom);
        }
    }

    private Tile calcTile(Tile cameFrom, Tile t) {
        t.setParent(cameFrom);
        t.setH(Math.sqrt(Math.abs(t.getXPos() - goal.getXPos()) * Math.abs(t.getXPos() - goal.getXPos()) + Math.abs(t.getYPos() - goal.getYPos()) * Math.abs(t.getYPos() - goal.getYPos())));
        return t;
    }

    @Override
    public void onCharInput(CharInputEvent event) {

    }

    @Override
    public boolean update(double time) {
        if(autoPlay) nextAction();
        return true;
    }

    @Override
    public Color getBackgroundColor() {
        return cf.getGreen();
    }

    @Override
    public void drawWorld(Canvas canvas) {
        for (int y = 0; y < worldHeight; ++y) {
            for (int x = 0; x < worldWidth; ++x) {
                tileDrawer.draw(canvas, tiles[x][y]);
            }
        }
    }

    @Override
    public void drawHUD(Canvas canvas) {

    }

    @Override
    public void destroy() {

    }

    public static void main(String[] args) {
        PanAndZoom2DApp app = new SearchAlgorithmVisualizationApp();
        Platform pc = new PCPlatformImpl();
        pc.runApplication(new PanAndZoomAdapter(app));
    }

}
