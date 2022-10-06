package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDto;
import dtos.CityInfoDto;
import dtos.HobbyDto;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.RenameMe;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
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
import static org.hamcrest.Matchers.notNullValue;
//Uncomment the line below, to temporarily disable this test
@Disabled

public class CityInfoResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static CityInfo c1, c2;

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
        try {
            em.getTransaction().begin();
//            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/cityinfo").then().statusCode(200);
    }
    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/cityinfo")
                .then().statusCode(200);
    }
    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/cityinfo")
                .then().log().body().statusCode(200);
    }
    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/cityinfo/{id}",c1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(c1.getId()))
                .body("zipcode", equalTo(c1.getZipcode()))
                .body("city", equalTo(c1.getCity()));
    }
    @Test
    public void getAll() throws Exception {
        List<CityInfoDto> cityInfoDtos;

        cityInfoDtos = given()
                .contentType("application/json")
                .when()
                .get("/cityinfo")
                .then()
                .extract().body().jsonPath().getList("", CityInfoDto.class);

        CityInfoDto c1Dto = new CityInfoDto(c1);
        CityInfoDto c2Dto = new CityInfoDto(c2);
        assertThat(cityInfoDtos, containsInAnyOrder(c1Dto, c2Dto));

    }
    @Test
    public void create() {
        CityInfo c = new CityInfo("8800", "Tarm");
        CityInfoDto cityInfoDto = new CityInfoDto(c);
        String requestBody = GSON.toJson(cityInfoDto);
        System.out.println(cityInfoDto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/cityinfo")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("zipcode", equalTo(cityInfoDto.getZipcode()))
                .body("city", equalTo(cityInfoDto.getCity()));
    }
    @Test
    public void updateTest() {
        c1.setZipcode("9001");
        CityInfoDto cityInfoDto = new CityInfoDto(c1);
        String requestBody = GSON.toJson(cityInfoDto);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/cityinfo/"+c1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(c1.getId()))
                .body("zipcode", equalTo("9001"))
                .body("city", equalTo(cityInfoDto.getCity()));
    }
}
