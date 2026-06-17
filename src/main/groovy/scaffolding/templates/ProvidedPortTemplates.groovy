package scaffolding.templates

import scaffolding.DomainModelContext

class ProvidedPortTemplates {
    private final DomainModelContext ctx

    ProvidedPortTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String presenter() {
        return """\
package ${ctx.rootPackage}.application.http;

import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}Presenter {
  // TODO: Add presentation logic
}
"""
    }

    String requestMapper() {
        return """\
package ${ctx.rootPackage}.application.http;

import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}RequestMapper {
  // TODO: Add request mapping logic
}
"""
    }

    String responseMapper() {
        return """\
package ${ctx.rootPackage}.application.http;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ${ctx.modelName}ResponseMapper {
  // TODO: Add response mapping methods
}
"""
    }

    String providedPort() {
        return """\
package ${ctx.rootPackage}.application.http;

import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}Facade;
import org.springframework.stereotype.Component;

@Component
public class ${ctx.modelName}ProvidedPort {

  private final ${ctx.modelName}Presenter ${ctx.propertyName}Presenter;
  private final ${ctx.modelName}RequestMapper ${ctx.propertyName}RequestMapper;
  private final ${ctx.modelName}Facade ${ctx.propertyName}Facade;

  public ${ctx.modelName}ProvidedPort(
      final ${ctx.modelName}Presenter ${ctx.propertyName}Presenter,
      final ${ctx.modelName}RequestMapper ${ctx.propertyName}RequestMapper,
      final ${ctx.modelName}Facade ${ctx.propertyName}Facade) {
    this.${ctx.propertyName}Presenter = ${ctx.propertyName}Presenter;
    this.${ctx.propertyName}RequestMapper = ${ctx.propertyName}RequestMapper;
    this.${ctx.propertyName}Facade = ${ctx.propertyName}Facade;
  }

  // TODO: Implementation
}
"""
    }
}