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
    
    @Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAccessControlHeaders(resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		doGet(request, response);
	}
	
	private void setAccessControlHeaders(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
		response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Origin, Accept, X-Requested-With, Content-Type");
	}

}
