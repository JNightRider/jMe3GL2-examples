/*
 * This project is free for use and/or modification.
 * Visit https://github.com/JNightRide/jMe3GL2-examples for more information
 */
package e.g.dodgethecreeps;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.Styles;

/**
 * Class in charge of initializing a theme for the components to be used (Lemur)
 * @author wil
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LemurGuiStyle {

    /**
     * Initializes a custom style.
     * @param assetManager asset-manager
     */
    public static void loadAppStyle(AssetManager assetManager) {
        Styles styles = GuiGlobals.getInstance().getStyles();

        Attributes attrs;
        TbtQuadBackgroundComponent gradient = TbtQuadBackgroundComponent.create(
                "/com/simsilica/lemur/icons/bordered-gradient.png",
                1, 1, 1, 126, 126,
                1f, false);

        attrs = styles.getSelector("StartButton", "glass");

        attrs.set("color", new ColorRGBA(0.878f, 0.878f, 0.878f, 1));
        attrs.set("background", gradient.clone());
        ((TbtQuadBackgroundComponent) attrs.get("background")).setColor(new ColorRGBA(0.298f, 0.282f, 0.333f, 0.8f));
        attrs.set("insets", new Insets3f(2, 2, 2, 2));
    }
}
