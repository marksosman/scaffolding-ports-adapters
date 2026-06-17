package scaffolding

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import scaffolding.templates.CoreDomainTemplates
import scaffolding.templates.CoreDomainTemplatesTest
import scaffolding.templates.EventListenerTemplates
import scaffolding.templates.EventListenerTemplatesTest
import scaffolding.templates.EventPublisherTemplates
import scaffolding.templates.EventPublisherTemplatesTest
import scaffolding.templates.EventSourcingTemplates
import scaffolding.templates.PersistenceTemplates
import scaffolding.templates.PersistenceTemplatesTest
import scaffolding.templates.EventSourcingTemplatesTest
import scaffolding.templates.ProvidedPortTemplates
import scaffolding.templates.ProvidedPortTemplatesTest
import scaffolding.templates.RestControllerTemplates
import scaffolding.templates.RestControllerTemplatesTest

@DisableCachingByDefault(because = 'Domain model scaffolding generates code and should always run explicitly')
class DomainModelTask extends DefaultTask {

    @Input
    String rootPackage

    @Input
    @Optional
    String applicationName

    @Input
    @Optional
    String domain

    @Input
    @Optional
    String subdomain

    @Input
    @Optional
    String domainModelName

    @Input
    boolean dryRun = false

    @Input
    boolean withCoreDomain = true

    @Input
    boolean withPersistence = true

    @Input
    boolean withProvidedPort = true

    @Input
    boolean withEventPublisher = false

    @Input
    boolean withEventListener = false

    @Input
    boolean withRestController = false

    @Input
    boolean withEventSourcing = false

    @TaskAction
    void scaffold() {
        if (!domainModelName?.trim()) {
            throw new IllegalArgumentException('domainModelName is required. Use -PdomainModelName=YourModelName')
        }
        StringUtils.validateRootPackage(rootPackage)
        if (withProvidedPort && withRestController) {
            throw new IllegalArgumentException('withProvidedPort and withRestController are mutually exclusive — choose one adapter type')
        }

        def ctx = DomainModelContext.create(
                project.projectDir,
                rootPackage,
                applicationName,
                domain,
                subdomain,
                domainModelName,
                withCoreDomain,
                withPersistence,
                withProvidedPort,
                withEventPublisher,
                withEventListener,
                withRestController,
                withEventSourcing)

        def fileCreator = new FileCreator(dryRun, logger)

        printConfiguration(ctx)
        if (dryRun) logger.lifecycle('\n[DRY RUN MODE] No files will be created\n')

        if (ctx.withCoreDomain)     generateCoreDomain(ctx, new CoreDomainTemplates(ctx), fileCreator)
        if (ctx.withPersistence)    generatePersistence(ctx, new PersistenceTemplates(ctx), fileCreator)
        if (ctx.withProvidedPort)   generateProvidedPort(ctx, new ProvidedPortTemplates(ctx), fileCreator)
        if (ctx.withEventPublisher) generateEventPublisher(ctx, new EventPublisherTemplates(ctx), fileCreator)
        if (ctx.withEventListener)  generateEventListener(ctx, new EventListenerTemplates(ctx), fileCreator)
        if (ctx.withRestController) generateRestController(ctx, new RestControllerTemplates(ctx), fileCreator)
        if (ctx.withEventSourcing)  generateEventSourcing(ctx, new EventSourcingTemplates(ctx), fileCreator)

        printSummary(ctx, fileCreator)
    }

    private void generateSection(String label, FileCreator fc, Closure body) {
        logger.lifecycle("\n[${label}] Generating...")
        body(fc)
        logger.lifecycle("[${label}] Done")
    }

    private void generateCoreDomain(DomainModelContext ctx, CoreDomainTemplates t, FileCreator fc) {
        generateSection('Core Domain', fc) {
            ['root', 'core', 'coreModel', 'attribute',
             'corePorts', 'portsIncoming', 'portsOutgoing', 'infrastructure',
             'testRoot', 'testCore', 'testCoreModel'].each {
                fc.createDirectory(ctx.directories[it])
            }

            fc.createFile(new File(ctx.directories.attribute,     "${ctx.modelName}Id.java"),           t.idValueObject())
            fc.createFile(new File(ctx.directories.coreModel,     "${ctx.modelName}.java"),             t.aggregateRoot())
            fc.createFile(new File(ctx.directories.portsIncoming, "${ctx.modelName}Facade.java"),       t.facade())
            fc.createFile(new File(ctx.directories.portsOutgoing, "${ctx.modelName}Repository.java"),   t.repository())
            fc.createFile(new File(ctx.directories.core,          "${ctx.modelName}Provider.java"),     t.provider())

            def tt = new CoreDomainTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testCore, "${ctx.modelName}ProviderTest.java"),                   tt.providerTest())
            fc.createFile(new File(ctx.directories.testRoot, "${ctx.modelName}BoundedContextArchTest.java"),         tt.boundedContextArchTest())
            fc.createFile(new File(ctx.directories.testRoot, "${ctx.modelName}BoundedContextArchTestOnlyTest.java"), tt.boundedContextArchTestOnly())
        }
    }

    private void generatePersistence(DomainModelContext ctx, PersistenceTemplates t, FileCreator fc) {
        generateSection('Persistence', fc) {
            ['persistence', 'persistenceModel', 'testPersistence'].each { fc.createDirectory(ctx.directories[it]) }

            fc.createFile(new File(ctx.directories.persistenceModel, "${ctx.modelName}DbEntity.java"),  t.dbEntity())
            fc.createFile(new File(ctx.directories.persistence,      "${ctx.modelName}Db.java"),        t.dbRepository())
            fc.createFile(new File(ctx.directories.persistence,      "${ctx.modelName}DbMapper.java"),  t.dbMapper())
            fc.createFile(new File(ctx.directories.persistence,      "${ctx.modelName}DbAdapter.java"), t.dbAdapter())

            def tt = new PersistenceTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testPersistence, "${ctx.modelName}DbAdapterTest.java"), tt.dbAdapterTest())
        }
    }

    private void generateProvidedPort(DomainModelContext ctx, ProvidedPortTemplates t, FileCreator fc) {
        generateSection('Provided Port', fc) {
            ['http', 'testHttp'].each { fc.createDirectory(ctx.directories[it]) }

            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}Presenter.java"),      t.presenter())
            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}RequestMapper.java"),  t.requestMapper())
            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}ResponseMapper.java"), t.responseMapper())
            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}ProvidedPort.java"),   t.providedPort())

            def tt = new ProvidedPortTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testHttp, "${ctx.modelName}ProvidedPortTest.java"), tt.providedPortTest())
        }
    }

    private void generateEventPublisher(DomainModelContext ctx, EventPublisherTemplates t, FileCreator fc) {
        generateSection('Event Publisher', fc) {
            ['infrastructureMessaging', 'testMessaging'].each { fc.createDirectory(ctx.directories[it]) }

            fc.createFile(new File(ctx.directories.infrastructureMessaging, "${ctx.modelName}EventMapper.java"),    t.eventMapper())
            fc.createFile(new File(ctx.directories.portsOutgoing,           "${ctx.modelName}Publisher.java"),      t.publisherInterface())
            fc.createFile(new File(ctx.directories.infrastructureMessaging, "${ctx.modelName}EventPublisher.java"), t.eventPublisher())

            def tt = new EventPublisherTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testMessaging, "${ctx.modelName}EventPublisherTest.java"), tt.eventPublisherTest())
        }
    }

    private void generateEventListener(DomainModelContext ctx, EventListenerTemplates t, FileCreator fc) {
        generateSection('Event Listener', fc) {
            ['applicationMessaging', 'testMessaging'].each { fc.createDirectory(ctx.directories[it]) }

            fc.createFile(new File(ctx.directories.portsIncoming,       "${ctx.modelName}EventFacade.java"),    t.eventFacade())
            fc.createFile(new File(ctx.directories.core,                 "${ctx.modelName}EventProvider.java"), t.eventProvider())
            fc.createFile(new File(ctx.directories.applicationMessaging, "${ctx.modelName}EventListener.java"), t.eventListener())

            def tt = new EventListenerTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testMessaging, "${ctx.modelName}EventListenerTest.java"), tt.eventListenerTest())
        }
    }

    private void generateRestController(DomainModelContext ctx, RestControllerTemplates t, FileCreator fc) {
        generateSection('Rest Controller', fc) {
            ['http', 'testHttp'].each { fc.createDirectory(ctx.directories[it]) }

            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}Presenter.java"),      t.presenter())
            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}RequestMapper.java"),  t.requestMapper())
            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}ResponseMapper.java"), t.responseMapper())
            fc.createFile(new File(ctx.directories.http, "${ctx.modelName}Controller.java"),     t.controller())

            def tt = new RestControllerTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testHttp, "${ctx.modelName}ControllerTest.java"), tt.controllerTest())
        }
    }

    private void generateEventSourcing(DomainModelContext ctx, EventSourcingTemplates t, FileCreator fc) {
        generateSection('Event Sourcing', fc) {
            fc.createDirectory(ctx.directories.testInfrastructure)

            fc.createFile(new File(ctx.directories.core,          "${ctx.modelName}Event.java"),          t.eventMarkerInterface())
            fc.createFile(new File(ctx.directories.attribute,     "${ctx.modelName}State.java"),           t.state())
            fc.createFile(new File(ctx.directories.coreModel,     "${ctx.modelName}EventProjector.java"),  t.eventProjector())
            fc.createFile(new File(ctx.directories.portsOutgoing, "${ctx.modelName}EventRepository.java"), t.eventRepository())
            fc.createFile(new File(ctx.directories.infrastructure,"${ctx.modelName}EventAdapter.java"),    t.eventAdapter())

            def tt = new EventSourcingTemplatesTest(ctx)
            fc.createFile(new File(ctx.directories.testInfrastructure, "${ctx.modelName}EventAdapterTest.java"), tt.eventAdapterTest())
        }
    }

    private void printConfiguration(DomainModelContext ctx) {
        logger.lifecycle("""
╔════════════════════════════════════════════════════════════════
║ Domain Model Scaffolding
╠════════════════════════════════════════════════════════════════
║ Model Name:        ${ctx.modelName}
║ Package:           ${ctx.rootPackage}
║
║ Features:
║   - Core Domain:     ${ctx.withCoreDomain      ? '[YES]' : '[NO]'}
║   - Persistence:     ${ctx.withPersistence     ? '[YES]' : '[NO]'}
║   - Provided Port:   ${ctx.withProvidedPort    ? '[YES]' : '[NO]'}
║   - Event Publisher: ${ctx.withEventPublisher  ? '[YES]' : '[NO]'}
║   - Event Listener:  ${ctx.withEventListener   ? '[YES]' : '[NO]'}
║   - Rest Controller: ${ctx.withRestController  ? '[YES]' : '[NO]'}
║   - Event Sourcing:  ${ctx.withEventSourcing   ? '[YES]' : '[NO]'}
╚════════════════════════════════════════════════════════════════
""")
    }

    private void printSummary(DomainModelContext ctx, FileCreator fc) {
        def stats = fc.getStats()
        logger.lifecycle("""
╔════════════════════════════════════════════════════════════════
║ ${dryRun ? '[DRY RUN] ' : ''}Scaffolding Summary
╠════════════════════════════════════════════════════════════════
║ Model:             ${ctx.modelName}
║ Files Created:     ${stats.created}
║ Files Skipped:     ${stats.skipped}
║
║ Next Steps:
║   1. Review generated code in: ${ctx.rootPath}
║   2. Implement business logic in ${ctx.modelName}Provider
║   3. Add validation and business rules
║   4. Add appropriate tests
║
╚════════════════════════════════════════════════════════════════
""")
    }
}
