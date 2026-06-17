package scaffolding.templates

import scaffolding.DomainModelContext

class EventPublisherTemplatesTest {
    private final DomainModelContext ctx

    EventPublisherTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String eventPublisherTest() {
        return """\
package ${ctx.rootPackage}.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

class ${ctx.modelName}EventPublisherTest {

  private ${ctx.modelName}EventPublisher testSubject;
  private ApplicationEventPublisher applicationEventPublisher;

  @BeforeEach
  void setUp() {
    applicationEventPublisher = mock(ApplicationEventPublisher.class);
    testSubject = new ${ctx.modelName}EventPublisher(applicationEventPublisher);
  }

  @Test
  void testSubject_implementsPublisher() {
    assertThat(testSubject).isInstanceOf(${ctx.modelName}Publisher.class);
  }
}
"""
    }
}
