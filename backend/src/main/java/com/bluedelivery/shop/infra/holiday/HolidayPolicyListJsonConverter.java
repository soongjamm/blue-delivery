package com.bluedelivery.shop.infra.holiday;

import com.bluedelivery.shop.domain.holiday.HolidayPolicy;
import com.bluedelivery.shop.infra.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter
public class HolidayPolicyListJsonConverter implements AttributeConverter<List<HolidayPolicy>, String> {
    @Override
    public String convertToDatabaseColumn(List<HolidayPolicy> attribute) {
        ObjectMapper mapper = ObjectMapperUtil.getInstance();

        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        try {
            return mapper.writerFor(new TypeReference<List<HolidayPolicy>>() {
            }).writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("# Failed to convert to DB Column = " + e);
        }
    }

    @Override
    public List<HolidayPolicy> convertToEntityAttribute(String dbData) {
        ObjectMapper mapper = ObjectMapperUtil.getInstance();

        try {
            if (StringUtils.isBlank(dbData)) {
                return Collections.emptyList();
            }
            return Arrays.asList(mapper.readValue(dbData, HolidayPolicy[].class));
        } catch (IOException e) {
            throw new IllegalArgumentException("# Failed to convert to Entity Attribute = " + e);
        }
    }
}
