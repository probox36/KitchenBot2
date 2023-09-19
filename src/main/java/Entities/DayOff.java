package Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name="days_off")
@Entity
public class DayOff {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private KitchenUser user;

    @Column(name = "begin_date")
    @Basic
    private LocalDate beginDate;

    @Column(name = "end_date")
    @Basic
    private LocalDate endDate;

    public DayOff() {}

    public DayOff(KitchenUser user, LocalDate beginDate, LocalDate endDate) {
        this.user = user;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public KitchenUser getUser() {
        return user;
    }

    public void setUser(KitchenUser user) {
        this.user = user;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DayOff{" +
                "user=" + user +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}
