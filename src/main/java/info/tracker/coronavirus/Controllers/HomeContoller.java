package info.tracker.coronavirus.Controllers;

import info.tracker.coronavirus.models.CoronaDataModel;
import info.tracker.coronavirus.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeContoller {
    @Autowired
    CoronaVirusDataService dataService;

    @GetMapping("/")
    public String home(Model model) {
        List<CoronaDataModel> allStats = dataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationsStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}
