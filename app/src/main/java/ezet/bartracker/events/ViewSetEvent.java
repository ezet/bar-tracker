package ezet.bartracker.events;

import ezet.bartracker.models.ExerciseSet;

/**
 * Created by larsk on 06-Apr-16.
 */
public class ViewSetEvent {

    public ExerciseSet set;

    public ViewSetEvent(ExerciseSet set) {
        this.set = set;
    }
}
