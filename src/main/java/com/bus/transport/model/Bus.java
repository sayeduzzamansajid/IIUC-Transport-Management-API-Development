package com.bus.transport.model;

public class Bus {
private int busNo;
private String busNumber;
private String busType;      
private String driverName;
private String driverPhone;
private String routePath;
private String departureTime;
private String dayOfWeek;
private String targetGroup;    
private String direction;      
private String category;      
private String startingPoint;

public Bus() {}  

public Bus(int busNo, String busNumber, String busType, String driverName, String driverPhone,   
           String routePath, String departureTime, String dayOfWeek, String targetGroup,   
           String direction, String category, String startingPoint) {  
    this.busNo = busNo;  
    this.busNumber = busNumber;  
    this.busType = busType;  
    this.driverName = driverName;  
    this.driverPhone = driverPhone;  
    this.routePath = routePath;  
    this.departureTime = departureTime;  
    this.dayOfWeek = dayOfWeek;  
    this.targetGroup = targetGroup;  
    this.direction = direction;  
    this.category = category;  
    this.startingPoint = startingPoint;  
}  

public int getBusNo() { return busNo; }  
public void setBusNo(int busNo) { this.busNo = busNo; }  
public String getBusNumber() { return busNumber; }  
public void setBusNumber(String busNumber) { this.busNumber = busNumber; }  
public String getBusType() { return busType; }  
public void setBusType(String busType) { this.busType = busType; }  
public String getDriverName() { return driverName; }  
public void setDriverName(String driverName) { this.driverName = driverName; }  
public String getDriverPhone() { return driverPhone; }  
public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }  
public String getRoutePath() { return routePath; }  
public void setRoutePath(String routePath) { this.routePath = routePath; }  
public String getDepartureTime() { return departureTime; }  
public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }  
public String getDayOfWeek() { return dayOfWeek; }  
public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }  
public String getTargetGroup() { return targetGroup; }  
public void setTargetGroup(String targetGroup) { this.targetGroup = targetGroup; }  
public String getDirection() { return direction; }  
public void setDirection(String direction) { this.direction = direction; }  
public String getCategory() { return category; }  
public void setCategory(String category) { this.category = category; }  
public String getStartingPoint() { return startingPoint; }  
public void setStartingPoint(String startingPoint) { this.startingPoint = startingPoint; }

}