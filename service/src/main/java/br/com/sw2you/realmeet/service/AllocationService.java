package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.*;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.AllocationCannotBeDeletedException;
import br.com.sw2you.realmeet.exception.AllocationNotFoundException;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final RoomRepository roomRepository;
    private final AllocationValidator allocationValidator;
    private final AllocationMapper allocationMapper;

    public AllocationService(
        AllocationRepository allocationRepository,
        RoomRepository roomRepository,
        AllocationValidator allocationValidator,
        AllocationMapper allocationMapper
    ) {
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationValidator = allocationValidator;
        this.allocationRepository = allocationRepository;
    }

    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        var room = roomRepository.findById(createAllocationDTO.getRoomId()).orElseThrow(() -> new RoomNotFoundException("Room not found: " + createAllocationDTO.getRoomId()));
        allocationValidator.validate(createAllocationDTO);

        var allocation = allocationMapper.fromCreateAllocationDTOToEntity(createAllocationDTO, room);
        allocationRepository.save(allocation);
        return allocationMapper.fromEntityToAllocationDTO(allocation);
    }

    public void deleteAllocation(Long allocationId) {
        var allocation = allocationRepository
            .findById(allocationId)
            .orElseThrow(() -> new AllocationNotFoundException("Allocation not found: " + allocationId));

        if (allocation.getEndAt().isBefore(DateUtils.now())) {
            throw new AllocationCannotBeDeletedException();
        }
        allocationRepository.delete(allocation);
    }


}
