package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link entities.Person} entity
 */
public class PersonDto implements Serializable {
    private Integer id;
    @Size(max = 45)
    @NotNull
    private final String email;
    @Size(max = 45)
    @NotNull
    private final String firstName;
    @Size(max = 45)
    @NotNull
    private final String lastName;
    @NotNull
    private final PersonDto.InnerAddressDto address;
    private final Set<InnerHobbyDto> hobbies = new HashSet<>();
    private final Set<InnerPhoneDto> phones = new HashSet<>();


    public PersonDto(Integer id, String email, String firstName, String lastName, InnerAddressDto address) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;

    }

    public PersonDto(Person person) {
        this.id = person.getId();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = new InnerAddressDto(person.getAddress());
        person.getHobbies().forEach(hobby -> {
            hobbies.add(new InnerHobbyDto(hobby));
        });
        person.getPhones().forEach(phone -> {
            phones.add(new InnerPhoneDto(phone));

        });
    }

    public static List<PersonDto> getDtos(List<Person> persons) {
        List<PersonDto> personDtos = new ArrayList();
        persons.forEach(person -> personDtos.add(new PersonDto(person)));
        return personDtos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public InnerAddressDto getAddress() {
        return address;
    }

    public Set<InnerHobbyDto> getHobbies() {
        return hobbies;
    }

    public Set<InnerPhoneDto> getPhones() {
        return phones;
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

    /*@Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "email = " + email + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", ";
    }*/

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", hobbies=" + hobbies +
                ", phones=" + phones +
                '}';
    }

    /**
     * A DTO for the {@link entities.Address} entity
     */
    public static class InnerAddressDto implements Serializable {
        private final Integer id;
        @Size(max = 45)
        @NotNull
        private final String street;
        @Size(max = 45)
        @NotNull
        private final String additionalInfo;

        private final InnerCityInfoDto innerCityInfoDto;

        public InnerAddressDto(Integer id, String street, String additionalInfo, InnerCityInfoDto innerCityInfoDto) {
            this.id = id;
            this.street = street;
            this.additionalInfo = additionalInfo;
            this.innerCityInfoDto = innerCityInfoDto;
        }

        public InnerAddressDto(Address address) {
            this.id = address.getId();
            this.street = address.getStreet();
            this.additionalInfo = address.getAdditionalInfo();
            this.innerCityInfoDto = new InnerCityInfoDto(address.getCityInfo().getId(), address.getCityInfo().getZipcode(), address.getCityInfo().getCity());
        }

        public Integer getId() {
            return id;
        }

        public String getStreet() {
            return street;
        }

        public String getAdditionalInfo() {
            return additionalInfo;
        }

        public InnerCityInfoDto getInnerCityInfoDto() {
            return innerCityInfoDto;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InnerAddressDto)) return false;
            InnerAddressDto that = (InnerAddressDto) o;
            return getId().equals(that.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "street = " + street + ", " +
                    "additionalInfo = " + additionalInfo + ")";
        }

        public static class InnerCityInfoDto implements Serializable {
            private final Integer id;
            @Size(max = 45)
            @NotNull
            private final String zipcode;
            @Size(max = 45)
            @NotNull
            private final String city;



            public InnerCityInfoDto(Integer id, String zipcode, String city) {
                this.id = id;
                this.zipcode = zipcode;
                this.city = city;
            }

            public Integer getId() {
                return id;
            }

            public String getZipcode() {
                return zipcode;
            }

            public String getCity() {
                return city;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof InnerCityInfoDto)) return false;
                InnerCityInfoDto that = (InnerCityInfoDto) o;
                return getId().equals(that.getId());
            }

            @Override
            public int hashCode() {
                return Objects.hash(getId());
            }

            @Override
            public String toString() {
                return "InnerCityInfo{" +
                        "id=" + id +
                        ", zipcode='" + zipcode + '\'' +
                        ", city='" + city + '\'' +
                        '}';
            }
        }
    }

    /**
     * A DTO for the {@link entities.Hobby} entity
     */
    public static class InnerHobbyDto implements Serializable {
        private final Integer id;
        @Size(max = 45)
        @NotNull
        private final String name;
        @Size(max = 45)
        @NotNull
        private final String description;

        public InnerHobbyDto(Integer id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public InnerHobbyDto(Hobby hobby) {
            this.id = hobby.getId();
            this.name = hobby.getName();
            this.description = hobby.getDescription();
        }


        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerHobbyDto entity = (InnerHobbyDto) o;
            return Objects.equals(this.id, entity.id) &&
                    Objects.equals(this.name, entity.name) &&
                    Objects.equals(this.description, entity.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, description);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "name = " + name + ", " +
                    "description = " + description + ")";
        }
    }

    /**
     * A DTO for the {@link entities.Phone} entity
     */
    public static class InnerPhoneDto implements Serializable {
        private final Integer id;
        @NotNull
        private final Integer number;
        @Size(max = 45)
        @NotNull
        private final String description;

        public InnerPhoneDto(Integer id, Integer number, String description) {
            this.id = id;
            this.number = number;
            this.description = description;
        }

        public InnerPhoneDto(Phone phone) {
            this.id = phone.getId();
            this.number = phone.getNumber();
            this.description = phone.getDescription();
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


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerPhoneDto entity = (InnerPhoneDto) o;
            return Objects.equals(this.id, entity.id) &&
                    Objects.equals(this.number, entity.number) &&
                    Objects.equals(this.description, entity.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, number, description);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
                    "number = " + number + ", " +
                    "description = " + description + ")";
        }

    }

}