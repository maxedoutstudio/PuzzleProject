
public class Grid {
	
	private int rows = 0;
	private int columns = 0;
	private String difficulty;
	
	Grid() { }
	
	Grid(int r, int c, String s) {
		rows = r;
		columns = c;
		difficulty = s;
	}
	
	public void setRowSize(int r)
	{
		rows = r;
	}
	
	public int getRowSize()
	{
		return rows;
	}
	
	public void setColumnSize(int c)
	{
		columns = c;
	}

	public int getColumnSize()
	{
		return columns;
	}
	
	public void setDifficulty(String s)
	{
		difficulty = s;
	}
	
	public String getDifficulty()
	{
		return difficulty;
	}
	
}
