package ezet.bartracker.activities.fragments.dummy;

import ezet.bartracker.models.ExerciseSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ExerciseSetProvider {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ExerciseSet> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Long, ExerciseSet> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ExerciseSet item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ExerciseSet createDummyItem(int position) {
        return new ExerciseSet(position);
    }


}
