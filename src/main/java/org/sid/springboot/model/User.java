package org.sid.springboot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity //Pour que ça puisse être mapper dans la base de données
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String role;
}
