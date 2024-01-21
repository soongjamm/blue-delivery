package com.bluedelivery.shop.domain;

import com.bluedelivery.category.domain.Category;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.shop.domain.holiday.HolidayPolicy;
import com.bluedelivery.shop.infra.holiday.HolidayPolicyListJsonConverter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

import static com.bluedelivery.order.domain.ExceptionMessage.ORDERED_AMOUNT_LOWER_THAN_MINIMUM_ORDER_AMOUNT;
import static com.bluedelivery.order.domain.ExceptionMessage.SHOP_IS_NOT_OPEN;

@Table(name = "shop")
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOP_ID")
    private Long id;
    private String name;
    private String introduce;
    private String phone;
    private String deliveryAreaGuide;
    private int minimumOrderAmount;

    @ElementCollection
    @CollectionTable(name = "DELIVERY_AREA", joinColumns = @JoinColumn(name = "SHOP_ID"))
    private List<DeliveryArea> deliveryAreas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "BUSINESS_HOUR", joinColumns = @JoinColumn(name = "SHOP_ID"))
    private List<BusinessHour> businessHours = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "SHOP_CATEGORY", joinColumns = @JoinColumn(name = "SHOP_ID"))
    private List<Long> categoryIds = new ArrayList<>();

    @Column(name = "holiday_policies", columnDefinition = "json")
    @Convert(converter = HolidayPolicyListJsonConverter.class)
    private List<HolidayPolicy> holidayPolicies = new ArrayList<>();

    @Column(name = "suspension_until")
    private LocalDateTime suspensionUntil = LocalDateTime.now();
    private boolean exposed;

    public Shop() {
    }

    @Builder
    public Shop(Long id, String name, String introduce, String phone, String deliveryAreaGuide, int minimumOrderAmount,
                List<BusinessHour> businessHours, List<Long> categoryIds, List<HolidayPolicy> holidayPolicies,
                boolean exposed) {
        this.id = id;
        this.name = name;
        this.introduce = introduce;
        this.phone = phone;
        this.deliveryAreaGuide = deliveryAreaGuide;
        this.minimumOrderAmount = minimumOrderAmount;
        this.businessHours = businessHours;
        this.categoryIds = categoryIds;
        this.holidayPolicies = holidayPolicies;
        this.exposed = exposed;
    }

    public void updateBusinessHours(List<BusinessHour> input) {
        this.businessHours.clear();
        this.businessHours.addAll(input);
    }

    public void updateCategoryIds(List<Category> categories) {
        List<Long> ids = categories.stream().map(each -> each.getId()).collect(Collectors.toList());
        this.categoryIds.clear();
        this.categoryIds.addAll(ids);
    }

    public List<BusinessHour> getBusinessHours() {
        return this.businessHours;
    }

    public List<Long> getCategoryIds() {
        return this.categoryIds;
    }

    public Long getId() {
        return id;
    }

    public void editIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void editPhoneNumber(String phone) {
        this.phone = phone;
    }

    public void editDeliveryAreaGuide(String guide) {
        this.deliveryAreaGuide = guide;
    }

    public void rename(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void addClosingDayPolicy(HolidayPolicy policy) {
        this.holidayPolicies.add(policy);
    }

    public boolean isOpen(LocalDateTime now) {
        return exposed
                && suspensionUntil.isBefore(now)
                && holidayPolicies.stream().noneMatch(it -> it.isHoliday(now.toLocalDate()))
                && businessHours.stream().anyMatch(x -> x.isOpen(now));
    }

    public void updateExposeStatus(Boolean expose) {
        this.exposed = expose;
    }

    public int getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(int minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public boolean updateDeliveryArea(List<DeliveryArea> deliveryAreas) {
        this.deliveryAreas.clear();
        return this.deliveryAreas.addAll(deliveryAreas);
    }

    public void isOrderPossible(Order order) {
        if (!isOpen(LocalDateTime.now())) {
            throw new IllegalStateException(SHOP_IS_NOT_OPEN);
        }
        if (order.totalOrderAmount() < this.minimumOrderAmount) {
            throw new IllegalArgumentException(ORDERED_AMOUNT_LOWER_THAN_MINIMUM_ORDER_AMOUNT);
        }
    }

    public void suspendUntil(LocalDateTime from) {
        this.suspensionUntil = from;
    }
}
