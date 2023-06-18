package itmo.app.shared.entities;

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
    implements Comparable<Vehicle> {
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
}
