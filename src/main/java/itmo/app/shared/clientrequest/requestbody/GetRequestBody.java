package itmo.app.shared.clientrequest.requestbody;

import java.util.ArrayList;

public class GetRequestBody implements RequestBody<ArrayList<Void>> {

    @Override
    public ArrayList<Void> getResponseBody() {
        return new ArrayList<>();
    }
}
