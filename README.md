# 🚌 University Transport Management System API

A high-performance RESTful Web API built with **Spring Boot** and **Java 17** for managing university bus transportation services. The system provides dynamic operational schedule management (handling regular classes, exam weeks, admission tests, and university vacations), smart route search filtering, emergency transport tracking, and complete CRUD capabilities for bus schedules.

---

## 📋 Table of Contents
- [Overview](#-overview)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Data Model](#-data-model)
- [API Endpoints Documentation](#-api-endpoints-documentation)
  - [1. Administrative & Schedule Controls](#1-administrative--schedule-controls)
  - [2. Bus Data Management & Search](#2-bus-data-management--search)
- [Business Logic & Filtering Rules](#-business-logic--filtering-rules)
- [How to Test & Run the Project](#-how-to-test--run-the-project)
  - [Prerequisites](#prerequisites)
  - [Building and Running](#building-and-running)
  - [Testing Endpoints (cURL Examples)](#testing-endpoints-curl-examples)
  - [Testing via Postman / Insomnia](#testing-via-postman--insomnia)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🌐 Overview

Managing university transport logistics can be complex due to varying academic schedules—such as exam periods, admission test days, weekend services, and official university vacations. 

The **Transport Management System** addresses these complexities by providing a flexible backend system that automatically adjusts bus availability based on active institutional states. It allows students, faculty, and administrative staff to search for available buses while empowering transport administrators to manage bus records and schedule modes dynamically.

---

## ✨ Key Features

- 🎓 **Dynamic Institutional States**: 
  - **Varsity Status**: Toggle between `OPEN` and `CLOSED` (Vacation Mode).
  - **Academic Exam Modes**: Activate/deactivate schedule rules for `Mid`, `Final`, and `Admission` tests.
- 🔍 **Smart Bus Search & Filtering**:
  - Filter by direction (`To Campus`, `From Campus`), passenger target group, category, stoppage spot, day of week, and bus number.
- ⚡ **Rule-Based Transport Dispatching**:
  - Automatically suspends regular student/staff/teacher buses when exam or admission modes are active.
  - Enforces Friday & Thursday transport operational rules.
  - Protects emergency **Ambulance** services so they remain active 24/7, even during vacation closures.
- 🛠️ **Full Bus Management (CRUD)**:
  - Batch insertion of bus schedules.
  - View all registered buses or find specific buses by bus number.
  - Update bus parameters matching composite attributes.
  - Delete bus entries when routes or schedules change.

---

## 🛠️ Tech Stack

- **Framework**: Spring Boot 4.0.6 (Spring Web MVC)
- **Language**: Java 17
- **Build Tool**: Apache Maven (with Maven Wrapper `mvnw`)
- **Data Storage**: In-Memory Thread-Safe Collection (`ArrayList` with stream filtering)
- **Architecture**: Controller-Service-Model Layered Architecture

---

## 📁 Project Structure

```text
Transport-Management-System/
├── .mvn/                              # Maven Wrapper files
├── mvnw / mvnw.cmd                     # Maven Wrapper executables
├── pom.xml                             # Project dependencies and plugins
├── README.md                           # Project documentation
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/bus/transport/
    │   │       ├── TransportApplication.java    # Main Spring Boot application
    │   │       ├── controller/
    │   │       │   └── BusController.java       # REST API Endpoints
    │   │       ├── model/
    │   │       │   └── Bus.java                 # Bus entity data model
    │   │       └── service/
    │   │           └── BusService.java          # Core business logic & state control
    │   └── resources/
    │       └── application.properties           # Spring Boot application configuration
    └── test/
        └── java/
            └── com/bus/transport/
                └── TransportApplicationTests.java
```

---

## 📊 Data Model (`Bus`)

Every bus entity managed in the system contains the following attributes:

| Field Name | Type | Description | Example |
| :--- | :--- | :--- | :--- |
| `busNo` | `int` | Unique internal numerical identifier | `101` |
| `busNumber` | `String` | Vehicle registration code / designation | `"BUS-01"` |
| `busType` | `String` | Type of vehicle | `"Double Decker"`, `"Ac Bus"` |
| `driverName` | `String` | Assigned driver's name | `"Rahim Uddin"` |
| `driverPhone` | `String` | Driver contact phone number | `"01711000000"` |
| `routePath` | `String` | Stoppages / Path string (comma or arrow separated) | `"Mirpur 10 -> Agargaon -> Campus"` |
| `departureTime` | `String` | Scheduled departure time | `"07:30 AM"` |
| `dayOfWeek` | `String` | Operational day of the week | `"Sunday"`, `"Monday"`, `"Friday"` |
| `targetGroup` | `String` | Target passenger classification | `"MaleStudent"`, `"FemaleStudent"`, `"MaleTeacher"`, `"FemaleTeacher"`, `"MaleStaff"`, `"FemaleStaff"`, `"Ambulance"` |
| `direction` | `String` | Transit direction | `"To Campus"`, `"From Campus"` |
| `category` | `String` | Service operating category | `"Regular"`, `"Mid_Term"`, `"Final_Exam"`, `"Admission_Test"`, `"Special"`, `"MBA"`, `"Ambulance"` |
| `startingPoint` | `String` | Stoppage / Terminal origin point | `"Mirpur 10"` |

---

## 📡 API Endpoints Documentation

Base URL: `http://localhost:8080/api/transport`

---

### 1. Administrative & Schedule Controls

#### 🔴 Toggle Varsity Closure Status
Updates the overall status of the university to open or closed (vacation mode).

- **URL**: `/admin/varsity-status`
- **Method**: `PUT`
- **Query Parameters**:
  - `closed` (`boolean`, required): `true` to activate vacation mode (closes varsity), `false` to open.
- **Sample Request**:
  ```http
  PUT /api/transport/admin/varsity-status?closed=true
  ```
- **Response**: `200 OK`
  ```text
  "Varsity status updated to: CLOSED (Vacation Mode Active)"
  ```

---

#### 🟡 Toggle Academic Schedule Mode
Activates or deactivates special schedule rules for Mid-Term Exams, Final Exams, or Admission Tests.

- **URL**: `/admin/toggle-schedule`
- **Method**: `PUT`
- **Query Parameters**:
  - `type` (`String`, required): Mode type — Acceptable values: `Mid`, `Final`, `Admission`.
  - `status` (`boolean`, required): `true` to activate schedule mode, `false` to deactivate.
- **Sample Request**:
  ```http
  PUT /api/transport/admin/toggle-schedule?type=Mid&status=true
  ```
- **Response**: `200 OK`
  ```text
  "Mid exam/admission schedule active status set to: true"
  ```

---

### 2. Bus Data Management & Search

#### 🟢 Add New Buses (Batch Insert)
Adds a list of bus entries into the transport schedule.

- **URL**: `/add`
- **Method**: `POST`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
  ```json
  [
    {
      "busNo": 101,
      "busNumber": "BUS-01",
      "busType": "Double Decker",
      "driverName": "Rahim Uddin",
      "driverPhone": "01711000000",
      "routePath": "Mirpur 10 -> Agargaon -> Farmgate -> Campus",
      "departureTime": "07:30 AM",
      "dayOfWeek": "Sunday",
      "targetGroup": "MaleStudent",
      "direction": "To Campus",
      "category": "Regular",
      "startingPoint": "Mirpur 10"
    },
    {
      "busNo": 102,
      "busNumber": "BUS-02",
      "busType": "Ac Bus",
      "driverName": "Karim Khan",
      "driverPhone": "01811000000",
      "routePath": "Uttara -> Airport -> Mohakhali -> Campus",
      "departureTime": "08:00 AM",
      "dayOfWeek": "Sunday",
      "targetGroup": "FemaleStudent",
      "direction": "To Campus",
      "category": "Regular",
      "startingPoint": "Uttara"
    }
  ]
  ```
- **Response**: `200 OK` (Returns the saved array of buses)

---

#### 🔵 Get All Buses
Retrieves all registered bus entries currently in the system.

- **URL**: `/all`
- **Method**: `GET`
- **Response**: `200 OK`
  ```json
  [
    {
      "busNo": 101,
      "busNumber": "BUS-01",
      "busType": "Double Decker",
      "driverName": "Rahim Uddin",
      "driverPhone": "01711000000",
      "routePath": "Mirpur 10 -> Agargaon -> Farmgate -> Campus",
      "departureTime": "07:30 AM",
      "dayOfWeek": "Sunday",
      "targetGroup": "MaleStudent",
      "direction": "To Campus",
      "category": "Regular",
      "startingPoint": "Mirpur 10"
    }
  ]
  ```

---

#### 🔍 Find Bus by Bus Number ID
Locates a specific bus using its numerical identifier `busNo`.

- **URL**: `/find`
- **Method**: `GET`
- **Query Parameters**:
  - `busNo` (`int`, required): Unique bus identification number.
- **Sample Request**:
  ```http
  GET /api/transport/find?busNo=101
  ```
- **Response**: `200 OK` (Bus JSON object or string message if not found)

---

#### 🔎 Search Buses Dynamic Route Query
Performs dynamic search for buses while enforcing institutional rules and schedule states.

- **URL**: `/search`
- **Method**: `GET`
- **Query Parameters**:
  - `direction` (`String`, required): e.g. `"To Campus"`, `"From Campus"`
  - `targetGroup` (`String`, required): e.g. `"MaleStudent"`, `"FemaleStudent"`, `"MaleTeacher"`, `"FemaleTeacher"`, `"MaleStaff"`, `"FemaleStaff"`, `"Ambulance"`
  - `category` (`String`, optional, default: `"Regular"`): Service category (`Regular`, `Mid_Term`, `Final_Exam`, `Admission_Test`, `Special`, `MBA`, `Ambulance`)
  - `spot` (`String`, required): Specific location/stoppage along the `routePath` (case-insensitive substring match)
  - `day` (`String`, required): Operational day (e.g. `"Sunday"`, `"Monday"`, `"Thursday"`, `"Friday"`)
  - `busNo` (`int`, optional, default: `0`): Specific bus number (use `0` to search across all buses)
- **Sample Request**:
  ```http
  GET /api/transport/search?direction=To Campus&targetGroup=MaleStudent&category=Regular&spot=Agargaon&day=Sunday
  ```
- **Response**: `200 OK` (JSON array of matching `Bus` objects, or an informative string explaining schedule suspensions/closures).

---

#### ✏️ Update Bus Details
Updates an existing bus record identified by composite key (`busNumber`, `departureTime`, `category`).

- **URL**: `/update`
- **Method**: `PUT`
- **Query Parameters**:
  - `busNumber` (`String`, required): Bus registration number
  - `departureTime` (`String`, required): Original departure time
  - `category` (`String`, required): Service category
- **Request Body**: JSON object containing modified fields
  ```json
  {
    "departureTime": "08:15 AM",
    "driverName": "Updated Driver Name",
    "driverPhone": "01999999999"
  }
  ```
- **Response**: `200 OK` (Updated `Bus` object or `"Bus not found to update."`)

---

#### 🗑️ Delete Bus
Deletes a bus entry identified by composite key (`busNumber`, `departureTime`, `category`).

- **URL**: `/delete`
- **Method**: `DELETE`
- **Query Parameters**:
  - `busNumber` (`String`, required): Bus registration number
  - `departureTime` (`String`, required): Scheduled departure time
  - `category` (`String`, required): Service category
- **Sample Request**:
  ```http
  DELETE /api/transport/delete?busNumber=BUS-01&departureTime=07:30 AM&category=Regular
  ```
- **Response**: `200 OK`
  ```text
  "Bus deleted successfully."
  ```

---

## ⚡ Business Logic & Filtering Rules

The system includes automated operational logic inside `BusService.java`:

1. **Exam / Admission Mode Enforcement**:
   - If `Mid_Term`, `Final_Exam`, or `Admission_Test` modes are active, all `Regular` category student, teacher, and staff schedules are automatically **suspended**. Passengers are guided to search under the relevant exam or special transport category.
   - If an exam category is searched while its control flag is inactive, the system returns an informative notice that exams are inactive.
2. **Vacation Closure Protection**:
   - When the university is closed (`isVarsityOfficiallyClosed = true`), **all regular bus operations are suspended**, except emergency **Ambulance** services.
3. **Weekly Operational Restrictions**:
   - **Thursday**: Regular buses are restricted for the specified route search.
   - **Friday**: Only `Special`, `MBA`, and `Ambulance` category buses are allowed to operate.
4. **Emergency Ambulance Handling**:
   - Requests for `Ambulance` bypass vacation closures and exam suspensions to ensure emergency medical transport is accessible at all times.

---

## 🧪 How to Test & Run the Project

### Prerequisites
- **Java Development Kit (JDK)**: Java 17 or higher
- **Maven**: 3.8+ (or use the provided `./mvnw` wrapper)

---

### Building and Running

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/muntasiralmamun/Bus-Management-System-SD-1.git
   cd Bus-Management-System-SD-1
   ```

2. **Compile and Build**:
   ```bash
   # Using Maven Wrapper (Linux/macOS)
   ./mvnw clean compile

   # Using Maven Wrapper (Windows Command Prompt / PowerShell)
   .\mvnw.cmd clean compile
   ```

3. **Run Unit Tests**:
   ```bash
   ./mvnw test
   ```

4. **Start the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   *The server will start locally on port `8080` (`http://localhost:8080`).*

---

### Testing Endpoints (cURL Examples)

Execute the following commands in your terminal to test each feature step-by-step:

#### 1. Add Buses to Memory
```bash
curl -X POST "http://localhost:8080/api/transport/add" \
     -H "Content-Type: application/json" \
     -d '[
           {
             "busNo": 101,
             "busNumber": "BUS-01",
             "busType": "Double Decker",
             "driverName": "Rahim Uddin",
             "driverPhone": "01711000000",
             "routePath": "Mirpur 10 -> Agargaon -> Farmgate -> Campus",
             "departureTime": "07:30 AM",
             "dayOfWeek": "Sunday",
             "targetGroup": "MaleStudent",
             "direction": "To Campus",
             "category": "Regular",
             "startingPoint": "Mirpur 10"
           },
           {
             "busNo": 102,
             "busNumber": "BUS-102",
             "busType": "Microbus",
             "driverName": "Emergency Driver",
             "driverPhone": "01999000111",
             "routePath": "Medical Center -> Campus Gate -> Hospital",
             "departureTime": "24/7 Emergency",
             "dayOfWeek": "Sunday",
             "targetGroup": "Ambulance",
             "direction": "To Campus",
             "category": "Ambulance",
             "startingPoint": "Medical Center"
           }
         ]'
```

#### 2. Get All Registered Buses
```bash
curl -X GET "http://localhost:8080/api/transport/all"
```

#### 3. Find Bus by Bus Number
```bash
curl -X GET "http://localhost:8080/api/transport/find?busNo=101"
```

#### 4. Search for Available Buses
```bash
curl -X GET "http://localhost:8080/api/transport/search?direction=To%20Campus&targetGroup=MaleStudent&category=Regular&spot=Agargaon&day=Sunday"
```

#### 5. Activate Mid-Term Exam Mode (Admin Control)
```bash
curl -X PUT "http://localhost:8080/api/transport/admin/toggle-schedule?type=Mid&status=true"
```

#### 6. Test Regular Bus Suspension during Exam Season
```bash
# This will return a notice that regular student buses are suspended
curl -X GET "http://localhost:8080/api/transport/search?direction=To%20Campus&targetGroup=MaleStudent&category=Regular&spot=Agargaon&day=Sunday"
```

#### 7. Toggle Varsity Closure (Vacation Mode)
```bash
curl -X PUT "http://localhost:8080/api/transport/admin/varsity-status?closed=true"
```

#### 8. Update Bus Schedule Details
```bash
curl -X PUT "http://localhost:8080/api/transport/update?busNumber=BUS-01&departureTime=07:30%20AM&category=Regular" \
     -H "Content-Type: application/json" \
     -d '{
           "departureTime": "08:00 AM",
           "driverName": "Updated Rahim Uddin"
         }'
```

#### 9. Delete a Bus Schedule
```bash
curl -X DELETE "http://localhost:8080/api/transport/delete?busNumber=BUS-01&departureTime=08:00%20AM&category=Regular"
```

---

### Testing via Postman / Insomnia

1. Import or create a new HTTP Request in Postman.
2. Set the HTTP Method (`GET`, `POST`, `PUT`, `DELETE`).
3. Set URL to `http://localhost:8080/api/transport/{endpoint}`.
4. Add Query Parameters in the **Params** tab (e.g. `direction`, `spot`, `day`).
5. For `POST /add` and `PUT /update`, set the **Body** tab to `raw` and select **JSON** format.

---

## 🤝 Contributing

Contributions make the open-source community an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**!

If you want to contribute to this project:

1. **Fork the Repository**: Click the `Fork` button at the top right of the GitHub repository page.
2. **Clone your Forked Repository**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/Bus-Management-System-SD-1.git
   cd Bus-Management-System-SD-1
   ```
3. **Create a Feature Branch**:
   ```bash
   git checkout -b feature/AmazingFeature
   ```
4. **Make Your Changes**:
   - Follow standard Java and Spring Boot code formatting conventions.
   - Maintain proper encapsulation and REST standards.
5. **Commit Your Changes**:
   ```bash
   git commit -m "Add some AmazingFeature"
   ```
6. **Push to Your Branch**:
   ```bash
   git push origin feature/AmazingFeature
   ```
7. **Open a Pull Request**:
   - Go to the original repository on GitHub.
   - Click **New Pull Request**.
   - Describe the features or bug fixes implemented in your PR.

---

## 📄 License

Distributed under the MIT License. See `LICENSE` file if included or contact project maintainers for details.
