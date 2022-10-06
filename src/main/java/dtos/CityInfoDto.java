package dtos;

import entities.Address;
import entities.CityInfo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link entities.CityInfo} entity
 */
public class CityInfoDto implements Serializable {
    private Integer id;
    @Size(max = 45)
    @NotNull
    private final String zipcode;
    @Size(max = 45)
    @NotNull
    private final String city;

    public CityInfoDto(Integer id, String zipcode, String city) {
        this.id = id;
        this.zipcode = zipcode;
        this.city = city;
    }

    public CityInfoDto(CityInfo cityInfo) {
        this.id = cityInfo.getId();
        this.zipcode = cityInfo.getZipcode();
        this.city = cityInfo.getCity();
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


    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityInfoDto)) return false;
        CityInfoDto that = (CityInfoDto) o;
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
                "zipcode = " + zipcode + ", " +
                "city = " + city + ", " ;
    }

    /**
     * A DTO for the {@link entities.Address} entity
     */
}