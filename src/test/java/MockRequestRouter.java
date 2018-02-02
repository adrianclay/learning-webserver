public class MockRequestRouter implements RequestRouter {

    public boolean returnsNotFound;

    @Override
    public Response respondTo(Request request) {

        if ( returnsNotFound ) {
            Response response = new Response();
            response.body = new byte[0];
            response.responseCode = "404 NOT FOUND";
            response.headers = new String[]{
                    "Content-Length: 0",
                    "Content-Type: text/plain"
            };
            return response;
        } else {
            Response response = new Response();
            response.body = request.route.getBytes();
            response.responseCode = "200 OK";
            response.headers = new String[]{
                    "Content-Length: " + response.body.length,
                    "Content-Type: text/plain"
            };
            return response;
        }

    }
}
