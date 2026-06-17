package scaffolding.templates

import scaffolding.DomainModelContext

class ProvidedPortTemplatesTest {
    private final DomainModelContext ctx

    ProvidedPortTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String providedPortTest() {
        return """\
package ${ctx.rootPackage}.application.http;

import static org.assertj.core.api.Assertions.assertThat;

import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}Facade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ${ctx.modelName}ProvidedPortTest {

  @Mock private ${ctx.modelName}Presenter ${ctx.propertyName}Presenter;
  @Mock private ${ctx.modelName}RequestMapper ${ctx.propertyName}RequestMapper;
  @Mock private ${ctx.modelName}Facade ${ctx.propertyName}Facade;

  private ${ctx.modelName}ProvidedPort testSubject;

  @BeforeEach
  void setUp() {
    testSubject = new ${ctx.modelName}ProvidedPort(
        ${ctx.propertyName}Presenter,
        ${ctx.propertyName}RequestMapper,
        ${ctx.propertyName}Facade);
  }

  @Test
  void setUp_createsInstance() {
    assertThat(testSubject).isNotNull();
    // TODO: add behaviour tests
  }
}
"""
    }
}