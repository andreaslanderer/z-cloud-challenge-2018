package com.zuehlke.cloudchallenge.messagegransformer.rest;

import com.zuehlke.cloudchallenge.messagegransformer.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counters")
public class CounterController {

    private final CounterService service;

    @Autowired
    public CounterController(CounterService service) {
        this.service = service;
    }

    @GetMapping("/{airport}")
    public ResponseEntity<CounterResponse> getCounterForAirport(@PathVariable("airport") String airport) {
        final Integer count = this.service.getCounterForAirport(airport);
        if(count == null) {
            return ResponseEntity.noContent().build();
        } else {
            CounterResponse response = new CounterResponse();
            response.airport = airport;
            response.count = count;
            return ResponseEntity.ok(response);
        }
    }
}
