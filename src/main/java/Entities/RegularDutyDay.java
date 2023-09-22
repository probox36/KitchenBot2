package Entities;

import java.time.LocalTime;

public class RegularDutyDay implements DutyDay {
    @Override
    public boolean isRegular() {
        return true;
    }
    private KitchenUser userOnDuty;
    private LocalTime time;

    public KitchenUser getUserOnDuty() { return userOnDuty; }
    public void setUserOnDuty(KitchenUser userOnDuty) { this.userOnDuty = userOnDuty; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public RegularDutyDay(KitchenUser userOnDuty) {
        this.userOnDuty = userOnDuty;
    }

    @Override
    public String toString() {
        return "Дежурство: " + userOnDuty;
    }
}
