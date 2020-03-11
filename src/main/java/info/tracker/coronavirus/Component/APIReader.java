package info.tracker.coronavirus.Component;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.services.CoronaVirusDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class APIReader {

    @Autowired
    CoronaVirusDataServiceImpl dataService;

    public void readFromWebAPI() throws APIRuntimeException, IOException {
        dataService.parseVirusData();
    }

    public void downloadCSV() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataService.fileDownloader();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
