package edu.stanford.bmir.protege.web.shared.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.auth.Pwd;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CreateUserAccount")
public abstract class CreateUserAccountAction implements Action<CreateUserAccountResult> {

    @JsonCreator
    public static CreateUserAccountAction create(@JsonProperty("userId") UserId userId,
                                   @JsonProperty("emailAddress") EmailAddress emailAddress,
                                   @JsonProperty("password") Pwd password) {
        return new AutoValue_CreateUserAccountAction(userId, emailAddress, password);
    }

    public abstract UserId getUserId();

    public abstract EmailAddress getEmailAddress();

    public abstract Pwd getPassword();
}
