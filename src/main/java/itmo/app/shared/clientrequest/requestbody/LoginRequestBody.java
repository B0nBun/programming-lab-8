package itmo.app.shared.clientrequest.requestbody;

import itmo.app.server.DataSource;
import itmo.app.shared.AuthResult;
import java.io.Serializable;
import java.sql.SQLException;

public class LoginRequestBody implements RequestBody<LoginRequestBody.Response> {

    public record Response(AuthResult result, String error) implements Serializable {}

    @Override
    public Response getResponseBody(RequestBody.Context context) {
        try {
            return new Response(
                DataSource.Auth.userAuthorized(context.login(), context.password()),
                null
            );
        } catch (SQLException err) {
            return new Response(null, "SQL exception: " + err.getMessage());
        }
    }

    public boolean mutating() {
        return false;
    }
}
