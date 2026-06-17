package scaffolding.templates

import scaffolding.DomainModelContext

class EventPublisherTemplates {
    private final DomainModelContext ctx

    EventPublisherTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String publisherInterface() {
        return """\
package ${ctx.rootPackage}.core.ports.outgoing;

public interface ${ctx.modelName}Publisher {
  // TODO: Add event publishing methods
}
"""
    }

    String eventMapper() {
        return """\
package ${ctx.rootPackage}.infrastructure.messaging;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface ${ctx.modelName}EventMapper {
  // TODO: Add event mapping methods
}
"""
    }

    String eventPublisher() {
        return """\
package ${ctx.rootPackage}.infrastructure.messaging;

import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}EventPublisher implements ${ctx.modelName}Publisher {

  private final ApplicationEventPublisher eventPublisher;

  public ${ctx.modelName}EventPublisher(final ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  // TODO: Implement event publishing methods
}
"""
    }
}