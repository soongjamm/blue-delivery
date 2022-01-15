package com.bluedelivery.shop.interfaces.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateDeliveryAreaRequest {
    
    private List<String> townCodes = new ArrayList<>();
    
    public UpdateDeliveryAreaRequest(List<String> townCodes) {
        this.townCodes.addAll(townCodes);
    }
}
