package ezet.bartracker.events;

import ezet.bartracker.models.RepAnalyzer;

/**
 * Created by larsk on 06-Apr-16.
 */
public class ViewRepEvent {

    public RepAnalyzer rep;

    public ViewRepEvent(RepAnalyzer rep) {
        this.rep = rep;
    }
}
