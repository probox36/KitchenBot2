package Entities;

public class RegularDutyDay implements DutyDay {
    @Override
    public boolean isRegular() {
        return true;
    }
    private KitchenUser userOnDuty;

    public KitchenUser getUserOnDuty() { return userOnDuty; }
    public void setUserOnDuty(KitchenUser userOnDuty) { this.userOnDuty = userOnDuty; }

    public RegularDutyDay(KitchenUser userOnDuty) {
        this.userOnDuty = userOnDuty;
    }

    @Override
    public String toString() {
        return "Дежурство: " + userOnDuty;
    }
}
