package com.wikipedia.pages;

import com.wikipedia.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class NavigationPage extends BasePage {

    @FindBy(css = ".mw-logo, a.mw-wiki-logo, #p-logo a")
    private WebElement wikiLogo;

    @FindBy(css = "#vector-main-menu, #mw-panel, .mw-portlet-navigation")
    private WebElement mainMenu;

    @FindBy(css = "a[href*='Special:Random']")
    private WebElement randomArticleLink;

    @FindBy(css = "a[href*='Wikipedia:Contents']")
    private WebElement contentsLink;

    @FindBy(css = "a[href*='Wikipedia:About']")
    private WebElement aboutLink;

    @FindBy(css = "a[href*='Wikipedia:Contact_us']")
    private WebElement contactLink;

    @FindBy(css = "#footer, .mw-footer")
    private WebElement footer;

    @FindBy(css = "#footer-places li, .footer-places li")
    private List<WebElement> footerLinks;

    @FindBy(css = "#footer-info-lastmod, .footer-info-lastmod")
    private WebElement lastModified;

    @FindBy(css = ".mw-portlet-lang, #p-lang-btn")
    private WebElement languageButton;

    @FindBy(css = "#p-views .mw-list-item a, .vector-tab-noicon a")
    private List<WebElement> pageViewTabs;

    @FindBy(css = "a[href*='action=edit'], #ca-edit a")
    private WebElement editButton;

    @FindBy(css = "a[href*='action=history'], #ca-history a")
    private WebElement historyButton;

    public NavigationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoDisplayed() {
        return isElementDisplayed(By.cssSelector(".mw-logo, a.mw-wiki-logo, #p-logo a"));
    }

    public boolean isMainMenuDisplayed() {
        return isElementDisplayed(By.cssSelector("#vector-main-menu, #mw-panel, .mw-portlet-navigation"));
    }

    public boolean isFooterDisplayed() {
        return isElementDisplayed(By.cssSelector("#footer, .mw-footer"));
    }

    public void clickLogo() {
        WaitUtil.waitForClickable(driver, By.cssSelector(".mw-logo, a.mw-wiki-logo, #p-logo a")).click();
    }

    public void clickRandomArticle() {
        By randomLinkBy = By.cssSelector("#n-randompage a, a[href*='Special:Random']");
        WebElement link = driver.findElement(randomLinkBy);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public boolean isRandomArticleLinkPresent() {
        // The link exists in the DOM even if the sidebar is collapsed
        return !driver.findElements(By.cssSelector("a[href*='Special:Random']")).isEmpty();
    }

    public boolean isFooterLastModifiedPresent() {
        return isElementDisplayed(By.cssSelector("#footer-info-lastmod, li#footer-info-lastmod"));
    }

    public List<WebElement> getFooterLinks() {
        return driver.findElements(By.cssSelector("#footer-places li a, .footer-places li a"));
    }

    public int getFooterLinkCount() {
        return driver.findElements(By.cssSelector("#footer-places li a")).size();
    }

    public boolean isLanguageButtonPresent() {
        return isElementDisplayed(By.cssSelector(".mw-portlet-lang, #p-lang-btn"));
    }

    public boolean isEditTabPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='action=edit'], #ca-edit"));
    }

    public boolean isHistoryTabPresent() {
        return isElementDisplayed(By.cssSelector("a[href*='action=history'], #ca-history"));
    }

    public void navigateToAboutPage() {
        navigateTo("https://en.wikipedia.org/wiki/Main_Page");
        WaitUtil.waitForVisible(driver, By.cssSelector("#mp-upper, #mw-content-text"));
        // The About link is in the footer — scroll to it and JS click
        WebElement aboutLink = driver.findElement(
                By.cssSelector("a[href*='Wikipedia:About']"));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", aboutLink);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", aboutLink);
        WaitUtil.waitForVisible(driver, By.cssSelector("#firstHeading, .firstHeading"));
    }

    public boolean openLanguageSettingsModal() {
        if (isLanguageSettingsModalOpen()) {
            return true;
        }

        if (openLanguageSettingsViaJsApi()) {
            return true;
        }

        boolean menuOpened = clickAnyAndWaitFor(
            By.cssSelector(".uls-menu, .uls-dialog, .uls-language-list, .mw-interlanguage-selector__menu"),
                By.cssSelector("button.mw-interlanguage-selector"),
                By.cssSelector("#p-lang-btn-label"),
                By.cssSelector("#p-lang-btn-sticky-header"),
                By.cssSelector("#p-lang-btn")
        );

        if (!menuOpened) {
            return false;
        }

        if (isLanguageSettingsModalOpen()) {
            return true;
        }

        boolean settingsOpened = clickAnyAndWaitFor(
            By.xpath("//*[contains(normalize-space(),'Language settings') or contains(normalize-space(),'Input settings')]"),
                By.cssSelector(".uls-settings-trigger"),
            By.cssSelector(".uls-language-settings-button"),
            By.cssSelector(".mw-interlanguage-selector__menu-settings"),
            By.cssSelector("button[aria-label*='Language settings']"),
            By.cssSelector("button[aria-label*='settings']"),
                By.xpath("//button[contains(normalize-space(),'Language settings')]"),
            By.xpath("//a[contains(normalize-space(),'Language settings')]")
        );

        if (!settingsOpened) {
            return false;
        }

        return isLanguageSettingsModalOpen();
    }

    private boolean openLanguageSettingsViaJsApi() {
        try {
            Object opened = ((org.openqa.selenium.JavascriptExecutor) driver).executeAsyncScript(
                    "const done = arguments[arguments.length - 1];" +
                    "try {" +
                    "  if (!window.mw || !mw.loader || typeof mw.loader.using !== 'function') { done(false); return; }" +
                    "  mw.loader.using(['ext.uls.interface']).then(function () {" +
                    "    try {" +
                    "      if (mw.uls && mw.uls.settings && typeof mw.uls.settings.open === 'function') { mw.uls.settings.open(); done(true); return; }" +
                    "      if (mw.uls && typeof mw.uls.showSettings === 'function') { mw.uls.showSettings(); done(true); return; }" +
                    "      if (window.$ && $.uls && $.uls.settings && typeof $.uls.settings.open === 'function') { $.uls.settings.open(); done(true); return; }" +
                    "      if (window.$ && $.uls && typeof $.uls.showSettings === 'function') { $.uls.showSettings(); done(true); return; }" +
                    "      done(false);" +
                    "    } catch (e) { done(false); }" +
                    "  }).catch(function () { done(false); });" +
                    "} catch (e) { done(false); }"
            );

            if (!Boolean.TRUE.equals(opened)) {
                return false;
            }

            new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> isLanguageSettingsModalOpen());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean setInputLanguageToKoreanAndApply() {
        try {
            if (!openLanguageSettingsModal()) {
                return false;
            }

            // Step 1: Click on the Input tab
            clickFirstVisible(
                    By.cssSelector(".uls-input-settings-tab, button[data-uls-input-settings]"),
                    By.xpath("//button[contains(normalize-space(),'Input')]")
            );

            // Anonymous users can have input tools disabled by default.
            // Enable them first so language/keyboard options become available.
            List<WebElement> enableInputTools = driver.findElements(By.xpath(
                    "//button[contains(normalize-space(),'Enable input tools')]"
            ));
            if (!enableInputTools.isEmpty() && enableInputTools.get(0).isDisplayed()) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", enableInputTools.get(0));
            }

            // Step 2: Select Korean
            if (!clickKoreanLanguageChoice()) {
                if (!selectKoreanViaMoreLanguages()) {
                    return false;
                }
            }

            // Step 3: Select keyboard (prefer native keyboard toggle if present)
            if (!selectKeyboardOption()) {
                return false;
            }

            // Step 4: Click Apply
            clickFirstVisible(
                    By.cssSelector("button.uls-settings-apply, button[data-uls-apply]"),
                    By.xpath("//button[contains(normalize-space(),'Apply')]")
            );

            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean selectKoreanViaMoreLanguages() {
        try {
            boolean clickedMore = clickAnyAndWaitFor(
                    By.cssSelector(".uls-menu, .uls-language-list, .uls-dialog, .uls-grid"),
                    By.xpath("//button[normalize-space()='...']"),
                    By.cssSelector("button.uls-language-more"),
                    By.xpath("//button[contains(@aria-label,'More')]"),
                    By.xpath("//button[contains(normalize-space(),'More')]")
            );

            if (!clickedMore) {
                return false;
            }

            // Some layouts show Korean directly after clicking the ellipsis.
            if (clickKoreanLanguageChoice()) {
                return true;
            }

            List<WebElement> searchInputs = driver.findElements(
                    By.cssSelector("input[type='search'], #uls-languagefilter, .uls-languagefilter"));
            if (searchInputs.isEmpty()) {
                return clickKoreanLanguageChoice();
            }

            WebElement search = searchInputs.get(0);
            search.clear();
            search.sendKeys("Korean");

            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d ->
                    !d.findElements(By.xpath(
                        "//*[self::button or self::a or self::li][contains(normalize-space(),'Korean') or contains(normalize-space(),'한국어') or @lang='ko' or @data-language='ko' or @data-lang='ko' or @data-code='ko']"
                    )).isEmpty()
            );

            return clickKoreanLanguageChoice();
        } catch (Exception ignored) {
            return false;
        }
    }
    private boolean selectKeyboardOption() {
        try {
            List<WebElement> writingLanguageOptions = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'Language used for writing')]/following::*[self::button or self::a]"
            ));
            if (!writingLanguageOptions.isEmpty()) {
                return true;
            }

            List<WebElement> nativeKeyboard = driver.findElements(By.xpath(
                    "//label[contains(normalize-space(),'Use native keyboard') or contains(normalize-space(),'Native keyboard')]/preceding-sibling::input[@type='radio']"
            ));
            if (!nativeKeyboard.isEmpty()) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", nativeKeyboard.get(0));
                return true;
            }

            List<WebElement> keyboardOptions = driver.findElements(By.cssSelector(
                    "button.uls-keyboard-option, button.uls-keyboard, button[data-keyboard], input[type='radio'][name*='keyboard']"
            ));
            if (keyboardOptions.isEmpty()) {
                return false;
            }

            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", keyboardOptions.get(0));
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isKoreanInputLanguageSelected() {
        try {
            if (!openLanguageSettingsModal()) {
                return false;
            }

            clickFirstVisible(
                    By.cssSelector(".uls-input-settings-tab"),
                    By.cssSelector(".uls-input-settings-item"),
                    By.xpath("//button[contains(normalize-space(),'Input')]"),
                    By.xpath("//a[contains(normalize-space(),'Input')]"),
                    By.xpath("//div[contains(normalize-space(),'Input')]")
            );

            List<WebElement> selectedKorean = driver.findElements(By.xpath(
                    "//button[(contains(normalize-space(),'Korean') or contains(normalize-space(),'한국어')) and (contains(@class,'selected') or contains(@class,'progressive') or contains(@class,'primary') or @aria-pressed='true' or @aria-current='true')]"
            ));

            if (!selectedKorean.isEmpty()) {
                return true;
            }

            List<WebElement> koreanInWritingRow = driver.findElements(By.xpath(
                    "//*[contains(normalize-space(),'Language used for writing')]/following::*[self::button or self::a][contains(normalize-space(),'Korean') or contains(normalize-space(),'한국어')]"
            ));
            return !koreanInWritingRow.isEmpty();
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean clickKoreanLanguageChoice() {
        List<WebElement> koreanButtons = driver.findElements(By.xpath(
            "//*[self::button or self::a or self::li][contains(normalize-space(),'Korean') or contains(normalize-space(),'한국어') or @lang='ko' or @data-language='ko' or @data-lang='ko' or @data-code='ko']"
        ));

        if (koreanButtons.isEmpty()) {
            return false;
        }

        for (WebElement candidate : koreanButtons) {
            if (!candidate.isDisplayed()) {
                continue;
            }
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", candidate);
            return true;
        }

        return false;
    }

    private boolean isLanguageSettingsModalOpen() {
        List<WebElement> modalHeadings = driver.findElements(By.xpath(
                "//*[contains(normalize-space(),'Language settings') or contains(normalize-space(),'Input settings')]"
        ));
        return modalHeadings.stream().anyMatch(WebElement::isDisplayed);
    }

    private void clickFirstVisible(By... locators) {
        for (By locator : locators) {
            List<WebElement> elements = driver.findElements(locator);
            if (elements.isEmpty()) {
                continue;
            }
            WebElement element = elements.get(0);
            if (!element.isDisplayed()) {
                continue;
            }
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            return;
        }
    }

    private boolean clickAnyAndWaitFor(By successLocator, By... clickLocators) {
        for (By clickLocator : clickLocators) {
            List<WebElement> elements = driver.findElements(clickLocator);
            if (elements.isEmpty()) {
                continue;
            }

            WebElement target = elements.get(0);
            if (!target.isDisplayed()) {
                continue;
            }

            try {
                target.click();
            } catch (Exception clickEx) {
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
                } catch (Exception ignored) {
                    continue;
                }
            }

            try {
                new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> {
                    List<WebElement> successElements = d.findElements(successLocator);
                    return successElements.stream().anyMatch(WebElement::isDisplayed);
                });
                return true;
            } catch (Exception ignored) {
                // Try next click locator
            }
        }

        return false;
    }
}
