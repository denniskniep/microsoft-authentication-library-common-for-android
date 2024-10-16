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
package com.microsoft.identity.internal.testutils.nativeauth.api.models

import com.google.gson.annotations.SerializedName

/**
 * Test configuration retrieved from Labs KeyVault and used in native auth tests.
 * This class defines what test parameters to use.
 */
data class NativeAuthTestConfig(
    val configs: Map<String, Config>
) {
    data class Config(
        @SerializedName("email") val email: String,
        @SerializedName("client_id") val clientId: String,
        @SerializedName("authority_url") val authorityUrl: String,
        @SerializedName("resources") val resources: List<Resource>
    )

    data class Resource(
        @SerializedName("resource_name") val resourceName: String,
        @SerializedName("resource_id") val resourceId: String,
        @SerializedName("scopes") val scopes: List<String>
    )
}


