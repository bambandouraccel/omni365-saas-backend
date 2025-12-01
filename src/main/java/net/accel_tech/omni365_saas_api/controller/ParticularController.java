package net.accel_tech.omni365_saas_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.dto.ApiResponse;
import net.accel_tech.omni365_saas_api.dto.ParticularFormRequest;
import net.accel_tech.omni365_saas_api.entity.ParticularForm;
import net.accel_tech.omni365_saas_api.exception.ResourceNotFoundException;
import net.accel_tech.omni365_saas_api.message.Message;
import net.accel_tech.omni365_saas_api.repository.ParticularFormRepository;
import net.accel_tech.omni365_saas_api.service.ParticularFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NdourBamba18
 **/

@RestController
@RequestMapping("/api/particulars")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ParticularController {

    private final ParticularFormService particularFormService;
    private final ParticularFormRepository particularFormRepository;

    /**
     * @url http://localhost:8080/api/particulars
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<ParticularForm>> submitParticularForm(
            @Valid @RequestBody ParticularFormRequest request) {

        // Convertir le DTO en Entity
        ParticularForm form = new ParticularForm();
        form.setFirstName(request.getFirstName());
        form.setLastName(request.getLastName());
        form.setPersonalEmail(request.getPersonalEmail());
        form.setPhoneNumber(request.getPhoneNumber());
        form.setMessage(request.getMessage());
        form.setNameAccount(request.getNameAccount());

        ParticularForm processedForm = particularFormService.processParticularForm(form);

        return ResponseEntity.ok(new ApiResponse<>(true, processedForm));
    }


    @GetMapping("")
    public ResponseEntity<?> findAllParticulars(){
        List<ParticularForm> list = particularFormRepository.findAll();
        list = list.stream()
                .sorted(Comparator.comparing(ParticularForm::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, list));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findParticularFormById(@PathVariable Long id){
        ParticularForm particularForm = particularById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, particularForm));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteParticularFormById(@PathVariable("id") Long id){
        particularFormRepository.delete(particularById(id));
        return ResponseEntity.ok(new ApiResponse<>(true, new Message("Object deleted successfully with id="+id)));
    }

    public ParticularForm particularById(Long id) {
        return particularFormRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Object not found with id:"+id));
    }
}
