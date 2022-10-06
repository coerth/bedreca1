package services;

import dtos.HobbyDto;
import entities.Hobby;

public class HobbyHandler {

    public static Hobby mergeDTOAndEntity(HobbyDto hobbyDto, Hobby hobby)
    {
        Hobby updatedHobby = hobby;

        if(hobbyDto.getName() != null && !hobbyDto.getName().equals(hobby.getName()))
        {
            updatedHobby.setName(hobbyDto.getName());
        }

        if(hobbyDto.getDescription() != null && !hobbyDto.getDescription().equals(hobby.getDescription()))
        {
            updatedHobby.setDescription(hobbyDto.getDescription());
        }

        return updatedHobby;
    }

}
