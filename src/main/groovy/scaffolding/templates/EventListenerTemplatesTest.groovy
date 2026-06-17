package scaffolding.templates

import scaffolding.DomainModelContext

class EventListenerTemplatesTest {
    private final DomainModelContext ctx

    EventListenerTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String eventListenerTest() {
        return """\
package ${ctx.rootPackage}.application.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}EventFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ${ctx.modelName}EventListenerTest {

  @Mock private ${ctx.modelName}EventFacade ${ctx.propertyName}EventFacade;

  private ${ctx.modelName}EventListener testSubject;

  @BeforeEach
  void setUp() {
    testSubject = new ${ctx.modelName}EventListener(${ctx.propertyName}EventFacade);
  }

  @Test
  void setUp_createsInstance() {
    assertThat(testSubject).isNotNull();
    // TODO: add event handling tests
  }
}
"""
    }
}