package scaffolding

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir

import static org.junit.jupiter.api.Assertions.assertTrue

abstract class ScaffoldingPluginBaseTest {

    @TempDir
    File projectDir

    @BeforeEach
    void setup() {
        new File(projectDir, 'settings.gradle').text = "rootProject.name = 'test-project'"
    }

    protected void writeBuildFile(Map config = [:]) {
        def lines = config.collect { key, value ->
            value instanceof Boolean || value == 'true' || value == 'false'
                ? "    ${key} = ${value}"
                : "    ${key} = '${value}'"
        }.join('\n')
        new File(projectDir, 'build.gradle').text = """
            plugins {
                id 'io.github.marksosman.scaffolding'
            }
            scaffolding {
${lines}
            }
        """
    }

    protected BuildResult run(String... args) {
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments(args)
                .build()
    }

    protected BuildResult runAndFail(String... args) {
        GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments(args)
                .buildAndFail()
    }

    protected void assertPackageStructureExists(String basePath) {
        ['application', 'crosscutting', 'domain', 'infrastructure'].each { pkg ->
            def dir = new File(projectDir, "src/main/java/${basePath}/${pkg}")
            assertTrue(dir.exists(), "Expected directory to exist: ${dir.path}")
            assertTrue(dir.isDirectory(), "Expected path to be a directory: ${dir.path}")
        }
    }
}