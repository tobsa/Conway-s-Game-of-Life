package GameOfLife;



import java.util.LinkedHashMap;
import java.util.Map;

public class Grid {
    public static final int INVALID_STATE = -1;
    public static final int DEAD = 0;
    public static final int ALIVE = 1;
    
    private int rows;
    private int columns;
    private int grid[];
    
    public Grid(int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        
        grid = new int[rows * columns];
    }
    
    public void setState(int column, int row, int state) {
        if(row < 0 || row >= rows || column < 0 || column >= columns) 
            return;
        
        grid[column + row * columns] = state;
    }
    
    public void setAlive(int column, int row) {
        setState(column, row, ALIVE);
    }
    
    public void setDead(int column, int row) {
        setState(column, row, DEAD);
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public int getState(int column, int row) {
        if(row < 0 || row >= rows || column < 0 || column >= columns) 
            return INVALID_STATE;
        
        return grid[column + row * columns];
    }
    
    public int getLiveNeighbourAmount(int column, int row) {
        int index     = column + row * columns;
        int liveCells = 0;

        // Get the boundries so we don't go out of bounds
        int r = row * columns + columns;     // Right
        int l = row * columns;               // Left
        int d = grid.length;                 // Down
        int u = 0;                           // Up

        // Check against right, left, down and up neighbour
        if(index + 1 < r && grid[index + 1] == ALIVE)
            liveCells++;
        if(index - 1 >= l && grid[index - 1] == ALIVE)
            liveCells++;
        if(index + columns < d && grid[index + columns] == ALIVE)
            liveCells++;
        if(index - columns >= u && grid[index - columns] == ALIVE)
            liveCells++;

        // Check against up-left, down-left, top-right, bottom-right 
        if(index - 1 >= l && index - columns >= u && grid[index - columns - 1] == ALIVE)
            liveCells++;
        if(index - 1 >= l && index + columns < d && grid[index + columns - 1] == ALIVE)
            liveCells++;
        if(index + 1 < r && index - columns >= u && grid[index - columns + 1] == ALIVE)
            liveCells++;
        if(index + 1 < r && index + columns < d && grid[index + columns + 1] == ALIVE)
            liveCells++;

        return liveCells;
    }
       
    public void update() {           
        Map<Integer, Integer> map = new LinkedHashMap();
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                int index = column + row * columns;
                
                int liveNeighbours = getLiveNeighbourAmount(column, row);

                if(liveNeighbours == 2)
                    map.put(index, grid[index]);
                else if(liveNeighbours < 2 || liveNeighbours > 3)
                    map.put(index, DEAD);
                else
                    map.put(index, ALIVE);
            }
        }
        
        for(int i : map.keySet())
            grid[i] = map.get(i);
    }
    
    public void clear() {
        for(int i = 0; i < grid.length; i++)
            grid[i] = DEAD;
    }
}
