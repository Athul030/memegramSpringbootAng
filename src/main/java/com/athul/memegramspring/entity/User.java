package com.athul.memegramspring.entity;

import com.athul.memegramspring.enums.Provider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 100)
    private String fullName;

    @Column(nullable = false,length = 100)
    private String userHandle;

    @Column(nullable = false,length = 100)
    private String email;

    private String password;

    private String bio;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Post> post = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name="users",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="role",referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    private String profilePicUrl;

    private boolean userPresence;

    private int reportedCount;

    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authorities;
    }

    @OneToMany(mappedBy = "follower")
    private List<Follow> followers;

    @OneToMany(mappedBy = "following")
    private List<Follow> following;

    @ManyToMany
    @JoinTable(name = "user_blocks",joinColumns = @JoinColumn(name = "blockingUserId"),inverseJoinColumns = @JoinColumn(name = "blockedUserId"))
    private Set<User> blockedUsers;

    @Override
    public String getUsername() {
        return this.email;
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
