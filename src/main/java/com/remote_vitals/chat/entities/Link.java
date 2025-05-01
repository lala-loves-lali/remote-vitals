package com.remote_vitals.chat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)

@Entity
@DiscriminatorValue("link")
public class Link extends Message implements Linkable {
    @Override
    public void link() {
        // yahan sari implementation ayegi jb link pe click kia jaega
    }
}
