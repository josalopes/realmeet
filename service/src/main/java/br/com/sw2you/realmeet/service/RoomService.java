package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.validator.RoomValidator;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomValidator roomValidator;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomValidator roomValidator, RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
        this.roomValidator = roomValidator;
        this.roomRepository = roomRepository;
    }

    public RoomDTO getRoom(Long id) {
        Room room = getActiveRoomOrThrow(id);
        return roomMapper.fromEntityToDto(room);
    }

    public RoomDTO createRoom(CreateRoomDTO createRoomDTO) {
        roomValidator.validate(createRoomDTO);
        var room = roomMapper.fromCreateRoomDtoToEntity(createRoomDTO);
        roomRepository.save(room);
        return roomMapper.fromEntityToDto(room);
    }

    @Transactional
    public void updateRoom(Long roomId, UpdateRoomDTO updateRoomDTO) {
        getActiveRoomOrThrow(roomId);
        roomValidator.validate(roomId, updateRoomDTO);
        roomRepository.updateRoom(roomId, updateRoomDTO.getName(), updateRoomDTO.getSeats());
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = getActiveRoomOrThrow(roomId);
        roomRepository.deactivate(roomId);
    }

    private Room getActiveRoomOrThrow(Long id) {
        Objects.requireNonNull(id);
        return roomRepository
            .findByIdAndActive(id, true)
            .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
    }
}
