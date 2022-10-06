package dtos;


import entities.Person;
import entities.Phone;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link entities.Phone} entity
 */
public class PhoneDto implements Serializable {
    private Integer id;
    @NotNull
    private final Integer number;
    @Size(max = 45)
    @NotNull
    private final String description;
    @NotNull
    private final PersonDto person;

    public PhoneDto(Integer number, String description, PersonDto person) {
        this.number = number;
        this.description = description;
        this.person = person;
    }

    public PhoneDto(Phone phone){
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.description = phone.getDescription();
        this.person = new PersonDto(phone.getPerson());
    }

    public PhoneDto(Phone phone, PersonDto personDto){
        this.id = phone.getId();
        this.number = phone.getNumber();
        this.description = phone.getDescription();
        this.person = personDto;

    }
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDto)) return false;
        PhoneDto phoneDto = (PhoneDto) o;
        return getId().equals(phoneDto.getId());
    }


    public Integer getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public PersonDto getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "number = " + number + ", " +
                "description = " + description + ", " +
                "person = " + person + ")";
    }

    /**
     * A DTO for the {@link entities.Person} entity
     */
    public static class PersonDto implements Serializable {
        private final Integer id;
        @Size(max = 45)
        @NotNull
        private final String email;
        @Size(max = 45)
        @NotNull
        private final String firstName;
        @Size(max = 45)
        @NotNull
        private final String lastName;

        public PersonDto(Integer id, String email, String firstName, String lastName) {
            this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public PersonDto(Person person) {
            this.id = person.getId();
            this.email = person.getEmail();
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
        }


        public Integer getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PersonDto)) return false;
            PersonDto personDto = (PersonDto) o;
            return getId().equals(personDto.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "email = " + email + ", " +
                    "firstName = " + firstName + ", " +
                    "lastName = " + lastName + ")";
        }
    }
}