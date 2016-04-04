package ezet.bartracker.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by larsk on 26-Mar-16.
 */
public class SetAnalyzer extends BaseAnalyzer {

    public ExerciseSet set;
    public List<RepAnalyzer> reps = new LinkedList<>();

    public SetAnalyzer(List<SensorData> data) {
        super(data);
    }

    public SetAnalyzer(ExerciseSet set) {
        super(set.data);
        this.set = set;
    }

    @Override
    public void analyze() {
        super.analyze();
        detectReps();
    }

    private void detectReps() {
        for (int i = 0; i < 5; ++i) {
            RepAnalyzer rep = new RepAnalyzer(data);
            rep.analyze();
            rep.id = i + 1;
            rep.analyze();
            reps.add(rep);
        }
    }

    public void analyze(SensorData event) {
        if (firstEventTimestamp == 0) firstEventTimestamp = event.timestamp;
        data.add(event);
        calculate(event);
    }


}
