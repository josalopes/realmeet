package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.validator.RoomValidator;
import java.util.Objects;
import org.springframework.stereotype.Service;

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
        Objects.requireNonNull(id);
        Room room = roomRepository
            .findByIdAndActive(id, true)
            .orElseThrow(() -> new RoomNotFoundException("Room not found: " + id));
        return roomMapper.fromEntityToDto(room);
    }

    public RoomDTO createRoom(CreateRoomDTO createRoomDTO) {
        roomValidator.validate(createRoomDTO);
        var room = roomMapper.fromCreateRoomDtoToEntity(createRoomDTO);
        roomRepository.save(room);
        return roomMapper.fromEntityToDto(room);
    }
}