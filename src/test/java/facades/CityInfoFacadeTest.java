package facades;


import dtos.CityInfoDto;
import entities.CityInfo;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class CityInfoFacadeTest  {
    private static EntityManagerFactory emf;
    private static CityInfoFacade facade;

    CityInfo c1, c2;

    public CityInfoFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CityInfoFacade.getInstance(emf);
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

            c1 = new CityInfo("2730", "Herlev");
            c2 = new CityInfo("2700", "Brønshøj");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
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
        CityInfoDto expected = new CityInfoDto(c1);
        CityInfoDto actual = facade.getById(c1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getAll(){
        int expected = 2;
        List<CityInfoDto> cityInfoList = facade.getAll();
        assertEquals(expected, cityInfoList.size());
    }

    @Test
    void create() {
        CityInfo c = new CityInfo("2400", "Nordvest");
        CityInfoDto newDTO = new CityInfoDto(c);
        CityInfoDto actual = facade.create(newDTO);
        assertNotNull(actual.getId());
    }

    @Test
    void update() {
        c1.setZipcode("2200");
        CityInfoDto expected = new CityInfoDto(c1);
        CityInfoDto actual = facade.update(expected);
        assertEquals(expected, actual);
    }

    @Test
    void delete(){
        facade.delete(c1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
    }
}
