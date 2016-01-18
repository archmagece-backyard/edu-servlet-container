package com.edu.tp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;

public class Process implements Runnable {

	private Socket socket;

	public Process(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String rawData = br.readLine();

			String raw = null;
			Cookie[] cookies = null;
			while ((raw = br.readLine()).length() != 0) {
				// Cookie:LOCALE_TAG=en; localeTag=ko;
				// JSESSID=mj7qr0npfit9mub69anbhmfok0

				if (raw.startsWith("Cookie:")) {
					for (String token : raw.split(" ")) {
						if (token.startsWith("JSESSID=")) {
							Cookie cookie = new Cookie("JSESSID", token.substring("JSESSID=".length()));
							cookies = new Cookie[] { cookie };
							break;
						}
					}
				} else {
					cookies = new Cookie[] {};
				}

			}

			System.out.println(rawData);
			String uri = rawData.split(" ")[1];
			String method = rawData.split(" ")[0];
			System.out.println(uri);

			File file = new File("document" + uri);

			if (!file.exists()) {

				HttpServlet servlet = Storage.fetch(uri.substring(1));
				if (servlet == null) {

					socket.getOutputStream().write("HTTP/1.1 404 Not Found\r\n".getBytes());
					socket.getOutputStream().write("Date: Mon, 18 Jan 2016 05:56:31 GMT\r\n\r\n".getBytes());
					socket.getOutputStream().flush();
					socket.getOutputStream().write("404....\r\n".getBytes());
					return;

				}

				THttpServletRequest request = new THttpServletRequest(uri, method, cookies);
				THttpServletResponse response = new THttpServletResponse(socket.getOutputStream(), cookies);

				servlet.service(request, response);
				response.flushBuffer();

			} else {
				socket.getOutputStream().write("HTTP/1.1 200 OK\r\n".getBytes());
				socket.getOutputStream().write("Date: Mon, 18 Jan 2016 05:52:36 GMT\r\n\r\n".getBytes());

				FileInputStream fis = new FileInputStream(file);

				byte[] data = new byte[1024];
				int readCount = -1;

				while ((readCount = fis.read(data, 0, data.length)) != -1) {
					socket.getOutputStream().write(data, 0, readCount);
				}

			}

			socket.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

	}

}
