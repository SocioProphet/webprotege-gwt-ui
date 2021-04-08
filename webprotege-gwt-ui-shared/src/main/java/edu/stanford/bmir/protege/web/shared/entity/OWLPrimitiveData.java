package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.ShortForm;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 * <p>
 *     A {@link OWLPrimitiveData} object wraps either an {@link OWLEntity}, an {@link OWLLiteral} or an
 *     {@link IRI}.
 * </p>
 */
public abstract class OWLPrimitiveData extends ObjectData implements Comparable<OWLPrimitiveData> {


    protected static ImmutableList<ShortForm> toShortFormList(@Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return shortForms.entrySet().stream().map(e -> ShortForm.get(e.getKey(), e.getValue())).collect(
                toImmutableList());
    }

    public Optional<IRI> asIRI() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    @JsonIgnore
    public abstract OWLPrimitive getObject();

    @JsonIgnore
    public ImmutableMap<DictionaryLanguage, String> getShortFormsMap() {
        return getShortForms()
                .stream()
                .collect(toImmutableMap(ShortForm::getDictionaryLanguage, ShortForm::getShortForm));
    }

    public abstract ImmutableList<ShortForm> getShortForms();

    /**
     * A convenience method that gets the first short form for this object
     */
    @JsonIgnore
    @Override
    public abstract String getBrowserText();

    protected String getFirstShortForm(Supplier<String> defaultValue) {
        return getShortForms()
                .stream()
                .findFirst()
                .map(ShortForm::getShortForm)
                .orElseGet(defaultValue);
    }

    public abstract <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E;

    public abstract <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue);

    @JsonIgnore
    public abstract PrimitiveType getType();

    @JsonIgnore
    public boolean isOWLEntity() {
        return getObject() instanceof OWLEntity;
    }

    @JsonIgnore
    public boolean isIRI() {
        return getObject() instanceof IRI;
    }

    @JsonIgnore
    public boolean isOWLLiteral() {
        return getObject() instanceof OWLLiteral;
    }

    public abstract Optional<OWLAnnotationValue> asAnnotationValue();

    public abstract Optional<OWLEntity> asEntity();

    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Override
    public int compareTo(OWLPrimitiveData o) {
        return getBrowserText().compareToIgnoreCase(o.getBrowserText());
    }

    public boolean isDeprecated() {
        return false;
    }
}
