package com.aim.project.ssp.instance.reader;

import java.nio.file.Path;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPInstanceReaderInterface;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.SSPInstance;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SSPInstanceReader implements SSPInstanceReaderInterface {

	@Override
	public SSPInstanceInterface readSSPInstance(Path path, Random random) {

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			// Read instance name
			String line = reader.readLine();
			String name = line.substring(line.indexOf(":") + 1).trim();

			// Read comment
			reader.readLine(); // Skip the comment line

			// Read hotel location marker
			reader.readLine(); // Skip the "HOTEL_LOCATION" marker

			// Read hotel coordinates
			line = reader.readLine();
			String[] hotelCoordinates = line.trim().split("\\s+");
			int hotelX = Integer.parseInt(hotelCoordinates[0]);
			int hotelY = Integer.parseInt(hotelCoordinates[1]);
			Location hotelLocation = new Location(hotelX, hotelY);

			// Read airport location marker
			reader.readLine(); // Skip the "AIRPORT_LOCATION" marker

			// Read airport coordinates
			line = reader.readLine();
			String[] airportCoordinates = line.trim().split("\\s+");
			int airportX = Integer.parseInt(airportCoordinates[0]);
			int airportY = Integer.parseInt(airportCoordinates[1]);
			Location airportLocation = new Location(airportX, airportY);

			// Read points of interest marker
			reader.readLine(); // Skip the "POINTS_OF_INTEREST" marker

			// Read all points of interest locations
			ArrayList<Location> locations = new ArrayList<>();

			while ((line = reader.readLine()) != null) {
				if (line.trim().equals("EOF")) {
					break; // Reached end of file
				}

				String[] coordinates = line.trim().split("\\s+");
				int x = Integer.parseInt(coordinates[0]);
				int y = Integer.parseInt(coordinates[1]);
				locations.add(new Location(x, y));
			}

			// Convert ArrayList to array and create the SSPInstance
			int numberOfLocations = locations.size();
			Location[] locationsArray = locations.toArray(new Location[numberOfLocations]);

			return new SSPInstance(numberOfLocations, locationsArray, hotelLocation, airportLocation, random);

		} catch (IOException e) {
			System.err.println("Error reading SSP instance file: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
