package com.kms.seft203.api.contact;

import static com.kms.seft203.utils.UrlConstraint.CONTACTS_DELETE_URL;
import static com.kms.seft203.utils.UrlConstraint.CONTACTS_DOWNLOAD_URL;
import static com.kms.seft203.utils.UrlConstraint.CONTACTS_GET_URL;
import static com.kms.seft203.utils.UrlConstraint.CONTACTS_PUT_URL;
import static com.kms.seft203.utils.UrlConstraint.CONTACTS_SEARCH_URL;
import static com.kms.seft203.utils.UrlConstraint.CONTACTS_UPLOAD_URL;
import static com.kms.seft203.utils.UrlConstraint.CONTACTS_URL;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.kms.seft203.domain.contact.Contact;
import com.kms.seft203.domain.contact.ContactRepository;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(CONTACTS_URL)
@RequiredArgsConstructor
public class ContactApi {
    private final ContactRepository contactRepository;
    private final CSVService fileService;

    @GetMapping
    public ResponseEntity<List<Contact>> getAll() {
        return ResponseEntity.ok(contactRepository.findAll());
    }

    @GetMapping(CONTACTS_GET_URL)
    public ResponseEntity<Contact> getOne(@PathVariable String id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            return ResponseEntity.ok(contact);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Contact> create(@Valid @RequestBody SaveContactRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            Contact contact = convert(request);
            return ResponseEntity.ok(contactRepository.save(contact));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(CONTACTS_PUT_URL)
    public ResponseEntity<Contact> update(@PathVariable String id, @RequestBody SaveContactRequest request) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = convert(request);
            contact.setId(id);
            return ResponseEntity.ok(contactRepository.save(contact));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping(CONTACTS_DELETE_URL)
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            contactRepository.delete(contactOptional.get());
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping(CONTACTS_UPLOAD_URL)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (CSVHelper.hasCSVFormat(file)) {

            try {
                fileService.save(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping(CONTACTS_DOWNLOAD_URL)
    public ResponseEntity<Resource> getFile() {
        String filename = "contacts.csv";
        InputStreamResource file = new InputStreamResource(fileService.load());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv")).body(file);
    }

    @GetMapping(CONTACTS_SEARCH_URL)
    public ResponseEntity<List<Contact>> search(@RequestParam("keyword") String keyword) {
        List<Contact> contacts = contactRepository.findByFirstNameContaining(keyword);

        return ResponseEntity.ok(contacts);
    }

    public Contact convert(SaveContactRequest request) {
        Contact contact = new Contact();
        contact.setTitle(request.getTitle());
        contact.setProject(request.getProject());
        contact.setLastName(request.getLastName());
        contact.setFirstName(request.getLastName());
        contact.setEmployeeId(request.getEmployeeId());
        contact.setDepartment(request.getDepartment());
        contact.setAvatar(request.getDepartment());

        return contact;
    }

}
