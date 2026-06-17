package scaffolding.templates

import scaffolding.DomainModelContext

class RestControllerTemplates {
    private final DomainModelContext ctx

    RestControllerTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String presenter() {
        return """\
package ${ctx.rootPackage}.application.http;

import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}Presenter {

}
"""
    }

    String requestMapper() {
        return """\
package ${ctx.rootPackage}.application.http;

import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}RequestMapper {

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

    String controller() {
        return """\
package ${ctx.rootPackage}.application.http;

import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}Facade;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ${ctx.modelName}Controller {

  private final ${ctx.modelName}Presenter ${ctx.propertyName}Presenter;
  private final ${ctx.modelName}RequestMapper ${ctx.propertyName}RequestMapper;
  private final ${ctx.modelName}Facade ${ctx.propertyName}Facade;

  public ${ctx.modelName}Controller(
      final ${ctx.modelName}Presenter ${ctx.propertyName}Presenter,
      final ${ctx.modelName}RequestMapper ${ctx.propertyName}RequestMapper,
      final ${ctx.modelName}Facade ${ctx.propertyName}Facade) {
    this.${ctx.propertyName}Presenter = ${ctx.propertyName}Presenter;
    this.${ctx.propertyName}RequestMapper = ${ctx.propertyName}RequestMapper;
    this.${ctx.propertyName}Facade = ${ctx.propertyName}Facade;
  }
}
"""
    }

}