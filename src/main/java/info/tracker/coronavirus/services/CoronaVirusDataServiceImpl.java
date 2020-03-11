package info.tracker.coronavirus.services;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.models.CoronaDataModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

    public String fetchVirusData() throws APIRuntimeException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
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
        String apiOutput = null;
        apiOutput = EntityUtils.toString(httpEntity);
        return apiOutput;
    }

    public void fileDownloader() throws IOException {
        /*URL website = new URL(VIRUS_DATA_URL);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("information.csv");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);*/

        File file = new File(FILE_NAME + ".csv");
        FileUtils.copyURLToFile(new URL(VIRUS_DATA_URL), file);
    }

    public void parseVirusData() throws APIRuntimeException, IOException {
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
            dataModel.setDiffFromPrevDay(latestCase-prevDayCase);
            newStats.add(dataModel);
        }
        this.allStats = newStats;
    }

    public List<CoronaDataModel> getAllStats() {
        return allStats;
    }
}
