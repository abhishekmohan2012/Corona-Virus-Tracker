package info.tracker.coronavirus.services;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Iterator;

public interface CoronaVirusDataService {

    String fetchVirusData(String uri) throws APIRuntimeException, IOException;

    Iterator<CSVRecord> parseCSVIterator(String uri);

}
