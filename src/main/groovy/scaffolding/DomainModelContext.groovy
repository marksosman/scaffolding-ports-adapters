package scaffolding

class DomainModelContext {
    String modelName
    String propertyName
    String tableName
    String rootPath
    String testRootPath
    String rootPackage

    Map<String, String> directories = [:]

    boolean withCoreDomain
    boolean withPersistence
    boolean withProvidedPort
    boolean withEventPublisher
    boolean withEventListener
    boolean withRestController
    boolean withEventSourcing

    static DomainModelContext create(
            File projectDir,
            String rootPackage,
            String applicationName,
            String domain,
            String subdomain,
            String domainModelName,
            boolean withCoreDomain,
            boolean withPersistence,
            boolean withProvidedPort,
            boolean withEventPublisher,
            boolean withEventListener,
            boolean withRestController,
            boolean withEventSourcing) {

        def ctx = new DomainModelContext()
        ctx.withCoreDomain     = withCoreDomain
        ctx.withPersistence    = withPersistence
        ctx.withProvidedPort   = withProvidedPort
        ctx.withEventPublisher = withEventPublisher
        ctx.withEventListener  = withEventListener
        ctx.withRestController = withRestController
        ctx.withEventSourcing  = withEventSourcing
        ctx.modelName    = domainModelName
        ctx.propertyName = StringUtils.uncapitalize(domainModelName)
        ctx.tableName    = StringUtils.convertCamelToSnakeCase(domainModelName)

        def pathParts = [rootPackage.replace('.', '/')]
        def pkgParts  = [rootPackage]
        if (applicationName?.trim()) { pathParts << applicationName.trim(); pkgParts << applicationName.trim() }
        if (domain?.trim())          { pathParts << domain.trim();          pkgParts << domain.trim()          }

        def basePath    = pathParts.join('/')
        def basePackage = pkgParts.join('.')

        def domainDir = subdomain?.trim() ? "domain/${subdomain.trim()}" : 'domain'
        def domainPkg = subdomain?.trim() ? "domain.${subdomain.trim()}" : 'domain'
        def modelPkg  = domainModelName.toLowerCase()

        ctx.rootPath     = "${projectDir}/src/main/java/${basePath}/${domainDir}/${modelPkg}"
        ctx.testRootPath = "${projectDir}/src/test/java/${basePath}/${domainDir}/${modelPkg}"
        ctx.rootPackage  = "${basePackage}.${domainPkg}.${modelPkg}"

        ctx.initDirectories()
        return ctx
    }

    private void initDirectories() {
        directories = [
                root                   : rootPath,
                application            : "${rootPath}/application",
                http                   : "${rootPath}/application/http",
                applicationMessaging   : "${rootPath}/application/messaging",
                core                   : "${rootPath}/core",
                coreModel              : "${rootPath}/core/model",
                attribute              : "${rootPath}/core/model/attribute",
                corePorts              : "${rootPath}/core/ports",
                portsIncoming          : "${rootPath}/core/ports/incoming",
                portsOutgoing          : "${rootPath}/core/ports/outgoing",
                infrastructure         : "${rootPath}/infrastructure",
                persistence            : "${rootPath}/infrastructure/persistence",
                persistenceModel       : "${rootPath}/infrastructure/persistence/model",
                infrastructureMessaging: "${rootPath}/infrastructure/messaging",
                testRoot               : testRootPath,
                testCore               : "${testRootPath}/core",
                testCoreModel          : "${testRootPath}/core/model",
                testApplication        : "${testRootPath}/application",
                testHttp               : "${testRootPath}/application/http",
                testMessaging          : "${testRootPath}/application/messaging",
                testInfrastructure     : "${testRootPath}/infrastructure",
                testPersistence        : "${testRootPath}/infrastructure/persistence",
        ]
    }
}
