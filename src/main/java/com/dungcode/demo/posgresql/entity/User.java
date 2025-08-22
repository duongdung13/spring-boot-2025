package com.dungcode.demo.posgresql.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findAllByUsername",
                query = "SELECT u FROM users u WHERE u.username = :username")
})
public class User extends AbstractEntity {
    String username;
    String password;
    String firstName;
    String lastName;
    String address;
    LocalDate dob;
    Set<String> roles;

    @Builder.Default
    @Column(precision = 19, scale = 4)
    BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();
}
