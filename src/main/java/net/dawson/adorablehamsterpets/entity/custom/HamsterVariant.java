package net.dawson.adorablehamsterpets.entity.custom;

import java.util.Arrays;
import java.util.Comparator;
import org.jetbrains.annotations.Nullable; // Import Nullable

public enum HamsterVariant {

    // Base Colors First
    ORANGE(0, "orange", null),
    BLACK(1, "black", null),
    CHOCOLATE(2, "chocolate", null),
    CREAM(3, "cream", null),
    DARK_GRAY(4, "dark_gray", null), // Renamed from DOVE
    LIGHT_GRAY(5, "light_gray", null), // Renamed from SILVER_DOVE
    WHITE(6, "white", null), // White has no overlay

    // Overlays for Orange
    ORANGE_WHITE_SPLIT(7, "orange", "overlay_white_split"),
    ORANGE_MOSTLY_WHITE(8, "orange", "overlay_mostly_white"),
    ORANGE_WHITE_SPOTS(9, "orange", "overlay_white_spots"),
    ORANGE_WHITE_CHEST(10, "orange", "overlay_white_chest"),

    // Overlays for Black
    BLACK_WHITE_SPLIT(11, "black", "overlay_white_split"),
    BLACK_MOSTLY_WHITE(12, "black", "overlay_mostly_white"),
    BLACK_WHITE_SPOTS(13, "black", "overlay_white_spots"),
    BLACK_WHITE_CHEST(14, "black", "overlay_white_chest"),

    // Overlays for Chocolate
    CHOCOLATE_WHITE_SPLIT(15, "chocolate", "overlay_white_split"),
    CHOCOLATE_MOSTLY_WHITE(16, "chocolate", "overlay_mostly_white"),
    CHOCOLATE_WHITE_SPOTS(17, "chocolate", "overlay_white_spots"),
    CHOCOLATE_WHITE_CHEST(18, "chocolate", "overlay_white_chest"),

    // Overlays for Cream
    CREAM_WHITE_SPLIT(19, "cream", "overlay_white_split"),
    CREAM_MOSTLY_WHITE(20, "cream", "overlay_mostly_white"),
    CREAM_WHITE_SPOTS(21, "cream", "overlay_white_spots"),
    CREAM_WHITE_CHEST(22, "cream", "overlay_white_chest"),

    // Overlays for Dark Gray
    DARK_GRAY_WHITE_SPLIT(23, "dark_gray", "overlay_white_split"),
    DARK_GRAY_MOSTLY_WHITE(24, "dark_gray", "overlay_mostly_white"),
    DARK_GRAY_WHITE_SPOTS(25, "dark_gray", "overlay_white_spots"),
    DARK_GRAY_WHITE_CHEST(26, "dark_gray", "overlay_white_chest"),

    // Overlays for Light Gray
    LIGHT_GRAY_WHITE_SPLIT(27, "light_gray", "overlay_white_split"),
    LIGHT_GRAY_MOSTLY_WHITE(28, "light_gray", "overlay_mostly_white"),
    LIGHT_GRAY_WHITE_SPOTS(29, "light_gray", "overlay_white_spots"),
    LIGHT_GRAY_WHITE_CHEST(30, "light_gray", "overlay_white_chest");

    private static final HamsterVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HamsterVariant::getId)).toArray(HamsterVariant[]::new);
    private final int id;
    private final String baseTextureName;
    @Nullable // Mark overlay as potentially null
    private final String overlayTextureName;

    HamsterVariant(int id, String baseTextureName, @Nullable String overlayTextureName) {
        this.id = id;
        this.baseTextureName = baseTextureName;
        this.overlayTextureName = overlayTextureName;
    }

    public int getId() {
        return this.id;
    }

    public String getBaseTextureName() {
        return this.baseTextureName;
    }

    @Nullable
    public String getOverlayTextureName() {
        // White variant never has an overlay
        if (this == WHITE) return null;
        return this.overlayTextureName;
    }

    // Helper to get the base color variant (e.g., CHOCOLATE_WHITE_SPOTS -> CHOCOLATE)
    public HamsterVariant getBaseVariant() {
        return switch (this.baseTextureName) {
            case "orange" -> ORANGE;
            case "black" -> BLACK;
            case "chocolate" -> CHOCOLATE;
            case "cream" -> CREAM;
            case "dark_gray" -> DARK_GRAY;
            case "light_gray" -> LIGHT_GRAY;
            case "white" -> WHITE;
            default -> ORANGE; // Fallback
        };
    }

    public static HamsterVariant byId(int id) {
        if (id < 0 || id >= BY_ID.length) {
            // Handle invalid ID, maybe return a default or log an error
            return ORANGE; // Default fallback
        }
        return BY_ID[id];
    }
}
