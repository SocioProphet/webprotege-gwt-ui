package edu.stanford.bmir.protege.web.shared.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-14
 *
 * Represents a password in the clear
 */
@AutoValue
public abstract class Pwd {

    /**
     * Create a Password using the specified clear text password
     */
    @JsonCreator
    @Nonnull
    public static Pwd create(@Nonnull String password) {
        return new AutoValue_Pwd(password);
    }

    @JsonValue
    @Nonnull
    public abstract String getPassword();
}
