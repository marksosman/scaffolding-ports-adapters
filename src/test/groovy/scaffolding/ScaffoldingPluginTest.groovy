package scaffolding

import org.junit.jupiter.api.Test

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import static org.junit.jupiter.api.Assertions.assertTrue

class ScaffoldingPluginTest extends ScaffoldingPluginBaseTest {

    @Test
    void 'plugin applies successfully'() {
        writeBuildFile(rootPackage: 'com.example')

        def result = run('tasks')

        assertTrue(result.output.contains('scaffolding'))
    }

    @Test
    void 'scaffolding task is registered'() {
        writeBuildFile(rootPackage: 'com.example')

        def result = run('tasks', '--group=scaffolding')

        assertTrue(result.output.contains('scaffolding'))
        assertTrue(result.output.contains('Generate base Ports & Adapters directory structure'))
    }

    @Test
    void 'scaffolding task succeeds'() {
        writeBuildFile(rootPackage: 'com.example')

        def result = run('scaffolding')

        def outcome = result.task(':scaffolding').outcome
        assertTrue(
                outcome == SUCCESS || outcome == UP_TO_DATE,
                "Expected task to succeed but was: $outcome"
        )
    }
}