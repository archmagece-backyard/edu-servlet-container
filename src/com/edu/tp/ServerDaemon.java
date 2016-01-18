package com.edu.tp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDaemon {

	public static void main(String[] args) throws IOException {

		ExecutorService se = Executors.newCachedThreadPool();

		ServerSocket ss = new ServerSocket(8080);
		while (true) {
			se.submit(new Process(ss.accept()));
		}

	}

}
