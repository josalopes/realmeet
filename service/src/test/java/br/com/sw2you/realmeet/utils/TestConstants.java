package br.com.sw2you.realmeet.utils;

import java.time.OffsetDateTime;

public class TestConstants {
    public static final long DEFAULT_ROOM_ID = 1L;
    public static final String DEFAULT_ROOM_NAME = "Room A";
    public static final int DEFAULT_ROOM_SEATS = 6;
    public static final long DEFAULT_ALLOCATION_ID = 1L;
    public static final String DEFAULT_ALLOCATION_SUBJECT = "Some subject";
    public static final String DEFAULT_EMPLOYEE_NAME = "John Doe";
    public static final String DEFAULT_EMPLOYEE_EMAIL = "john@email.com";
    public static final OffsetDateTime DEFAULT_ALLOCATION_START_AT = OffsetDateTime.now().plusDays(1);
    public static final OffsetDateTime DEFAULT_ALLOCATION_END_AT = DEFAULT_ALLOCATION_START_AT.plusDays(1);

    private TestConstants() {}
}
