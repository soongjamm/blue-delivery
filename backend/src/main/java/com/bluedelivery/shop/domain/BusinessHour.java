package com.bluedelivery.shop.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class BusinessHour implements Comparable<BusinessHour> {
    
    @Enumerated(value = EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private LocalTime open;
    private LocalTime close;
    
    protected BusinessHour() {
    }
    
    public BusinessHour(DayOfWeek dayOfWeek, LocalTime open, LocalTime close) {
        this.dayOfWeek = dayOfWeek;
        this.open = open;
        this.close = close;
    }
    
    /**
     * #isOpening()
     * 영업종료시간이 익일로 넘어가는 경우 현재시간은 23시인데 close가 03시라 영업시간이 아니게 되는 문제가 발생할 수 있다.
     * 그래서 이 경우 다음 두 케이스를 비교한다.
     * - 1. open ~ 자정 직전
     * - 2. 자정 ~ close
     *
     * @param today 영업 시간에 포함되는지 확인할 시간
     * @return 영업중이 맞다면 true
     */
    public boolean isOpen(LocalDateTime today) {
        return isOpenToday(today) || isNextDayAndOpen(today);
    }
    
    private boolean isNextDayAndOpen(LocalDateTime today) { // 영업종료시간이 익일인 경우
        if (close.isBefore(open)) { // 영업종료시간이 익일인 경우
            if (isNextDay(today) && isOpenOverMidnight(today.toLocalTime())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isNextDay(LocalDateTime today) {
        return this.dayOfWeek.plus(1) == today.getDayOfWeek();
    }
    
    private boolean isOpenToday(LocalDateTime today) {
        LocalTime now = today.toLocalTime();
        return this.getDayOfWeek() == today.getDayOfWeek()
                && (open.isBefore(now) && now.isBefore(close));
    }
    
    private boolean isOpenOverMidnight(LocalTime now) {
        return (open.isBefore(now) && now.isBefore(LocalTime.MAX))
                || (now.isAfter(LocalTime.MIDNIGHT) && now.isBefore(close));
    }
    
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }
    
    public LocalTime getOpen() {
        return open;
    }
    
    public LocalTime getClose() {
        return close;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BusinessHour that = (BusinessHour) obj;
        return Objects.equals(dayOfWeek, that.dayOfWeek)
                && Objects.equals(open, that.open)
                && Objects.equals(close, that.close);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek);
    }
    
    @Override
    public String toString() {
        return "BusinessHour{"
                + "shopId=" + ", open=" + open + ", close=" + close
                + ", dayOfWeek=" + dayOfWeek + '}';
    }
    
    @Override
    public int compareTo(BusinessHour obj) {
        return this.dayOfWeek.compareTo(obj.dayOfWeek);
    }
}
