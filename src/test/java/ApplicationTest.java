import com.jayway.restassured.RestAssured;
import ocr.Application;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * Created by Iuliia on 06.12.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)   // Like any other Spring based test, we need the SpringJUnit4ClassRunner so that an application context is created.
@SpringApplicationConfiguration(classes = Application.class)   // is similar to the @ContextConfiguration annotation in that it is used to specify which application context(s) that should be used in the test. Additionally, it will trigger logic for reading Spring Boot specific configurations, properties, and so on.
@WebAppConfiguration   // must be present in order to tell Spring that a WebApplicationContext should be loaded for the test. It also provides an attribute for specifying the path to the root of the web application.
@IntegrationTest("server.port:0")   // is used to instruct Spring Boot that the embedded web server should be started. By providing colon- or equals-separated name-value pair(s), any environment variable can be overridden. In this example, the "server.port:0" will override the server’s default port setting. Normally, the server would start using the specified port number, but the value 0 has a special meaning. When specified, it tells Spring Boot to scan the ports on the host environment and start the server on a random, available port. That is useful if you have different services occupying different ports on the development machines and the build server that could potentially collide with the application port, in which case the application will not start. Secondly, if you create multiple integration tests with different application contexts, they may also collide if the tests are running concurrently.
public class ApplicationTest {

//    Character mickey;
//    Character minnie;
//    Character pluto;

    @Value("${local.server.port}")   //will be resolved to the actual port number that is used
            int port;

    @Before
    public void setUp() {
//        // 7
//        mickey = new Character("Mickey Mouse");
//        minnie = new Character("Minnie Mouse");
//        pluto = new Character("Pluto");
//
//        // 8
//        repository.deleteAll();
//        repository.save(Arrays.asList(mickey, minnie, pluto));
//
        // We instruct Rest Assured to use the correct port. It is an open source project that provides a Java DSL for testing restful services
        RestAssured.port = port;
    }

//    // 10
//    @Test
//    public void canFetchMickey() {
//        Integer mickeyId = mickey.getId();
//
//        RestAssured.when().
//                get("/characters/{id}", mickeyId).
//        then().
//                statusCode(HttpStatus.SC_OK).
//                body("name", Matchers.is("Mickey Mouse")).
//                body("id", Matchers.is(mickeyId));
//    }

    @Test
    public void loadIndex() {
        RestAssured.when().
                get("/enumbservice-0.2.0/index.html").
                then().
                statusCode(HttpStatus.OK.value());
    }

//    @Test
//    public void canDeletePluto() {
//        Integer plutoId = pluto.getId();
//
//        when()
//                .delete("/characters/{id}", plutoId).
//                then().
//                statusCode(HttpStatus.SC_NO_CONTENT);
//    }
}
