package com.microsoft.identity.internal.testutils.mocks;

import com.microsoft.identity.common.internal.net.HttpResponse;

import java.util.HashMap;
import java.util.List;

public class MockServerResponse {

    public static HttpResponse getMockTokenSuccessResponse() {
        final String mockResponse = "{\n" +
                "\t\"token_type\": \"Bearer\",\n" +
                "\t\"scope\": \"User.Read\",\n" +
                "\t\"expires_in\": 3599,\n" +
                "\t\"ext_expires_in\": 3599,\n" +
                "\t\"access_token\": \"b06d0810-12ff-4a4e-850b-4bda1540d895\",\n" +
                "\t\"refresh_token\": \"6b80f5b5-d53c-4c46-992d-66c5dcd4cfb1\",\n" +
                "\t\"id_token\": \"" + MockTokenCreator.createMockIdToken() + "\",\n" +
                "\t\"client_info\": \"" + MockTokenCreator.createMockRawClientInfo() + "\"\n" +
                "}";
        return new HttpResponse(200, mockResponse, new HashMap<String, List<String>>());
    }

    public static HttpResponse getMockTokenFailureInvalidGrantResponse() {
        final String mockResponse = "{\n" +
                "\t\"error\": \"invalid_grant\",\n" +
                "\t\"error_description\": \"AADSTS70000: Provided grant is invalid or malformed\",\n" +
                "\t\"error_codes\": [70000],\n" +
                "\t\"timestamp\": \"2019-10-23 21:05:16Z\",\n" +
                "\t\"trace_id\": \"8497799a-e9f9-402f-a951-7060b5014600\",\n" +
                "\t\"correlation_id\": \"390d7507-c607-4f05-bb8a-51a2a7a6282b\",\n" +
                "\t\"error_uri\": \"https://login.microsoftonline.com/error?code=70000\",\n" +
                "\t\"suberror\": \"bad_token\"\n" +
                "}";
        final HttpResponse response = new HttpResponse(
                400,
                mockResponse,
                new HashMap<String, List<String>>()
        );
        return response;
    }

    public static HttpResponse getMockTokenFailureInvalidScopeResponse() {
        final String mockResponse = "{\n" +
                "\t\"error\": \"invalid_scope\",\n" +
                "\t\"error_description\": \"AADSTS70000: Provided scope is invalid or malformed\",\n" +
                "\t\"error_codes\": [70000],\n" +
                "\t\"timestamp\": \"2019-10-23 21:05:16Z\",\n" +
                "\t\"trace_id\": \"8497799a-e9f9-402f-a951-7060b5014600\",\n" +
                "\t\"correlation_id\": \"390d7507-c607-4f05-bb8a-51a2a7a6282b\",\n" +
                "\t\"error_uri\": \"https://login.microsoftonline.com/error?code=70000\",\n" +
                "\t\"suberror\": \"bad_token\"\n" +
                "}";
        final HttpResponse response = new HttpResponse(
                400,
                mockResponse,
                new HashMap<String, List<String>>()
        );
        return response;
    }

    public static HttpResponse getMockTokenFailureServiceUnavailable() {
        final String mockResponse = "{\n" +
                "\t\"error\": \"service_unavailable\",\n" +
                "\t\"error_description\": \"AADSTS70000: Service is unavailable\",\n" +
                "\t\"error_codes\": [70000],\n" +
                "\t\"timestamp\": \"2019-10-23 21:05:16Z\",\n" +
                "\t\"trace_id\": \"8497799a-e9f9-402f-a951-7060b5014600\",\n" +
                "\t\"correlation_id\": \"390d7507-c607-4f05-bb8a-51a2a7a6282b\",\n" +
                "\t\"error_uri\": \"https://login.microsoftonline.com/error?code=70000\",\n" +
                "\t\"suberror\": \"bad_token\"\n" +
                "}";
        final HttpResponse response = new HttpResponse(
                503,
                mockResponse,
                new HashMap<String, List<String>>()
        );
        return response;
    }

    public static HttpResponse getMockTokenFailureProtectionPolicyRequiredResponse() {
        final String mockResponse = "{\n" +
                "\t\"error\": \"unauthorized_client\",\n" +
                "\t\"error_description\": \"AADSTS53005: Application needs to enforce Intune protection policies\",\n" +
                "\t\"error_codes\": [70000],\n" +
                "\t\"timestamp\": \"2019-10-23 21:05:16Z\",\n" +
                "\t\"trace_id\": \"8497799a-e9f9-402f-a951-7060b5014600\",\n" +
                "\t\"correlation_id\": \"390d7507-c607-4f05-bb8a-51a2a7a6282b\",\n" +
                "\t\"error_uri\": \"https://login.microsoftonline.com/error?code=70000\",\n" +
                "\t\"suberror\": \"protection_policy_required\"\n" +
                "}";
        final HttpResponse response = new HttpResponse(
                400,
                mockResponse,
                new HashMap<String, List<String>>()
        );
        return response;
    }
}
