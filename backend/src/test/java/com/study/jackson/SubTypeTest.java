package com.study.jackson;

import com.bluedelivery.shop.domain.holiday.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubTypeTest {

    private ObjectMapper sut;

    @BeforeEach
    void setUp() {
        sut = JsonMapper.builder()
                .addMixIn(HolidayPolicy.class, HolidayPolicyExampleMixIn.class)
                .addModule(new ParameterNamesModule())
                .addModule(new JavaTimeModule())
                .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED).build();
    }

    @Test
    void write1() throws JsonProcessingException {
        String result = sut.writeValueAsString(new CyclicRegularHolidayPolicy(WeekCycle.LAST, DayOfWeek.MONDAY));

        assertThat(result).isEqualTo("{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"LAST\",\"dayOfWeek\":\"MONDAY\"}");
    }

    @Test
    void write2() throws JsonProcessingException {
        String result = sut.writeValueAsString(new TemporaryHolidayPolicy(LocalDate.MAX.minusDays(1), LocalDate.MAX));

        assertThat(result).isEqualTo("{\"@type\":\"TemporaryHolidayPolicy\",\"from\":[999999999,12,30],\"to\":[999999999,12,31]}");
    }

    @Test
    void write_list() throws JsonProcessingException {
        String result = sut.writerFor(new TypeReference<List<HolidayPolicy>>() {
        }).writeValueAsString(
                List.of(
                        new CyclicRegularHolidayPolicy(WeekCycle.FIRST, DayOfWeek.SUNDAY),
                        new CyclicRegularHolidayPolicy(WeekCycle.LAST, DayOfWeek.MONDAY)
                )
        );

        assertThat(result).isEqualTo("[{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"FIRST\",\"dayOfWeek\":\"SUNDAY\"},{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"LAST\",\"dayOfWeek\":\"MONDAY\"}]");
    }

    @Test
    void read1() throws JsonProcessingException {
        HolidayPolicy result = sut.readValue("{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"LAST\",\"dayOfWeek\":\"MONDAY\"}", HolidayPolicy.class);

        assertThat(result).isInstanceOf(CyclicRegularHolidayPolicy.class);
    }

    @Test
    void read2() throws JsonProcessingException {
        HolidayPolicy result = sut.readValue("{\"@type\":\"TemporaryHolidayPolicy\",\"from\":[999999999,12,30],\"to\":[999999999,12,31]}", HolidayPolicy.class);

        assertThat(result).isInstanceOf(TemporaryHolidayPolicy.class);
    }

    @Test
    void read_list() throws JsonProcessingException {
        HolidayPolicy[] results = sut.readValue("[{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"FIRST\",\"dayOfWeek\":\"SUNDAY\"},{\"@type\":\"CyclicRegularHolidayPolicy\",\"weekCycle\":\"LAST\",\"dayOfWeek\":\"MONDAY\"},{\"@type\":\"TemporaryHolidayPolicy\",\"from\":[999999999,12,30],\"to\":[999999999,12,31]}]", HolidayPolicy[].class);

        assertThat(results).hasSize(3);
        assertThat(results[0]).isInstanceOf(CyclicRegularHolidayPolicy.class);
        assertThat(results[1]).isInstanceOf(CyclicRegularHolidayPolicy.class);
        assertThat(results[2]).isInstanceOf(TemporaryHolidayPolicy.class);
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            defaultImpl = Void.class)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = CyclicRegularHolidayPolicy.class),
            @JsonSubTypes.Type(value = LegalHolidayPolicy.class),
            @JsonSubTypes.Type(value = TemporaryHolidayPolicy.class)
    })
    interface HolidayPolicyExampleMixIn {

    }

}
