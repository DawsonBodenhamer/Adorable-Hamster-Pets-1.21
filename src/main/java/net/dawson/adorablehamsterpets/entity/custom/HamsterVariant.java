package net.dawson.adorablehamsterpets.entity.custom;

import java.util.Arrays;
import java.util.Comparator;

public enum HamsterVariant {

    // main versions
    DEFAULT(0),
    BLACK(1),
    CHOCOLATE(2),
    CREAM(3),
    DOVE(4),
    SILVER_DOVE(5),
    WHITE(6),

    // Additional Variants
    DEFAULT_BANDED(7),
    DEFAULT_ROAN(8),
    DEFAULT_SPOTTED(9),
    DEFAULT_WHITEBELLY(10),

    BLACK_BANDED(11),
    BLACK_ROAN(12),
    BLACK_SPOTTED(13),
    BLACK_WHITEBELLY(14),

    CHOCOLATE_BANDED(15),
    CHOCOLATE_ROAN(16),
    CHOCOLATE_SPOTTED(17),
    CHOCOLATE_WHITEBELLY(18),

    CREAM_BANDED(19),
    CREAM_ROAN(20),
    CREAM_SPOTTED(21),
    CREAM_WHITEBELLY(22),

    DOVE_BANDED(23),
    DOVE_ROAN(24),
    DOVE_SPOTTED(25),
    DOVE_WHITEBELLY(26),

    SILVER_DOVE_BANDED(27),
    SILVER_DOVE_ROAN(28),
    SILVER_DOVE_SPOTTED(29),
    SILVER_DOVE_WHITEBELLY(30);

    private static final HamsterVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HamsterVariant::getId)).toArray(HamsterVariant[]::new);
    private final int id;

    HamsterVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HamsterVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }


}
