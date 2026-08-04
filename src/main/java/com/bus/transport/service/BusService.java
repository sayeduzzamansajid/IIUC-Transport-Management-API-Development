package com.bus.transport.service;

import com.bus.transport.model.Bus;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusService {
private final List<Bus> busList = new ArrayList<>();


private boolean isVarsityOfficiallyClosed = false;   
private boolean isMidTermActive = false;  
private boolean isFinalExamActive = false;  
private boolean isAdmissionActive = false;  

public String setVarsityClosure(boolean closed) {  
    this.isVarsityOfficiallyClosed = closed;  
    return closed ? "Varsity status updated to: CLOSED (Vacation Mode Active)"   
                  : "Varsity status updated to: OPEN (Regular Operations Active)";  
}  

public String updateScheduleControl(String type, boolean status) {  
    if (type.equalsIgnoreCase("Mid")) {  
        this.isMidTermActive = status;  
    } else if (type.equalsIgnoreCase("Final")) {  
        this.isFinalExamActive = status;  
    } else if (type.equalsIgnoreCase("Admission")) {  
        this.isAdmissionActive = status;  
    } else {  
        return "Invalid schedule type! Use 'Mid', 'Final', or 'Admission'.";  
    }  
    return type + " exam/admission schedule active status set to: " + status;  
}  

public List<Bus> saveBuses(List<Bus> buses) {  
    busList.addAll(buses);  
    return buses;  
}  

public List<Bus> getAllBuses() {  
    return busList;  
}  

public Object getBusByNumber(int busNo) {  
    for (Bus b : busList) {  
        if (b.getBusNo() == busNo) {  
            return b;  
        }  
    }  
    return "Bus not found with Bus No: " + busNo;  
}  

public Object filterBuses(String direction, String targetGroup, String category, String spot, String day, int busNo) {  
      
    if (category.equalsIgnoreCase("Mid_Term") && !isMidTermActive) {  
        return "Mid-term exam buses are currently inactive. Exams are either over or haven't started yet.";  
    }  
    if (category.equalsIgnoreCase("Final_Exam") && !isFinalExamActive) {  
        return "Final exam buses are currently inactive. Exams are either over or haven't started yet.";  
    }  
    if (category.equalsIgnoreCase("Admission_Test") && !isAdmissionActive) {  
        return "Admission test bus schedules are currently suspended or inactive.";  
    }  

    if (isMidTermActive || isFinalExamActive || isAdmissionActive) {  
        if (category.equalsIgnoreCase("Regular")) {  
            
            if (targetGroup.equalsIgnoreCase("MaleStudent") || targetGroup.equalsIgnoreCase("FemaleStudent")) {  
                return "Regular student bus schedules are suspended during this exam/admission season. Please search using 'Mid_Term', 'Final_Exam', or 'Admission_Test'.";  
            }  
             
            if (targetGroup.equalsIgnoreCase("MaleTeacher") || targetGroup.equalsIgnoreCase("FemaleTeacher")) {  
                return "Regular teacher buses are suspended due to official exam/admission duty. Please use 'Special' or 'MBA' category if applicable.";  
            }  
            if (targetGroup.equalsIgnoreCase("MaleStaff") || targetGroup.equalsIgnoreCase("FemaleStaff")) {  
                return "Regular staff bus schedules are suspended due to exam/admission duty. Please contact the transport office or use designated exam transport.";  
            }  
            return "All regular bus services are currently suspended due to ongoing exam/admission season.";  
        }  
    }  

    if (isVarsityOfficiallyClosed) {  
        if (!category.equalsIgnoreCase("Ambulance") && !targetGroup.equalsIgnoreCase("Ambulance")) {  
            return "The university is currently closed for vacation. No buses are operating for this route.";  
        }  
    }  

    if (day.equalsIgnoreCase("Thursday")) {  
        if (category.equalsIgnoreCase("Regular")) {  
            return "There is no bus for this route.";  
        }  
    }  

    if (day.equalsIgnoreCase("Friday")) {  
        if (!category.equalsIgnoreCase("Special") &&   
            !category.equalsIgnoreCase("MBA") &&   
            !category.equalsIgnoreCase("Ambulance")) {  
            return "There is no bus for this route.";  
        }  
    }  

    if (category.equalsIgnoreCase("Ambulance") || targetGroup.equalsIgnoreCase("Ambulance")) {  
        List<Bus> ambulances = busList.stream()  
            .filter(b -> b.getCategory().equalsIgnoreCase("Ambulance"))  
            .collect(Collectors.toList());  
        return ambulances.isEmpty() ? "No Ambulance available at this moment." : ambulances;  
    }  

    List<Bus> filteredList = busList.stream()  
        .filter(b -> busNo == 0 || b.getBusNo() == busNo)   
        .filter(b -> b.getTargetGroup().equalsIgnoreCase(targetGroup))  
        .filter(b -> b.getCategory().equalsIgnoreCase(category))  
        .filter(b -> b.getDayOfWeek().equalsIgnoreCase(day))   
        .filter(b -> b.getDirection().equalsIgnoreCase(direction))  
        .filter(b -> b.getRoutePath().toLowerCase().contains(spot.toLowerCase()))  
        .collect(Collectors.toList());  

    if (filteredList.isEmpty()) {  
        return "There is no bus for this route.";  
    }  

    return filteredList;  
}  

  
public Object updateBus(String busNumber, String departureTime, String category, Bus updatedBusInfo) {  
    for (Bus b : busList) {  
        if (b.getBusNumber().equalsIgnoreCase(busNumber) &&   
            b.getDepartureTime().equalsIgnoreCase(departureTime) &&  
            b.getCategory().equalsIgnoreCase(category)) {  
              
            if (updatedBusInfo.getDepartureTime() != null) b.setDepartureTime(updatedBusInfo.getDepartureTime());  
            if (updatedBusInfo.getRoutePath() != null) b.setRoutePath(updatedBusInfo.getRoutePath());  
            if (updatedBusInfo.getDriverName() != null) b.setDriverName(updatedBusInfo.getDriverName());  
            if (updatedBusInfo.getDriverPhone() != null) b.setDriverPhone(updatedBusInfo.getDriverPhone());  
            if (updatedBusInfo.getStartingPoint() != null) b.setStartingPoint(updatedBusInfo.getStartingPoint());  
            if (updatedBusInfo.getDayOfWeek() != null) b.setDayOfWeek(updatedBusInfo.getDayOfWeek());  
            if (updatedBusInfo.getBusNo() != 0) b.setBusNo(updatedBusInfo.getBusNo());  
            if (updatedBusInfo.getCategory() != null) b.setCategory(updatedBusInfo.getCategory());  
            if (updatedBusInfo.getTargetGroup() != null) b.setTargetGroup(updatedBusInfo.getTargetGroup());  
              
            return b;   
        }  
    }  
    return "Bus not found to update.";  
}  

public String deleteBus(String busNumber, String departureTime, String category) {  
    boolean removed = busList.removeIf(b ->   
        b.getBusNumber().equalsIgnoreCase(busNumber) &&   
        b.getDepartureTime().equalsIgnoreCase(departureTime) &&  
        b.getCategory().equalsIgnoreCase(category)  
    );  
    if (removed) {  
        return "Bus deleted successfully.";  
    }  
    return "No bus found to delete.";  
}

}