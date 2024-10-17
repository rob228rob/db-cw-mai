package com.k_plus.internship.CertificatePackage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pdf_files")
public class FilePDF {

    @Id
    private UUID id;

    @Lob
    private byte[] file;

    @OneToOne(mappedBy = "pdf")
    private Certificate certificate;
}
