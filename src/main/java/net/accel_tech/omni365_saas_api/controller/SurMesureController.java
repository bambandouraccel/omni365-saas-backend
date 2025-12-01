package net.accel_tech.omni365_saas_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.dto.ApiResponse;
import net.accel_tech.omni365_saas_api.entity.SurmesureForm;
import net.accel_tech.omni365_saas_api.exception.ResourceNotFoundException;
import net.accel_tech.omni365_saas_api.message.Message;
import net.accel_tech.omni365_saas_api.repository.SurMesureFormRepository;
import net.accel_tech.omni365_saas_api.service.SurMesureFormService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NdourBamba18
 **/

@RestController
@RequestMapping("/api/surmesures")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SurMesureController {

    private final SurMesureFormService surMesureFormService;
    private final SurMesureFormRepository surMesureFormRepository;

    /**
     * @url http://localhost:8080/api/surmesures
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody SurmesureForm surmesureForm) {

        SurmesureForm processedForm = surMesureFormService.processSurMesureForm(surmesureForm);

        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        new Message("Demande sur mesure enregistrée et emails envoyés avec succès")),
                HttpStatus.CREATED
        );
    }


    @GetMapping("")
    public ResponseEntity<?> findAllSurMesureForms(){
        List<SurmesureForm> list = surMesureFormRepository.findAll();
        list = list.stream()
                .sorted(Comparator.comparing(SurmesureForm::getCreatedAt).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, list));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findSurMesureFormById(@PathVariable Long id){
        SurmesureForm surmesureForm = surMesureFormById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, surmesureForm));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteSureMesureFormById(@PathVariable("id") Long id){
        surMesureFormRepository.delete(surMesureFormById(id));
        return ResponseEntity.ok(new ApiResponse<>(true, new Message("Object deleted successfully with id="+id)));
    }

    public SurmesureForm surMesureFormById(Long id) {
        return surMesureFormRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Object not found with id:"+id));
    }
}
