package GameOfLife;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import javax.swing.JPanel;

public class Board extends JPanel {
        private Grid grid;
        private int gridSize;

        public Board(int columns, int rows, int gridSize) {  
            grid = new Grid(columns, rows);
            this.gridSize = gridSize;
            
            setPreferredSize(new Dimension(grid.getColumns() * gridSize, grid.getRows() * gridSize));
        }
                        
        public void updateGrid() {
            grid.update();
            repaint();
        }
        
        public void clearGrid() {
            grid.clear();
            repaint();
        }
        
        public int getRows() {
            return grid.getRows();
        }
        
        public int getColumns() {
            return grid.getColumns();
        }
        
        public int getGridSize() {
            return gridSize;
        }
        
        public void addPattern(int x, int y, List<Point> points) {
            for(Point point : points)
                grid.setAlive(x + point.x, y + point.y);
            repaint();
        }
                
        @Override
        public void paintComponent(Graphics g) {        
            super.paintComponent(g);

            int width  = getWidth()  / grid.getColumns();
            int height = getHeight() / grid.getRows();
            
            for(int column = 0; column < grid.getColumns(); column++) {
                for(int row = 0; row < grid.getRows(); row++) {
                    if(grid.getState(column, row) == Grid.ALIVE)
                        g.setColor(Color.GREEN);
                    else
                        g.setColor(Color.BLACK);

                    int x = column * width;
                    int y = row * height;

                    g.fillRect(x, y, width, height);
                }
            }
        }
}