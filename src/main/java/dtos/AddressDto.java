package dtos;

import entities.Address;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link entities.Address} entity
 */
public class AddressDto implements Serializable {
    private Integer id;
    @Size(max = 45)
    @NotNull
    private final String street;
    @Size(max = 45)
    @NotNull
    private final String additionalInfo;
    private final Set<InnerPersonDto> people = new HashSet<>();

    private InnerCityInfoDto innerCityInfoDto;

    public AddressDto(Integer id, String street, String additionalInfo) {
        this.id = id;
        this.street = street;
        this.additionalInfo = additionalInfo;
    }

    public AddressDto(Address a) {
        this.id = a.getId();
        this.street = a.getStreet();
        this.additionalInfo = a.getAdditionalInfo();
        a.getPeople().forEach(person -> {
            people.add(new InnerPersonDto(person.getId(),person.getEmail(),person.getFirstName(),person.getLastName()));
        });
        this.innerCityInfoDto = new InnerCityInfoDto(a.getCityInfo().getId(), a.getCityInfo().getZipcode(), a.getCityInfo().getCity());

    }

    public AddressDto(AddressDto addressDto) {
        this.id = addressDto.getId();
        this.street = addressDto.getStreet();
        this.additionalInfo = addressDto.getAdditionalInfo();

    }

    public static List<AddressDto> getDtos(List<Address> addresses) {
        List<AddressDto> addressDtos = new ArrayList();
        addresses.forEach(address -> addressDtos.add(new dtos.AddressDto(address)));
        return addressDtos;
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

    public Set<InnerPersonDto> getPeople() {
        return people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDto)) return false;
        AddressDto that = (AddressDto) o;
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
                "additionalInfo = " + additionalInfo + ", " +
                "people = " + people + ")";
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * A DTO for the {@link entities.Person} entity
     */
    public static class InnerPersonDto implements Serializable {
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

        public InnerPersonDto(Integer id, String email, String firstName, String lastName) {
            this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
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
            if (!(o instanceof InnerPersonDto)) return false;
            InnerPersonDto personDto = (InnerPersonDto) o;
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

    public static class  InnerCityInfoDto implements Serializable {
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
            return "InnerCityInfoDto{" +
                    "id=" + id +
                    ", zipcode='" + zipcode + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

}