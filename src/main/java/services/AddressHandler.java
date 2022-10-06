package services;

import dtos.AddressDto;
import dtos.HobbyDto;
import entities.Address;
import entities.Hobby;

public class AddressHandler {

    public static Address mergmergeDTOAndEntity(AddressDto addressDto, Address address)
    {
        Address updatedAddress = address;

        if(addressDto.getStreet() != null && !addressDto.getStreet().equals(address.getStreet()))
        {
            updatedAddress.setStreet(addressDto.getStreet());
        }

        if(addressDto.getAdditionalInfo() != null && !addressDto.getAdditionalInfo().equals(address.getAdditionalInfo()))
        {
            updatedAddress.setAdditionalInfo(addressDto.getAdditionalInfo());
        }


        return updatedAddress;
    }
}
