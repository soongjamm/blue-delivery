package com.bluedelivery.shop.application.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bluedelivery.order.adapter.out.persistence.OrderRepositoryJpa;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.shop.application.OrderRankingStrategy;
import com.bluedelivery.shop.domain.Shop;
import com.bluedelivery.shop.domain.ShopRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TotalOrdersPerHour implements OrderRankingStrategy {

    private final String key = "shop:ranking:";

    private final OrderRepositoryJpa orderRepositoryJpa;
    private final ShopRepository shopRepository;
    private final RedisTemplate redisTemplate;

    public TotalOrdersPerHour(OrderRepositoryJpa orderRepositoryJpa, ShopRepository shopRepository,
                              RedisTemplate redisTemplate) {
        this.orderRepositoryJpa = orderRepositoryJpa;
        this.shopRepository = shopRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List getShopRanking() {
        Set set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 10);

        List shopRankingList = new ArrayList(set);

        return shopRankingList;
    }


    @Scheduled(cron = "0 59 0/1 * * *")
    private void addShopRankingForRedis() {
        String newKeyByHours = key.replaceAll(key, key + LocalDateTime.now().getHour());

        List<Shop> shops = openingShops();

        for (int i = 0; i < shops.size(); i++) {
            redisTemplate.opsForZSet()
                    .add(newKeyByHours, shops.get(i).getName(), countOrdersByShopId(shops.get(i).getId()));
        }
        log.info("주문 많은 순 Ranking 업데이트");
    }


    private double countOrdersByShopId(Long shopId) {
        long countOrdersByTime = findAllOrdersBetweenTime()
                .stream()
                .filter(o -> o.getShopId() == shopId)
                .count();
        return countOrdersByTime;
    }


    private List<Shop> openingShops() {
        List<Shop> openingShops = shopRepository.findAll()
                .stream()
                .filter(s -> s.isOpen())
                .collect(Collectors.toList());
        return openingShops;
    }


    private List<Order> findAllOrdersBetweenTime() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Order> orderListByTime = orderRepositoryJpa.findAll()
                .stream()
                .filter(o -> o.getCreateDate().getHour() == currentTime.getHour())
                .collect(Collectors.toList());
        return orderListByTime;
    }

}
