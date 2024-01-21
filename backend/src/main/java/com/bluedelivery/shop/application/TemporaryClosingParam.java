package com.bluedelivery.shop.application;

import java.time.LocalDate;

import com.bluedelivery.shop.domain.holiday.TemporaryHolidayPolicy;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TemporaryClosingParam {
    
    private LocalDate from;
    private LocalDate to;
    
    public TemporaryClosingParam(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) { // to가 시간 순서상 앞서는 경우, 순서를 반대로 값을 저장한다.
            this.from = to;
            this.to = from;
            return;
        }
    
        this.from = from;
        this.to = to;
    }
    
    public TemporaryHolidayPolicy toEntity() {
        return new TemporaryHolidayPolicy(this.from, this.to);
    }
    
    public LocalDate getFrom() {
        return from;
    }
    
    public LocalDate getTo() {
        return to;
    }
}
