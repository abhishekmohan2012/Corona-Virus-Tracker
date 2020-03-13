package info.tracker.coronavirus.Controllers;

import info.tracker.coronavirus.models.CoronaCountryModel;
import info.tracker.coronavirus.services.CoronaVirusDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    CoronaVirusDataImpl virusData;

    @GetMapping("/")
    public String home(Model model) {
        try {
            Map<String, CoronaCountryModel> dataMap = virusData.getCountryDataMap();
            List<CoronaCountryModel> countryStats = new ArrayList<>();
            for (Map.Entry<String, CoronaCountryModel> map : dataMap.entrySet()) {
                countryStats.add(map.getValue());
            }
            int totalReportedCases = dataMap.entrySet().stream().mapToInt(stat -> stat.getValue().getLatestCases()).sum();
            int totalNewCases = dataMap.entrySet().stream().mapToInt(stat -> stat.getValue().getDiffFromPrevDay()).sum();
            model.addAttribute("locationsStats", countryStats);
            model.addAttribute("totalReportedCases", totalReportedCases);
            model.addAttribute("totalNewCases", totalNewCases);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "home";
    }
}
