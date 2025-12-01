package net.accel_tech.omni365_saas_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NdourBamba18
 **/

@Entity
@Table(name = "particular_forms")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class ParticularForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First Name field is required!")
    @Size(min = 2, max = 100)
    @Column(name = "firstName")
    private String firstName;

    @NotBlank(message = "Last Name field is required!")
    @Size(min = 2, max = 100)
    @Column(name = "lastName")
    private String lastName;

    @NotBlank(message = "Personal Email field is required!")
    @Size(max = 180)
    @Email(message = "Please, enter a valid email address.")
    @Column(name = "personalEmail", unique = true) // Changer le nom de la colonne
    private String personalEmail;

    @NotBlank(message = "Phone Number field is required!")
    @Size(max = 20)
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @NotBlank(message = "Message field is required!")
    @Size(max = 300)
    @Column(name = "message")
    private String message;

    @NotBlank(message = "Account name field is required!")
    @Size(min = 3, max = 50, message = "Account name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
            message = "Account name can only contain letters, numbers, dots, underscores and hyphens")
    @Column(name = "nameAccount", unique = true) // Unique pour éviter les doublons
    private String nameAccount;

    @Transient // Ce champ n'est pas persisté en base
    private String fullAccountEmail; // Pour stocker le compte complet avec @gafa.com

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private Date createdAt;

    // Méthode utilitaire pour obtenir le nom complet
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Méthode pour générer l'email complet Gafa
    public String getFullAccountEmail() {
        if (nameAccount != null && !nameAccount.trim().isEmpty()) {
            return nameAccount.toLowerCase().trim() + "@heritage.africa";
        }
        return null;
    }

    // Méthode pour valider et formater le nameAccount
    @PrePersist
    @PreUpdate
    public void formatNameAccount() {
        if (nameAccount != null) {
            // Supprimer @gafa.com s'il est déjà présent pour éviter les doublons
            nameAccount = nameAccount.replace("@heritage.africa", "");
            // Supprimer les espaces et mettre en minuscule
            nameAccount = nameAccount.trim().toLowerCase();

            // Validation supplémentaire
            if (!nameAccount.matches("^[a-zA-Z0-9._-]+$")) {
                throw new IllegalArgumentException("Invalid account name format");
            }
        }

        // Validation de l'email personnel
        if (personalEmail != null) {
            personalEmail = personalEmail.trim().toLowerCase();
            if (!personalEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid personal email format");
            }
        }
    }
}