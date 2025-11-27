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
@Table(name = "surmesure_forms")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class SurmesureForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name field is required!")
    @Size(min = 2, max = 100)
    @Column(name = "fullName")
    private String fullName;

    @NotBlank(message = "Email field is required!")
    @Size(max = 180)
    @Email(message = "Please, enter a valid email.")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Phone field is required!")
    @Size(max = 20)
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @NotBlank(message = "Message field is required!")
    @Size(max = 300)
    @Column(name = "message")
    private String message;

    @NotBlank(message = "Enterprise Name field is required!")
    @Size(min = 2, max = 100)
    @Column(name = "enterpriseName")
    private String enterpriseName;

    @Max(100)
    @Min(1)
    @Column(name = "accountNumber")
    private Integer accountNumber;

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private Date createdAt;

}
