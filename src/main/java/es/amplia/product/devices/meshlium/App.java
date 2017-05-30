package es.amplia.product.devices.meshlium;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	private static Agent agent = null;
	
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOGGER.info("Shutdown agent...");
				try {
					if (agent != null) agent.stop();
				} catch (SQLException e) {
					
				}
			}
		});
		if (args.length != 1) {
			LOGGER.error("You must use only one arguments: configPath.");
			return;
		}
		try {
			agent = new Agent(args[0]);
			agent.start();
		} catch (FileNotFoundException  e) {
			LOGGER.error("Error accesing files on path " + args[0]);
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error("Error accesing files or bad configuration files on path " + args[0]);
			e.printStackTrace();
		} catch (SQLException e) {
			LOGGER.error("Error accesing data base configured on path " + args[0]);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			LOGGER.error("Error on data base configured on path " + args[0]);
			e.printStackTrace();
		} catch (URISyntaxException e) {
			LOGGER.error("Bad web socket URI configured on path " + args[0]);
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOGGER.error("Error starting HTTP server configured on path " + args[0]);
			e.printStackTrace();
		}
	}

}
