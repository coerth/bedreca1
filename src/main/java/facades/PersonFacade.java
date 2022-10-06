package facades;

import dtos.PersonDto;
import entities.*;
import interfaces.facades.IFacade;
import services.PersonHandler;
import utils.EMF_Creator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PersonFacade implements IFacade<PersonDto> {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    public static PersonFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonDto getById(Integer id) {
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        if (p == null)
            throw new EntityNotFoundException("Not found");
        return new PersonDto(p);
    }

    @Override
    public List<PersonDto> getAll() {
        List<PersonDto> personDtoList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        query.getResultList().forEach(person -> {
            personDtoList.add(new PersonDto(person));
        });
        return personDtoList;
    }

    public List<PersonDto> getAllPersonsWithHobby(String hobbyName)
    {
        List<PersonDto> personDtoList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :name", Person.class);
        query.setParameter("name" , hobbyName);
        query.getResultList().forEach(person -> {
            personDtoList.add(new PersonDto(person));
        });
        return personDtoList;
    }

    public int getAmountOfPersonsWithHobby(String hobbyName)
    {
        List<PersonDto> personDtoList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :name", Person.class);
        query.setParameter("name" , hobbyName);
        query.getResultList().forEach(person -> {
            personDtoList.add(new PersonDto(person));
        });
        return personDtoList.size();
    }

    public List<PersonDto> getAllPersonsInCity(String zip) {
        List<PersonDto> personDtoList = new ArrayList<>();
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address a JOIN a.cityInfo c WHERE c.zipcode = :zip", Person.class);
        query.setParameter("zip" , zip);
        query.getResultList().forEach(person -> {
            personDtoList.add(new PersonDto(person));
        });
        return personDtoList;
    }

    @Override
    public PersonDto create(PersonDto personDto) {
        EntityManager em = getEntityManager();

        Set<Hobby> hobbySet = new LinkedHashSet<>();
        personDto.getHobbies().forEach(innerHobbyDto -> {
            hobbySet.add(em.find(Hobby.class, innerHobbyDto.getId()));
        });

        Set<Phone> phoneSet = new LinkedHashSet<>();

        Address address = em.find(Address.class, personDto.getAddress().getId());

        Person person = new Person(personDto);
        person.setHobbies(hobbySet);
        person.setAddress(address);

        try {
            em.getTransaction().begin();
            personDto.getPhones().forEach(innerPhoneDto -> {
            Phone phone = new Phone(innerPhoneDto.getNumber(), innerPhoneDto.getDescription(), person);
            em.persist(phone);
            phoneSet.add(phone);
            person.setPhones(phoneSet);
        });
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        System.out.println(person);
        return new PersonDto(person);
    }


    public PersonDto update(PersonDto personDto) {

        EntityManager em = getEntityManager();
        Person existingPerson = em.find(Person.class, personDto.getId());
        Person person = PersonHandler.mergeDTOAndEntity(personDto, existingPerson);


        try {
            em.getTransaction().begin();
            person.getPhones().forEach(phone -> {
                if (phone.getId() == null) {
                    em.persist(phone);
                }
            });
            em.merge(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDto(person);
    }

    @Override
    public PersonDto delete(Integer Id) {
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, Id);
        try {

            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDto(p);
    }

    public PersonDto getByPhone(Integer phone) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT p from Person p JOIN p.phones ph WHERE ph.number =:number", Person.class);
            query.setParameter("number", phone);
            PersonDto personDto = new PersonDto(query.getSingleResult());
            return personDto;

        } finally {
            em.close();
        }
    }

}
