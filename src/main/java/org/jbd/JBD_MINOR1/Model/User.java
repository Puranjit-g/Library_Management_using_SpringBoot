package org.jbd.JBD_MINOR1.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    private String password;//pass nd authority becomes mandatory for spring security
                            //after implementing the user details methods.

    private String authorities;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNo;

    @Column(unique = true, length = 40)
    private String email;

    @Column
    private String address;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    @Enumerated
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private List<Book> bookList;

    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"user","book"})
    private List<Txn> txnList;

    private String temp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] auth = authorities.split(",");
        return Arrays.stream(auth).map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;//in this case
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
