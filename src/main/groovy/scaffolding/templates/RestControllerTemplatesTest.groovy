package scaffolding.templates

import scaffolding.DomainModelContext

class RestControllerTemplatesTest {
    private final DomainModelContext ctx

    RestControllerTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String controllerTest() {
        return """\
package ${ctx.rootPackage}.application.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@SpringBootTest
@AutoConfigureMockMvc
class ${ctx.modelName}ControllerTest {

  @Autowired private MockMvcTester mockMvc;

  @Test
  void validRequest_get_returnOk() {
    // given
    // when
    final MvcTestResult result =
        mockMvc
            .get()
            .uri("/TODO")
            .exchange();
    // then
    assertThat(result).hasStatus(HttpStatus.OK);
  }
}
"""
    }
}