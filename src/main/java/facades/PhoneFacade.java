package facades;

import dtos.PersonDto;
import dtos.PhoneDto;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import entities.Phone;
import interfaces.facades.IFacade;
import services.PhoneHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class PhoneFacade implements IFacade<PhoneDto> {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PhoneFacade() {}

    public static PhoneFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public PhoneDto getById(Integer id) {
        EntityManager em = getEntityManager();
       Phone phone = em.find(Phone.class, id);

        if(phone != null)
        {
            return new PhoneDto(phone);
        }

        return null;
    }

    @Override
    public List<PhoneDto> getAll() {
        List<PhoneDto> phoneDtoList = new ArrayList<>();
        EntityManager em = getEntityManager();
        TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p", Phone.class);
        query.getResultList().forEach(phone -> {
            phoneDtoList.add(new PhoneDto(phone));
        });
        return phoneDtoList;
    }

    @Override
    public PhoneDto create(PhoneDto phoneDto) {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, phoneDto.getPerson().getId());
        Phone phone = new Phone(phoneDto);

        try {
            em.getTransaction().begin();
            if(person != null){
                em.persist(phone);
                em.merge(person);
            }
            else {
                Person newPerson = new Person(phoneDto.getPerson());
                em.persist(phone);
                em.persist(newPerson);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDto(phone);
    }

    @Override
    public PhoneDto update(PhoneDto phone) {
        EntityManager em = getEntityManager();
        Phone existingPhone = em.find(Phone.class, phone.getId());
        Phone p = PhoneHandler.mergeDTOAndEntity(phone, existingPhone);

        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDto(p);
    }

    @Override
    public PhoneDto delete(Integer id) {
        EntityManager em = getEntityManager();
        Phone p = em.find(Phone.class, id);

        try{
            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDto(p);
    }
}