package info.tracker.coronavirus.services;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.models.CoronaCountryModel;
import info.tracker.coronavirus.models.CoronaStateModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
public class CoronaVirusDataServiceImpl implements CoronaVirusDataService, Constants {

    private Map<String, CoronaCountryModel> countryDataMap = new TreeMap<>();

    public String fetchVirusData() {
        String apiOutput = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet getRequest = new HttpGet(VIRUS_DATA_URL);
            HttpResponse response = null;
            response = httpClient.execute(getRequest);

            int statusCode = NOT_FOUND;
            if (response != null && response.getStatusLine() != null)
                statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != SUCCESS) {
                throw new APIRuntimeException("Failed with HTTP error code : " + statusCode);
            }
            HttpEntity httpEntity = response.getEntity();
            apiOutput = EntityUtils.toString(httpEntity);
        } catch (IOException | APIRuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiOutput;
    }

    public void parseVirusData() {
        Map<String, CoronaCountryModel> newStats = new HashMap<>();
        try {
            StringReader csvReader = new StringReader(fetchVirusData());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
            for (CSVRecord record : records) {
                countryDataMap.put(record.get(COUNTRY), setCoronaCountyModel(record));
                newStats.putAll(countryDataMap);
            }
            countryDataMap.putAll(newStats);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, CoronaCountryModel> getCountryDataMap() {
        return getSortedSet();
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

        Collections.sort(list, new Comparator<Map.Entry<String, CoronaCountryModel>>() {
            @Override
            public int compare(Map.Entry<String, CoronaCountryModel> o1, Map.Entry<String, CoronaCountryModel> o2) {
                return o1.getValue().getCountry().compareTo(o2.getValue().getCountry());
            }
        });

        HashMap<String, CoronaCountryModel> sortedMap = new LinkedHashMap<String, CoronaCountryModel>();
        for (Map.Entry<String, CoronaCountryModel> map : list) {
            sortedMap.put(map.getKey(), map.getValue());
        }
        return sortedMap;
    }
}
