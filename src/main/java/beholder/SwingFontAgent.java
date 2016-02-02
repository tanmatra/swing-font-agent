package beholder;

import java.util.Enumeration;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class SwingFontAgent
{
    public static final String DEFAULT_FONT = "Segoe UI";

    private static String fontName;

    @SuppressWarnings("unused")
    public static void premain(String args) {
        if ((args == null) || args.isEmpty()) {
            fontName = DEFAULT_FONT;
        } else {
            fontName = args;
        }
        UIManager.addPropertyChangeListener(propertyChangeEvent -> {
            if ("lookAndFeel".equals(propertyChangeEvent.getPropertyName())) {
                changeFonts();
            }
        });
    }

    private static void changeFonts() {
        final UIDefaults uiDefaults = UIManager.getLookAndFeelDefaults();
        final Enumeration<Object> keys = uiDefaults.keys();
        while (keys.hasMoreElements()) {
            final Object key = keys.nextElement();
            final Object value = uiDefaults.get(key);
            if (value instanceof FontUIResource) {
                final FontUIResource oldFont = (FontUIResource) value;
                final FontUIResource newFont =
                        new FontUIResource(fontName, oldFont.getStyle(), oldFont.getSize());
                uiDefaults.put(key, newFont);
            }
        }
    }
}
