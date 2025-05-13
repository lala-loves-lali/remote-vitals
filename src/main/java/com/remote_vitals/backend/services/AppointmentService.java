package com.remote_vitals.backend.services;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.appointment.entities.Schedule;
import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.appointment.repositories.AppointmentRepository;
import com.remote_vitals.backend.appointment.repositories.ScheduleRepository;
import com.remote_vitals.backend.db_handler.StaticDataClass;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.repositories.DoctorRepository;
import com.remote_vitals.backend.user.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    // beans
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;

    // constructor
    public AppointmentService(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            ScheduleRepository scheduleRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // methods
    @Transactional
    public String RequestAppointment(Integer DoctorId) {
        if (DoctorId == null) return "DoctorId is Empty";
        
        Optional<Doctor> doctorOptional = doctorRepository.findById(DoctorId);
        if (!doctorOptional.isPresent()) {
            return "Doctor Not Found";
        }
        
        doctorOptional.ifPresent(doctor -> {
            Patient patient = patientRepository.findById(StaticDataClass.currentUserId).get();
            Appointment appointment = new Appointment(
                    AppointmentStatus.REQUESTED, patient, doctor, null
            );
            if(doctor.getAppointments() == null) doctor.setAppointments(Arrays.asList(appointment));
            else doctor.getAppointments().add(appointment);
            if(patient.getAppointments() == null) patient.setAppointments(Arrays.asList(appointment));
            else patient.getAppointments().add(appointment);
            appointmentRepository.save(appointment);
        });
        
        return "Appointment Requested Successfully";
    }

    @Transactional
    public String addScheduleToAppointment(Integer AppointmentId, LocalDateTime startingTime, LocalDateTime endingTime) {
        if (AppointmentId == null) return "Appointment Not Present";
        if (startingTime == null) return "Starting Time Is Empty";
        if (endingTime == null) return "Ending Time is Empty";
        if (startingTime.isAfter(endingTime)) return "Starting Time Is After Ending Time";
        Optional<Appointment> wrappedAppointment = appointmentRepository.findById(AppointmentId);
        if (wrappedAppointment.isEmpty()) return "Appointment Id doesnt correspond to any appointment";
        wrappedAppointment.ifPresent(appointment -> {
            Schedule schedule = new Schedule(startingTime, endingTime, appointment);
            appointment.setSchedule(schedule);
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            scheduleRepository.save(schedule);
        });
        return "Schedule Added Successfully";
    }

    @Transactional
    public String changeAppointmentStatus(Integer AppointmentId, AppointmentStatus newStatus) {
        Optional<Appointment> wrappedAppointment = appointmentRepository.findById(AppointmentId);
        if (wrappedAppointment.isEmpty()) return "Appointment Id doesnt correspond to any appointment";
        wrappedAppointment.ifPresent(appointment -> {
            appointment.setStatus(newStatus);
        });
        return "Appointment Status Changed Successfully";
    }
    @Transactional
    public String deleteAppointment(Integer AppointmentId) {
        if(AppointmentId == null) return "Appointment Id Is Empty";
        Optional<Appointment> wrappedAppointment = appointmentRepository.findById(AppointmentId);
        if(wrappedAppointment.isEmpty()) return "Appointment Id Does Not Correspond To Any Appointment";
        wrappedAppointment.ifPresent(appointment -> {
            appointmentRepository.delete(appointment);
            appointment.getPatient().getAppointments().remove(appointment);
            appointment.getDoctor().getAppointments().remove(appointment);
        });
        return "Appointment Deleted Successfully";
    }

    // for patient to get all the doctors
    // for requesting appointments
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // for doctor to get all the patients who have appointments with him/her
    public List<Appointment> getAllAppointments() {
        Optional<Doctor> wrappedDoctor = doctorRepository.findById(StaticDataClass.currentUserId);
        // ye function is project men null return normally nhi karega
        return wrappedDoctor.map(Doctor::getAppointments).orElse(null);
    }
    
    // for patient to get all their appointments
    public List<Appointment> getAllPatientAppointments() {
        Optional<Patient> wrappedPatient = patientRepository.findById(StaticDataClass.currentUserId);
        return wrappedPatient.map(Patient::getAppointments).orElse(null);
    }
    
    // Get the latest appointment for the current user
    public Optional<Appointment> getLatestAppointment() {
        List<Appointment> appointments = getAllPatientAppointments();
        if (appointments != null && !appointments.isEmpty()) {
            // Return the last appointment in the list (most recently added)
            return Optional.of(appointments.get(appointments.size() - 1));
        }
        return Optional.empty();
    }
    
    /**
     * Add or update the meeting link for an appointment
     * 
     * @param appointmentId The ID of the appointment to update
     * @param meetingLink The meeting link to add to the appointment
     * @return A string indicating the result of the operation
     */
    @Transactional
    public String addMeetingLink(Integer appointmentId, String meetingLink) {
        if (appointmentId == null) return "Appointment ID is empty";
        if (meetingLink == null || meetingLink.trim().isEmpty()) return "Meeting link is empty";
        
        Optional<Appointment> wrappedAppointment = appointmentRepository.findById(appointmentId);
        if (wrappedAppointment.isEmpty()) return "Appointment ID doesn't correspond to any appointment";
        
        wrappedAppointment.ifPresent(appointment -> {
            appointment.setLinkForRoom(meetingLink);
            appointmentRepository.save(appointment);
        });
        
        return "Meeting link updated successfully";
    }
}