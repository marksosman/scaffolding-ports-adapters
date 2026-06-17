package scaffolding

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = 'Scaffolding generates project structure and should always run explicitly')
class ScaffoldingTask extends DefaultTask {
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
    boolean dryRun = false

    private static final List<String> PORTS_AND_ADAPTERS_PACKAGES = [
            'application',
            'crosscutting',
            'domain',
            'infrastructure',
    ]

    @TaskAction
    void scaffold() {
        StringUtils.validateRootPackage(rootPackage)
        def basePath = buildBasePath()
        def creator = new FileCreator(dryRun, logger)
        logger.lifecycle("Scaffolding ports & adapters structure under: ${basePath.replace('/', '.')}")

        PORTS_AND_ADAPTERS_PACKAGES.each { pkg ->
            def pkgPath = (pkg == 'domain' && subdomain?.trim()) ? "${pkg}/${subdomain.trim()}" : pkg
            creator.createDirectory("${project.projectDir}/src/main/java/${basePath}/${pkgPath}")
            creator.createDirectory("${project.projectDir}/src/test/java/${basePath}/${pkgPath}")
        }
    }

    private String buildBasePath() {
        def parts = [rootPackage]

        if (applicationName?.trim()) {
            parts << applicationName.trim()
        }

        if (domain?.trim()) {
            parts << domain.trim()
        }

        return parts
                .collect { it.replace('.', '/') }
                .join('/')
    }
}
