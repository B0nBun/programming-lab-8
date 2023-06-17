package itmo.app.shared.clientrequest.requestbody;

import java.util.UUID;

public record UpdateRequestBody(UUID vehicleUUID, Void updatedElement)
    implements RequestBody {}
