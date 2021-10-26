package br.com.sw2you.realmeet.utils;

import static br.com.sw2you.realmeet.domain.entity.Room.*;
import static br.com.sw2you.realmeet.utils.TestConstants.*;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;

public class TestDataCreator {

    private TestDataCreator() {}

    public static Builder newRoomBuilder() {
        return newBuilder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateRoomDTO newCreateRoomDTO() {
        return new CreateRoomDTO().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }
}
