package br.com.sw2you.realmeet.domain.repository;

import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    Optional<Room> findByIdAndActive(Long id, Boolean active);

    Optional<Room> findByNameAndActive(String name, Boolean active);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Room r SET r.active = false WHERE r.id = :roomId")
    void deactivate(@Param("roomId") Long roomId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Room r SET r.name = :name, r.seats = :seats WHERE r.id = :roomId")
    void updateRoom(@Param("roomId") Long roomId, @Param("roomId") String name, @Param("roomId") Integer seats);
}
