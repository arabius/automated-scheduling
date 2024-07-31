package org.arabius.platform.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.arabius.platform.domain.Guide;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvGuideLoader {

	public static List<Guide> loadGuides(String filePath) throws IOException {
		List<Guide> guideList = new ArrayList<>();

		try (FileReader reader = new FileReader(filePath);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

			for (CSVRecord csvRecord : csvParser) {
				Guide guide = new Guide();
				guide.setId(Integer.parseInt(csvRecord.get("id")));
				guide.setName(csvRecord.get("guide_name"));
				guide.setLevels(csvRecord.get("levels"));
				guideList.add(guide);
			}
		}

		return guideList;
	}
}