package tambola.ticket.generator.model;

import java.util.Arrays;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@ElementCollection
	public int numbers[][];

	public Ticket() {
		this.numbers = new int[3][9];
	}

	public int getRowCount(int r) {
		int count = 0;
		for (int i = 0; i < 9; i++) {
			if (numbers[r][i] != 0)
				count++;
		}

		return count;
	}

	public int getColCount(int c) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (numbers[i][c] != 0)
				count++;
		}

		return count;
	}

	// gives the row number of first found empty cell in given column
	public int getEmptyCellInCol(int c) {
		for (int i = 0; i < 3; i++) {
			if (numbers[i][c] == 0)
				return i;
		}

		return -1;
	}

	private void sortColumnWithThreeNumbers(int c) throws Exception {
		int emptyCell = this.getEmptyCellInCol(c);
		if (emptyCell != -1) {
			throw new Exception("Hey! your column has <3 cells filled, invalid function called");
		}

		int tempArr[] = new int[] { this.numbers[0][c], this.numbers[1][c], this.numbers[2][c] };
		Arrays.sort(tempArr);

		for (int r = 0; r < 3; r++) {
			this.numbers[r][c] = tempArr[r];
		}
	}
	
	private void sortColumnWithTwoNumbers(int c) throws Exception {
		int emptyCell = this.getEmptyCellInCol(c);
		if (emptyCell == -1) {
			throw new Exception("Hey! your column has 3 cells filled, invalid function called");
		}

		int cell1, cell2;
		if (emptyCell == 0) {
			cell1 = 1;
			cell2 = 2;
		} else if (emptyCell == 1) {
			cell1 = 0;
			cell2 = 2;
		} else { // emptyCell == 2
			cell1 = 0;
			cell2 = 1;
		}

		if (this.numbers[cell1][c] < this.numbers[cell2][c]) {
			return;
		} else {
			// swap
			int temp = this.numbers[cell1][c];
			this.numbers[cell1][c] = this.numbers[cell2][c];
			this.numbers[cell2][c] = temp;
		}
	}

	private void sortColumn(int c) throws Exception {
		if (this.getColCount(c) == 1) {
			return;
		}

		else if (this.getColCount(c) == 2) {
			this.sortColumnWithTwoNumbers(c);
		}

		else {
			this.sortColumnWithThreeNumbers(c);
		}
	}

	public void sortColumns() throws Exception {
		for (int c = 0; c < 9; c++) {
			this.sortColumn(c);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[][] getNumbers() {
		return numbers;
	}

	public void setNumbers(int[][] numbers) {
		this.numbers = numbers;
	}
	
	

}
