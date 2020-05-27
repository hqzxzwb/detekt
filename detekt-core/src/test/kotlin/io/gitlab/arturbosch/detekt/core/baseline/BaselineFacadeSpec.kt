package io.gitlab.arturbosch.detekt.core.baseline

import io.github.detekt.test.utils.createTempDirectoryForTest
import io.github.detekt.test.utils.resourceAsPath
import io.gitlab.arturbosch.detekt.api.Finding
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class BaselineFacadeSpec : Spek({

    describe("a baseline facade") {

        val dir = createTempDirectoryForTest("baseline_format")
        val validBaseline = resourceAsPath("/baseline_feature/valid-baseline.xml")

        it("creates a baseline file") {
            val fullPath = dir.resolve("baseline.xml")
            assertNonEmptyBaseline(fullPath)
        }

        it("creates on top of an existing a baseline file") {
            val fullPath = dir.resolve("baseline2.xml")

            Files.copy(validBaseline, fullPath, StandardCopyOption.REPLACE_EXISTING)

            assertNonEmptyBaseline(fullPath)
        }

        it("filters without an existing baseline file") {
            assertFilter(dir)
        }

        it("filters with an existing baseline file") {
            assertFilter(validBaseline)
        }
    }
})

private fun assertNonEmptyBaseline(fullPath: Path) {
    val baselineFacade = BaselineFacade(fullPath)
    baselineFacade.create(emptyList())
    val lines = Files.readAllLines(fullPath)
    assertThat(lines).isNotEmpty
}

private fun assertFilter(path: Path) {
    val finding = mockk<Finding>()
    every { finding.id }.returns("test")
    every { finding.signature }.returns("test")
    val findings = listOf(finding)
    val result = BaselineFacade(path).filter(findings)
    assertThat(result).isEqualTo(findings)
}
