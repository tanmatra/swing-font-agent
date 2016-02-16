package beholder;

import java.util.Enumeration;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class SwingFontAgent
{
    public static final String DEFAULT_FONT = "Segoe UI";

    private static int oldFontSize;

    private static int newFontSize;

    private static String fontName;

    private static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @SuppressWarnings("unused")
    public static void premain(String args) {
        if ((args == null) || args.isEmpty()) {
            fontName = DEFAULT_FONT;
        } else {
            final int sizesIndex = args.indexOf(';');
            if (sizesIndex < 0) {
                fontName = args;
            } else {
                fontName = args.substring(0, sizesIndex);
                final String sizesString = args.substring(sizesIndex + 1);
                final int equalsIndex = sizesString.indexOf('=');
                if (equalsIndex < 0) {
                    newFontSize = parseInt(sizesString);
                } else {
                    final String oldSizeStr = sizesString.substring(0, equalsIndex);
                    final String newSizeStr = sizesString.substring(equalsIndex + 1);
                    oldFontSize = parseInt(oldSizeStr);
                    newFontSize = parseInt(newSizeStr);
                }
            }
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
                int size = oldFont.getSize();
                if (newFontSize != 0) {
                    if ((oldFontSize == 0) || (oldFontSize == size)) {
                        size = newFontSize;
                    }
                }
                final FontUIResource newFont = new FontUIResource(fontName, oldFont.getStyle(), size);
                uiDefaults.put(key, newFont);
            }
        }
    }
}
