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

			// Read hotel location marker and coordinates
			reader.readLine(); // Skip the "HOTEL_LOCATION" marker
			Location hotelLocation = readLocationFromFile(reader);

			// Read airport location marker and coordinates
			reader.readLine(); // Skip the "AIRPORT_LOCATION" marker
			Location airportLocation = readLocationFromFile(reader);

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

	/**
	 * Reads a location (x, y coordinates) from the provided reader.
	 *
	 * @param reader The BufferedReader to read from
	 * @return A new Location object with the parsed coordinates
	 * @throws IOException If an I/O error occurs
	 */
	private Location readLocationFromFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		String[] coordinates = line.trim().split("\\s+");
		int x = Integer.parseInt(coordinates[0]);
		int y = Integer.parseInt(coordinates[1]);
		return new Location(x, y);
	}
}

