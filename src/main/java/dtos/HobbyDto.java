package dtos;

import entities.Hobby;
import entities.Person;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link entities.Hobby} entity
 */
public class HobbyDto implements Serializable {
    private Integer id;
    @Size(max = 45)
    @NotNull
    private final String name;
    @Size(max = 45)
    @NotNull
    private final String description;

    private int amountOfHobbyists = 0;

    public HobbyDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public HobbyDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public HobbyDto(Hobby hobby) {
        this.id = hobby.getId();
        this.name = hobby.getName();
        this.description = hobby.getDescription();
        this.amountOfHobbyists = hobby.getPeople().size();
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

    public int getAmountOfHobbyists() {
        return amountOfHobbyists;
    }

    public void setAmountOfHobbyists(int amountOfHobbyists) {
        this.amountOfHobbyists = amountOfHobbyists;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HobbyDto)) return false;
        HobbyDto hobbyDto = (HobbyDto) o;
        return getId().equals(hobbyDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "amountOfHobbyists = " + amountOfHobbyists + ")";
    }

}