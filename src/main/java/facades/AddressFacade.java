package facades;

import dtos.AddressDto;
import dtos.CityInfoDto;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import interfaces.facades.IFacade;
import services.AddressHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public class AddressFacade implements IFacade<AddressDto> {

    private static facades.AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressFacade() {
    }

    public static facades.AddressFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new facades.AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public AddressDto getById(Integer id) {
        EntityManager em = getEntityManager();
        Address a = em.find(Address.class, id);
        return new AddressDto(a);
    }

    @Override
    public List<AddressDto> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a", Address.class);
        List<Address> addressList = query.getResultList();
        return AddressDto.getDtos(addressList);
    }

    @Override
    public AddressDto create(AddressDto addressDto) {
        EntityManager em = getEntityManager();
        CityInfo cityInfo = em.find(CityInfo.class, addressDto.getInnerCityInfoDto().getId());
        Address address = new Address(addressDto);
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.merge(cityInfo);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AddressDto(address);
    }

    @Override
    public AddressDto update(AddressDto addressdto) {
        EntityManager em = getEntityManager();
        Address existingAddress = em.find(Address.class, addressdto.getId());
        Address address = AddressHandler.mergmergeDTOAndEntity(addressdto, existingAddress);
        if (existingAddress == null) {
            throw new EntityNotFoundException("Adress not found");
        }
        try {
            em.getTransaction().begin();
            em.merge(address);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AddressDto(address);
    }


    @Override
    public AddressDto delete(Integer id) {
        EntityManager em = getEntityManager();
        Address a = em.find(Address.class, id);

        try {
            em.getTransaction().begin();
            em.remove(a);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AddressDto(a);
    }

//    @Override
//    public AddressDto delete(Integer id) {
//        EntityManager em = getEntityManager();
//        Address a = em.find(Address.class, id);
//        Address undefined = new Address("undefined", "undefined", new CityInfo("undefined", "undefined"));
//        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.address.id = :id", Person.class);
//        query.setParameter("id", id);
//
//        try {
//            em.getTransaction().begin();
//            em.persist(undefined);
//        query.getResultList().forEach(person -> {
//            person.setAddress(undefined);
//            em.persist(person);
//        });
//            em.remove(a);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//        return new AddressDto(a);
//    }
}
