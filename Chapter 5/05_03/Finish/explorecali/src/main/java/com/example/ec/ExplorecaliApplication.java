package com.example.ec;

import com.example.ec.service.TourPackageService;
import com.example.ec.service.TourService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Main Class for the Spring Boot Application
 *
 * Created by Mary Ellen Bowman
 */
@SpringBootApplication
public class ExplorecaliApplication implements CommandLineRunner {

	@Autowired
	private TourPackageService tourPackageService;
	@Autowired
	private TourService tourService;

	public static void main(String[] args) {
		SpringApplication.run(ExplorecaliApplication.class, args);
	}

	/**
	 * Method invoked after this class has been instantiated by Spring container
	 * Initializes the in-memory database with all the TourPackages and Tours.
	 *
	 * @param strings
	 * @throws Exception if problem occurs.
     */
	@Override
	public void run(String... strings) throws Exception {
		//Create the default tour packages
		tourPackageService.createTourPackage("BC", "Backpack Cal");
		tourPackageService.createTourPackage("CC", "California Calm");
		tourPackageService.createTourPackage("CH", "California Hot springs");
		tourPackageService.createTourPackage("CY", "Cycle California");
		tourPackageService.createTourPackage("DS", "From Desert to Sea");
		tourPackageService.createTourPackage("KC", "Kids California");
		tourPackageService.createTourPackage("NW", "Nature Watch");
		tourPackageService.createTourPackage("SC", "Snowboard Cali");
		tourPackageService.createTourPackage("TC", "Taste of California");
		System.out.println("Number of tours packages =" + tourPackageService.total());

		//Persist the Tours to the database
		TourFromFile.read().forEach(tourFromFile ->
				tourService.createTour(tourFromFile.getTitle(),
						tourFromFile.getPackageName(), tourFromFile.getDetails())
		);
		System.out.println("Number of tours =" + tourService.total());


	}

	/**
	 * Helper class to import the records in the ExploreCalifornia.json
	 */
	static class TourFromFile {
		//attributes as listed in the .json file
		String title;
		String packageName;
		Map<String, String> details;

		TourFromFile(Map<String, String> record) {
			this.title = record.get("title");
			this.packageName = record.get("packageType");
			this.details = record;
			this.details.remove("packageType");
			this.details.remove("title");
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public Map<String, String> getDetails() {
			return details;
		}

		public void setDetails(Map<String, String> details) {
			this.details = details;
		}

		/**
		 * Open the ExploreCalifornia.json, unmarshal every entry into a TourFromFile Object.
		 *
		 * @return a List of TourFromFile objects.
		 * @throws IOException if ObjectMapper unable to open file.
         */
		static List<TourFromFile> read() throws IOException {
			List<Map<String, String>> records = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
					.readValue(new FileInputStream("/ExploreCalifornia.json"),
							new TypeReference<List<Map<String, String>>>() {});
			return records.stream().map(TourFromFile::new)
					.collect(Collectors.toList());
		}
	}

}
