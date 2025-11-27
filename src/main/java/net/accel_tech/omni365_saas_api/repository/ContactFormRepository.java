package net.accel_tech.omni365_saas_api.repository;

import net.accel_tech.omni365_saas_api.entity.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author NdourBamba18
 **/

@Repository
public interface ContactFormRepository extends JpaRepository<ContactForm, Long> {
}
