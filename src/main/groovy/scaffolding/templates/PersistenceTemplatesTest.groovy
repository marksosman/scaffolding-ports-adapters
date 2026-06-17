package scaffolding.templates

import scaffolding.DomainModelContext

class PersistenceTemplatesTest {
    private final DomainModelContext ctx

    PersistenceTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String dbAdapterTest() {
        return """\
package ${ctx.rootPackage}.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.infrastructure.persistence.model.${ctx.modelName}DbEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ${ctx.modelName}DbAdapterTest {

  @Mock private ${ctx.modelName}Db ${ctx.propertyName}Db;
  @Mock private ${ctx.modelName} ${ctx.propertyName};

  private ${ctx.modelName}DbAdapter testSubject;

  @BeforeEach
  void setup() {
    testSubject = new ${ctx.modelName}DbAdapter(${ctx.propertyName}Db, new ${ctx.modelName}DbMapperImpl());
  }

  @Test
  void existing${ctx.modelName}_retrieve_return${ctx.modelName}() {
    // given
    final ${ctx.modelName}Id ${ctx.propertyName}Id = ${ctx.modelName}Id.generate();
    final ${ctx.modelName}DbEntity entity = new ${ctx.modelName}DbEntity();
    entity.set${ctx.modelName}Id(${ctx.propertyName}Id.value());
    when(${ctx.propertyName}Db.findBy${ctx.modelName}Id(${ctx.propertyName}Id.value())).thenReturn(Optional.of(entity));
    // when
    final Optional<${ctx.modelName}> result = testSubject.retrieve(${ctx.propertyName}Id);
    // then
    assertThat(result).isPresent();
    assertThat(result.get().getId()).isEqualTo(${ctx.propertyName}Id);
  }

  @Test
  void unknown${ctx.modelName}Id_retrieve_returnEmpty() {
    // given
    final ${ctx.modelName}Id ${ctx.propertyName}Id = ${ctx.modelName}Id.generate();
    when(${ctx.propertyName}Db.findBy${ctx.modelName}Id(${ctx.propertyName}Id.value())).thenReturn(Optional.empty());
    // when
    final Optional<${ctx.modelName}> result = testSubject.retrieve(${ctx.propertyName}Id);
    // then
    assertThat(result).isEmpty();
  }

  @Test
  void valid${ctx.modelName}_add_return${ctx.modelName}Id() {
    // given
    final ${ctx.modelName}Id ${ctx.propertyName}Id = ${ctx.modelName}Id.generate();
    final ${ctx.modelName}DbEntity entity = new ${ctx.modelName}DbEntity();
    entity.set${ctx.modelName}Id(${ctx.propertyName}Id.value());
    when(${ctx.propertyName}Db.save(any(${ctx.modelName}DbEntity.class))).thenReturn(entity);
    // when
    final ${ctx.modelName}Id result = testSubject.add(${ctx.propertyName});
    // then
    assertThat(result).isEqualTo(${ctx.propertyName}Id);
  }
}
"""
    }
}