package scaffolding

class ScaffoldingExtension {
    String rootPackage = 'com.example'
    String applicationName = null
    String domain = null
    String subdomain = null
    boolean dryRun = false

    boolean withCoreDomain = true
    boolean withPersistence = true
    boolean withProvidedPort = true
    boolean withEventPublisher = false
    boolean withEventListener = false
    boolean withRestController = false
    boolean withEventSourcing = false
}