package ezet.bartracker.activities.view_set.dummy;

import ezet.bartracker.models.RepAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RepProvider {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<RepAnalyzer> ITEMS = new ArrayList<RepAnalyzer>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, RepAnalyzer> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(RepAnalyzer item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static RepAnalyzer createDummyItem(int position) {
        return new RepAnalyzer(position, "RepAnalyzer " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
