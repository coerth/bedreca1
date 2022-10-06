/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.HobbyDto;
import dtos.RenameMeDTO;
import entities.*;

import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author tha
 */
public class Populator {
  /*  public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

        HobbyFacade hobbyFacade = HobbyFacade.getInstance(emf);
        Hobby hobby1 = hobbyFacade.create(new Hobby("Bagning","Bag med Betül"));
        Hobby hobby2 = hobbyFacade.create(new Hobby("Computerspil", "Computer for dig og mig"));
        Set<Hobby> hobbySet = new HashSet<>();
        hobbySet.add(hobby1);
        hobbySet.add(hobby2);
        CityInfoFacade cityInfoFacade = CityInfoFacade.getInstance(emf);
        CityInfo cityInfo = cityInfoFacade.create(new CityInfo("3000", "Frederiksberg"));
        AddressFacade addressFacade = AddressFacade.getInstance(emf);
        Address address = addressFacade.create(new Address("Lyngbyvej 13", "MF", cityInfo));
        PersonFacade personFacade = PersonFacade.getInstance(emf);
        Person firstPerson = personFacade.create(new Person("hartmann@hardman.dk", "Thomas", "Hartmann", address));
        PhoneFacade phoneFacade = PhoneFacade.getInstance(emf);
        Phone phone = phoneFacade.create(new Phone(45454545,"Hjemmetelefon", firstPerson));
        Set<Phone> phoneSet = new HashSet<>();
        phoneSet.add(phone);
        Person personToPost = personFacade.create(new Person(firstPerson.getEmail(),firstPerson.getFirstName(),firstPerson.getLastName(),firstPerson.getAddress(), hobbySet,phoneSet));
        System.out.println(personToPost);
    }
    */
  /*  public static void main(String[] args) {
        populate();
    }*/
}
