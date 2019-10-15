// Copyright 2010-2016 Ed Anuff, Michel Kraemer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package de.undercouch.bson4jackson.serializers;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;

import de.undercouch.bson4jackson.BsonConstants;
import de.undercouch.bson4jackson.BsonGenerator;
import org.bson.BsonBinary;
import org.bson.UuidRepresentation;

/**
 * Serializer for writing UUIDs as BSON binary fields with UUID subtype
 * @author Ed Anuff
 * @author Michel Kraemer
 */
public class BsonUuidSerializer extends JsonSerializer<UUID> {
	@Override
	public void serialize(UUID value, JsonGenerator gen,
			SerializerProvider provider) throws IOException {
		if (gen instanceof BsonGenerator) {
			BsonGenerator bgen = (BsonGenerator)gen;
			bgen.writeBinary(null, BsonConstants.SUBTYPE_UUID,
					uuidToBigEndianBytes(value), 0, 16);
		} else {
			new UUIDSerializer().serialize(value, gen, provider);
		}
	}

	/**
	 * Utility routine for converting UUIDs to bytes in big endian format.
	 * @param uuid The UUID to convert
	 * @return a byte array representing the UUID in big endian format
	 */
	protected static byte[] uuidToBigEndianBytes(UUID uuid) {
		return new BsonBinary(uuid, UuidRepresentation.STANDARD).getData();
	}
}
