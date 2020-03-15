package info.tracker.coronavirus.Components;

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
    private Map<String, CoronaCountryModel> newStats;

    public Map<String, CoronaCountryModel> getCountryDataMap() {
        return getSortedSet();
    }

    public void setData(String uri) {
        newStats = new TreeMap<>();
        for (Iterator<CSVRecord> it = dataService.parseCSVIterator(uri); it.hasNext(); ) {
            CSVRecord record = it.next();
            newStats.put(record.get(COUNTRY), setCoronaCountyModel(record));
        }
        countryDataMap.putAll(newStats);
    }

    public void setDeathData(String uri) {
        for (Iterator<CSVRecord> it = dataService.parseCSVIterator(uri); it.hasNext(); ) {
            CSVRecord record = it.next();
            setCoronaCountryModelDeathData(record);
        }
    }

    private CoronaCountryModel setCoronaCountyModel(CSVRecord record) {
        CoronaCountryModel model;
        int latestCase = Integer.parseInt(record.get(record.size() - 1));
        int prevDayCase = Integer.parseInt(record.get(record.size() - 2));
        if (newStats.containsKey(record.get(COUNTRY))) {
            model = newStats.get(record.get(COUNTRY));
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

    private void setCoronaCountryModelDeathData(CSVRecord record) {
        CoronaCountryModel model = newStats.get(record.get(COUNTRY));
        int death = (!record.get(record.size() - 1).isEmpty()) ? Integer.parseInt(record.get(record.size() - 1)) : 0;
        int prevDeath = death - Integer.parseInt(record.get(record.size() - 2));
        if (model.getDeath() > 0) death += model.getDeath();
        if (model.getDeathDiffFromPrevDay() > 0) prevDeath += model.getDeathDiffFromPrevDay();
        model.setDeath(death);
        model.setDeathDiffFromPrevDay(prevDeath);
        if(!record.get(STATE).isEmpty()) setCoronaStateModelDeathData(record, model);
    }

    private void setCoronaStateModelDeathData(CSVRecord record, CoronaCountryModel model) {
        for (CoronaStateModel stateModel : model.getStateModelsList()) {
            if(record.get(STATE).contentEquals(stateModel.getState())) {
                int death = (!record.get(record.size() - 1).isEmpty()) ? Integer.parseInt(record.get(record.size() - 1)) : 0;
                stateModel.setDeath(death);
            }
        }
    }

    public void setCountryDataMap() {
        countryDataMap.putAll(newStats);
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
