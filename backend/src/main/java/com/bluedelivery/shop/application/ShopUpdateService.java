package com.bluedelivery.shop.application;

import static com.bluedelivery.common.response.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import com.bluedelivery.shop.domain.holiday.LegalHolidayPolicy;
import com.bluedelivery.shop.interfaces.SuspensionPeriod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluedelivery.category.application.CategoryManagerService;
import com.bluedelivery.common.response.ApiException;
import com.bluedelivery.shop.application.businesshour.DayOfWeekMapper;
import com.bluedelivery.shop.application.dto.BusinessHoursTarget;
import com.bluedelivery.shop.application.dto.UpdateDeliveryAreaTarget;
import com.bluedelivery.shop.domain.BusinessHour;
import com.bluedelivery.shop.domain.DeliveryArea;
import com.bluedelivery.shop.domain.Shop;
import com.bluedelivery.shop.domain.ShopRepository;
import com.bluedelivery.shop.interfaces.UpdateCategoryRequest;
import com.bluedelivery.shop.interfaces.UpdateClosingDaysRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ShopUpdateService {
    
    private final ShopRepository shopRepository;
    private final CategoryManagerService categoryManagerService;
    private final AddressMapper addressMapper;
    private final DayOfWeekMapper dayOfWeekMapper;
    
    public List<BusinessHour> updateBusinessHour(BusinessHoursTarget target) {
        Shop shop = getShop(target.getShopId());
        shop.updateBusinessHours(dayOfWeekMapper.map(target.getBusinessHourType(), target.getBusinessHours()));
        return shop.getBusinessHours();
    }
    
    public void editIntroduce(Long id, String introduce) {
        Shop shop = getShop(id);
        shop.editIntroduce(introduce);
    }
    
    public void editPhoneNumber(Long id, String phone) {
        Shop shop = getShop(id);
        shop.editPhoneNumber(phone);
    }
    
    public void editDeliveryAreaGuide(Long id, String guide) {
        Shop shop = getShop(id);
        shop.editDeliveryAreaGuide(guide);
    }
    
    public void rename(Long id, String name) {
        Shop shop = getShop(id);
        shop.rename(name);
    }
    
    public void updateCategory(Long shopId, UpdateCategoryRequest dto) {
        Shop shop = getShop(shopId);
        shop.updateCategoryIds(categoryManagerService.getCategoriesById(dto.getCategoryIds()));
    }
    
    public void updateClosingDays(Long id, UpdateClosingDaysRequest closingDays) {
        Boolean closingOnLegalHolidays = closingDays.getLegalHolidays();
        List<TemporaryClosingParam> temporaries = closingDays.getTemporaryClosing();
        List<RegularClosingParam> regulars = closingDays.getRegularClosing();
        Shop shop = getShop(id);
        
        if (Boolean.TRUE.equals(closingOnLegalHolidays)) {
            shop.addClosingDayPolicy(new LegalHolidayPolicy());
        }
        temporaries.forEach(temporary -> shop.addClosingDayPolicy( temporary.toEntity()));
        regulars.forEach(regular -> shop.addClosingDayPolicy(regular.toEntity()));
    }
    
    public void expose(Long shopId, Boolean status) {
        Shop shop = getShop(shopId);
        shop.updateExposeStatus(status);
    }
    
    public void suspend(Long shopId, SuspensionPeriod suspensionPeriod) {
        Shop shop = getShop(shopId);
        shop.suspendUntil(suspensionPeriod.from(LocalDateTime.now()));
    }
    
    @Transactional
    public List<DeliveryArea> updateDeliveryArea(UpdateDeliveryAreaTarget target) {
        Shop shop = getShop(target.getShopId());
        List<DeliveryArea> areas = addressMapper.deliveryAreas(target.getTownCodes());
        shop.updateDeliveryArea(areas);
        return areas;
    }
    
    private Shop getShop(Long id) {
        return shopRepository.findById(id).orElseThrow(() -> new ApiException(SHOP_DOES_NOT_EXIST));
    }
}
