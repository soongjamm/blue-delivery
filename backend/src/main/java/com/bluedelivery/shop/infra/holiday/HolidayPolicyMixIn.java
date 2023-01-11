package com.bluedelivery.shop.infra.holiday;

import com.bluedelivery.shop.domain.holiday.CyclicRegularHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.LegalHolidayPolicy;
import com.bluedelivery.shop.domain.holiday.TemporaryHolidayPolicy;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        defaultImpl = Void.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CyclicRegularHolidayPolicy.class),
        @JsonSubTypes.Type(value = LegalHolidayPolicy.class),
        @JsonSubTypes.Type(value = TemporaryHolidayPolicy.class)
})
public interface HolidayPolicyMixIn {

}
