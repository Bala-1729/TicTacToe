package com.tictactoe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.commons.json.JSONObject;

@WebServlet("/ResponseServlet")
public class ResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAccessControlHeaders(resp);
	}

	static char[][] array = { { '.', '.', '.' }, { '.', '.', '.' }, { '.', '.', '.' } };
	static int[] index = { 0, 0, 0 };

	public boolean checkMate() {

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

	public boolean isTie() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (array[i][j] == '.')
					return false;
			}
		}
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setAccessControlHeaders(response);

		String[] s = request.getReader().readLine().split(",");
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				array[i][j] = s[index++].charAt(0);
			}
		}

		Map<String, String> map = new HashMap<>();
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				System.out.print(array[i][j]);
			System.out.println();
		}

		if (checkMate()) {
			map.put("status", "checkmate");
			map.put("player", "1");
			map.put("index1", String.valueOf(ResponseServlet.index[0]));
			map.put("index2", String.valueOf(ResponseServlet.index[1]));
			map.put("index3", String.valueOf(ResponseServlet.index[2]));
		} else if (isTie()) {
			map.put("status", "tie");
		} else {
			int[] cpu = Algorithms.RandomAlgorithm();

			if (checkMate()) {
				map.put("status", "checkmate");
				map.put("player", "2");
				map.put("index1", String.valueOf(ResponseServlet.index[0]));
				map.put("index2", String.valueOf(ResponseServlet.index[1]));
				map.put("index3", String.valueOf(ResponseServlet.index[2]));
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

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				System.out.print(array[i][j]);
			System.out.println();
		}

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
