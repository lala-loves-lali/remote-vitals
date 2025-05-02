package com.remote_vitals.backend.appointment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Check(name = "schedule_check", constraints = "starting_time < ending_time")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ScheduleGenerator")
    @TableGenerator(
            name = "ScheduleGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "schedule",
            allocationSize = 10
    )
    private Integer id;

    @NotNull
    private LocalDateTime startingTime;

    @NotNull
    private LocalDateTime endingTime;

    // Relationships
    @OneToMany(mappedBy = "schedule")
    private List<Appointment> appointments;
}
