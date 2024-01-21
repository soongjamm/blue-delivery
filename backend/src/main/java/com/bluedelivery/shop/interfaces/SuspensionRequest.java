package com.bluedelivery.shop.interfaces;

import lombok.Getter;

@Getter
public class SuspensionRequest {

    private SuspensionPeriod period;
    
    public SuspensionRequest(SuspensionPeriod period) {
        this.period = period;
    }
}
