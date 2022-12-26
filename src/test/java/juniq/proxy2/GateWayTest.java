package juniq.proxy2;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.juniqlim.apicall.http.Method;
import io.github.juniqlim.apicall.http2.Api;
import io.github.juniqlim.apicall.http2.HttpRequest.Smart;
import org.junit.jupiter.api.Test;

class GateWayTest {
    @Test
    void name() throws JsonProcessingException {
        GateWay gateWay = new GateWay();
        int countA = 0;
        int countB = 0;
        for (int i = 0; i < 100; i++) {
            String call = gateWay.call();
            if (call.equals("hello")) {
                countA++;
            }

            if (call.equals("hello2")) {
                countB++;
            }
        }
        System.out.println("countA = " + countA);
        System.out.println("countB = " + countB);
    }

    @Test
    void name2() throws JsonProcessingException {
        String response = new Api.Smart<String>().to(Smart.to(Method.GET, "http://localhost:8080/routes"))
                .response(String.class);

        System.out.println("response = " + response);

        int countA = 0;
        int countB = 0;
        int countC = 0;
        for (int i = 0; i < 100; i++) {
            String call = new Api.Smart<String>().to(Smart.to(Method.GET, "http://localhost:8080/hello")).response(String.class);;
            if (call.equals("hello")) {
                countA++;
            }

            if (call.equals("hello2")) {
                countB++;
            }

            if (call.equals("hello3")) {
                countC++;
            }

        }

        System.out.println("countA = " + countA);
        System.out.println("countB = " + countB);
        System.out.println("countC = " + countC);
    }
}