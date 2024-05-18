package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.history.ContactHistoryDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.model.ContactMetric;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.ContactMetricRepository;
import com.itesm.panoptimize.repository.ContactRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final ContactRepository contactRepository;
    @Autowired
    public HistoryService(ContactRepository contactRepository, UserRepository userRepository,
                          ContactMetricRepository contactMetricRepository) {
        this.contactRepository = contactRepository;
    }

    public List<ContactHistoryDTO> getContactHistory() {
        List<Contact> contacts = contactRepository.findAll();



        return results;
    }

    private ContactHistoryDTO convertToContactHistoryDTO(Contact contact, User user){
        ContactHistoryDTO contactHistoryDTO = new ContactHistoryDTO();

        contactHistoryDTO.setContact_id(contact.getId());
        contactHistoryDTO.setAgent_id(contact.getId());
        contactHistoryDTO.setSatisfaction(contact.getSatisfaction());
        contactHistoryDTO.setResolution_status(contact.getResolutionStatus());

        contactHistoryDTO.setStart_time(contact.getStartTime());
        contactHistoryDTO.setEnd_time(contact.getEndTime());

        Duration duration = Duration.between(contactHistoryDTO.getStart_time(), contactHistoryDTO.getEnd_time());
        contactHistoryDTO.setDuration(duration.getSeconds());

        contactHistoryDTO.setAgent_name(user.getFullName());

        return contactHistoryDTO;
    }

    private void getContactDetails(){

    }

}
