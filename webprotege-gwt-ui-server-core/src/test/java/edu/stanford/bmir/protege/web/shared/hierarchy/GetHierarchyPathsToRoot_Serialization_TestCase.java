package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetHierarchyPathsToRoot_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetHierarchyPathsToRootAction.create(ProjectId.getNil(),
                                                          mockOWLClass(),
                                                          HierarchyId.CLASS_HIERARCHY);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetHierarchyPathsToRootResult.create(Collections.emptySet());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}