package scaffolding

import org.gradle.api.logging.Logger

class FileCreator {
    private final boolean dryRun
    private final Logger logger
    private int createdCount = 0
    private int skippedCount = 0

    FileCreator(boolean dryRun, Logger logger) {
        this.dryRun = dryRun
        this.logger = logger
    }

    boolean createDirectory(String path) {
        File dir = new File(path)
        if (dryRun) {
            logger.lifecycle("  [DRY RUN] Would create directory: ${dir.absolutePath}")
            return true
        }

        if (dir.exists()) {
            logger.info("Directory already exists: ${path}")
            return false
        }

        dir.mkdirs()
        logger.info("Created directory: ${path}")
        return true
    }

    boolean createFile(File file, String content) {
        if (dryRun) {
            logger.lifecycle("  [DRY RUN] Would create file: ${file.name}")
            return true
        }

        if (file.exists()) {
            logger.warn("WARNING: Skipping ${file.name} (already exists).")
            skippedCount++
            return false
        }

        file.write(content.stripMargin())
        logger.lifecycle("Created ${file.name}")
        createdCount++
        return true
    }

    Map<String, Integer> getStats() {
        return [created: createdCount, skipped: skippedCount]
    }
}