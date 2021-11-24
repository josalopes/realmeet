package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.utils.TestDataCreator;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ALLOCATION_ID;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ALLOCATION_SUBJECT;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newUpdateAllocationDTO;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllocationUpdateValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenAllocationIsValid() {
        victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO());
    }

    @Test
    void testValidateWhenAllocationIdIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(null, TestDataCreator.newUpdateAllocationDTO())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_ID, ALLOCATION_ID + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationSubjectIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, TestDataCreator.newUpdateAllocationDTO().subject(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    @DisplayName("Test should not pass when subject exceeds its maximum length")
    void testValidateWhenAllocationSubjectExceedsMaxLength() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    TestDataCreator.newUpdateAllocationDTO().subject(rightPad("X", ALLOCATION_SUBJECT_MAX_LENGTH + 1, "X"))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationStartAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, TestDataCreator.newUpdateAllocationDTO().startAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSING),
                exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenAllocationEndAtIsMissing() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, TestDataCreator.newUpdateAllocationDTO().endAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSING),
                exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateSequenceIsInvalid() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    TestDataCreator.newUpdateAllocationDTO()
                        .startAt(DateUtils.now().plusDays(1))
                        .endAt(DateUtils.now().plusDays(1).minusMinutes(30)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT),
            exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateIsInThePast() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    TestDataCreator.newUpdateAllocationDTO()
                        .startAt(DateUtils.now().minusMinutes(30))
                        .endAt(DateUtils.now().plusMinutes(30)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST),
                exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateIntervalExceedMaxDuration() {
        var exception = Assertions.assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, TestDataCreator.newUpdateAllocationDTO().startAt(DateUtils.now().plusDays(1)).endAt(DateUtils.now().plusDays(1).plusSeconds(ALLOCATION_MAX_DURATION_SECONDS + 1)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION),
                exception.getValidationErrors().getError(0));
    }

}
