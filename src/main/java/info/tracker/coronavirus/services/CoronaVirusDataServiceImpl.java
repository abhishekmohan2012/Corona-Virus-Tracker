package info.tracker.coronavirus.services;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.models.CoronaDataModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataServiceImpl implements CoronaVirusDataService, Constants {

    private List<CoronaDataModel> allStats = new ArrayList<>();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiOutput;
    }

    public void fileDownloader() throws IOException {
        File file = new File(FILE_NAME + ".csv");
        FileUtils.copyURLToFile(new URL(VIRUS_DATA_URL), file);
    }

    public void parseVirusData() throws IOException {
        List<CoronaDataModel> newStats = new ArrayList<>();
        StringReader csvReader = new StringReader(fetchVirusData());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {
            CoronaDataModel dataModel = new CoronaDataModel();
            dataModel.setState(record.get(STATE));
            dataModel.setCountry(record.get(COUNTRY));
            int latestCase = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCase = Integer.parseInt(record.get(record.size() - 2));
            dataModel.setLatestCases(latestCase);
            dataModel.setDiffFromPrevDay(latestCase - prevDayCase);
            newStats.add(dataModel);
        }
        this.allStats = newStats;
    }

    public List<CoronaDataModel> getAllStats() {
        return allStats;
    }
}
