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
@Table(name = "contact_forms")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class ContactForm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full Name field is required!")
    @Size(min = 2, max = 100)
    @Column(name = "fullName")
    private String fullName;

    @NotBlank(message = "Email field is required!")
    @Size(max = 180)
    @Email(message = "Please, enter a valid email.")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Phone Number field is required!")
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

    @NotBlank(message = "Domain Name field is required!")
    @Size(min = 2, max = 100)
    @Column(name = "domainName")
    private String domainName;

    @Max(100)
    @Min(1)
    @Column(name = "accountNumber")
    private Integer accountNumber;

    @NotBlank(message = "Account List field is required!")
    @Size(min = 2, max = 500)
    @Column(name = "accountList")
    private String accountList;

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private Date createdAt;

}
