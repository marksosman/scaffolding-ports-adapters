package scaffolding.templates

import scaffolding.DomainModelContext

class EventSourcingTemplatesTest {
    private final DomainModelContext ctx

    EventSourcingTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String eventAdapterTest() {
        return """\
package ${ctx.rootPackage}.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import ${ctx.rootPackage}.core.${ctx.modelName}Event;
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ${ctx.modelName}EventAdapterTest {

  private ${ctx.modelName}EventAdapter testSubject;

  @BeforeEach
  void setUp() {
    testSubject = new ${ctx.modelName}EventAdapter();
  }

  @Test
  void fetchEvents_returnsEmptyList() {
    final ${ctx.modelName}Id ${ctx.propertyName}Id = ${ctx.modelName}Id.generate();
    final List<${ctx.modelName}Event> result = testSubject.fetchEvents(${ctx.propertyName}Id);
    assertThat(result).isEmpty();
  }
}
"""
    }
}
