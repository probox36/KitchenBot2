package Entities;

import jakarta.persistence.*;
//import org.hibernate.type.NumericBooleanConverter;
import org.telegram.telegrambots.meta.api.objects.User;

//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name="users")
@Entity
public class KitchenUser extends User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "first_name")
    private String firstName;

    @Enumerated(EnumType.STRING)
    private Status status;

//    @Convert(converter = NumericBooleanConverter.class)
//    @Column(name = "temporarily_absent")
//    private boolean temporarilyAbsent;

//    @Column(name = "begin_date")
//    @Basic
//    private LocalDate beginDate;

//    @Column(name = "end_date")
//    @Basic
//    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "delays", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "delay")
    private List<Integer> notificationDelays;

    public List<Integer> getNotificationDelays() {
        return notificationDelays;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setNotificationDelays(ArrayList<Integer> notificationDelays) {
        this.notificationDelays = notificationDelays;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

//    public boolean isTemporarilyAbsent() {
//        return temporarilyAbsent;
//    }
//
//    public void setTemporarilyAbsent(boolean temporarilyAbsent) {
//        this.temporarilyAbsent = temporarilyAbsent;
//    }
//
//    public LocalDate getBeginDate() {
//        return beginDate;
//    }
//
//    public void setBeginDate(LocalDate beginDate) {
//        this.beginDate = beginDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }

    @Override
    public String toString() {
        return "KitchenUser{" +
                "Id = '" + id + '\'' +
                ", Юзернейм = '" + userName + '\'' +
                ", Имя = '" + firstName + '\'' +
                ", Статус = " + status +
                '}';
    }
}
