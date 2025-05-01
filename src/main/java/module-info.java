module com.remote_vitals {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    
    // Java SE modules
    requires java.sql;
    requires java.desktop;
    requires java.xml;
    
    // Spring modules
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.data.jpa;
    
    // Jakarta modules
    requires jakarta.persistence;
    requires jakarta.transaction;
    
    // Add access to unnamed modules
    requires transitive java.naming;
    requires jakarta.validation;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires itextpdf;
    requires com.github.vlsi.mxgraph.jgraphx;
    requires jakarta.mail;

    // Export packages
    exports com.remote_vitals;
    exports com.remote_vitals.appointment.entities;
    exports com.remote_vitals.appointment.repositories;
    exports com.remote_vitals.vital.entities;
    exports com.remote_vitals.vitalReport.entities;
    exports com.remote_vitals.checkup.entities;
    exports com.remote_vitals.user.entities;
    exports com.remote_vitals.user.repositories;
    exports com.remote_vitals.user.enums;
    exports com.remote_vitals.chat.repositories;
    
    // Open packages for reflection and FXML
    opens com.remote_vitals to javafx.fxml, spring.core;
    opens com.remote_vitals.appointment.entities to spring.core;
    opens com.remote_vitals.appointment.repositories to spring.core;
    opens com.remote_vitals.user.enums to spring.core;
    opens com.remote_vitals.user.entities to spring.core;
    opens com.remote_vitals.user.repositories to spring.core;
    opens com.remote_vitals.vital.entities to spring.core;
    opens com.remote_vitals.vitalReport.entities to spring.core;
    opens com.remote_vitals.checkup.entities to spring.core;
    opens com.remote_vitals.chat.repositories to spring.core;
}