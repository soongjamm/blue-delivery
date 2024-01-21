package com.bluedelivery.shop.interfaces.adapter;

import static com.bluedelivery.common.response.HttpResponse.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.bluedelivery.category.application.CategoryNotFoundException;
import com.bluedelivery.common.response.ApiException;
import com.bluedelivery.common.response.ErrorCode;
import com.bluedelivery.common.response.HttpResponse;
import com.bluedelivery.shop.application.ShopUpdateService;
import com.bluedelivery.shop.application.dto.UpdateDeliveryAreaTarget;
import com.bluedelivery.shop.domain.BusinessHour;
import com.bluedelivery.shop.domain.DeliveryArea;
import com.bluedelivery.shop.interfaces.EditPhoneRequest;
import com.bluedelivery.shop.interfaces.ShopUpdateController;
import com.bluedelivery.shop.interfaces.SuspensionRequest;
import com.bluedelivery.shop.interfaces.UpdateCategoryRequest;
import com.bluedelivery.shop.interfaces.UpdateClosingDaysRequest;
import com.bluedelivery.shop.interfaces.dto.BusinessHoursRequest;
import com.bluedelivery.shop.interfaces.dto.DeliveryAreaResponse;
import com.bluedelivery.shop.interfaces.dto.UpdateDeliveryAreaRequest;

@RestController
public class ShopUpdateControllerImpl implements ShopUpdateController {
    
    private ShopUpdateService updateService;
    
    public ShopUpdateControllerImpl(ShopUpdateService updateService) {
        this.updateService = updateService;
    }
    
    public ResponseEntity<HttpResponse<?>> updateBusinessHours(Long shopId, BusinessHoursRequest dto) {
        try {
            List<BusinessHour> businessHours = updateService.updateBusinessHour(dto.toTarget(shopId));
            return ResponseEntity.ok(response(businessHours));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response(FAIL, ex.getMessage()));
        }
    }
    
    public void editIntroduce(Long id, String introduce) {
        updateService.editIntroduce(id, introduce);
    }
    
    public void editPhoneNumber(Long id, EditPhoneRequest dto) {
        updateService.editPhoneNumber(id, dto.getPhone());
    }
    
    public void editDeliveryAreaGuid(Long id, String guide) {
        updateService.editDeliveryAreaGuide(id, guide);
    }
    
    public void rename(Long id, String name) {
        updateService.rename(id, name);
    }
    
    public ResponseEntity<HttpResponse<?>> updateCategory(Long shopId, UpdateCategoryRequest dto) {
        try {
            updateService.updateCategory(shopId, dto);
            return ResponseEntity.ok(response("successfully updated"));
        } catch (CategoryNotFoundException ex) {
            throw new ApiException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
    
    public void updateClosingDays(Long shopId, UpdateClosingDaysRequest closingDays) {
        updateService.updateClosingDays(shopId, closingDays);
    }
    
    public void setExpose(Long shopId, Boolean expose) {
        updateService.expose(shopId, expose);
    }
    
    public void suspendShop(Long shopId, SuspensionRequest request) {
        updateService.suspend(shopId, request.getPeriod());
    }
    
    @Override
    public ResponseEntity<HttpResponse<?>> updateDeliveryArea(Long shopId, UpdateDeliveryAreaRequest dto) {
        try {
            List<DeliveryArea> deliveryAreas =
                    updateService.updateDeliveryArea(UpdateDeliveryAreaTarget.of(shopId, dto.getTownCodes()));
            return ResponseEntity.ok(response(SUCCESS, DeliveryAreaResponse.of(shopId, deliveryAreas)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response(FAIL, ex.getMessage()));
        }
    }
    
}
