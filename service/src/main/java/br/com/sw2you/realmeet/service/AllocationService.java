package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.*;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final RoomRepository roomRepository;
    private final AllocationMapper allocationMapper;

    public AllocationService(
        AllocationRepository allocationRepository,
        RoomRepository roomRepository,
        AllocationMapper allocationMapper
    ) {
        this.allocationMapper = allocationMapper;
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
    }

    public AllocationDTO createAllocation(CreateAllocationDTO createAllocationDTO) {
        return null;
    }


}
