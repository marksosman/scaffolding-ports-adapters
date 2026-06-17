package scaffolding.templates

import scaffolding.DomainModelContext

class CoreDomainTemplatesTest {
    private final DomainModelContext ctx

    CoreDomainTemplatesTest(DomainModelContext ctx) {
        this.ctx = ctx
    }

    String providerTest() {
        return """\
package ${ctx.rootPackage}.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.when;

import ${ctx.rootPackage}.core.model.${ctx.modelName};
import ${ctx.rootPackage}.core.model.attribute.${ctx.modelName}Id;
import ${ctx.rootPackage}.core.ports.outgoing.${ctx.modelName}Repository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ${ctx.modelName}ProviderTest {

  @Mock private ${ctx.modelName}Repository ${ctx.propertyName}Repository;

  private ${ctx.modelName}Provider testSubject;

  @BeforeEach
  void setup() {
    testSubject = new ${ctx.modelName}Provider(${ctx.propertyName}Repository);
  }

  @Test
  void unknown${ctx.modelName}Id_fetch_throwsNoSuchElementException() {
    // given
    final ${ctx.modelName}Id ${ctx.propertyName}Id = ${ctx.modelName}Id.generate();
    when(${ctx.propertyName}Repository.retrieve(${ctx.propertyName}Id)).thenReturn(Optional.empty());
    // when
    final Throwable thrown = catchThrowable(() -> testSubject.fetch${ctx.modelName}(${ctx.propertyName}Id));
    // then
    assertThat(thrown).isInstanceOf(NoSuchElementException.class);
  }
}
"""
    }

    String boundedContextArchTest() {
        return """\
package ${ctx.rootPackage};

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = ${ctx.modelName}BoundedContextArchTest.PACKAGE_NAME, importOptions = ImportOption.DoNotIncludeTests.class)
public class ${ctx.modelName}BoundedContextArchTest {

  static final String PACKAGE_NAME = "${ctx.rootPackage}";

  @ArchTest
  public static final ArchRule coreMustNotDependOnApplication =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat().resideInAPackage("..application..")
          .because("core must not depend on application");

  @ArchTest
  public static final ArchRule coreMustNotDependOnInfrastructure =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
          .because("core must not depend on infrastructure");

  @ArchTest
  public static final ArchRule applicationMustNotDependOnInfrastructure =
      noClasses()
          .that().resideInAPackage("..application..")
          .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
          .because("application must not depend on infrastructure");
}
"""
    }

    String boundedContextArchTestOnly() {
        return """\
package ${ctx.rootPackage};

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = ${ctx.modelName}BoundedContextArchTestOnlyTest.PACKAGE_NAME, importOptions = ImportOption.OnlyIncludeTests.class)
public class ${ctx.modelName}BoundedContextArchTestOnlyTest {

  static final String PACKAGE_NAME = "${ctx.rootPackage}";

  @ArchTest
  public static final ArchRule testCoreMustNotDependOnApplication =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat().resideInAPackage("..application..")
          .because("core tests must not depend on application");

  @ArchTest
  public static final ArchRule testCoreMustNotDependOnInfrastructure =
      noClasses()
          .that().resideInAPackage("..core..")
          .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
          .because("core tests must not depend on infrastructure");

  @ArchTest
  public static final ArchRule testApplicationMustNotDependOnInfrastructure =
      noClasses()
          .that().resideInAPackage("..application..")
          .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
          .because("application tests must not depend on infrastructure");
}
"""
    }
}