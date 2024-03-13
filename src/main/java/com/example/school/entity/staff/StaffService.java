package com.example.school.entity.staff;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {

    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);
    private final StaffRepository staffRepository;

    public void deleteStaff(Long id) {
        if (id == null) {
            logger.error("Given id is null. Termination of operation");
            throw new NullPointerException("Given id is null");
        }
        staffRepository.deleteById(id);
    }

    public void saveStaff(Staff staff) {
        if (staff == null) {
            logger.error("Given staff is null. Termination of operation.");
            throw new NullPointerException("Given staff is null");
        }

        logger.info("Staff saved, id: {}", staffRepository.save(staff).getId());
    }
}
