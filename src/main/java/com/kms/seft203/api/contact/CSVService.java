package com.kms.seft203.api.contact;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.kms.seft203.domain.contact.Contact;
import com.kms.seft203.domain.contact.ContactRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CSVService {
  private final ContactRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Contact> tutorials = CSVHelper.csvToEntity(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<Contact> tutorials = repository.findAll();

    ByteArrayInputStream in = CSVHelper.entityToCSV(tutorials);
    return in;
  }

  public List<Contact> getAllTutorials() {
    return repository.findAll();
  }
}