package Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name="swap_list")
@Entity
public class SwapTicket {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private KitchenUser user;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private KitchenUser otherUser;

    @Override
    public String toString() {
        return "SwapTicket{" +
                "date=" + date +
                ", user=" + user +
                ", otherUser=" + otherUser +
                '}';
    }

    public SwapTicket(LocalDate date, KitchenUser user, KitchenUser otherUser) {
        this.date = date;
        this.user = user;
        this.otherUser = otherUser;
    }

    public SwapTicket() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public KitchenUser getUser() {
        return user;
    }

    public void setUser(KitchenUser user) {
        this.user = user;
    }

    public KitchenUser getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(KitchenUser otherUser) {
        this.otherUser = otherUser;
    }
}
