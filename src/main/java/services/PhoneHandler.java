package services;

import dtos.PhoneDto;
import entities.Phone;

public class PhoneHandler {

    public static Phone mergeDTOAndEntity(PhoneDto phoneDto, Phone phone){
        Phone updatedPhone = phone;
        if(phoneDto.getNumber() != null && !phoneDto.getNumber().equals(phone.getNumber())){
            updatedPhone.setNumber(phoneDto.getNumber());
        }
        if(phoneDto.getDescription() != null && !phoneDto.getDescription().equals(phone.getDescription())){
            updatedPhone.setDescription(phoneDto.getDescription());
        }
        return updatedPhone;
    }
}
