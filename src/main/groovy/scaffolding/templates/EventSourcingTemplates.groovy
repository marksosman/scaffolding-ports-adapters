package scaffolding.templates

import scaffolding.DomainModelContext

class EventSourcingTemplates {
    private final DomainModelContext ctx

    EventSourcingTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String eventMarkerInterface() {
        return """\
package ${ctx.rootPackage}.core;

public interface ${ctx.modelName}Event {
  // Marker interface — implement for each concrete ${ctx.modelName} domain event
}
"""
    }

    String state() {
        return """\
package ${ctx.rootPackage}.core.model.attribute;

public record ${ctx.modelName}State(${ctx.modelName}Id ${ctx.propertyName}Id) {

  public static ${ctx.modelName}State empty() {
    return new ${ctx.modelName}State(null);
  }

  public boolean isEmpty() {
    return ${ctx.propertyName}Id == null;
  }
}
"""
    }

    String eventProjector() {
        return """\
package ${ctx.rootPackage}.core.model;

import ${ctx.rootPackage}.core.${ctx.modelName}Event;
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}State;
import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}EventRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}EventProjector {

  private final ${ctx.modelName}EventRepository ${ctx.propertyName}EventRepository;

  public ${ctx.modelName}EventProjector(final ${ctx.modelName}EventRepository ${ctx.propertyName}EventRepository) {
    this.${ctx.propertyName}EventRepository = ${ctx.propertyName}EventRepository;
  }

  public ${ctx.modelName}State replay(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
    // TODO: fetch events and fold into state
    return ${ctx.modelName}State.empty();
  }
}
"""
    }

    String eventRepository() {
        return """\
package ${ctx.rootPackage}.core.ports.outgoing;

import ${ctx.rootPackage}.core.${ctx.modelName}Event;
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import java.util.List;

public interface ${ctx.modelName}EventRepository {

  void add(${ctx.modelName}Event event);

  List<${ctx.modelName}Event> fetchEvents(${ctx.modelName}Id ${ctx.propertyName}Id);
}
"""
    }

    String eventAdapter() {
        return """\
package ${ctx.rootPackage}.infrastructure;

import ${ctx.rootPackage}.core.${ctx.modelName}Event;
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}EventRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}EventAdapter implements ${ctx.modelName}EventRepository {

  @Override
  public void add(final ${ctx.modelName}Event event) {
    // TODO: implement event storage
  }

  @Override
  public List<${ctx.modelName}Event> fetchEvents(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
    // TODO: implement event fetching
    return List.of();
  }
}
"""
    }
}
