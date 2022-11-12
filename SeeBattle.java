package com.example;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntPredicate;

public class SeeBattle {
	
	public enum Dimension {
		UNSET,HORSONTAL, VERTICAL
	}
	Random ran = ThreadLocalRandom.current();
	
	/*private int grid[][]= {
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0}
			};
	
*/
	        private int[][] grid= {
			  {0,0,0,0,0,0,0,0},
			  {0,0,0,0,0,0,0,0},
			  {0,0,0,0,0,0,0,0},
			  {0,0,0,0,0,0,0,0},
			  {0,0,0,0,0,0,0,0},
			  {0,0,0,0,0,0,0,0},
			  {0,0,0,0,0,0,0,0},
			};
			
			private int[] ships = { 1, 1, 1, 1, 2, 2, 2, 3, 3, 4 };

			public void arrangeShips() {
				cleanGrid();
				shuffleArray(ships);
				if (arrange(0)) {
					printGrid();
				} else {
					System.out.println("Can't arrange!!!");
				}

			}

			private boolean arrange(int currShipIndex) {
				if (currShipIndex == ships.length) {
					return true;
				}
				int shipLength = ships[currShipIndex];
				Dimension dimensionBeforeBacktrack = Dimension.UNSET;
				for (int columnInd = 0; columnInd < grid.length; columnInd++) {
					for (int rowInd = 0; rowInd < grid.length; rowInd++) {
						Dimension validDimension = findPlace(columnInd, rowInd, shipLength, dimensionBeforeBacktrack);
						dimensionBeforeBacktrack = Dimension.UNSET;
						if (validDimension != Dimension.UNSET) {
							paintShip(shipLength, validDimension, columnInd, rowInd, shipLength);
							if (arrange(currShipIndex + 1)) {
								return true;
							} else {
								paintShip(0, validDimension, columnInd, rowInd, shipLength);
								dimensionBeforeBacktrack = validDimension;
								//System.out.println("Backtracking! Will rearange ship:"+ shipLength);
							}
						}

					}
				}
				return false;
			}

			private Dimension findPlace(int columnInd, int rowInd, int shipLength, Dimension dimensionBeforeBacktrack) {

				Dimension validDimension = Dimension.UNSET;
				Dimension tryDimension;
				if (dimensionBeforeBacktrack != Dimension.UNSET) {
					tryDimension = (dimensionBeforeBacktrack == Dimension.HORSONTAL) ? Dimension.VERTICAL
							: Dimension.HORSONTAL;
				} else {
					tryDimension = (ran.nextInt(2) == 1) ? Dimension.HORSONTAL : Dimension.VERTICAL;
				}
				if (isValidArange(tryDimension, columnInd, rowInd, shipLength)) {
					validDimension = tryDimension;
				}
				return validDimension;
			}

			boolean isValidArange(Dimension dimension, int columnInd, int rowInd, int shipLength) {
				if (dimension == Dimension.HORSONTAL) {
					return isValidArangeHorisontal(columnInd, rowInd, shipLength);
				} else if (dimension == Dimension.VERTICAL) {
					return isValidArangeVerical(columnInd, rowInd, shipLength);
				}
				return false;
			}

			private boolean isValidArangeHorisontal(int columnInd, int rowInd, int shipLength) {

				if (rowInd + shipLength >= grid.length) {
					return false;
				}

				IntPredicate validate = column -> {
					int startPos = (rowInd - 1 >= 0) ? rowInd - 1 : rowInd;
					int endPos = (startPos + shipLength + 1 < grid.length) ? startPos + shipLength + 1
							: startPos + shipLength;
					for (int i = startPos; i <= endPos; i++) {
						if (grid[column][i] > 0) {
							return false;
						}
					}
					return true;
				};

				if (!validate.test(columnInd)) {
					return false;
				}

				if (columnInd - 1 >= 0 && !validate.test(columnInd - 1)) {
					return false;
				}
				if (columnInd + 1 < grid.length && !validate.test(columnInd + 1)) {
					return false;
				}

				if (rowInd + shipLength + 1 <= grid.length && grid[columnInd][rowInd + shipLength + 1] == 1) {
					return false;
				}

				if (rowInd - 1 >= 0 && grid[columnInd][rowInd - 1] == 1) {
					return false;
				}

				return true;
			}

			boolean isValidArangeVerical(int columnInd, int rowInd, int shipLength) {
				if (columnInd + shipLength >= grid.length) {
					return false;
				}

				IntPredicate validate = row -> {
					int startPos = (columnInd - 1 >= 0) ? columnInd - 1 : columnInd;
					int endPos = (startPos + shipLength + 1 < grid.length) ? startPos + shipLength + 1
							: startPos + shipLength;

					for (int i = startPos; i <= endPos; i++) {
						if (grid[i][row] > 0) {
							return false;
						}
					}
					return true;
				};

				if (!validate.test(rowInd)) {
					return false;
				}

				if (rowInd + 1 <= grid.length && !validate.test(rowInd + 1)) {
					return false;
				}

				if (rowInd - 1 >= 0 && !validate.test(rowInd - 1)) {
					return false;
				}

				return true;
			}

			private void paintShip(int dot, Dimension dimension, int columnInd, int rowInd, int shipLength) {
				if (dimension == Dimension.VERTICAL) {
					for (int i = columnInd; i < columnInd + shipLength; i++) {
						grid[i][rowInd] = dot;
					}
				}

				if (dimension == Dimension.HORSONTAL) {
					for (int i = rowInd; i < rowInd + shipLength; i++) {
						grid[columnInd][i] = dot;
					}
				}
			}

			private void printGrid() {
				System.out.println("  See Battle Grid");
				for (int columnInd = 0; columnInd < grid.length; columnInd++) {
					for (int rowInd = 0; rowInd < grid.length; rowInd++) {
						System.out.print((grid[columnInd][rowInd] > 0) ? " " + grid[columnInd][rowInd] + " " : " - ");
					}
					System.out.println();
				}
				System.out.println();

			}

			private void cleanGrid() {
				for (int columnInd = 0; columnInd < grid.length; columnInd++) {
					for (int rowInd = 0; rowInd < grid.length; rowInd++) {
						grid[columnInd][rowInd] = 0;
					}
				}

			}

			private void shuffleArray(int[] ar) {
				for (int i = ar.length - 1; i > 0; i--) {
					int index = ran.nextInt(i + 1);
					int a = ar[index];
					ar[index] = ar[i];
					ar[i] = a;
				}
			}
	
}
