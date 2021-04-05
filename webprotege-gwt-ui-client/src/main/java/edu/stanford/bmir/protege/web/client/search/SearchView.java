package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public interface SearchView extends HasBusy, IsWidget, HasInitialFocusable {

    interface IncrementSelectionHandler {
        void handleIncrementSelection();
    }

    interface DecrementSelectionHandler {
        void handleDecrementSelection();
    }

    void setIncrementSelectionHandler(@Nonnull IncrementSelectionHandler handler);

    void setDecrementSelectionHandler(@Nonnull DecrementSelectionHandler handler);

    void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler);

    String getSearchString();

    void setSearchStringChangedHandler(SearchStringChangedHandler handler);

    void setLangTagFilterVisible(boolean visible);

    @Nonnull
    AcceptsOneWidget getLangTagFilterContainer();

    @Nonnull
    AcceptsOneWidget getSearchResultsContainer();

    void setSearchFilterVisible(boolean visible);

    @Nonnull
    AcceptsOneWidget getSearchFilterContainer();
}
