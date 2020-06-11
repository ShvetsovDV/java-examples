package ${package}.endpoints;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.somehost.soapserviceprototype.schema.test.TestRequest;
import ru.somehost.soapserviceprototype.schema.test.TestResponse;
import ${package}.services.TestService;

@Endpoint
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestEndpoint {

	private static final String TEST_NAMESPACE =  "http://www.somehost.ru/soapserviceprototype/schema/test";

	private TestService testService;

	@PayloadRoot(localPart = "TestRequest", namespace = TEST_NAMESPACE)
	@ResponsePayload
	public TestResponse test(
			final @RequestPayload TestRequest request
	){
		TestResponse testResponse = new TestResponse();
		testResponse.setResult(testService.getContent(request.getName()));
		return testResponse;
	}
}
