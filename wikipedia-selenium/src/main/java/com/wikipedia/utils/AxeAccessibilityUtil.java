package com.wikipedia.utils;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AxeAccessibilityUtil {

    private static final String REPORT_DIR = "target/wcag-reports/";
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    static {
        try {
            Files.createDirectories(Paths.get(REPORT_DIR));
        } catch (IOException e) {
            System.err.println("Could not create WCAG report directory: " + e.getMessage());
        }
    }

    /**
     * Runs an Axe accessibility scan on the current page and saves a WCAG JSON report.
     *
     * @param driver   the WebDriver instance
     * @param pageName a label used for the report filename
     * @return the Axe Results object
     */
    public static Results runAccessibilityScan(WebDriver driver, String pageName) {
        Results results;
        try {
            results = new AxeBuilder().analyze(driver);
        } catch (Exception e) {
            System.err.println("[WCAG] Axe scan failed for page '" + pageName + "': " + e.getMessage());
            return null;
        }

        // Save report to file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String safeName = pageName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String reportPath = REPORT_DIR + safeName + "_" + timestamp + ".json";

        try {
            WcagReport report = new WcagReport();
            report.setPageUrl(driver.getCurrentUrl());
            report.setPageName(pageName);
            report.setTimestamp(new Date().toString());
            report.setViolations(results.getViolations());
            report.setPasses(results.getPasses());
            report.setIncomplete(results.getIncomplete());
            report.setInapplicable(results.getInapplicable());
            report.setViolationCount(results.getViolations() != null ? results.getViolations().size() : 0);
            report.setPassCount(results.getPasses() != null ? results.getPasses().size() : 0);

            mapper.writeValue(new File(reportPath), report);
            System.out.println("[WCAG] Report saved: " + reportPath
                    + " | Violations: " + report.getViolationCount()
                    + " | Passes: " + report.getPassCount());
        } catch (IOException e) {
            System.err.println("[WCAG] Failed to write report for '" + pageName + "': " + e.getMessage());
        }

        return results;
    }

    /**
     * Logs a summary of WCAG violations to stdout.
     */
    public static void logViolationSummary(Results results, String pageName) {
        if (results == null) return;
        List<Rule> violations = results.getViolations();
        if (violations == null || violations.isEmpty()) {
            System.out.println("[WCAG] " + pageName + ": No accessibility violations found.");
        } else {
            System.out.println("[WCAG] " + pageName + ": " + violations.size() + " violation(s) found:");
            for (Rule v : violations) {
                System.out.println("  - [" + v.getImpact() + "] " + v.getId() + ": " + v.getDescription());
            }
        }
    }

    // ---- Inner DTO for JSON serialization ----
    public static class WcagReport {
        private String pageName;
        private String pageUrl;
        private String timestamp;
        private int violationCount;
        private int passCount;
        private List<Rule> violations;
        private List<Rule> passes;
        private List<Rule> incomplete;
        private List<Rule> inapplicable;

        public String getPageName() { return pageName; }
        public void setPageName(String v) { this.pageName = v; }
        public String getPageUrl() { return pageUrl; }
        public void setPageUrl(String v) { this.pageUrl = v; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String v) { this.timestamp = v; }
        public int getViolationCount() { return violationCount; }
        public void setViolationCount(int v) { this.violationCount = v; }
        public int getPassCount() { return passCount; }
        public void setPassCount(int v) { this.passCount = v; }
        public List<Rule> getViolations() { return violations; }
        public void setViolations(List<Rule> v) { this.violations = v; }
        public List<Rule> getPasses() { return passes; }
        public void setPasses(List<Rule> v) { this.passes = v; }
        public List<Rule> getIncomplete() { return incomplete; }
        public void setIncomplete(List<Rule> v) { this.incomplete = v; }
        public List<Rule> getInapplicable() { return inapplicable; }
        public void setInapplicable(List<Rule> v) { this.inapplicable = v; }
    }
}
