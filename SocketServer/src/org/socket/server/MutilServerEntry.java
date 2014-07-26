package org.socket.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutilServerEntry {

	private static final Logger LOGGER = LoggerFactory.getLogger(MutilServerEntry.class);
	
	public static void main(String[] args) {
		
		try {
			new MutilServer().service();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		
	}

}
