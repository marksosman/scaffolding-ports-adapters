package scaffolding

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertTrue

class ScaffoldingDirectoryStructureTest extends ScaffoldingPluginBaseTest {

    @Test
    void 'uses default root package when not configured'() {
        new File(projectDir, 'build.gradle').text = """
            plugins {
                id 'ch.sosman.scaffolding'
            }
        """

        run('scaffolding')

        assertPackageStructureExists('com/example')
    }

    @Test
    void 'creates ports and adapters structure from rootPackage only'() {
        writeBuildFile(rootPackage: 'ch.sosman')

        run('scaffolding')

        assertPackageStructureExists('ch/sosman')
    }

    @Test
    void 'creates ports and adapters structure with applicationName'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        run('scaffolding')

        assertPackageStructureExists('ch/sosman/myapp')
    }

    @Test
    void 'creates ports and adapters structure with domain'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp', domain: 'orders')

        run('scaffolding')

        assertPackageStructureExists('ch/sosman/myapp/orders')
    }

    @Test
    void 'creates subdomain under domain package only'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp', domain: 'orders', subdomain: 'returns')

        run('scaffolding')

        ['application', 'crosscutting', 'infrastructure'].each { pkg ->
            def dir = new File(projectDir, "src/main/java/ch/sosman/myapp/orders/${pkg}")
            assertTrue(dir.exists(), "Expected directory to exist: ${dir.path}")
        }

        def subdomainDir = new File(projectDir, 'src/main/java/ch/sosman/myapp/orders/domain/returns')
        assertTrue(subdomainDir.exists(), "Expected subdomain directory to exist: ${subdomainDir.path}")
    }

    @Test
    void 'handles nested root package correctly'() {
        writeBuildFile(rootPackage: 'ch.sosman.myapp')

        run('scaffolding')

        assertPackageStructureExists('ch/sosman/myapp')
    }
}