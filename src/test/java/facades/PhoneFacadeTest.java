package facades;

import dtos.PhoneDto;
import entities.*;
import entities.Phone;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PhoneFacadeTest
{
    private static EntityManagerFactory emf;
    private static PhoneFacade facade;
    CityInfo c1, c2, c3;
    Address a1, a2, a3;
    Person person1, person2; 
    Phone phone1, phone2;

    public PhoneFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PhoneFacade.getInstance(emf);

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

            c1 = new CityInfo("2300","Amager");
            c2 = new CityInfo("2400","Nordvest");
            c3 = new CityInfo("2100","Østerbro");
            a1 = new Address("Amagerbrogade 12","3. TV",c1);
            a2 = new Address("Stærevej 6","1.TH",c2);
            a3 = new Address("Østerbrogade 271","ST, MF",c3);
            person1 = new Person("Denis@P.dk", "Denis", "Pedersen", a1);
            person2 = new Person("Betül@I.dk", "Betül", "Iskender", a2);
            phone1 = new Phone(1020304, "Home", person1);
            phone2 = new Phone(5060708, "Work", person2);

        try {
            em.getTransaction().begin();

            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(person1);
            em.persist(person2);
            em.persist(phone1);
            em.persist(phone2);

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
        PhoneDto expected = new PhoneDto(phone1);
        PhoneDto actual = facade.getById(phone1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getAll(){
        int expected = 2;
        List<PhoneDto> PhoneList = facade.getAll();
        assertEquals(expected, PhoneList.size());
    }

    @Test
    void create() {
        Phone phone = new Phone(12233445, "Work", person1);
        PhoneDto phoneDto = new PhoneDto(phone);
        PhoneDto actual = facade.create(phoneDto);
        System.out.println(actual);
        assertNotNull(actual.getId());
    }

    @Test
    void update() {
        phone1.setDescription("Mobile");
        PhoneDto expected = new PhoneDto(phone1);
        PhoneDto actual = facade.update(expected);
        assertEquals(expected, actual);
    }

    @Test
    void delete(){
        facade.delete(phone1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
    }
}
