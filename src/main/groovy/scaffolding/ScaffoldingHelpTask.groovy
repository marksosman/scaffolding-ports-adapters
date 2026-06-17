package scaffolding

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = 'Help task always prints current output')
class ScaffoldingHelpTask extends DefaultTask {

    @TaskAction
    void printHelp() {
        logger.lifecycle("""
╔════════════════════════════════════════════════════════════════════════════
║  Sosman Scaffolding Plugin — Usage Reference
╠════════════════════════════════════════════════════════════════════════════
║
║  TASKS
║    ./gradlew scaffolding              – Generate base P&A directory structure
║    ./gradlew scaffoldDomainModel      – Generate bounded context source files
║    ./gradlew scaffoldingHelp          – Show this help
║
╠════════════════════════════════════════════════════════════════════════════
║  build.gradle CONFIGURATION SNIPPET
╠════════════════════════════════════════════════════════════════════════════

plugins {
    id 'ch.sosman.scaffolding' version '0.0.1'
}

scaffolding {
    rootPackage     = 'ch.sosman.bz'        // required
    applicationName = 'myapp'            // optional – appended to rootPackage
    domain          = 'orders'           // optional – sub-package under applicationName
    subdomain       = 'returns'          // optional – sub-package under domain
    dryRun          = false              // preview without writing any files
}

╠════════════════════════════════════════════════════════════════════════════
║  FEATURE FLAGS  (set in extension or override with -P at runtime)
╠════════════════════════════════════════════════════════════════════════════

  Flag                Default   Generates
  ────────────────────────────────────────────────────────────────────────
  withCoreDomain      true      OrderId, Order, OrderFacade, OrderRepository,
                                OrderProvider + ProviderTest + ArchUnit tests
  withPersistence     true      OrderDbEntity, OrderDb, OrderDbMapper,
                                OrderDbAdapter + DbAdapterTest
  withProvidedPort    true      Presenter, RequestMapper, ResponseMapper,
                                ProvidedPort + ProvidedPortTest
                                ⚠ mutually exclusive with withRestController
  withRestController  false     Presenter, RequestMapper, ResponseMapper,
                                @RestController + ControllerTest
                                ⚠ mutually exclusive with withProvidedPort
  withEventPublisher  false     Publisher interface + Spring EventPublisher adapter
                                + EventPublisherTest
  withEventListener   false     EventFacade, EventProvider, EventListener
                                + EventListenerTest
  withEventSourcing   false     State, EventProjector, EventRepository,
                                EventAdapter + EventAdapterTest

╠════════════════════════════════════════════════════════════════════════════
║  CLI FLAGS  (override extension values at runtime)
╠════════════════════════════════════════════════════════════════════════════

  # Generate the base directory structure
  ./gradlew scaffolding

  # Generate a bounded context (domainModelName is required)
  ./gradlew scaffoldDomainModel -PdomainModelName=Order

  # Preview without writing
  ./gradlew scaffoldDomainModel -PdomainModelName=Order -PdryRun=true

  # Toggle feature flags per invocation
  ./gradlew scaffoldDomainModel -PdomainModelName=Order \\\\
      -PwithCoreDomain=true        \\\\
      -PwithPersistence=true       \\\\
      -PwithRestController=true    \\\\
      -PwithEventSourcing=true     \\\\
      -PwithEventListener=true     \\\\
      -PwithProvidedPort=false     \\\\
      -PwithEventPublisher=false

╠════════════════════════════════════════════════════════════════════════════
║  GENERATED STRUCTURE EXAMPLE  (Order, all features enabled)
╠════════════════════════════════════════════════════════════════════════════

  src/main/java/.../order/
  ├── application/
  │   ├── http/               Presenter, RequestMapper, ResponseMapper,
  │   │                       Controller (withRestController) | ProvidedPort (withProvidedPort)
  │   └── messaging/          OrderEventListener.java        (withEventListener)
  ├── core/
  │   ├── model/
  │   │   ├── attribute/      OrderId.java, OrderState.java  (withEventSourcing)
  │   │   ├── Order.java
  │   │   └── OrderEventProjector.java                       (withEventSourcing)
  │   ├── ports/
  │   │   ├── incoming/       OrderFacade.java, OrderEventFacade.java
  │   │   └── outgoing/       OrderRepository.java, OrderEventRepository.java,
  │   │                       OrderPublisher.java            (withEventPublisher)
  │   ├── OrderProvider.java
  │   └── OrderEventProvider.java
  └── infrastructure/
      ├── messaging/          EventMapper, EventPublisher    (withEventPublisher)
      ├── persistence/        DbEntity, Db, DbMapper, DbAdapter
      └── OrderEventAdapter.java                             (withEventSourcing)

  src/test/java/.../order/
  ├── OrderBoundedContextArchTest.java
  ├── OrderBoundedContextArchTestOnlyTest.java
  ├── application/
  │   ├── http/               OrderControllerTest.java (withRestController)
  │   │                       | OrderProvidedPortTest.java (withProvidedPort)
  │   └── messaging/          OrderEventListenerTest.java    (withEventListener)
  ├── core/                   OrderProviderTest.java
  └── infrastructure/
      ├── messaging/          OrderEventPublisherTest.java   (withEventPublisher)
      ├── persistence/        OrderDbAdapterTest.java
      └── OrderEventAdapterTest.java                         (withEventSourcing)

╚════════════════════════════════════════════════════════════════════════════
""")
    }
}