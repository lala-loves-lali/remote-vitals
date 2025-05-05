package com.remote_vitals.backend.chat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)

@Entity
@DiscriminatorValue("text_message")
public class TextMessage extends Message {
}
