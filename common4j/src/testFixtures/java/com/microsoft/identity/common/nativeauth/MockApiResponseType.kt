//  Copyright (c) Microsoft Corporation.
//  All rights reserved.
//
//  This code is licensed under the MIT License.
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files(the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions :
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.

package com.microsoft.identity.common.nativeauth

/**
 * Enumerates all of the different types of responses received by Mock API for Native Auth.
 */
enum class MockApiResponseType(val stringValue: String) {
    ATTRIBUTE_VALIDATION_FAILED("AttributeValidationFailed"),
    ATTRIBUTES_REQUIRED("AttributesRequired"),
    AUTHORIZATION_PENDING("AuthorizationPending"),
    AUTH_NOT_SUPPORTED("AuthNotSupported"),
    CHALLENGE_TYPE_OOB("ChallengeTypeOOB"),
    CHALLENGE_TYPE_PASSWORD("ChallengeTypePassword"),
    CHALLENGE_TYPE_REDIRECT("ChallengeTypeRedirect"),
    CREDENTIAL_REQUIRED("CredentialRequired"),
    EXPIRED_TOKEN("ExpiredToken"),
    INITIATE_SUCCESS("InitiateSuccess"),
    INTROSPECT_REQUIRED("IntrospectRequired"),
    INTROSPECT_SUCCESS("IntrospectSuccess"),
    INVALID_AUTHENTICATION_METHOD("InvalidAuthMethodForUser"),
    INVALID_CLIENT("InvalidClient"),
    INVALID_USERNAME("InvalidUsername"),
    INVALID_GRANT("InvalidGrant"),
    INVALID_OOB_VALUE("InvalidOOBValue"),
    INVALID_PASSWORD("InvalidPassword"),
    INVALID_PURPOSE_TOKEN("InvalidPurposeToken"),
    INVALID_REQUEST("InvalidRequest"),
    INVALID_SCOPE("InvalidScope"),
    INVALID_TOKEN("InvalidToken"),
    MFA_REQUIRED("MFARequired"),
    NEW_PASSWORD_IS_INVALID("NewPasswordIsInvalid"),
    PASSWORD_BANNED("PasswordBanned"),
    PASSWORD_RECENTLY_USED("PasswordRecentlyUsed"),
    PASSWORD_TOO_LONG("PasswordTooLong"),
    PASSWORD_TOO_SHORT("PasswordTooShort"),
    PASSWORD_TOO_WEAK("PasswordTooWeak"),
    SIGNIN_INVALID_PASSWORD("InvalidPassword"),
    SIGNUP_START_SUCCESS("SignUpStartSuccess"),
    SIGNUP_CONTINUE_SUCCESS("SignUpContinueSuccess"),
    SLOW_DOWN("SlowDown"),
    SSPR_CONTINUE_SUCCESS("SSPRContinueSuccess"),
    SSPR_POLL_FAILED("SSPRPollFailed"),
    SSPR_POLL_IN_PROGRESS("SSPRPollInProgress"),
    SSPR_POLL_SUCCESS("SSPRPollSuccess"),
    SSPR_START_SUCCESS("SSPRStartSuccess"),
    SSPR_SUBMIT_SUCCESS("SSPRSubmitSuccess"),
    TOKEN_EXPIRED_SLT("TokenExpiredSLT"), // This is tentative
    TOKEN_SUCCESS("TokenSuccess"),
    UNAUTHORIZED_CLIENT("UnauthorizedClient"),
    UNSUPPORTED_CHALLENGE_TYPE("UnsupportedChallengeType"),
    USER_ALREADY_EXISTS("UserAlreadyExists"),
    USER_NOT_FOUND("UserNotFound"),
    VALIDATION_FAILED("AttributeValidationFailed"),
    VERIFICATION_REQUIRED("VerificationRequired"),
}
