package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public abstract class OWLEntityData extends OWLPrimitiveData {

    public OWLEntity getEntity() {
        return (OWLEntity) getObject();
    }

    public boolean isIRIEmpty() {
        return getEntity().getIRI().length() == 0;
    }

    public int compareToIgnorePrefixNames(OWLEntityData other) {
        int prefixSepIndex = getPrefixSeparatorIndex();
        String comparisonString = getBrowserText().substring(prefixSepIndex != -1 ? prefixSepIndex : 0);
        int otherPrefixSepIndex = other.getPrefixSeparatorIndex();
        String otherComparisonString = other.getBrowserText().substring(otherPrefixSepIndex != -1 ? otherPrefixSepIndex : 0);
        return comparisonString.compareToIgnoreCase(otherComparisonString);
    }

    public abstract <R> R accept(OWLEntityDataVisitorEx<R> visitor);

    @Override
    public String getBrowserText() {
        IRI iri = getEntity().getIRI();
        if(FreshEntityIri.isFreshEntityIri(iri)) {
            return FreshEntityIri.parse(iri.toString()).getSuppliedName();
        }
        else {
            return getFirstShortForm(iri::toQuotedString);
        }
    }

    public int compareToIgnoreCase(OWLEntityData other) {
        return getBrowserText().compareToIgnoreCase(other.getBrowserText());
    }

    public int getPrefixSeparatorIndex() {
        return getBrowserText().indexOf(':');
    }

    @Override
    public String getUnquotedBrowserText() {
        String browserText = getBrowserText();
        if(browserText.startsWith("'") && browserText.endsWith("'")) {
            return browserText.substring(1, browserText.length() - 1);
        }
        else {
            return browserText;
        }
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getEntity().getIRI());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.of(getEntity());
    }

    public abstract boolean isDeprecated();
}
