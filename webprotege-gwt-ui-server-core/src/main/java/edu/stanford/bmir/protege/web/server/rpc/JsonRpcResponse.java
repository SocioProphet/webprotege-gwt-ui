package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcResponse {



    @JsonProperty("jsonrpc")
    public final String getJsonRpc() {
        return "2.0";
    }

    @JsonProperty("result")
    @Nullable
    public abstract Result getResultInternal();

    @JsonIgnore
    public Optional<Result> getResult() {
        return Optional.ofNullable(getResultInternal());
    }

    @JsonProperty("error")
    @Nullable
    public abstract JsonRpcError getErrorInternal();

    public Optional<JsonRpcError> getError() {
        return Optional.ofNullable(getErrorInternal());
    }

    @JsonProperty("id")
    public abstract String getId();

    @JsonCreator
    public static JsonRpcResponse create(@JsonProperty("result") Result newResultInternal,
                                         @JsonProperty("error") JsonRpcError newErrorInternal,
                                         @JsonProperty("id") String newId) {
        return new AutoValue_JsonRpcResponse(newResultInternal, newErrorInternal, newId);
    }
}
