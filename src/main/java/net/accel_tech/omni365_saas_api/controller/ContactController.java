package net.accel_tech.omni365_saas_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.accel_tech.omni365_saas_api.dto.ApiResponse;
import net.accel_tech.omni365_saas_api.dto.ContactFormRequest;
import net.accel_tech.omni365_saas_api.entity.ContactForm;
import net.accel_tech.omni365_saas_api.exception.ResourceNotFoundException;
import net.accel_tech.omni365_saas_api.message.Message;
import net.accel_tech.omni365_saas_api.repository.ContactFormRepository;
import net.accel_tech.omni365_saas_api.service.ContactFormService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author NdourBamba18
 **/

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactController {

	private final ContactFormRepository contactFormRepository;
	private final ContactFormService contactFormService;


	/**
	 * @url http://localhost:8080/api/contacts
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> submitContactForm(@Valid @RequestBody ContactFormRequest request) {

		// Convert from DTO to Entity
		ContactForm form = new ContactForm();
		form.setEmail(request.getEmail());
		form.setPhoneNumber(request.getPhoneNumber());
		form.setFullName(request.getFullName());
		form.setMessage(request.getMessage());
		form.setAccountList(request.getAccountList());
		form.setAccountNumber(request.getAccountNumber());
		form.setDomainName(request.getDomainName());
		form.setEnterpriseName(request.getEnterpriseName());
		form.setCreatedAt(new Date());

		ContactForm contactForm = contactFormService.processContactForm(form);

		return new ResponseEntity<>(
				new ApiResponse<>(true, contactForm),
				HttpStatus.CREATED
		);
	}


	@GetMapping("")
	public ResponseEntity<?> findAllContacts(){
		List<ContactForm> list = contactFormRepository.findAll();
		list = list.stream()
				.sorted(Comparator.comparing(ContactForm::getCreatedAt).reversed())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new ApiResponse<>(true, list));

	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<?> findContactFormById(@PathVariable Long id){
		ContactForm contactForm = contactFormById(id);
		return ResponseEntity.ok(new ApiResponse<>(true, contactForm));
	}

	@DeleteMapping(path = "/{id}")
	public ResponseEntity<?> deleteContactFormById(@PathVariable("id") Long id){
		contactFormRepository.delete(contactFormById(id));
		return ResponseEntity.ok(new ApiResponse<>(true, new Message("Object deleted successfully with id="+id)));
	}

	public ContactForm contactFormById(Long id) {
		return contactFormRepository.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Object not found with id:"+id));
	}
}
