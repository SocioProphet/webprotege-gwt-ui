package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.change.matcher.EditedAnnotationAssertion;
import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.dispatch.validators.WritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractUpdateFrameHandler<A extends UpdateFrameAction<F, S>, F extends EntityFrame<S>,  S extends OWLEntity> extends AbstractHasProjectActionHandler<A, Result> implements ActionHandler<A, Result> {

    private final ValidatorFactory<WritePermissionValidator> validatorFactory;

    @Inject
    public AbstractUpdateFrameHandler(OWLAPIProjectManager projectManager,
                                      ValidatorFactory<WritePermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    /**
     * Gets an additional validator that is specific to the implementing handler.  This is returned as part of a
     * {@link edu.stanford.bmir.protege.web.server.dispatch.validators.CompositeRequestValidator} by the the implementation
     * of
     * the {@link #getRequestValidator(edu.stanford.bmir.protege.web.shared.dispatch.Action,
     * edu.stanford.bmir.protege.web.server.dispatch.RequestContext)} method.
     * @param action The action that the validation will be completed against.
     * @param requestContext The {@link edu.stanford.bmir.protege.web.server.dispatch.RequestContext} that describes the
     * context for the request.
     * @return A {@link edu.stanford.bmir.protege.web.server.dispatch.RequestValidator} for this handler.  Not {@code
     *         null}.
     */
    @Override
    protected RequestValidator getAdditionalRequestValidator(A action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param project The project that the action should be executed with respect to.
     * @param executionContext The {@link edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext} that should be
     * used to provide details such as the
     * {@link edu.stanford.bmir.protege.web.shared.user.UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Override
    protected Result execute(A action, OWLAPIProject project, ExecutionContext executionContext) {
        LabelledFrame<F> from = action.getFrom();
        LabelledFrame<F> to = action.getTo();
        final EventTag startTag = project.getEventManager().getCurrentTag();
        if(from.equals(to)) {
            return createResponse(action.getTo(), project.getEventManager().getEventsFromTag(startTag));
        }

        UserId userId = executionContext.getUserId();

        FrameTranslator<F, S> translator = createTranslator();

        final FrameChangeGenerator<F, S> changeGenerator = new FrameChangeGenerator<>(from.getFrame(), to.getFrame(), translator);
        ChangeDescriptionGenerator<S> generator = project.getChangeDescriptionGeneratorFactory().get("Edited " + from.getDisplayName());
        project.applyChanges(userId, changeGenerator, generator);
        EventList<ProjectEvent<?>> events = project.getEventManager().getEventsFromTag(startTag);
        return createResponse(action.getTo(), events);
    }

//    private void applyChangesToChangeDisplayName(OWLAPIProject project, ExecutionContext executionContext, LabelledFrame<F> from, LabelledFrame<F> to, UserId userId) {
//        // Set changes
//        EntityCrudKitHandler<?, ChangeSetEntityCrudSession> entityEditorKit = project.getEntityCrudKitHandler();
//        ChangeSetEntityCrudSession session = entityEditorKit.createChangeSetSession();
//        OntologyChangeList.Builder<S> changeListBuilder = new OntologyChangeList.Builder<>();
//        entityEditorKit.update(session, to.getFrame().getSubject(),
//                                 EntityShortForm.get(to.getTitle()),
//                                 project.getEntityCrudContext(executionContext.getUserId()),
//                                 changeListBuilder);
//        FixedChangeListGenerator<S> changeListGenerator = FixedChangeListGenerator.get(changeListBuilder.build().getChanges());
//        String typePrintName = from.getFrame().getSubject().getEntityType().getPrintName().toLowerCase();
//        FixedMessageChangeDescriptionGenerator<S> changeDescriptionGenerator =
//                FixedMessageChangeDescriptionGenerator.get("Renamed the " + typePrintName + " " + from.getTitle() + " to " + to.getTitle());
//        project.applyChanges(userId, changeListGenerator, changeDescriptionGenerator);
//    }

    protected abstract Result createResponse(LabelledFrame<F> to, EventList<ProjectEvent<?>> events);

    protected abstract FrameTranslator<F, S> createTranslator();

    protected abstract String getChangeDescription(LabelledFrame<F> from, LabelledFrame<F> to);
}
