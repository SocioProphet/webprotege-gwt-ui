package edu.stanford.bmir.protege.web.shared.viz;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class SetUserProjectEntityGraphSettingsResult implements Result {

    private SetUserProjectEntityGraphSettingsResult() {
    }

    public static SetUserProjectEntityGraphSettingsResult create() {
        return new SetUserProjectEntityGraphSettingsResult();
    }
}
