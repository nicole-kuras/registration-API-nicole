package pl.sda.registrationapi.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void testHandleResourceNotFound() {
        // given
        final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ResourceNotFoundException ex = new ResourceNotFoundException("Not found message.");
        final ResponseEntity<ErrorResponse> expectedResponse = getErrorResponseEntity(httpStatus, ex);

        // when
        final ResponseEntity<ErrorResponse> actualResponse = errorHandler.handleResourceNotFound(ex);

        // then
        assertResponsesEquals(expectedResponse, actualResponse);
    }

    @Test
    void testHandleConflict() {
        // given
        final HttpStatus httpStatus = HttpStatus.CONFLICT;
        final ConflictException ex = new ConflictException("Conflict!!");
        final ResponseEntity<ErrorResponse> expectedResponse = getErrorResponseEntity(httpStatus, ex);

        // when
        ResponseEntity<ErrorResponse> actualResponse = errorHandler.handleConflict(ex);

        // then
        assertResponsesEquals(expectedResponse, actualResponse);
    }

    @Test
    void testHandleIllegalArgument() {
        // given
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final IllegalArgumentException ex = new IllegalArgumentException("IllegalArgument!!");
        final ResponseEntity<ErrorResponse> expectedResponse = getErrorResponseEntity(httpStatus, ex);

        // when
        ResponseEntity<ErrorResponse> actualResponse = errorHandler.handleIllegalArgument(ex);

        // then
        assertResponsesEquals(expectedResponse, actualResponse);
    }


    private ResponseEntity<ErrorResponse> getErrorResponseEntity(HttpStatus httpStatus, RuntimeException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(httpStatus, ex);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private void assertResponsesEquals(ResponseEntity<ErrorResponse> expectedResponse,
                                       ResponseEntity<ErrorResponse> actualResponse) {

        ErrorResponse actualBody = actualResponse.getBody();
        ErrorResponse expectedBody = expectedResponse.getBody();

        Assertions.assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());

        Assertions.assertEquals(expectedBody.getError(), actualBody.getError());
        Assertions.assertEquals(expectedBody.getStatus(), actualBody.getStatus());
        Assertions.assertEquals(expectedBody.getMessage(), actualBody.getMessage());
    }

}