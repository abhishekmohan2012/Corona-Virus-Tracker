package info.tracker.coronavirus.services;

import info.tracker.coronavirus.Constants;
import info.tracker.coronavirus.exceptions.APIRuntimeException;
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
import java.util.Iterator;

@Service
public class CoronaVirusDataServiceImpl implements CoronaVirusDataService, Constants {
    @Override
    public String fetchVirusData(String uri) {
        String apiOutput = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet getRequest = new HttpGet(uri);
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

    @Override
    public Iterator<CSVRecord> parseCSVIterator(String uri) {
        Iterable<CSVRecord> records = null;
        try {
            StringReader csvReader = new StringReader(fetchVirusData(uri));
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records.iterator();
    }
}
