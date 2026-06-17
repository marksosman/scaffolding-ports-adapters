package scaffolding

import org.junit.jupiter.api.Test

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

class DomainModelTaskTest extends ScaffoldingPluginBaseTest {

    @Test
    void 'scaffoldDomainModel task is registered'() {
        writeBuildFile(rootPackage: 'ch.sosman')

        def result = run('tasks', '--group=scaffolding')

        assertTrue(result.output.contains('scaffoldDomainModel'))
    }

    @Test
    void 'task fails without domainModelName'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        def result = runAndFail('scaffoldDomainModel')

        assertTrue(result.task(':scaffoldDomainModel').outcome == FAILED)
        assertTrue(result.output.contains('domainModelName is required'))
    }

    @Test
    void 'generates core domain files for model'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp', subdomain: 'backorders')

        run('scaffoldDomainModel', '-PdomainModelName=Order')

        def base = 'src/main/java/ch/sosman/myapp/domain/backorders/order'
        assertFileExists("${base}/core/model/attribute/OrderId.java")
        assertFileExists("${base}/core/model/Order.java")
        assertFileExists("${base}/core/ports/incoming/OrderFacade.java")
        assertFileExists("${base}/core/ports/outgoing/OrderRepository.java")
        assertFileExists("${base}/core/OrderProvider.java")
    }

    @Test
    void 'generates persistence files when withPersistence is true'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        run('scaffoldDomainModel', '-PdomainModelName=Invoice')

        def base = 'src/main/java/ch/sosman/myapp/domain/invoice'
        assertFileExists("${base}/infrastructure/persistence/model/InvoiceDbEntity.java")
        assertFileExists("${base}/infrastructure/persistence/InvoiceDb.java")
        assertFileExists("${base}/infrastructure/persistence/InvoiceDbMapper.java")
        assertFileExists("${base}/infrastructure/persistence/InvoiceDbAdapter.java")
    }

    @Test
    void 'skips persistence when withPersistence is false'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        run('scaffoldDomainModel', '-PdomainModelName=Invoice', '-PwithPersistence=false')

        def persistenceDir = new File(projectDir, 'src/main/java/ch/sosman/myapp/domain/invoice/infrastructure/persistence')
        assertFalse(persistenceDir.exists())
    }

    @Test
    void 'dryRun creates no files'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        run('scaffoldDomainModel', '-PdomainModelName=Order', '-PdryRun=true')

        def modelDir = new File(projectDir, 'src/main/java/ch/sosman/myapp/domain/order')
        assertFalse(modelDir.exists())
    }

    @Test
    void 'can run multiple times for different models'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp')

        run('scaffoldDomainModel', '-PdomainModelName=Order')
        run('scaffoldDomainModel', '-PdomainModelName=Invoice')

        assertFileExists('src/main/java/ch/sosman/myapp/domain/order/core/model/Order.java')
        assertFileExists('src/main/java/ch/sosman/myapp/domain/invoice/core/model/Invoice.java')
    }

    @Test
    void 'model is placed under subdomain when subdomain is set'() {
        writeBuildFile(rootPackage: 'ch.sosman', applicationName: 'myapp', subdomain: 'backorders')

        run('scaffoldDomainModel', '-PdomainModelName=Order')

        assertFileExists('src/main/java/ch/sosman/myapp/domain/backorders/order/core/model/Order.java')
    }

    private void assertFileExists(String relativePath) {
        def file = new File(projectDir, relativePath)
        assertTrue(file.exists(), "Expected file to exist: ${file.path}")
    }
}