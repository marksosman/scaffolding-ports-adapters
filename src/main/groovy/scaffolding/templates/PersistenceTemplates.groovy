package scaffolding.templates

import scaffolding.DomainModelContext

class PersistenceTemplates {
    private final DomainModelContext ctx

    PersistenceTemplates(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String dbEntity() {
        return """\
package ${ctx.rootPackage}.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcType;
import org.hibernate.envers.Audited;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

@Entity
@Table(name = "${ctx.tableName}")
@Audited
public class ${ctx.modelName}DbEntity {

  @Id
  @JdbcType(CharJdbcType.class)
  @Column(length = 36)
  private UUID ${ctx.propertyName}Id;

  public UUID get${ctx.modelName}Id() {
    return ${ctx.propertyName}Id;
  }

  public void set${ctx.modelName}Id(final UUID uuid) {
    this.${ctx.propertyName}Id = uuid;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if (obj.getClass() != getClass()) return false;
    final ${ctx.modelName}DbEntity ${ctx.propertyName} = (${ctx.modelName}DbEntity) obj;
    return Objects.equals(${ctx.propertyName}Id, ${ctx.propertyName}.${ctx.propertyName}Id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(${ctx.propertyName}Id);
  }
}
"""
    }

    String dbRepository() {
        return """\
package ${ctx.rootPackage}.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ${ctx.rootPackage}.infrastructure.persistence.model.${ctx.modelName}DbEntity;

@Repository
public interface ${ctx.modelName}Db extends JpaRepository<${ctx.modelName}DbEntity, UUID> {

  Optional<${ctx.modelName}DbEntity> findBy${ctx.modelName}Id(UUID ${ctx.propertyName}Id);
}
"""
    }

    String dbMapper() {
        return """\
package ${ctx.rootPackage}.infrastructure.persistence;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.infrastructure.persistence.model.${ctx.modelName}DbEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ${ctx.modelName}DbMapper {

  @Mapping(target = "${ctx.propertyName}Id", source = "${ctx.propertyName}Id.value")
  ${ctx.modelName}DbEntity from(final ${ctx.modelName} ${ctx.propertyName});

  default ${ctx.modelName} toDomain(final ${ctx.modelName}DbEntity entity) {
    if (entity == null) return null;
    return ${ctx.modelName}.with${ctx.modelName}Id(${ctx.modelName}Id.fromUuid(entity.get${ctx.modelName}Id())).build();
  }
}
"""
    }

    String dbAdapter() {
        return """\
package ${ctx.rootPackage}.infrastructure.persistence;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}Repository;
import ${ctx.rootPackage}.infrastructure.persistence.model.${ctx.modelName}DbEntity;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ${ctx.modelName}DbAdapter implements ${ctx.modelName}Repository {

  private final ${ctx.modelName}Db ${ctx.propertyName}Db;
  private final ${ctx.modelName}DbMapper ${ctx.propertyName}DbMapper;

  public ${ctx.modelName}DbAdapter(
      final ${ctx.modelName}Db ${ctx.propertyName}Db,
      final ${ctx.modelName}DbMapper ${ctx.propertyName}DbMapper) {
    this.${ctx.propertyName}Db = ${ctx.propertyName}Db;
    this.${ctx.propertyName}DbMapper = ${ctx.propertyName}DbMapper;
  }

  @Override
  public Optional<${ctx.modelName}> retrieve(final ${ctx.modelName}Id ${ctx.propertyName}Id) {
    return ${ctx.propertyName}Db.findBy${ctx.modelName}Id(${ctx.propertyName}Id.value()).map(${ctx.propertyName}DbMapper::toDomain);
  }

  @Override
  public ${ctx.modelName}Id add(final ${ctx.modelName} ${ctx.propertyName}) {
    final ${ctx.modelName}DbEntity newEntity = ${ctx.propertyName}DbMapper.from(${ctx.propertyName});
    final ${ctx.modelName}DbEntity createdEntity = ${ctx.propertyName}Db.save(newEntity);
    return ${ctx.modelName}Id.fromUuid(createdEntity.get${ctx.modelName}Id());
  }
}
"""
    }
}