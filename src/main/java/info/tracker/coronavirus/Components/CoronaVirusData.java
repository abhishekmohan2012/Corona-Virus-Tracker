package info.tracker.coronavirus.Components;

import info.tracker.coronavirus.models.CoronaCountryModel;

import java.util.Map;

public interface CoronaVirusData {

    void setData(String uri);

    Map<String, CoronaCountryModel> getCountryDataMap();

}
