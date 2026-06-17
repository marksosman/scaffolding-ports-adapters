package scaffolding

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

class ScaffoldingDryRunForceTest extends ScaffoldingPluginBaseTest {

    @Test
    void 'dryRun does not create any directories'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp', dryRun: true)

        run('scaffolding')

        ['application', 'crosscutting', 'domain', 'infrastructure'].each { pkg ->
            def dir = new File(projectDir, "src/main/java/ch/sosman/myapp/${pkg}")
            assertFalse(dir.exists(), "Directory should not exist in dry-run mode: ${dir.path}")
        }
    }

    @Test
    void 'dryRun output contains DRY RUN marker'() {
        writeBuildFile(rootPackage: 'ch.sosman', dryRun: true)

        def result = run('scaffolding')

        assertTrue(result.output.contains('[DRY RUN]'), "Expected [DRY RUN] marker in output")
    }

    @Test
    void 'without dryRun directories are created normally'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        run('scaffolding')

        assertPackageStructureExists('ch/sosman/myapp')
    }
}