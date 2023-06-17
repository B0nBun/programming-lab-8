package itmo.app.shared.clientrequest.requestbody;

import java.util.UUID;

public record DeleteRequestBody(UUID vehicleUUID) implements RequestBody {}
