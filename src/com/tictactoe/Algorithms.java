package com.tictactoe;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Algorithms")
public class Algorithms extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Algorithms() {
		super();
		// TODO Auto-generated constructor stub
	}

	static int move = 0;

	

	public static int[] tryAndLose() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (ResponseServlet.array[i][j] == '.') {
					ResponseServlet.array[i][j] = 'O';
					return new int[] { i, j };
				}
			}
		}
		return new int[] { 0, 0 };
	}

	public static int[] myAlgo() {

		switch (move) {
		case 0:
			move++;
			if (ResponseServlet.array[2][0] == '.') {
				ResponseServlet.array[2][0] = 'O';
				return new int[] { 2, 0 };
			} else {
				ResponseServlet.array[0][0] = 'O';
				return new int[] { 0, 0 };
			}
		case 1:
			move++;
			if (ResponseServlet.array[0][2] == '.') {
				ResponseServlet.array[0][2] = 'O';
				return new int[] { 0, 2 };
			} else {
				ResponseServlet.array[0][0] = 'O';
				return new int[] { 0, 0 };
			}
		case 2:
			move++;
			if (ResponseServlet.array[1][1] == '.' && ResponseServlet.array[0][2] == 'O') {
				ResponseServlet.array[1][1] = 'O';
				return new int[] { 1, 1 };
			} else if (ResponseServlet.array[1][0] == 'X') {
				ResponseServlet.array[1][2] = 'O';
				return new int[] { 1, 2 };
			} else if (ResponseServlet.array[1][2] == 'X') {
				ResponseServlet.array[1][0] = 'O';
				return new int[] { 1, 0 };
			} else if (ResponseServlet.array[0][1] == 'X') {
				ResponseServlet.array[2][1] = 'O';
				return new int[] { 2, 1 };
			} else if (ResponseServlet.array[2][1] == 'O') {
				ResponseServlet.array[0][1] = 'X';
				return new int[] { 0, 1 };
			} else if (ResponseServlet.array[0][0] == 'X') {
				ResponseServlet.array[2][2] = 'O';
				return new int[] { 2, 2 };
			} else if (ResponseServlet.array[0][0] == '.') {
				ResponseServlet.array[0][0] = 'O';
				return new int[] { 0, 0 };
			} else {
				return searchAlgo();
			}
		case 3:
			if (ResponseServlet.array[1][0] == 'X' && ResponseServlet.array[2][2] == '.'
					&& ResponseServlet.array[1][2] != '.') {
				ResponseServlet.array[2][2] = 'O';
				return new int[] { 2, 2 };
			} else if (ResponseServlet.array[1][2] == 'X' && ResponseServlet.array[0][0] == '.') {
				ResponseServlet.array[0][0] = 'O';
				return new int[] { 0, 0 };
			} else if (ResponseServlet.array[0][1] == 'X' && ResponseServlet.array[2][2] == '.'
					&& ResponseServlet.array[1][2] != '.') {
				ResponseServlet.array[2][2] = 'O';
				return new int[] { 2, 2 };
			} else if (ResponseServlet.array[2][1] == 'X' && ResponseServlet.array[0][0] == '.') {
				ResponseServlet.array[0][0] = 'O';
				return new int[] { 0, 0 };
			} else if (ResponseServlet.array[0][0] == 'X' && ResponseServlet.array[1][0] == '.') {
				ResponseServlet.array[1][0] = 'O';
				return new int[] { 1, 0 };
			} else if (ResponseServlet.array[0][1] == '.') {
				ResponseServlet.array[0][1] = 'O';
				return new int[] { 0, 1 };
			} else if (ResponseServlet.array[0][1] == 'X' && ResponseServlet.array[2][1] == '.') {
				ResponseServlet.array[2][1] = 'O';
				return new int[] { 2, 1 };
			} else if (ResponseServlet.array[2][1] == 'X' && ResponseServlet.array[0][1] == '.') {
				ResponseServlet.array[0][1] = 'O';
				return new int[] { 0, 1 };
			} else if (ResponseServlet.array[1][0] == 'X' && ResponseServlet.array[1][2] == '.') {
				ResponseServlet.array[1][2] = 'O';
				return new int[] { 1, 2 };
			} else if (ResponseServlet.array[1][2] == 'X' && ResponseServlet.array[1][0] == '.') {
				ResponseServlet.array[1][0] = 'O';
				return new int[] { 1, 0 };
			} else {
				return searchAlgo();
			}
		default:
			return searchAlgo();
		}
	}
	
	public static int[] searchAlgo() {
		for(int i=0;i<3;i++)
		{
			int count=0;
			for(int j=0;j<3;j++)
			{
				if(ResponseServlet.array[i][j]=='X') {
					count++;
				}
			}
			if(count==2) {
				for(int j=0;j<3;j++) {
					if(ResponseServlet.array[i][j]=='.') {
						return new int[] {i,j};
					}
				}
			}
		}
		
		for(int i=0;i<3;i++)
		{
			int count=0;
			for(int j=0;j<3;j++)
			{
				if(ResponseServlet.array[j][i]=='X') {
					count++;
				}
			}
			if(count==2) {
				for(int j=0;j<3;j++) {
					if(ResponseServlet.array[j][i]=='.') {
						return new int[] {j,i};
					}
				}
			}
		}
		
		int count1=0,count2=0;
		for(int i=0;i<3;i++)
		{
			if(ResponseServlet.array[i][i]=='X') {
				count1++;
			}
			else if(ResponseServlet.array[2-i][i]=='X') {
				count2++;
			}
		}
		
		if(count1==2) {
			for(int i=0;i<3;i++)
			{
				if(ResponseServlet.array[i][i]=='.')
					return new int[] {i,i};
			}
		}
		if(count2==2) {
			for(int i=0;i<3;i++)
			{
				if(ResponseServlet.array[i][i]=='.')
					return new int[] {i,i};
			}
		}
		
		Random rad = new Random();
		count1 = rad.nextInt(2);
		while(ResponseServlet.array[count1%3][count2/3]!='.') {
			count1 = rad.nextInt(2);
		}
		return new int[] {count1%3,count1/3};
	}

	public static int[] PlayerFirst() {

		switch (move) {
		case 0:
			move++;
			if (ResponseServlet.array[1][1] == '.') {
				ResponseServlet.array[1][1] = 'O';
			} else {
				ResponseServlet.array[2][2] = 'O';
			}
		case 1:
			move++;
			if (ResponseServlet.array[0][0] == 'X' && ResponseServlet.array[2][2] == 'X'
					|| ResponseServlet.array[0][2] == 'X' && ResponseServlet.array[2][0] == 'X') {
				ResponseServlet.array[1][0]='O';
			}
			else if(ResponseServlet.array[0][0] == 'X' && ResponseServlet.array[0][2] == 'X') {
				ResponseServlet.array[0][1]='O';
			}
			else if(ResponseServlet.array[0][0] == 'X' && ResponseServlet.array[2][0] == 'X') {
				ResponseServlet.array[1][0]='O';
			}
			else if(ResponseServlet.array[0][2] == 'X' && ResponseServlet.array[2][2] == 'X') {
				ResponseServlet.array[1][2]='O';
			}
			else if(ResponseServlet.array[2][0] == 'X' && ResponseServlet.array[2][2] == 'X') {
				ResponseServlet.array[2][1]='O';
			}else if(ResponseServlet.array[0][0] == 'X' && ResponseServlet.array[0][1]=='X') {
				ResponseServlet.array[0][2]='O';
			}
			else if(ResponseServlet.array[0][1]=='X' && ResponseServlet.array[0][2]=='X') {
				ResponseServlet.array[0][0]='O';
			}
			else if(ResponseServlet.array[1][2]=='X' && ResponseServlet.array[0][2]=='X') {
				ResponseServlet.array[2][2]='O';
			}
			else if(ResponseServlet.array[1][2]=='X' && ResponseServlet.array[2][2]=='X') {
				ResponseServlet.array[0][2]='O';
			}
			else if(ResponseServlet.array[2][2]=='X' && ResponseServlet.array[2][1]=='X') {
				ResponseServlet.array[2][0]='O';
			}
			else if(ResponseServlet.array[2][1]=='X' && ResponseServlet.array[2][1]=='X') {
				ResponseServlet.array[2][2]='O';
			}
			else if(ResponseServlet.array[2][0]=='X' && ResponseServlet.array[1][0]=='X') {
				ResponseServlet.array[0][0]='O';
			}
			else if(ResponseServlet.array[0][0]=='X' && ResponseServlet.array[1][0]=='X') {
				ResponseServlet.array[2][0]='O';
			}
			else if(ResponseServlet.array[0][2]=='X') {
				ResponseServlet.array[2][0]='O';
			}
			else if(ResponseServlet.array[2][0]=='X') {
				ResponseServlet.array[0][2]='O';
			}
			else {
				ResponseServlet.array[2][0]='O';
			}
		default:
			return searchAlgo();
		}
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAccessControlHeaders(resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setAccessControlHeaders(response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setAccessControlHeaders(response);
		doGet(request, response);
	}

	private void setAccessControlHeaders(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
		response.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Origin, Origin, Accept, X-Requested-With, Content-Type");
	}

}
