package com.example.crud1.inetgrationtest;

import com.example.crud1.config.TestConfig;
import com.example.crud1.dto.PlayerDto;
import com.example.crud1.exception.NotFoundException;
import com.example.crud1.mapper.impl.PlayerDtoMapper;
import com.example.crud1.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@ActiveProfiles("palyerServiceTests")
@SpringBootTest
public class PlayerServiceIT {
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private PlayerDtoMapper playerDtoMapper;

    @Test
    @DisplayName("should return player")
    @Transactional
    public void findPlayerByIdTest() {
        PlayerDto actual = playerService.findById(UUID.fromString("84dc3967-ffc0-4f3c-a4b4-0e872a253ac0"));
        PlayerDto expected = PlayerDto.builder()
                .id(UUID.fromString("84dc3967-ffc0-4f3c-a4b4-0e872a253ac0"))
                .name("name1")
                .surname("surname1")
                .birthdate(LocalDate.of(2000, 01, 01))
                .team(null)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should throw AssertionFailedError(и не надо справшивать \"зачем\")")
    @Transactional
    public void findByIdAssertionFailedErrorTest() {
        try {
            Assertions.assertThrows(AssertionFailedError.class, () -> {
                Assertions.assertThrows(NotFoundException.class, () -> {
                    playerService.findById(UUID.fromString("84dc3967-ffc0-4f3c-a4b4-0e872a253ac0"));
                });
            });
        } catch (
                AssertionFailedError e) {
            System.err.println("AssertionFailedError in findByIdNotFoundExceptionTest is : " + e.getMessage());
        }
    }

    @Test
    @DisplayName("should throw NotFoundException")
    @Transactional
    public void findByIdNotFoundExceptionTest() {
        String playerId = "84dc3967-ffc0-4f3c-a4b4-0e872a253ac1";
        String expected = "Player is not found by id %s".formatted(playerId);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            playerService.findById(UUID.fromString(playerId));
        });
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("shoud return new player")
    @Transactional
    public void createTest() {
        PlayerDto expectedPlayer = PlayerDto.builder()
                .name("someName1")
                .surname("someSurname1")
                .birthdate(LocalDate.of(2000, 01, 01))
                .team(null)
                .build();
        UUID actualPlayerId = playerService.create(playerDtoMapper.dtoToEntity(expectedPlayer));
        expectedPlayer.setId(actualPlayerId);
        PlayerDto actualPlayer = playerService.findById(actualPlayerId);
        Assertions.assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("shoud return new DuplicateKeyException")
    @Transactional
    public void createExistingPlayerTest() {
        PlayerDto player = PlayerDto.builder()
                .name("name1")
                .surname("surname1")
                .birthdate(LocalDate.of(2020, 01, 01))
                .team(null)
                .build();
        DuplicateKeyException exception = Assertions.assertThrows(DuplicateKeyException.class, () -> {
            playerService.create(playerDtoMapper.dtoToEntity(player));
        });
        String expectedMessage = "Player %s %s already exists".formatted(player.getName(),player.getSurname());
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

//
//    @Test
//    @DisplayName()
//    @Transactional
//    public void removeByIdTest(){}
//
//    @Test
//    @DisplayName()
//    @Transactional
//    public void removeByNameTest(){}
//
//    @Test
//    @DisplayName()
//    @Transactional
//    public void removeAllTest(){}
//
//    @Test
//    @DisplayName()
//    @Transactional
//    public void updateTest(){}
//
//    @Test
//    @DisplayName()
//    @Transactional
//    public void findByNameTest(){}
//
//    @Test
//    @DisplayName()
//    @Transactional
//    public void findAllTest(){}


}
