package org.eclipse.yasson.internal;

import static org.eclipse.yasson.internal.AnnotationIntrospectorTestAsserts.assertCreatedInstanceContainsAllParameters;
import static org.eclipse.yasson.internal.AnnotationIntrospectorTestAsserts.assertParameters;
import static org.junit.Assert.assertNull;

import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithJsonbCreatorAndConstructorPropertiesAnnotation;
import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithJsonbCreatorAnnotatedConstructor;
import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithJsonbCreatorAnnotatedFactoryMethod;
import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithJsonbCreatorAnnotatedProtectedConstructor;
import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithNoArgAndJsonbCreatorAnnotatedProtectedConstructor;
import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithTwoJsonbCreatorAnnotatedSpots;
import org.eclipse.yasson.internal.AnnotationIntrospectorTestFixtures.ObjectWithoutAnnotatedConstructor;
import org.eclipse.yasson.internal.model.JsonbCreator;

import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;

import javax.json.spi.JsonProvider;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests the {@link AnnotationIntrospector}.
 * 
 * @see AnnotationIntrospectorTestFixtures
 * @see AnnotationIntrospectorTestAsserts
 */
public class AnnotationIntrospectorTest {

    private JsonbContext jsonbContext = new JsonbContext(new JsonbConfig(), JsonProvider.provider());

    /**
     * class under test.
     */
    private AnnotationIntrospector instrospector = new AnnotationIntrospector(jsonbContext);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testObjectShouldBeCreateableFromJsonbAnnotatedConstructor() {
        JsonbCreator creator = instrospector.getCreator(ObjectWithJsonbCreatorAnnotatedConstructor.class);
        assertParameters(ObjectWithJsonbCreatorAnnotatedConstructor.parameters(), creator);
        assertCreatedInstanceContainsAllParameters(ObjectWithJsonbCreatorAnnotatedConstructor.example(), creator);
    }

    @Test
    public void testObjectShouldBeCreateableFromJsonbAnnotatedStaticFactoryMethod() {
        JsonbCreator creator = instrospector.getCreator(ObjectWithJsonbCreatorAnnotatedFactoryMethod.class);
        assertParameters(ObjectWithJsonbCreatorAnnotatedFactoryMethod.parameters(), creator);
        assertCreatedInstanceContainsAllParameters(ObjectWithJsonbCreatorAnnotatedFactoryMethod.example(), creator);
    }

    @Test
    public void testObjectShouldBeCreateableFromJsonbAnnotatedStaticFactoryMethodIgnoringConstructorPorperties() {
        JsonbCreator creator = instrospector.getCreator(ObjectWithJsonbCreatorAndConstructorPropertiesAnnotation.class);
        assertParameters(ObjectWithJsonbCreatorAndConstructorPropertiesAnnotation.parameters(), creator);
        assertCreatedInstanceContainsAllParameters(ObjectWithJsonbCreatorAndConstructorPropertiesAnnotation.example(), creator);
    }

    @Test
    public void testJsonbAnnotatedProtectedConstructorLeadsToAnException() {
        exception.expect(JsonbException.class);
        exception.expectCause(IsInstanceOf.instanceOf(IllegalAccessException.class));
        JsonbCreator creator = instrospector.getCreator(ObjectWithJsonbCreatorAnnotatedProtectedConstructor.class);
        assertCreatedInstanceContainsAllParameters(ObjectWithJsonbCreatorAnnotatedProtectedConstructor.example(), creator);
    }

    // TODO Under discussion: https://github.com/eclipse-ee4j/yasson/issues/326
    @Ignore
    @Test
    public void testNoArgConstructorShouldBePreferredOverUnusableJsonbAnnotatedProtectedConstructor() {
        JsonbCreator creator = instrospector.getCreator(ObjectWithNoArgAndJsonbCreatorAnnotatedProtectedConstructor.class);
        assertParameters(ObjectWithNoArgAndJsonbCreatorAnnotatedProtectedConstructor.parameters(), creator);
        assertCreatedInstanceContainsAllParameters(ObjectWithNoArgAndJsonbCreatorAnnotatedProtectedConstructor.example(), creator);
    }

    @Test
    public void testMoreThanOneAnnotatedCreatorMethodShouldLeadToAnException() {
        exception.expect(JsonbException.class);
        exception.expectMessage("More than one @" + JsonbCreator.class.getSimpleName());
        instrospector.getCreator(ObjectWithTwoJsonbCreatorAnnotatedSpots.class);
    }

    @Test
    public void testCreatorShouldBeNullOnMissingConstructorAnnotation() {
        assertNull(instrospector.getCreator(ObjectWithoutAnnotatedConstructor.class));
    }

}