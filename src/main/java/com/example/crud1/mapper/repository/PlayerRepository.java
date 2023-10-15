package com.example.crud1.mapper.repository;

import com.example.crud1.entity.Player;
import com.example.crud1.entity.PlayerPrimaryKey;
import com.example.crud1.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PlayerRepository extends JpaRepository<Player, PlayerPrimaryKey> {

    @Query("from Player")
    List<Player> getAll(@Param("name") String name,
                        @Param("surname") String surname,
                        @Param("birthdate") LocalDate birthdate,
                        @Param("team") Team team);

    Optional<Player> findByNameAndSurname(String name, String surname);

    @Query("select p from  Player p where p.id = :id")
    Optional<Player> findById(UUID id);

    List<Optional<Player>> findByName(String name);

    List<Optional<Player>> findByTeam(Team team);

    List<Player> deleteByName(String name);

    @Modifying
    @Query("delete from Player p where p.name = :name")
    void removeByName(@Param("name") String name);

    @Modifying
    @Query("delete from Player p where p.id = :id")
    void deleteById(UUID id);
}
