package itmo.app.shared.entities;

import itmo.app.shared.fieldschema.FieldSchema;
import itmo.app.shared.fieldschema.FieldSchemaEnum;
import itmo.app.shared.fieldschema.FieldSchemaNum;
import itmo.app.shared.fieldschema.FieldSchemaString;
import java.io.Serializable;
import java.time.LocalDate;

public record Vehicle(
    Integer id,
    String name,
    String createdBy,
    Coordinates coordinates,
    LocalDate creationDate,
    Integer enginePower,
    VehicleType type,
    FuelType fuelType
)
    implements Comparable<Vehicle>, Serializable {
    public int compareTo(Vehicle other) {
        return this.name().compareTo(other.name());
    }

    public int compareToCreationSchema(Vehicle.CreationSchema other) {
        return this.name().compareTo(other.name());
    }

    public static Vehicle fromCreationSchema(
        int id,
        String createdBy,
        LocalDate creationDate,
        Vehicle.CreationSchema schema
    ) {
        return new Vehicle(
            id,
            schema.name(),
            createdBy,
            schema.coordinates(),
            creationDate,
            schema.enginePower(),
            schema.vehicleType(),
            schema.fuelType()
        );
    }

    public static record CreationSchema(
        String name,
        Coordinates coordinates,
        Integer enginePower,
        VehicleType vehicleType,
        FuelType fuelType
    )
        implements Serializable {}

    public static final class fields {

        public static final FieldSchemaString name = FieldSchema
            .str()
            .nonnull()
            .nonempty();

        public static final FieldSchemaNum<Integer> enginePower = FieldSchema
            .integer()
            .nonnull()
            .greaterThan(0);

        public static final FieldSchemaEnum<FuelType> fuelType = FieldSchema.enumeration(
            FuelType.class
        );

        public static final FieldSchemaEnum<VehicleType> vehicleType = FieldSchema.enumeration(
            VehicleType.class
        );
    }
}
