package com.omvaultchain.accesscontrol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class File {
    @Id
    private String id;

    private String ownerId;
}
