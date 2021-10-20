package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilder;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import br.com.sw2you.realmeet.validator.RoomValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class RoomValidatorUnitTest extends BaseUnitTest {
    private RoomValidator victim;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setupEach() {
        victim = new RoomValidator(roomRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(TestDataCreator.newCreateRoomDTO());
    }

    @Test
    void testValidateWhenRoomNameIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().name(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    @DisplayName("Test should not pass when name exceeds its maximum length")
    void testValidateWhenRoomNameExceedsMaxLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    TestDataCreator.newCreateRoomDTO().name(StringUtils.rightPad("X", ROOM_NAME_MAX_LENGTH + 1, "X"))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().seats(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreLessThanMinValue() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().seats(ROOM_SEATS_MIN_VALUE - 1))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + BELOW_MIN_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreGreaterThanMaxValue() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO().seats(ROOM_SEATS_MAX_VALUE + 1))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + EXCEEDS_MAX_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomNameIsDuplicated() {
        given(roomRepository.findByNameAndActive(DEFAULT_ROOM_NAME, true))
            .willReturn(Optional.of(newRoomBuilder().build()));
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(TestDataCreator.newCreateRoomDTO())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + DUPLICATE),
            exception.getValidationErrors().getError(0)
        );
    }
}
