package com.k_plus.internship.CertificatePackage;

import com.k_plus.internship.CoursePackage.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "certificates")
public class Certificate {

    @Id
    private UUID id;

    private boolean sent;

    private String subject;

    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @OneToOne(fetch = FetchType.LAZY)
    private FilePDF pdf;
}
