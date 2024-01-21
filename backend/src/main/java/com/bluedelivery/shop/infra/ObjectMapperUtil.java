package com.bluedelivery.shop.infra;

import com.bluedelivery.shop.domain.holiday.HolidayPolicy;
import com.bluedelivery.shop.infra.holiday.HolidayPolicyMixIn;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ConstructorDetector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class ObjectMapperUtil {

    private static final ObjectMapper INSTANCE = JsonMapper.builder()
            .addMixIn(HolidayPolicy.class, HolidayPolicyMixIn.class)
            .addModule(new ParameterNamesModule())
            .addModule(new JavaTimeModule())
            .constructorDetector(ConstructorDetector.USE_PROPERTIES_BASED)
            .build();

    private ObjectMapperUtil() {

    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

}
