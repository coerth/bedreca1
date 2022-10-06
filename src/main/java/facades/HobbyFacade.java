package facades;

import dtos.HobbyDto;
import entities.Hobby;
import interfaces.facades.IFacade;
import services.HobbyHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class HobbyFacade implements IFacade<HobbyDto> {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {}

    public static HobbyFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public HobbyDto getById(Integer id) {
        EntityManager em = getEntityManager();
        Hobby h = em.find(Hobby.class, id);
        return  new HobbyDto(h);
    }

    @Override
    public List<HobbyDto> getAll() {
        List<HobbyDto> hobbyDtos = new ArrayList<>();
        EntityManager em = getEntityManager();
        TypedQuery<Hobby> query =em.createQuery("SELECT h FROM Hobby h", Hobby.class);
        query.getResultList().forEach( hobby -> {
            hobbyDtos.add(new HobbyDto(hobby));
        });
        return hobbyDtos;
    }

    @Override
    public HobbyDto create(HobbyDto hobbyDto) {
        EntityManager em = getEntityManager();
        Hobby hobby = new Hobby(hobbyDto);
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDto(hobby);
    }

    @Override
    public HobbyDto update(HobbyDto hobbyDto) {
        EntityManager em = getEntityManager();
        Hobby existingHobby = em.find(Hobby.class, hobbyDto.getId());
        Hobby hobby = HobbyHandler.mergeDTOAndEntity(hobbyDto, existingHobby);
        try {
            em.getTransaction().begin();
            em.merge(hobby);
            em.getTransaction().commit();
        } finally {

            em.close();
        }
        return new HobbyDto(hobby);
    }

    @Override
    public HobbyDto delete(Integer id) {
        EntityManager em = getEntityManager();
        Hobby h = em.find(Hobby.class, id);

        try{
            em.getTransaction().begin();
            em.remove(h);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDto(h);
    }
}
