package com.assign.organization.domain.contact.repository;

import com.assign.organization.domain.contact.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, String>, ContactRepositoryCustom {

}
