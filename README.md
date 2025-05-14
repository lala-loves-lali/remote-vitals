# Remote Vitals System - Class Structure

This document provides an overview of the main classes in the Remote Vitals System and their relationships.

## User Management

```mermaid
classDiagram
    class User {
        -Integer id
        -String profilePhoto
        -String firstName
        -String lastName
        -Gender gender
        -String phoneNumber
        -Visibility pnVisibility
        -String email
        -Visibility eVisibility
        -String password
    }
    
    class Doctor {
        -String qualificationString
        -String description
        -List~Appointment~ appointments
        -List~CheckUp~ checkUps
    }
    
    class Patient {
        -String medicalHistory
        -String bloodGroup
        -LocalDate dateOfBirth
        -List~Appointment~ appointments
        -List~CheckUp~ checkUps
        -List~VitalReport~ vitalReports
    }
    
    class Admin {
    }
    
    User <|-- Doctor
    User <|-- Patient
    User <|-- Admin
```

## Appointments and Scheduling

```mermaid
classDiagram
    class Appointment {
        -Integer id
        -AppointmentStatus status
        -String linkForRoom
        -Patient patient
        -Doctor doctor
        -Schedule schedule
    }
    
    class Schedule {
        -Integer id
        -LocalDateTime startingTime
        -LocalDateTime endingTime
        -Appointment appointment
    }
    
    class CheckUp {
        -Integer id
        -String prescription
        -String feedback
        -LocalDateTime timeWhenMade
        -Patient patient
        -Doctor doctor
    }
    
    class AppointmentStatus {
        <<enumeration>>
        REQUESTED
        SCHEDULED
        REJECTED
        CANCELED
        POSTPONED
    }
    
    Appointment "1" -- "1" Schedule
    Appointment "many" -- "1" Patient
    Appointment "many" -- "1" Doctor
    Appointment ..|> AppointmentStatus
    CheckUp "many" -- "1" Patient
    CheckUp "many" -- "1" Doctor
```

## Vital Records System

```mermaid
classDiagram
    class VitalReport {
        -Integer id
        -LocalDateTime reportWhenMade
        -List~VitalRecord~ vitalRecords
        -Patient patient
    }
    
    class VitalRecord {
        -Integer id
        -float value
        -VitalStatus status
        -VitalReport vitalReport
    }
    
    class BodyTemperature {
        +MIN_TEMP: 30.0f
        +MAX_TEMP: 42.0f
    }
    
    class HeartRate {
        +MIN_HEART_RATE: 60
        +MAX_HEART_RATE: 100
    }
    
    class RespiratoryRate {
        +MIN_RESPIRATORY_RATE: 12.0f
        +MAX_RESPIRATORY_RATE: 20.0f
    }
    
    class BloodPressureSystolic {
        +MIN_SYSTOLIC: 90
        +MAX_SYSTOLIC: 180
    }
    
    class BloodPressureDiastolic {
        +MIN_DIASTOLIC: 60
        +MAX_DIASTOLIC: 120
    }
    
    class Height {
        +MIN_HEIGHT: 0.0f
        +MAX_HEIGHT: 272.0f
    }
    
    class Weight {
        +MIN_WEIGHT: 0.0f
        +MAX_WEIGHT: 500.0f
    }
    
    class VitalStatus {
        <<enumeration>>
        NORMAL
        ABNORMAL
    }
    
    VitalReport "1" -- "many" VitalRecord
    VitalReport "many" -- "1" Patient
    VitalRecord <|-- BodyTemperature
    VitalRecord <|-- HeartRate
    VitalRecord <|-- RespiratoryRate
    VitalRecord <|-- BloodPressureSystolic
    VitalRecord <|-- BloodPressureDiastolic
    VitalRecord <|-- Height
    VitalRecord <|-- Weight
    VitalRecord ..|> VitalStatus
```

## System Architecture

```mermaid
classDiagram
    class Controller {
        <<interface>>
    }
    
    class Service {
        <<interface>>
    }
    
    class Repository {
        <<interface>>
    }
    
    class Entity {
        <<interface>>
    }
    
    Controller ..> Service : uses
    Service ..> Repository : uses
    Repository ..> Entity : manages
    
    class UserController {
    }
    
    class AppointmentController {
    }
    
    class VitalReportController {
    }
    
    class UserService {
    }
    
    class AppointmentService {
    }
    
    class VitalReportService {
    }
    
    Controller <|.. UserController
    Controller <|.. AppointmentController
    Controller <|.. VitalReportController
    
    Service <|.. UserService
    Service <|.. AppointmentService
    Service <|.. VitalReportService
    
    UserController ..> UserService
    AppointmentController ..> AppointmentService
    VitalReportController ..> VitalReportService
```

## Frontend Structure

```mermaid
classDiagram
    class BaseController {
        +getContext()
        +navigateTo()
        +showErrorAlert()
        +showInfoAlert()
    }
    
    class LoginController {
    }
    
    class DoctorDashboardController {
    }
    
    class PatientDashboardController {
    }
    
    class DoctorAppointmentsController {
    }
    
    class PatientAppointmentsController {
    }
    
    BaseController <|-- LoginController
    BaseController <|-- DoctorDashboardController
    BaseController <|-- PatientDashboardController
    BaseController <|-- DoctorAppointmentsController
    BaseController <|-- PatientAppointmentsController
```

## Data Flow

```mermaid
flowchart TD
    FE[Frontend] <--> BE[Backend]
    BE <--> DB[(Database)]
    
    P[Patient] --> FE
    D[Doctor] --> FE
    
    subgraph Frontend
    UI[User Interface]
    C[Controllers]
    U[Utils]
    end
    
    subgraph Backend
    S[Services]
    R[Repositories]
    E[Entities]
    end
    
    UI --> C
    C --> U
    FE --> BE
    S --> R
    R --> E
    E --> DB
``` 