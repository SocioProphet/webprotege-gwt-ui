package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.projects.MoveProjectToTrash")
public abstract class MoveProjectsToTrashResult implements Result {

    @JsonCreator
    public static MoveProjectsToTrashResult create() {
        return new AutoValue_MoveProjectsToTrashResult();
    }
}
