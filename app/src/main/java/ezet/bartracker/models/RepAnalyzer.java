package ezet.bartracker.models;

import java.util.List;

/**
 * A dummy item representing a piece of content.
 */
public class RepAnalyzer extends BaseAnalyzer {
    public int id;
    public String content;
    public String details;


    public RepAnalyzer(List<SensorData> data) {
        super(data);
        this.data = data;
    }

    public RepAnalyzer(int id, String content, String details) {
        super(null);
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }


}
