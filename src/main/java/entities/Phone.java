package entities;

import dtos.PersonDto;
import dtos.PhoneDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@NamedQuery(name = "Phone.deleteAllRows",query = "DELETE from Phone")
@Table(name = "phone")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "number", nullable = false)
    private Integer number;

    @Size(max = 45)
    @NotNull
    @Column(name = "description", nullable = false, length = 45)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Phone() {
    }

    public Phone(Integer number, String description) {
        this.number = number;
        this.description = description;
    }

    public Phone(Integer number, String description, Person person) {
        this.number = number;
        this.description = description;
        this.person = person;
    }

    public Phone(PhoneDto phoneDto) {
        this.id = phoneDto.getId();
        this.number = phoneDto.getNumber();
        this.description = phoneDto.getDescription();
        this.person = new Person(phoneDto.getPerson());
    }

    public Phone(Integer id, Integer number, String description) {
        this.id = id;
        this.number = number;
        this. description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone)) return false;
        Phone phone = (Phone) o;
        return getNumber().equals(phone.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number=" + number +
                ", description='" + description + '\'' +
                ", person=" + person +
                '}';
    }
}