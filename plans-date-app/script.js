
class ThemeSwitcher {
    constructor(themes, html) {
        this.themes = themes;
        this.html = html;
        this.init();
    }

    init() {
        const theme = this.getThemeFromLocalStorage();
        if(theme) {this.setTheme(theme);}
        this.addThemeEventListeners();
    }

    addThemeEventListeners() {
        this.themes.forEach((theme) => {
            theme.addEventListener("click", () => {
                const themeName = theme.getAttribute("theme");
                this.setTheme(themeName);
                this.saveThemeToLocalStorage(themeName);
            });
        });
    }

    setTheme(themeName) {this.html.setAttribute("data-theme", themeName);}
    getThemeFromLocalStorage() {return localStorage.getItem("theme");}
    saveThemeToLocalStorage(themeName) {localStorage.setItem("theme", themeName);}
}



// const activityItemFormatter = new ActivityItemFormatter();
// const activityManager = new ActivityManager(activityItemFormatter);
// const uiManager = new UIManager(activityManager, activityItemFormatter);
const themes = document.querySelectorAll(".theme-item");
const html = document.querySelector("html");
const themeSwitcher = new ThemeSwitcher(themes, html);