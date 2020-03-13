package info.tracker.coronavirus.Components;

import info.tracker.coronavirus.exceptions.APIRuntimeException;
import info.tracker.coronavirus.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class AppInitializerComponent {

    @Autowired
    CoronaVirusDataService dataService;

    @PostConstruct
    @Scheduled(cron = "0 */10 * ? * *")
    public void init() throws APIRuntimeException, IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataService.parseVirusData();
                } catch (APIRuntimeException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
