package juniq.proxy2;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.juniqlim.apicall.http.Method;
import io.github.juniqlim.apicall.http2.Api;
import io.github.juniqlim.apicall.http2.HttpRequest.Smart;

public class GateWay {
    public String call() throws JsonProcessingException {
        return new Api.Smart<String>().to(Smart.to(Method.GET, "http://localhost:8080/hello"))
                .response(String.class);
    }
}