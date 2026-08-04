package com.bus.transport.controller;

import com.bus.transport.model.Bus;
import com.bus.transport.service.BusService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transport")
public class BusController {

private final BusService busService;  

public BusController(BusService busService) {  
    this.busService = busService;  
}  

@PutMapping("/admin/varsity-status")  
public String toggleVarsityStatus(@RequestParam boolean closed) {  
    return busService.setVarsityClosure(closed);  
}  

@PutMapping("/admin/toggle-schedule")  
public String toggleSchedule(@RequestParam String type, @RequestParam boolean status) {  
    return busService.updateScheduleControl(type, status);  
}  

@PostMapping("/add")  
public List<Bus> createBuses(@RequestBody List<Bus> buses) {  
    return busService.saveBuses(buses);  
}  

@GetMapping("/all")  
public List<Bus> getAllBuses() {  
    return busService.getAllBuses();  
}  

@GetMapping("/find")  
public Object findBusByNo(@RequestParam int busNo) {  
    return busService.getBusByNumber(busNo);  
}  

@GetMapping("/search")  
public Object searchBuses(  
        @RequestParam String direction,  
        @RequestParam String targetGroup,  
        @RequestParam(required = false, defaultValue = "Regular") String category,   
        @RequestParam String spot,  
        @RequestParam String day,  
        @RequestParam(required = false, defaultValue = "0") int busNo) {   
      
    return busService.filterBuses(direction, targetGroup, category, spot, day, busNo);  
}  

@PutMapping("/update")  
public Object updateBus(  
        @RequestParam String busNumber,  
        @RequestParam String departureTime,  
        @RequestParam String category,   
        @RequestBody Bus updatedBusInfo) {  
    return busService.updateBus(busNumber, departureTime, category, updatedBusInfo);  
}  

@DeleteMapping("/delete")  
public String deleteBus(  
        @RequestParam String busNumber,  
        @RequestParam String departureTime,  
        @RequestParam String category) {   
    return busService.deleteBus(busNumber, departureTime, category);  
}

}