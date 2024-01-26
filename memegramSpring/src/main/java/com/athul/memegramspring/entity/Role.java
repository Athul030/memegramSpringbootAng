    package com.athul.memegramspring.entity;


    import jakarta.persistence.*;
    import lombok.*;

    import java.util.Collection;

    @Data
    @AllArgsConstructor @NoArgsConstructor
    @Entity
    @Table(name = "roles")
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "role_id")
        private Long id;
        private String name;

        @ManyToMany
        private Collection<Admin> admins;
    }
