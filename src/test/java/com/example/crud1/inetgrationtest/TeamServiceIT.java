package com.example.crud1.inetgrationtest;

import com.example.crud1.config.TestConfig;
import com.example.crud1.exception.NotFoundException;
import com.example.crud1.mapper.impl.TeamDtoMapper;
import com.example.crud1.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("teamServiceTests")
public class TeamServiceIT {

    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private TeamDtoMapper teamDtoMapper;

    @Test
    @DisplayName("should throw NotFoundException")
    @Transactional
    public void findByIdNotFoundExceptionTest() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            teamService.findById(UUID.fromString("84dc3967-ffc0-4f3c-a4b4-0e872a253ac1"));
        });
    }
}
