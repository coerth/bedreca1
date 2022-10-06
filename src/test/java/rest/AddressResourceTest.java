package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDto;
import entities.Address;
import entities.CityInfo;
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
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class AddressResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static CityInfo c1, c2;
    private static Address a1, a2;

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
        a1 = new Address( "Bælgevej 16", "Til højre",c1);
        a2 = new Address("Paradisæblevej 111", "Her", c2);
        try {
            em.getTransaction().begin();
//            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
//            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(a1);
            em.persist(a2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/address").then().statusCode(200);
    }
    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/address")
                .then().statusCode(200);
    }
    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/address")
                .then().log().body().statusCode(200);
    }
    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/address/{id}",a1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(a1.getId()))
                .body("street", equalTo(a1.getStreet()))
                .body("additionalInfo", equalTo(a1.getAdditionalInfo()))
                .body("innerCityInfoDto", hasEntry("id", a1.getCityInfo().getId()))
                .body("innerCityInfoDto", hasEntry("zipcode", a1.getCityInfo().getZipcode()))
                .body("innerCityInfoDto", hasEntry("city", a1.getCityInfo().getCity()));

    }

    /*@Test*/
    public void getAll() throws Exception {
        List<AddressDto> addressDtos;

        addressDtos = given()
                .contentType("application/json")
                .when()
                .get("/address")
                .then()
                .extract().body().jsonPath().getList("", AddressDto.class);

        AddressDto a1Dto = new AddressDto(a1);
        AddressDto a2Dto = new AddressDto(a2);
        assertThat(addressDtos, containsInAnyOrder(a1Dto, a2Dto));

    }
    @Test
    public void create() {
        Address a = new Address("Bjergevej 40", "1 st",c1);
        AddressDto addressDto = new AddressDto(a);
        String requestBody = GSON.toJson(addressDto);
        System.out.println(addressDto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/address")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("street", equalTo("Bjergevej 40"))
                .body("additionalInfo", equalTo("1 st"))
                .body("innerCityInfoDto", hasEntry("id", addressDto.getInnerCityInfoDto().getId()))
                .body("innerCityInfoDto", hasEntry("zipcode", addressDto.getInnerCityInfoDto().getZipcode()))
                .body("innerCityInfoDto", hasEntry("city", addressDto.getInnerCityInfoDto().getCity()));
    }
    @Test
    public void updateTest() {
        a1.setStreet("Nygade");
        AddressDto addressDto = new AddressDto(a1);
        String requestBody = GSON.toJson(addressDto);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/address/"+a1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(a1.getId()))
                .body("street", equalTo("Nygade"))
                .body("additionalInfo", equalTo("Til højre"));
    }


    @Test
    public void testPrintResponse(){
        Response response = given().when().get("/address/"+a1.getId());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        response
                .then()
                .assertThat()
                .body("street",equalTo("Bælgevej 16"));
    }
}
