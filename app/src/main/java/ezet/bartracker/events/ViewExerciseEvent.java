package ezet.bartracker.events;

import ezet.bartracker.models.Exercise;

/**
 * Created by larsk on 06-Apr-16.
 */
public class ViewExerciseEvent {

    public Exercise exercise;

    public ViewExerciseEvent(Exercise exercise) {
        this.exercise = exercise;
    }
}
