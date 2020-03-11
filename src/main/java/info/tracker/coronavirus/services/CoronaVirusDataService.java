package info.tracker.coronavirus.services;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.models.CoronaDataModel;

import java.io.IOException;
import java.util.List;

public interface CoronaVirusDataService {

    public String fetchVirusData() throws APIRuntimeException, IOException;

    public void fileDownloader() throws IOException;

    public void parseVirusData() throws APIRuntimeException, IOException;

    public List<CoronaDataModel> getAllStats();

}
