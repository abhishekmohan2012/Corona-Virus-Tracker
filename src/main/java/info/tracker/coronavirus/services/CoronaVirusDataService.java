package info.tracker.coronavirus.services;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.models.CoronaCountryModel;

import java.io.IOException;
import java.util.Map;

public interface CoronaVirusDataService {

    public String fetchVirusData() throws APIRuntimeException, IOException;

    public void parseVirusData() throws APIRuntimeException, IOException;

    public Map<String, CoronaCountryModel> getCountryDataMap();

}
