package scaffolding.templates

import scaffolding.DomainModelContext

class EventListenerTemplates {
    private final DomainModelContext ctx

    EventListenerTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String eventFacade() {
        return """\
package ${ctx.rootPackage}.core.ports.incoming;

public interface ${ctx.modelName}EventFacade {
  // TODO: Add event handling methods
}
"""
    }

    String eventProvider() {
        return """\
package ${ctx.rootPackage}.core;

import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}EventFacade;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}EventProvider implements ${ctx.modelName}EventFacade {
  // TODO: Implement event handling logic
}
"""
    }

    String eventListener() {
        return """\
package ${ctx.rootPackage}.application.messaging;

import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}EventFacade;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}EventListener {

  private final ${ctx.modelName}EventFacade ${ctx.propertyName}EventFacade;

  public ${ctx.modelName}EventListener(final ${ctx.modelName}EventFacade ${ctx.propertyName}EventFacade) {
    this.${ctx.propertyName}EventFacade = ${ctx.propertyName}EventFacade;
  }

  @EventListener
  public void on(final ApplicationEvent event) {
    // TODO: cast to the specific event type and handle
  }
}
"""
    }

}