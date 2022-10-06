package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDto;
import dtos.HobbyDto;
import entities.Address;
import entities.Hobby;
import entities.RenameMe;
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

public class HobbyResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Hobby h1, h2;

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
        h1 = new Hobby("Spil guitar", "med Morten");
        h2 = new Hobby("aaa", "bbb");
        try {
            em.getTransaction().begin();
//            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.persist(h1);
            em.persist(h2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/hobby").then().statusCode(200);
    }
    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/hobby")
                .then().statusCode(200);
    }
    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/hobby")
                .then().log().body().statusCode(200);
    }

    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/hobby/{id}",h1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(h1.getId()))
                .body("name", equalTo(h1.getName()))
                .body("description", equalTo(h1.getDescription()));
    }
    @Test
    public void getAll() throws Exception {
        List<HobbyDto> hobbyDtos;

        hobbyDtos = given()
                .contentType("application/json")
                .when()
                .get("/hobby")
                .then()
                .extract().body().jsonPath().getList("", HobbyDto.class);

        HobbyDto h1Dto = new HobbyDto(h1);
        HobbyDto h2Dto = new HobbyDto(h2);
        assertThat(hobbyDtos, containsInAnyOrder(h1Dto, h2Dto));

    }
    @Test
    public void create() {
        Hobby h = new Hobby("Dans med Betül", "Lær at twerke som Nicki Minaj");
        HobbyDto hobbyDto = new HobbyDto(h);
        String requestBody = GSON.toJson(hobbyDto);
        System.out.println(hobbyDto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/hobby")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Dans med Betül"))
                .body("description", equalTo("Lær at twerke som Nicki Minaj"));
    }
    @Test
    public void updateTest() {
        h1.setName("Mortens fjollerier");
        HobbyDto hobbyDto = new HobbyDto(h1);
        String requestBody = GSON.toJson(hobbyDto);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/hobby/"+h1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(h1.getId()))
                .body("name", equalTo("Mortens fjollerier"))
                .body("description", equalTo("med Morten"));
    }

}
