/*******************************************************************************
 * Copyright (c) 2016, 2019 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Roman Grigoriadi
 ******************************************************************************/

package org.eclipse.yasson.internal.serializer;

import java.util.Collection;

import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

import org.eclipse.yasson.internal.JsonbContext;

/**
 * Serializer for collections.
 *
 * @param <V> type of {@code Collection} value
 */
public class CollectionSerializer<V> extends AbstractContainerSerializer<Collection<V>> implements EmbeddedItem {

    protected final JsonbContext jsonbContext;

    protected CollectionSerializer(SerializerBuilder builder) {
        super(builder);
        this.jsonbContext = builder.getJsonbContext();
    }

    @Override
    protected void serializeInternal(Collection<V> collection, JsonGenerator generator, SerializationContext ctx) {
        for (Object item : collection) {
            serializeItem(item, generator, ctx);
        }
    }

    @Override
    protected void writeStart(JsonGenerator generator) {
        generator.writeStartArray();
    }

    @Override
    protected void writeStart(String key, JsonGenerator generator) {
        generator.writeStartArray(key);
    }
}
