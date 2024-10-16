// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.internal.testutils.nativeauth

/**
 * Test configuration retrieved from Labs KeyVault and used in native auth tests.
 */
enum class ConfigType(val stringValue: String) {
    SSPR("SSPR"),
    SIGN_UP_PASSWORD("SIGN_UP_PASSWORD"),
    SIGN_IN_PASSWORD("SIGN_IN_PASSWORD"),
    SIGN_UP_OTP("SIGN_UP_OTP"),
    SIGN_IN_OTP("SIGN_IN_OTP"),
    SIGN_UP_PASSWORD_ATTRIBUTES("SIGN_UP_PASSWORD_ATTRIBUTES"),
    SIGN_UP_OTP_ATTRIBUTES("SIGN_UP_OTP_ATTRIBUTES"),
    SIGN_IN_MFA_SINGLE_AUTH("SIGN_IN_MFA_SINGLE_AUTH"),
    SIGN_IN_MFA_MULTI_AUTH("SIGN_IN_MFA_MULTI_AUTH"),
    ACCESS_TOKEN("ACCESS_TOKEN")
}