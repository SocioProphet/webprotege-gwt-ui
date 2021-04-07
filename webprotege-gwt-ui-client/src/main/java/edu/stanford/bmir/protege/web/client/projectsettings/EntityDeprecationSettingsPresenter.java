package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.EntityDeprecationSettings;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-23
 */
public class EntityDeprecationSettingsPresenter {

    @Nonnull
    private final EntityDeprecationSettingsView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityCriteriaPresenter replacedByCriteriaPresenter;

    @Inject
    public EntityDeprecationSettingsPresenter(@Nonnull EntityDeprecationSettingsView view,
                                              @Nonnull DispatchServiceManager dispatch,
                                              @Nonnull ProjectId projectId,
                                              @Nonnull EntityCriteriaPresenter entityCriteriaPresenter) {
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.projectId = checkNotNull(projectId);
        this.replacedByCriteriaPresenter = checkNotNull(entityCriteriaPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        replacedByCriteriaPresenter.start(view.getReplacedByFilterContainer());
        replacedByCriteriaPresenter.setDisplayAtLeastOneCriteria(false);
    }

    public void setValue(@Nonnull EntityDeprecationSettings settings) {
        try {
            view.clear();
            dispatch.beginBatch();
            settings.getReplacedByPropertyIri().ifPresent(iri -> {
                dispatch.execute(GetEntityRenderingAction.create(projectId, DataFactory.getOWLAnnotationProperty(iri)),
                                 result -> view.setReplacedByProperty((OWLAnnotationPropertyData) result.getEntityData()));
            });
            settings.getDeprecatedClassesParent().ifPresent(classesParent -> {
                dispatch.execute(GetEntityRenderingAction.create(projectId, classesParent),
                                 result -> view.setDeprecatedClassesParent((OWLClassData) result.getEntityData()));
            });
            settings.getDeprecatedObjectPropertiesParent().ifPresent(objectPropertiesParent -> {
                dispatch.execute(GetEntityRenderingAction.create(projectId, objectPropertiesParent),
                                 result -> view.setDeprecatedObjectPropertiesParent((OWLObjectPropertyData) result.getEntityData()));
            });
            settings.getDeprecatedDataPropertiesParent().ifPresent(dataPropertiesParent -> {
                dispatch.execute(GetEntityRenderingAction.create(projectId, dataPropertiesParent),
                                 result -> view.setDeprecatedDataPropertiesParent((OWLDataPropertyData) result.getEntityData()));
            });
            settings.getDeprecatedAnnotationPropertiesParent().ifPresent(annotationPropertiesParent -> {
                dispatch.execute(GetEntityRenderingAction.create(projectId, annotationPropertiesParent),
                                 result -> view.setDeprecatedAnnotationPropertiesParent((OWLAnnotationPropertyData) result
                                         .getEntityData()));
            });
            settings.getDeprecatedIndividualsParent().ifPresent(individualsParent -> {
                dispatch.execute(GetEntityRenderingAction.create(projectId, individualsParent),
                                 result -> view.setDeprecatedIndividualsParent((OWLClassData) result.getEntityData()));
            });
            replacedByCriteriaPresenter.clear();
            settings.getReplacedByFilter().ifPresent(replacedByCriteriaPresenter::setCriteria);
        } finally {
            dispatch.executeCurrentBatch();
        }

    }

    private CompositeRootCriteria getDefaultReplacementCriteria() {
        return CompositeRootCriteria.get(ImmutableList.of(
                EntityTypeIsOneOfCriteria.get(ImmutableSet.of(EntityType.CLASS,
                                                              EntityType.OBJECT_PROPERTY,
                                                              EntityType.DATA_PROPERTY,
                                                              EntityType.ANNOTATION_PROPERTY,
                                                              EntityType.NAMED_INDIVIDUAL,
                                                              EntityType.DATATYPE))), MultiMatchType.ALL);
    }

    public EntityDeprecationSettings getValue() {
        return EntityDeprecationSettings.get(view.getReplacedByPropertyIri().orElse(null),
                                             replacedByCriteriaPresenter.getCriteria().orElse(null),
                                             view.getDeprecatedClassesParent().orElse(null),
                                             view.getDeprecatedObjectPropertiesParent().orElse(null),
                                             view.getDeprecatedDataPropertiesParent().orElse(null),
                                             view.getDeprecatedAnnotationPropertiesParent().orElse(null),
                                             view.getDeprecatedIndividualsParent().orElse(null));
    }
}
