package info.tracker.coronavirus.Components;

import info.tracker.coronavirus.Components.CoronaVirusData;
import info.tracker.coronavirus.Constants;
import info.tracker.coronavirus.models.CoronaCountryModel;
import info.tracker.coronavirus.models.CoronaStateModel;
import info.tracker.coronavirus.services.CoronaVirusDataService;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CoronaVirusDataImpl implements CoronaVirusData, Constants {

    @Autowired
    CoronaVirusDataService dataService;

    private Map<String, CoronaCountryModel> countryDataMap = new TreeMap<>();

    public Map<String, CoronaCountryModel> getCountryDataMap() {
        return getSortedSet();
    }

    public void setData(String uri) {
        Map<String, CoronaCountryModel> newStats = new HashMap<>();
        for (Iterator<CSVRecord> it = dataService.parseCSVIterator(uri); it.hasNext(); ) {
            CSVRecord record = it.next();
            countryDataMap.put(record.get(COUNTRY), setCoronaCountyModel(record));
            newStats.putAll(countryDataMap);
        }
        countryDataMap.putAll(newStats);
    }

    private CoronaCountryModel setCoronaCountyModel(CSVRecord record) {
        CoronaCountryModel model;
        int latestCase = Integer.parseInt(record.get(record.size() - 1));
        int prevDayCase = Integer.parseInt(record.get(record.size() - 2));
        if (countryDataMap.containsKey(record.get(COUNTRY))) {
            model = countryDataMap.get(record.get(COUNTRY));
            boolean valueUpdated = (!record.get(record.size() - 1).isEmpty());
            if (!valueUpdated) {
                model.setUpdated(valueUpdated);
            }
            if (valueUpdated) {
                model.setLatestCases(model.getLatestCases() + latestCase);
                model.setDiffFromPrevDay(model.getDiffFromPrevDay() + latestCase - prevDayCase);
            }
        } else {
            model = new CoronaCountryModel();
            model.setCountry(record.get(COUNTRY));
            boolean valueUpdated = (!record.get(record.size() - 1).isEmpty());
            model.setUpdated(valueUpdated);
            if (valueUpdated) {
                model.setLatestCases(latestCase);
                model.setDiffFromPrevDay(latestCase - prevDayCase);
            }

        }
        if (record.get(STATE).isEmpty()) {
            model.setLatitude(Double.parseDouble(record.get(LATITUDE)));
            model.setLongitude(Double.parseDouble(record.get(LONGITUDE)));
        } else {
            model.getStateModelsList().add(setCoronaStateModel(record));
        }
        return model;
    }

    private CoronaStateModel setCoronaStateModel(CSVRecord record) {
        CoronaStateModel model = new CoronaStateModel();
        boolean valueUpdated = (!record.get(record.size() - 1).contentEquals(""));
        model.setUpdated(valueUpdated);
        if (valueUpdated) {
            int latestCase = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCase = Integer.parseInt(record.get(record.size() - 2));
            model.setLatestCases(latestCase);
            model.setDiffFromPrevDay(latestCase - prevDayCase);
        }
        model.setState(record.get(STATE));
        model.setLatitude(Double.parseDouble(record.get(LATITUDE)));
        model.setLongitude(Double.parseDouble(record.get(LONGITUDE)));
        return model;
    }

    private Map<String, CoronaCountryModel> getSortedSet() {
        List<Map.Entry<String, CoronaCountryModel>> list =
                new LinkedList<>(countryDataMap.entrySet());

        Collections.sort(list, Comparator.comparing(o -> o.getValue().getCountry()));

        HashMap<String, CoronaCountryModel> sortedMap = new LinkedHashMap<String, CoronaCountryModel>();
        for (Map.Entry<String, CoronaCountryModel> map : list) {
            sortedMap.put(map.getKey(), map.getValue());
        }
        return sortedMap;
    }
}
