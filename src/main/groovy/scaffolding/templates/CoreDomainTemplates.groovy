package scaffolding.templates

import scaffolding.DomainModelContext

class CoreDomainTemplates {
    private final DomainModelContext ctx

    CoreDomainTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String idValueObject() {
        return """\
package ${ctx.rootPackage}.core.model.attribute;

import java.util.Objects;
import java.util.UUID;

public record ${ctx.modelName}Id(UUID value) {

  public ${ctx.modelName}Id {
    Objects.requireNonNull(value, "value must not be null");
  }

  @Override
  public String toString() {
    return value.toString();
  }

  public static ${ctx.modelName}Id fromUuid(final UUID uuid) {
    Objects.requireNonNull(uuid, "uuid must not be null");
    return new ${ctx.modelName}Id(uuid);
  }

  public static ${ctx.modelName}Id generate() {
    return new ${ctx.modelName}Id(UUID.randomUUID());
  }
}
"""
    }

    String aggregateRoot() {
        return """\
package ${ctx.rootPackage}.core.model;

import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import java.util.Objects;

public class ${ctx.modelName} {

  private final ${ctx.modelName}Id ${ctx.propertyName}Id;

  private ${ctx.modelName}(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
    this.${ctx.propertyName}Id = ${ctx.propertyName}Id;
  }

  public ${ctx.modelName}Id getId() {
    return ${ctx.propertyName}Id;
  }

  public static ${ctx.modelName}Creator with${ctx.modelName}Id(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
    return new ${ctx.modelName}Builder().with${ctx.modelName}Id(${ctx.propertyName}Id);
  }

  public sealed interface ${ctx.modelName}Creator permits ${ctx.modelName}Builder {
    ${ctx.modelName} build();
  }

  private static final class ${ctx.modelName}Builder implements ${ctx.modelName}Creator {
    private ${ctx.modelName}Id ${ctx.propertyName}Id;

    private ${ctx.modelName}Builder() {}

    public ${ctx.modelName}Creator with${ctx.modelName}Id(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
      this.${ctx.propertyName}Id = ${ctx.propertyName}Id;
      return this;
    }

    @Override
    public ${ctx.modelName} build() {
      return new ${ctx.modelName}(${ctx.propertyName}Id);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if (obj.getClass() != getClass()) return false;
    final ${ctx.modelName} ${ctx.propertyName} = (${ctx.modelName}) obj;
    return Objects.equals(${ctx.propertyName}Id, ${ctx.propertyName}.${ctx.propertyName}Id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(${ctx.propertyName}Id);
  }
}
"""
    }

    String facade() {
        return """\
package ${ctx.rootPackage}.core.ports.incoming;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;

public interface ${ctx.modelName}Facade {

  ${ctx.modelName}Id create${ctx.modelName}(${ctx.modelName} ${ctx.propertyName});

  ${ctx.modelName} fetch${ctx.modelName}(${ctx.modelName}Id ${ctx.propertyName}Id);
}
"""
    }

    String repository() {
        return """\
package ${ctx.rootPackage}.core.ports.outgoing;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import java.util.Optional;

public interface ${ctx.modelName}Repository {

  Optional<${ctx.modelName}> retrieve(${ctx.modelName}Id ${ctx.propertyName}Id);

  ${ctx.modelName}Id add(${ctx.modelName} ${ctx.propertyName});
}
"""
    }

    String provider() {
        return """\
package ${ctx.rootPackage}.core;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.core.ports.incoming.${ctx.modelName}Facade;
import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}Repository;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}Provider implements ${ctx.modelName}Facade {

  private final ${ctx.modelName}Repository ${ctx.propertyName}Repository;

  public ${ctx.modelName}Provider(final ${ctx.modelName}Repository ${ctx.propertyName}Repository) {
    this.${ctx.propertyName}Repository = ${ctx.propertyName}Repository;
  }

  @Override
  public ${ctx.modelName}Id create${ctx.modelName}(final ${ctx.modelName} ${ctx.propertyName}) {
    throw new UnsupportedOperationException("TODO: implement create${ctx.modelName} logic");
  }

  @Override
  public ${ctx.modelName} fetch${ctx.modelName}(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
    return ${ctx.propertyName}Repository.retrieve(${ctx.propertyName}Id).orElseThrow();
  }
}
"""
    }
}