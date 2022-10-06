
package facades;

import dtos.HobbyDto;
import entities.Hobby;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class HobbyFacadeTest {

    private static EntityManagerFactory emf;
    private static HobbyFacade facade;

    Hobby h1, h2;

    public HobbyFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = HobbyFacade.getInstance(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

            h1 = new Hobby("Strikning", "Kun gamle damer strikker");
            h2 = new Hobby("Gaming", "Spil WOW med Morten");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(h1);
            em.persist(h2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    void getById() {
        HobbyDto expected = new HobbyDto(h1);
        HobbyDto actual = facade.getById(h1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getAll(){
        int expected = 2;
        List<HobbyDto> hobbyList = facade.getAll();
        assertEquals(expected, hobbyList.size());
    }

    @Test
    void create() {
        Hobby h = new Hobby("Madlavning", "Lav mad");
        HobbyDto expected = new HobbyDto(h);
        HobbyDto actual = facade.create(expected);
        assertNotNull(actual.getId());
    }

    @Test
    void update() {
        h1.setName("Hækleri");
        HobbyDto expected = new HobbyDto(h1);
        HobbyDto actual = facade.update(expected);
        assertEquals(expected, actual);
    }

    @Test
    void delete(){
        facade.delete(h1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
    }
}

