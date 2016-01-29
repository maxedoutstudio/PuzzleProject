import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class Square {

	private int id;				// ID of the square starting from 1
	private boolean flag;		// toggle the color of the square to black (true) or white (false)
	private int rowPosition;	// row of the square
	private int colPosition;	// column of the square
	private JButton square;		// button for the square
	
	Square(int id, boolean f, int rowPosition, int colPosition, JButton square)
	{
		this.id = id;
		this.flag = f;
		this.rowPosition = rowPosition;
		this.colPosition = colPosition;
		this.square = square;
		
		square.setBackground(Color.white);		// white is the default color
		// On-click, the square gets filled in
		square.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (flag)
				{
					square.setBackground(Color.white);
					setFlag(false);
				} else {
					square.setBackground(Color.black);
					setFlag(true);
				}

			}
		});
	}
	
	public void setID(int i) 
	{
		id = i;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setFlag(boolean f)
	{
		flag = f;
		
		// if TRUE, set to BLACK
		// if FALSE, set to WHITE
		square.setBackground(flag ? Color.black : Color.white);
	}
	
	public boolean getFlag()
	{
		return flag;
	}
	
	public void setRowPosition(int pos)
	{
		rowPosition = pos;
	}
	
	public int getRowPosition()
	{
		return rowPosition;
	}
	
	public void setColumnPosition(int pos)
	{
		colPosition = pos;
	}
	
	public int getColumnPosition()
	{
		return colPosition;
	}
	
	@Override
	public String toString()
	{
		return "ID:" + id + "\tFlag:" + flag + "\tRow:" + rowPosition + "\tColumn:" + colPosition;
	}
}
