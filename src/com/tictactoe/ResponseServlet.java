package com.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

class Operation {
	Integer player;
	Integer row;
	Integer col;
	
	Operation(Integer player, Integer row, Integer col) {
		this.player = player;
		this.row = row;
		this.col = col;
	}
}

@WebServlet("/ResponseServlet")
public class ResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAccessControlHeaders(resp);
	}

	static char[][] array = { { '.', '.', '.' }, { '.', '.', '.' }, { '.', '.', '.' } };
	static int[] index = { 0, 0, 0 };
	static int top=-1;
	static List<Operation> op = new ArrayList<>();
	static Map<String, String> map = new HashMap<>();
	public static boolean checkMate() {

		if ((array[0][0] == array[1][1] && array[1][1] == array[2][2]) && array[1][1] != '.') {
			index[0] = 0;
			index[1] = 4;
			index[2] = 8;
			return true;
		} else if (array[0][2] == array[1][1] && array[1][1] == array[2][0] && array[1][1] != '.') {
			index[0] = 2;
			index[1] = 4;
			index[2] = 6;
			return true;
		}

		for (int i = 0; i < 3; i++) {
			if (array[i][0] == array[i][1] && array[i][0] == array[i][2] && array[i][0] != '.') {
				index[0] = 0 + i * 3;
				index[1] = 1 + i * 3;
				index[2] = 2 + i * 3;

				return true;
			}

			if (array[0][i] == array[1][i] && array[0][i] == array[2][i] && array[0][i] != '.') {
				index[0] = i;
				index[1] = i + 3;
				index[2] = i + 6;

				return true;
			}
		}
		return false;
	}

	public static boolean isTie() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (array[i][j] == '.')
					return false;
			}
		}
		return true;
	}
	
	public void clean() {
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				ResponseServlet.array[i][j]='.';
			}
		}
		ResponseServlet.op = new ArrayList<>();
		ResponseServlet.map = new HashMap<>();
		ResponseServlet.top=-1;
		Algorithms.move=0;
	}
	
	public void display() {
		System.out.println("Printing array...");
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				System.out.print(array[i][j]);
			}
			System.out.println();
		}
		System.out.println("Printing stack");
		for(Operation o:op) {
			System.out.println("Player: "+o.player+" Row:"+o.row+" Col:"+o.col);
		}
		System.out.println("Top: "+top);
		System.out.println("Stack size: "+op.size());
	}
	
	public void undoRedo(HttpServletRequest request, HttpServletResponse response, String operation) throws ServletException, IOException {
		Map<String, String> map1 = new HashMap<>();
		 if(operation.equals("redo")) {
			 if(op.size()>top+1) {
				 top++;
				 map1.put("status", "complete");
				 map1.put("player1", String.valueOf(op.get(top).player));
				 map1.put("x1",String.valueOf(op.get(top).row));
				 map1.put("y1", String.valueOf(op.get(top).col));
				 array[op.get(top).row][op.get(top).col] = (op.get(top).player==1?'X':'O');
				 if(op.size()>top+1) {
					 top++;
					 map1.put("status", "complete");
					 map1.put("player2", String.valueOf(op.get(top).player));
					 map1.put("x2",String.valueOf(op.get(top).row));
					 map1.put("y2", String.valueOf(op.get(top).col));
					 array[op.get(top).row][op.get(top).col] = (op.get(top).player==1?'X':'O'); 
				 }
				 
				 display();
			 }
			 else {
				 map1.put("status", "error");
			 }
		 }
		 else {
			 if(top!=-1) {
				 map1.put("status", "complete");
				 map1.put("player2", String.valueOf(op.get(top).player));
				 map1.put("x2", String.valueOf(op.get(top).row));
				 map1.put("y2", String.valueOf(op.get(top).col));
				 array[op.get(top).row][op.get(top).col]='.';
				 top--;
				 if(top!=-1)
				 {
					 map1.put("player1", String.valueOf(op.get(top).player));
					 map1.put("x1",String.valueOf(op.get(top).row));
					 map1.put("y1", String.valueOf(op.get(top).col));
					 array[op.get(top).row][op.get(top).col]='.';
					 top--;
				 }
				 map1.put("top", String.valueOf(top));
				 display();
			 }
			 else {
				 map1.put("status", "error");
			 }
		 }
		 
		 JSONObject json = new JSONObject(map1);
		 response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");
		 response.getWriter().write(json.toString());
		 response.getWriter().close();
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);

		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str, operation, level = null, cleanSlate = null;
	    String[] s = null;
	    
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    }    
	    
	    JSONObject jObj = null;
	    
	    try {
			jObj = new JSONObject(sb.toString());
			cleanSlate = String.valueOf(jObj.get("clean"));
			if(cleanSlate.equals("true")) {
				clean();
				return;
			}
			
			operation = String.valueOf(jObj.get("operation"));
			if(operation.equals("redo") || operation.equals("undo"))
			{
				undoRedo(request,response,operation);
				return ;
			}
			else {
				str = String.valueOf(jObj.get("array"));
				level = String.valueOf(jObj.get("level"));
				s = str.substring(1,str.length()-1).split(",");
			}
		} catch (JSONException e) {}
		
		
		int stringIndex = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if(array[i][j]!=s[stringIndex].charAt(1))
				{
					op.add(top+1,new Operation(1,i,j));
					top++;
				}
				array[i][j] = s[stringIndex++].charAt(1);
			}
		}
		display();

		if (checkMate()) {
			map.put("status", "checkmate");
			map.put("player", "1");
			map.put("index1", String.valueOf(index[0]));
			map.put("index2", String.valueOf(index[1]));
			map.put("index3", String.valueOf(index[2]));
		} else if (isTie()) {
			map.put("status", "tie");
		} else {
			int[] cpu = level.equals("novice")?MiniMax.RandomAlgorithm():MiniMax.caller();
			op.add(top+1,new Operation(2,cpu[0],cpu[1]));
			top++;
			if (checkMate()) {
				map.put("status", "checkmate");
				map.put("player", "2");
				map.put("index1", String.valueOf(index[0]));
				map.put("index2", String.valueOf(index[1]));
				map.put("index3", String.valueOf(index[2]));
				map.put("cpu_position", String.valueOf(cpu[0] * 3 + cpu[1]));
			} else if (isTie()) {
				map.put("status", "tie");
				map.put("cpu_position", String.valueOf(cpu[0] * 3 + cpu[1]));
			} else {
				map.put("status", "ongoing");
				map.put("player", "2");
				map.put("cpu_position", String.valueOf(cpu[0] * 3 + cpu[1]));
			}
		}
		
		 display();

		JSONObject json = new JSONObject(map);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		response.getWriter().flush();
	}

	private void setAccessControlHeaders(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
		response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Accept, X-Requested-With, Content-Type");
	}

}
