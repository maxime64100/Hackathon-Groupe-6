package com.api.hackathon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_babyfoot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBabyfoot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    private String name;
    private String surname;
    private String mail;
    private String passwordUser;
}