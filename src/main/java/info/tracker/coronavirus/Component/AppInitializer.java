package info.tracker.coronavirus.Component;

import info.tracker.coronavirus.Component.APIReader;
import info.tracker.coronavirus.exceptions.APIRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class AppInitializer {
    @Autowired
    APIReader apiReader;

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void appInitializer() throws APIRuntimeException, IOException {
        apiReader.readFromWebAPI();
        apiReader.downloadCSV();
    }
}
