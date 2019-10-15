package de.undercouch.bson4jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import de.undercouch.bson4jackson.BsonConstants;
import de.undercouch.bson4jackson.BsonGenerator;
import org.bson.BsonBinary;
import org.bson.UuidRepresentation;

import java.io.IOException;
import java.util.UUID;

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
