package com.tictactoe;

import java.util.Random;

class Move{
	Integer row, col, score;
	public Move(){
	}
}

class TicTacToe{
	char player, computer, array[][] = new char[3][3];
	
	TicTacToe() {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				array[i][j]=ResponseServlet.array[i][j];
			}
		}
	}
}

public class MiniMax {
	
	public static int[] RandomAlgorithm() {
		Random r = new Random();
		int rand1 = r.nextInt(3);
		int rand2 = r.nextInt(3);

		while (ResponseServlet.array[rand1][rand2] != '.') {
			rand1 = r.nextInt(3);
			rand2 = r.nextInt(3);
		}
		ResponseServlet.array[rand1][rand2] = 'O';
		return new int[] { rand1, rand2 };
	}
	
	public static boolean checkMate(char[][] array) {

		if ((array[0][0] == array[1][1] && array[1][1] == array[2][2]) && array[1][1] != '.') {
			return true;
		} else if (array[0][2] == array[1][1] && array[1][1] == array[2][0] && array[1][1] != '.') {
			return true;
		}

		for (int i = 0; i < 3; i++) {
			if (array[i][0] == array[i][1] && array[i][0] == array[i][2] && array[i][0] != '.') {
				return true;
			}

			if (array[0][i] == array[1][i] && array[0][i] == array[2][i] && array[0][i] != '.') {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isTie(char[][] array) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (array[i][j] == '.')
					return false;
			}
		}
		return true;
	}
	public Move miniMax(TicTacToe t,boolean maximizingPlayer) {
		Move bestMove = new Move();
		if(MiniMax.checkMate(t.array))
		{
			if(maximizingPlayer)
				bestMove.score=-1;
			else
				bestMove.score=1;
			return bestMove;
		}
		else if(MiniMax.isTie(t.array))
		{
			bestMove.score=0;
			return bestMove;
		}
		
		bestMove.score = maximizingPlayer?-2:2;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				if(t.array[i][j] == '.') {
					t.array[i][j]=maximizingPlayer?'O':'X';
					Move boardState = miniMax(t,!maximizingPlayer);
					
					if(maximizingPlayer) {
						if(boardState.score>bestMove.score) {
							bestMove.score = boardState.score;
							bestMove.row = i;
							bestMove.col = j;
						}
					}
					else if(boardState.score<bestMove.score) {
						bestMove.score = boardState.score;
						bestMove.row = i;
						bestMove.col = j;
					}
					t.array[i][j]='.';
				}
			}
		}
		
		return bestMove;
	}
	
	public static int[] caller() {
		MiniMax m = new MiniMax();
		TicTacToe t = new TicTacToe();
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				System.out.print(t.array[i][j]);
			}
			System.out.println();
		}
		Move move = m.miniMax(t,true);
		ResponseServlet.array[move.row][move.col] = 'O';
		return new int[] {move.row,move.col};
	}

}
