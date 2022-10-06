
package facades;

import dtos.PersonDto;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    private static Person p1, p2, p3;
    private static CityInfo c1, c2, c3;
    private static Address a1, a2, a3;
    private static Hobby h1, h2;

    private static Phone phone1, phone2;


    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getInstance(emf);
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

            h1 = new Hobby("Gaming", "something");
            h2 = new Hobby("Strikning", "something");

            phone1 = new Phone(1232312, "mobile");
            phone2 = new Phone(234234242, "home");

            p1 = new Person("Morten@B.dk", "Morten", "Bendeke",a1);
            p2 = new Person("Denis@P.dk", "Denis", "Pedersen",a2);
            p3 = new Person("Betül@I.dk", "Betül", "Iskender", a3);

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
            em.persist(h1);
            em.persist(h2);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
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
    public void getById() {
        PersonDto expected = new PersonDto(p1);
        PersonDto actual = facade.getById(p1.getId());
        assertEquals(expected, actual);
    }


    @Test
    public void create() {
        Person newPerson = new Person("stiickish@yelong.dk", "Yelong","Hartl-He", a1);
        newPerson.getHobbies().add(h1);
        PersonDto personDto = new PersonDto(newPerson);
        PersonDto actual = facade.create(personDto);
        assertNotNull(actual.getId());
        System.out.println(actual);
        System.out.println(actual.getHobbies());
    }

    @Test
    void update() {
        p1.setEmail("Bjergkøbing@email.com");
        p1.getHobbies().add(h2);
        p1.getHobbies().add(h1);
        p1.getPhones().add(phone1);
        p1.getPhones().add(phone2);
        PersonDto updatedPerson = new PersonDto(p1);
        System.out.println(updatedPerson);
        PersonDto actual = facade.update(updatedPerson);
        System.out.println(actual);
        assertEquals(updatedPerson, actual);
        assertEquals(2, updatedPerson.getHobbies().size());
        assertEquals(2, updatedPerson.getPhones().size());
    }

   @Test
    public void getAllPersons() throws Exception {
        List<PersonDto> personDtoList = facade.getAll();
        int expected = 3;
        assertEquals(expected,personDtoList.size() );
    }

    @Test
    void delete() {
        facade.delete(p1.getId());
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }

    }



