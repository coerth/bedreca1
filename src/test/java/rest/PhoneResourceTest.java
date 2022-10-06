package rest;


import dtos.PhoneDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.Address;
import entities.CityInfo;
import entities.Person;
import entities.Phone;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
//Uncomment the line below, to temporarily disable this test
@Disabled

public class PhoneResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    private static Phone p1, p2;
    private static Address a1, a2;
    private static Person per1, per2;
    private static CityInfo c1, c2;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        c1 = new CityInfo("2630", "Værebro");
        c2 = new CityInfo("8880", "Aalborg");
        a1 = new Address("Bælgevej 16", "Til højre", c1);
        a2 = new Address("Paradisæblevej 111", "Her", c2);
        per1 = new Person("Denis@code1.dk", "Denis", "Pedersen", a1);
        per2 = new Person("Denis@code2.dk", "Denis", "Nielsen", a2);
        p1 = new Phone(12345678, "arbejde", per1);
        p2 = new Phone(87654321, "hjem", per2);
        try {
            em.getTransaction().begin();
//            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
//            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
//            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(a1);
            em.persist(a2);
            em.persist(per1);
            em.persist(per2);
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/phone").then().statusCode(200);
    }

    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/phone")
                .then().statusCode(200);
    }

    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/phone")
                .then().log().body().statusCode(200);
    }

    @Test
    public void getById() {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/phone/{id}", p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(p1.getId()))
                .body("number", equalTo(p1.getNumber()))
                .body("description", equalTo(p1.getDescription()));

    }

    @Test
    public void getAll() throws Exception {
        List<PhoneDto> phoneDtos;

        phoneDtos = given()
                .contentType("application/json")
                .when()
                .get("/phone")
                .then()
                .extract().body().jsonPath().getList("", PhoneDto.class);

        PhoneDto p1Dto = new PhoneDto(p1);
        PhoneDto p2Dto = new PhoneDto(p2);
        assertThat(phoneDtos, containsInAnyOrder(p1Dto, p2Dto));

    }


    @Test
    public void create() {
        Phone p = new Phone(23232323, "hjem", per1);
        PhoneDto phoneDto = new PhoneDto(p);
        String requestBody = GSON.toJson(phoneDto);
        System.out.println(phoneDto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/phone")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("number", equalTo(23232323))
                .body("description", equalTo("hjem"))
                .body("person", hasEntry("id", phoneDto.getPerson().getId()))
                .body("person", hasEntry("email", phoneDto.getPerson().getEmail()))
                .body("person", hasEntry("firstName", phoneDto.getPerson().getFirstName()))
                .body("person", hasEntry("lastName", phoneDto.getPerson().getLastName()));

    }

    @Test
    public void updatePhone() {
        p1.setNumber(88888888);
        PhoneDto phoneDto = new PhoneDto(p1);
        String requestBody = GSON.toJson(phoneDto);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/phone/" + p1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("number", equalTo(88888888))
                .body("description", equalTo("arbejde"));
    }

    @Test
    public void deletePhone() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", p1.getId())
                .delete("/phone/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(p1.getId()));
    }
}
