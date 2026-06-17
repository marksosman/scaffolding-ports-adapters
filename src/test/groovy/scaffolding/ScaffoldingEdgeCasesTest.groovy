package scaffolding

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ScaffoldingEdgeCasesTest extends ScaffoldingPluginBaseTest {

    @Test
    void 'subdomain creates domain subpackage even without domain set'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp', subdomain: 'returns')

        run('scaffolding')

        assertPackageStructureExists('ch/sosman/myapp')

        def subdomainDir = new File(projectDir, 'src/main/java/ch/sosman/myapp/domain/returns')
        assertTrue(subdomainDir.exists(), "Subdomain directory should exist under domain: ${subdomainDir.path}")
    }
}