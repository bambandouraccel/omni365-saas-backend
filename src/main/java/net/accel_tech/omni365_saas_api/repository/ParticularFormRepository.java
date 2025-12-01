package net.accel_tech.omni365_saas_api.repository;

import net.accel_tech.omni365_saas_api.entity.ParticularForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author NdourBamba18
 **/

@Repository
public interface ParticularFormRepository extends JpaRepository<ParticularForm, Long> {

    boolean existsByPersonalEmail(String personalEmail);

    boolean existsByNameAccount(String nameAccount);

    @Query("SELECT p FROM ParticularForm p WHERE LOWER(p.nameAccount) = LOWER(:nameAccount)")
    ParticularForm findByAccountNameIgnoreCase(@Param("nameAccount") String nameAccount);

    @Query("SELECT p FROM ParticularForm p WHERE LOWER(p.personalEmail) = LOWER(:email)")
    ParticularForm findByPersonalEmailIgnoreCase(@Param("email") String email);
}