package com.remote_vitals.backend.db_handler;

import com.remote_vitals.backend.appointment.entities.Appointment;

import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.appointment.repositories.AppointmentRepository;

//import com.remote_vitals.backend.chat.entities.ChatRoom;
//import com.remote_vitals.backend.chat.entities.Message;
//import com.remote_vitals.backend.chat.repositories.ChatRoomRepository;
//import com.remote_vitals.backend.chat.repositories.MessageRepository;
import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.checkup.repositories.CheckUpRepository;
import com.remote_vitals.backend.user.entities.*;
import com.remote_vitals.backend.user.repositories.*;
import com.remote_vitals.backend.vital.entities.VitalRecord;
import com.remote_vitals.backend.vital.repositories.VitalRecordRepository;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import com.remote_vitals.backend.vitalReport.repositories.VitalReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataBaseHandler {
    // beans
    private final VitalRecordRepository vitalRecordRepository;
    private final VitalReportRepository vitalReportRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final QualificationRepository qualificationRepository;
    private final CheckUpRepository checkUpRepository;
    private final AppointmentRepository appointmentRepository;
    
//   private final ChatRoomRepository chatRoomRepository;
//   private final MessageRepository messageRepository;

    @Autowired
    public DataBaseHandler(
            VitalRecordRepository vitalRecordRepository,
            UserRepository userRepository,
            VitalReportRepository vitalReportRepository,
            AdminRepository adminRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            QualificationRepository qualificationRepository,
            CheckUpRepository checkUpRepository,
            AppointmentRepository appointmentRepository
          
//            ,ChatRoomRepository chatRoomRepository,
//            MessageRepository messageRepository
            ) {
        this.vitalRecordRepository = vitalRecordRepository;
        this.vitalReportRepository = vitalReportRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.qualificationRepository = qualificationRepository;
        this.checkUpRepository = checkUpRepository;
        this.appointmentRepository = appointmentRepository;
    
//        this.chatRoomRepository = chatRoomRepository;
//        this.messageRepository = messageRepository;
    }

    // 1
    @Transactional
    public int registerDoctor(Doctor doctor){
        if(doctor == null || doctor.getId() != null) return -1;
        try {
            doctor = doctorRepository.save(doctor);
            return doctor.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 2
    @Transactional
    public int updateDoctor(Doctor doctor){
        if(doctor == null || doctor.getId() == null) return -1;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
        try {
            doctor = doctorRepository.save(doctor);
            return doctor.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 3
    @Transactional
    public int deleteDoctor(Doctor doctor){
        if(doctor == null || doctor.getId() == null) return -1;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
        try {
            doctorRepository.delete(doctor);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // 4
    @Transactional
    public int registerPatient(Patient patient){
        if(patient == null || patient.getId() != null) return -1;
        try {
            patient = patientRepository.save(patient);
            return patient.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 5
    @Transactional
    public int updatePatient(Patient patient){
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        try {
            patient = patientRepository.save(patient);
            return patient.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 6
    @Transactional
    public int deletePatient(Patient patient){
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        try {
            patientRepository.delete(patient);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // 7
    @Transactional
    public int addVitalReport(
            Patient patient,
            LocalDateTime timeWhenMade,
            ArrayList<VitalRecord> vitalRecords
    ){
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        if(vitalRecords == null || vitalRecords.isEmpty()) return -1;
        if(vitalRecords.stream().anyMatch(vitalRecord -> {
                    if(vitalRecord.getId() != null) return true;
                    return false;
        })) return -1;
        try {
            VitalReport vitalReport = new VitalReport();
            vitalReport.setPatient(patient);
            vitalReport.setReportWhenMade(timeWhenMade);
            vitalReport.setVitalRecords(vitalRecordRepository.saveAll(vitalRecords));
            vitalReport = vitalReportRepository.save(vitalReport);
            return vitalReport.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 8
    public List<List<VitalRecord>> getAllVitalReportsOf(Patient patient){
        if(patient == null || patient.getId() == null) return null;
        if(patientRepository.findById(patient.getId()).isEmpty()) return null;
        if(patient.getVitalReport() == null || patient.getVitalReport().isEmpty()) return null;
        return patient.getVitalReport().stream().map(VitalReport::getVitalRecords).toList();
    }

    // 9
    @Transactional
    public int deleteVitalReport(VitalReport vitalReport){
        if(vitalReport == null || vitalReport.getId() == null) return -1;
        if(vitalReportRepository.findById(vitalReport.getId()).isEmpty()) return -1;
        try {
            vitalRecordRepository.deleteAll(vitalReport.getVitalRecords());
            vitalReportRepository.delete(vitalReport);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // 10
    public Patient getPatient(Integer id){
        if(id == null) return null;
        return patientRepository.findById(id).orElse(null);
    }

    // 11
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    // 12
    public Doctor getDoctor(Integer id){
        if(id == null) return null;
        return doctorRepository.findById(id).orElse(null);
    }

    // 13
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    // 14
    @Transactional
    public int placeAppointmentRequest(
            Patient patient,
            Doctor doctor,
            LocalDateTime startingTime,
            LocalDateTime endingTime,
            String linkForRoom
    ){
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        if(doctor == null || doctor.getId() == null) return -1;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
        if(startingTime == null || endingTime == null || startingTime.isAfter(endingTime)) return -1;

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.REQUESTED);
        appointment.setStartingTime(startingTime);
        appointment.setEndingTime(endingTime);
        appointment.setLinkForRoom(linkForRoom);
        
        try {
            appointment = appointmentRepository.save(appointment);
            patient.getAppointments().add(appointment);
            doctor.getAppointments().add(appointment);
            patientRepository.save(patient);
            doctorRepository.save(doctor);
            return appointment.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // Simplified version without separate schedule
    @Transactional
    public int placeAppointmentRequest(
            Patient patient,
            Doctor doctor
    ){
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        if(doctor == null || doctor.getId() == null) return -1;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(AppointmentStatus.REQUESTED);
        
        // Set default appointment time (now + 1 day for 1 hour)
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        appointment.setStartingTime(startTime);
        appointment.setEndingTime(startTime.plusHours(1));
        
        try {
            appointment = appointmentRepository.save(appointment);
            patient.getAppointments().add(appointment);
            doctor.getAppointments().add(appointment);
            patientRepository.save(patient);
            doctorRepository.save(doctor);
            return appointment.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Assigns a patient to a doctor
     * @param patient The patient to be assigned
     * @param doctor The doctor to assign the patient to, or null to remove assignment
     * @return 0 if successful, -1 if failed
     */
    @Transactional
    public int assignPatientToDoctor(Patient patient, Doctor doctor) {
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        
        try {
            // Case 1: Doctor is null - remove assignment
            if (doctor == null) {
                return removePatientFromDoctor(patient);
            }
            
            // Case 2: Doctor is not null - assign patient to doctor
            if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
            
            // Remove patient from previous doctor if any
            if(patient.getAssignedDoctor() != null) {
                patient.getAssignedDoctor().getAssignedPatients().remove(patient);
                doctorRepository.save(patient.getAssignedDoctor());
            }

            // Assign patient to new doctor
            patient.setAssignedDoctor(doctor);
            doctor.getAssignedPatients().add(patient);

            // Save changes
            patientRepository.save(patient);
            doctorRepository.save(doctor);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    @Transactional
    public int removePatientFromDoctor(Patient patient){
        if(patient == null || patient.getId() == null) return -1;
        if(patientRepository.findById(patient.getId()).isEmpty()) return -1;
        if(patient.getAssignedDoctor() == null) return -1;
        
        // Remove from doctor's list
        patient.getAssignedDoctor().getAssignedPatients().remove(patient);
        doctorRepository.save(patient.getAssignedDoctor());
        
        // Set patient's doctor to null
        patient.setAssignedDoctor(null);
        patientRepository.save(patient);
        
        return 0;
    }

    // 15
    @Transactional
    public int changeAppointmentStatus(
            Appointment appointment,
            AppointmentStatus status
    ){
        if(appointment == null || appointment.getId() == null) return -1;
        if(appointmentRepository.findById(appointment.getId()).isEmpty()) return -1;
        if(status == null) return -1;
        try {
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // 16 - Updated to work directly with Appointment
    @Transactional
    public int updateAppointmentTime(
            Appointment appointment,
            LocalDateTime startingTime,
            LocalDateTime endingTime
    ){
        if(startingTime == null || endingTime == null || startingTime.isAfter(endingTime)) return -1;
        if(appointment == null || appointment.getId() == null) return -1;
        if(appointmentRepository.findById(appointment.getId()).isEmpty()) return -1;

        try {
            appointment.setStartingTime(startingTime);
            appointment.setEndingTime(endingTime);
            appointmentRepository.save(appointment);
            return appointment.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 18
    @Transactional
    public int deleteAppointment(Appointment appointment){
        if(appointment == null || appointment.getId() == null) return -1;
        if(appointmentRepository.findById(appointment.getId()).isEmpty()) return -1;
        try {
            appointment.getPatient().getAppointments().remove(appointment);
            appointment.getDoctor().getAppointments().remove(appointment);
            patientRepository.save(appointment.getPatient());
            appointmentRepository.delete(appointment);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // 19
    public List<Appointment> getAllAppointmentsForDoctor(
            Doctor doctor
    ){
        if(doctor == null || doctor.getId() == null) return null;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return null;
        if(doctor.getAppointments() == null || doctor.getAppointments().isEmpty()) return null;
        return doctor.getAppointments();
    }

    // 20
    public List<Appointment> getAllAppointmentsForPatient(
            Patient patient
    ){
        if(patient == null || patient.getId() == null) return null;
        if(patientRepository.findById(patient.getId()).isEmpty()) return null;
        if(patient.getAppointments() == null || patient.getAppointments().isEmpty()) return null;
        return patient.getAppointments();
    }

//    // 21
//    @Transactional
//    public int createChatRoomFor(Appointment appointment, LocalDateTime timeCreated){
//        if(timeCreated == null) return -1;
//        if(appointment == null || appointment.getId() == null) return -1;
//        if(appointmentRepository.findById(appointment.getId()).isEmpty()) return -1;
//        if(appointment.getChatRoom() != null) return -1;
//        try {
//            ChatRoom chatRoom = new ChatRoom();
//            chatRoom.setTimeCreated(timeCreated);
//            chatRoom.setAppointment(appointment);
//            appointment.setChatRoom(chatRoomRepository.save(chatRoom));
//            appointmentRepository.save(appointment);
//            return 0;
//        } catch (Exception e) {
//            return -1;
//        }
//    }

//    // 22
//    @Transactional
//    public int deleteChatRoom(ChatRoom chatRoom){
//        if(chatRoom == null || chatRoom.getId() == null) return -1;
//        if(chatRoomRepository.findById(chatRoom.getId()).isEmpty()) return -1;
//        try {
//            if(chatRoom.getAppointment() != null) {
//                appointmentRepository.delete(chatRoom.getAppointment());
//            }
//            if(chatRoom.getMessages() != null) {
//                messageRepository.deleteAll(chatRoom.getMessages());
//            }
//            chatRoomRepository.delete(chatRoom);
//            return 0;
//        } catch (Exception e) {
//            return -1;
//        }
//    }

    // 23
//    @Transactional
//    public int addChatMessage(ChatRoom chatRoom, Message message){
//        if(chatRoom == null || chatRoom.getId() == null) return -1;
//        if(chatRoomRepository.findById(chatRoom.getId()).isEmpty()) return -1;
//        if(
//                message == null ||
//                message.getId() != null ||
//                message.getChatRoom() != null
//        ) return -1;
//        try {
//            message.setChatRoom(chatRoom);
//            chatRoom.getMessages().add(messageRepository.save(message));
//            chatRoomRepository.save(chatRoom);
//            return 0;
//        } catch (Exception e) {
//            return -1;
//        }
//    }

    // 24
//    @Transactional
//    public int deleteChatMessage(ChatRoom chatRoom,Message message){
//        if(chatRoom == null || chatRoom.getId() == null) return -1;
//        if(chatRoomRepository.findById(chatRoom.getId()).isEmpty()) return -1;
//        if(message == null || message.getId() == null) return -1;
//        if(messageRepository.findById(message.getId()).isEmpty()) return -1;
//        try {
//            chatRoom.getMessages().remove(message);
//            chatRoomRepository.save(chatRoom);
//            messageRepository.delete(message);
//            return 0;
//        } catch (Exception e) {return -1;}
//    }

//    // 25
//    public List<ChatRoom> getAllChatRoomsOfDoctor(Doctor doctor){
//        if(doctor == null || doctor.getId() == null) return null;
//        if(doctorRepository.findById(doctor.getId()).isEmpty()) return null;
//        if(doctor.getAppointments() == null || doctor.getAppointments().isEmpty()) return null;
//        return doctor.getAppointments().stream().map(Appointment::getChatRoom).toList();
//    }

    // 26
//    public List<ChatRoom> getAllChatRoomsOfPatient(Patient patient){
//        if(patient == null || patient.getId() == null) return null;
//        if(patientRepository.findById(patient.getId()).isEmpty()) return null;
//        if(patient.getAppointments() == null || patient.getAppointments().isEmpty()) return null;
//        return patient.getAppointments().stream().map(Appointment::getChatRoom).toList();
//    }

    // 27
    public Optional<User> getUserFromPassword(String email, String password){
        return userRepository.findByEmail(email);
    }


 


    // 28
    @Transactional
    public int addQualificationTo(Doctor doctor, Qualification qualification){
        if(qualification == null || qualification.getId() != null) return -1;
        if(doctor == null || doctor.getId() == null) return -1;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
        try {
            qualification.setDoctor(doctor);
            doctor.getQualifications().add(qualificationRepository.save(qualification));
            doctorRepository.save(doctor);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    // 30
    @Transactional
    public int addCheckUp(CheckUp checkUp){
        if(checkUp == null || checkUp.getId() != null) return -1;
        if(
                checkUp.getPatient() == null ||
                checkUp.getPatient().getId() == null ||
                checkUp.getDoctor() == null ||
                checkUp.getDoctor().getId() == null ||
                patientRepository.findById(checkUp.getPatient().getId()).isEmpty() ||
                doctorRepository.findById(checkUp.getDoctor().getId()).isEmpty()
        )
            return -1;
        try {
            patientRepository.save(checkUp.getPatient());
            doctorRepository.save(checkUp.getDoctor());
            checkUp = checkUpRepository.save(checkUp);
            return checkUp.getId();
        } catch (Exception e) {return -1;}
    }

    // 31
    public List<CheckUp> getAllCheckUpsOf(Patient patient){
        if(patient == null || patient.getId() == null) return null;
        if(patientRepository.findById(patient.getId()).isEmpty()) return null;
        if(patient.getCheckups() == null || patient.getCheckups().isEmpty()) return null;
        return patient.getCheckups();
    }

    // 32
    public List<CheckUp> getAllCheckUpsOf(Doctor doctor){
        if(doctor == null || doctor.getId() == null) return null;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return null;
        if(doctor.getCheckups() == null || doctor.getCheckups().isEmpty()) return null;
        return doctor.getCheckups();
    }

    // 33
    public int registerAdmin(Admin admin){
        if(admin == null || admin.getId() != null) return -1;
        try {
            admin = adminRepository.save(admin);
            return admin.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 34
    public int updateAdmin(Admin admin){
        if(admin == null || admin.getId() == null) return -1;
        if(adminRepository.findById(admin.getId()).isEmpty()) return -1;
        try {
            admin = adminRepository.save(admin);
            return admin.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    // 35
    public int deleteAdmin(Admin admin){
        if(admin == null || admin.getId() == null) return -1;
        if(adminRepository.findById(admin.getId()).isEmpty()) return -1;
        try {
            adminRepository.delete(admin);
            return 0;
        }
        catch (Exception ex){
            return -1;
        }
    }

    // 36
    public Optional<User> getUserById(Integer id){
        if(id == null) return Optional.empty();
        return userRepository.findById(id);
    }

    // 37
    public Optional<Appointment> getAppointmentBId(Integer id){
        if(id == null) return Optional.empty();
        return appointmentRepository.findById(id);
    }

    // 38
    public Optional<CheckUp> getCheckUpById(Integer id){
        if(id == null) return Optional.empty();
        return checkUpRepository.findById(id);
    }

    // 39
    public Optional<VitalReport> getVitalReportById(Integer id){
        if(id == null) return Optional.empty();
        return vitalReportRepository.findById(id);
    }

    // 40
    public User getUserFromEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Updates a doctor's qualification string
     * @param doctor The doctor to update
     * @param qualification The new qualification string
     * @return 0 if successful, -1 if failed
     */
    @Transactional
    public int updateDoctorQualification(Doctor doctor, String qualification) {
        if(doctor == null || doctor.getId() == null) return -1;
        if(doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
        try {
            doctor.setQualificationString(qualification);
            doctorRepository.save(doctor);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    
}
