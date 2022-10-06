package facades;

import dtos.AddressDto;
import entities.Address;
import entities.CityInfo;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AddressFacadeTest {
        private static EntityManagerFactory emf;
        private static AddressFacade facade;
        CityInfo c1, c2, c3;
        Address a1, a2, a3;

        public AddressFacadeTest() {
        }

        @BeforeAll
        public static void setUpClass() {
            emf = EMF_Creator.createEntityManagerFactoryForTest();
            facade = AddressFacade.getInstance(emf);

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
            AddressDto expected = new AddressDto(a1);
            AddressDto actual = facade.getById(a1.getId());
            assertEquals(expected, actual);
        }

        @Test
        void getAll(){
            int expected = 3;
            List<AddressDto> AddressList = facade.getAll();
            assertEquals(expected, AddressList.size());
        }

        @Test
        void create() {
            Address address = new Address("Hagbartsvej", "5", c1);
            AddressDto expected = new AddressDto(address);
            AddressDto actual = facade.create(expected);
            System.out.println(actual);
            assertNotNull(actual.getId());
        }

        @Test
        void update() {
            a1.setStreet("Bygaden");
            AddressDto expected = new AddressDto(a1);
            AddressDto actual = facade.update(expected);
            assertEquals(expected, actual);
        }

        @Test
        void delete(){
            facade.delete(a1.getId());
            int expected = 2;
            int actual = facade.getAll().size();
            assertEquals(expected, actual);
        }

}
