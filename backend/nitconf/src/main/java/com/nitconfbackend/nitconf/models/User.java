package com.nitconfbackend.nitconf.models;

import java.util.*;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 * Represents a user in the system.
 * Implements UserDetails for Spring Security integration.
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Users")
public class User implements UserDetails {
    /**
     * The unique identifier for the user.
     */
    @Id public String id;
     /**
     * The first name of the user.
     */
    public String firstName;
    /**
     * The last name of the user.
     */
    public String lastName;
   /**
     * The unique email address of the user.
     */
    @Indexed(unique = true) public String email;
    /**
     * The password of the user (JsonIgnore to avoid exposing it).
     */
    @JsonIgnore public String password;
    // public Role role;
    /**
     * Indicates whether the user's email is verified.
     */
    public Boolean isVerified;

    /**
     * List of sessions associated with this user.
     */
    @JsonIgnore
    @DBRef
    public List<Session> sessions;

     
    /**
     * Get the authorities (roles) assigned to the user.
     * @return Collection of GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

     /**
     * Get the username of the user.
     * @return The user's email.
     */
    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }   

     /**
     * Check if the user's account is non-expired.
     * @return Always returns true.
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

      /**
     * Check if the user's account is non-locked.
     * @return Always returns true.
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() { 
        return true;
    }

     /**
     * Check if the user's credentials are non-expired.
     * @return Always returns true.
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() { 
        return true;
    }

    /**
     * Check if the user is enabled.
     * @return Always returns true.
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() { 
        return true;
    }

     /**
     * Get the password of the user.
     * @return The user's password.
     */
    @Override  
    public String getPassword() {
        return password;
    }

}
