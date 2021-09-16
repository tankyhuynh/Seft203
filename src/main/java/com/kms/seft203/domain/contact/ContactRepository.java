package com.kms.seft203.domain.contact;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, String>{
    
    List<Contact> findByFirstNameContaining(String firstName);
    Contact findOneByDepartment(String department);
    
}
