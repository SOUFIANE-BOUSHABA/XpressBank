package org.example.xpresbank.Controller;

import org.example.xpresbank.Service.StatisticsService;
import org.example.xpresbank.VM.StatisticsVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticsController {

    private final StatisticsService statisticsService;


    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<StatisticsVM> getStatistics() {
        StatisticsVM statistics = statisticsService.getStatistics();
        return ResponseEntity.ok(statistics);
    }
}
