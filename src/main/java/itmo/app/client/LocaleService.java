package itmo.app.client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LocaleService {

    public static enum Language {
        ENGLISH_SA(Locale.forLanguageTag("en-ZA"), "english.south_african"),
        RUSSIAN(Locale.forLanguageTag("ru"), "russian"),
        ICELANDIC(Locale.forLanguageTag("is"), "islandic"),
        FRENCH(Locale.forLanguageTag("fr"), "french");

        public final Locale locale;
        public final String key;

        private Language(Locale locale, String key) {
            this.locale = locale;
            this.key = key;
        }

        @Override
        public String toString() {
            return this.locale.getDisplayName();
        }
    }

    private static Locale current = Language.values()[0].locale;
    private static ResourceBundle bundle = ResourceBundle.getBundle(
        "Labels",
        LocaleService.current
    );
    private static Collection<BiConsumer<Locale, ResourceBundle>> listeners = new LinkedList<>();

    public static Collection<Locale> getLocales() {
        return List
            .of(LocaleService.Language.values())
            .stream()
            .map(l -> l.locale)
            .collect(Collectors.toList());
    }

    public static Collection<LocaleService.Language> getLanguages() {
        return List.of(LocaleService.Language.values());
    }

    public static Locale getCurrent() {
        return LocaleService.current;
    }

    public static void changeLocale(Locale locale) {
        LocaleService.bundle = ResourceBundle.getBundle("Labels", locale);
        LocaleService.current = locale;
        LocaleService.notifyListeners(locale, LocaleService.bundle);
    }

    public static Runnable onLocaleChange(BiConsumer<Locale, ResourceBundle> listener) {
        LocaleService.listeners.add(listener);
        return () -> LocaleService.listeners.remove(listener);
    }

    private static void notifyListeners(Locale locale, ResourceBundle rb) {
        for (var listener : LocaleService.listeners) {
            listener.accept(locale, rb);
        }
    }

    public static String translate(String key) {
        return LocaleService.bundle.getString(key);
    }
}
