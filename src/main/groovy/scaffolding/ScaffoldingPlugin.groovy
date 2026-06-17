package scaffolding

import org.gradle.api.Plugin
import org.gradle.api.Project

class ScaffoldingPlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {

        def extension = project.extensions.create('scaffolding', ScaffoldingExtension)

        project.tasks.register('scaffoldingHelp', ScaffoldingHelpTask) { task ->
            task.group = 'scaffolding'
            task.description = 'Show plugin usage, DSL reference, and CLI flag examples'
        }

        project.tasks.register('scaffolding', ScaffoldingTask) { task ->
            task.group = 'scaffolding'
            task.description = 'Generate base Ports & Adapters directory structure'
        }

        project.tasks.register('scaffoldDomainModel', DomainModelTask) { task ->
            task.group = 'scaffolding'
            task.description = 'Generate Ports & Adapters code for a bounded context (-PdomainModelName=Name)'
        }

        project.afterEvaluate {
            project.tasks.named('scaffolding', ScaffoldingTask) { task ->
                task.rootPackage     = extension.rootPackage
                task.applicationName = extension.applicationName
                task.domain          = extension.domain
                task.subdomain       = extension.subdomain
                task.dryRun = resolveBoolean(project, 'dryRun', extension.dryRun)
            }

            project.tasks.named('scaffoldDomainModel', DomainModelTask) { task ->
                task.rootPackage       = extension.rootPackage
                task.applicationName   = extension.applicationName
                task.domain            = extension.domain
                task.subdomain         = extension.subdomain
                task.domainModelName   = project.findProperty('domainModelName') ?: ''
                task.dryRun            = resolveBoolean(project, 'dryRun',            extension.dryRun)
                task.withCoreDomain    = resolveBoolean(project, 'withCoreDomain',    extension.withCoreDomain)
                task.withPersistence   = resolveBoolean(project, 'withPersistence',   extension.withPersistence)
                task.withProvidedPort  = resolveBoolean(project, 'withProvidedPort',  extension.withProvidedPort)
                task.withEventPublisher = resolveBoolean(project, 'withEventPublisher', extension.withEventPublisher)
                task.withEventListener  = resolveBoolean(project, 'withEventListener',  extension.withEventListener)
                task.withRestController = resolveBoolean(project, 'withRestController', extension.withRestController)
                task.withEventSourcing  = resolveBoolean(project, 'withEventSourcing',  extension.withEventSourcing)
            }
        }
    }

    private static boolean resolveBoolean(Project project, String key, boolean fallback) {
        project.hasProperty(key) ? project.property(key).toBoolean() : fallback
    }
}