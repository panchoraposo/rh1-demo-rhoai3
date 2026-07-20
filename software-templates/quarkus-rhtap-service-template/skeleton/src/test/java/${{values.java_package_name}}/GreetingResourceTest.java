package ${{values.java_package_name}};

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GreetingResourceTest {

  @Test
  void helloEndpoint() {
    given()
        .when().get("/api/greeting")
        .then()
        .statusCode(200)
        .body(is("hello from ${{values.component_id}}"));
  }
}

