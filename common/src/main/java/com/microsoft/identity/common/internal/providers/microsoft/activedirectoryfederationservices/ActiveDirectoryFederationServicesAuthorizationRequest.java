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
package com.microsoft.identity.common.internal.providers.microsoft.activedirectoryfederationservices;

import android.support.annotation.NonNull;

import com.microsoft.identity.common.exception.ClientException;
import com.microsoft.identity.common.internal.providers.microsoft.MicrosoftAuthorizationRequest;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.AzureActiveDirectoryPromptBehavior;
import com.microsoft.identity.common.internal.providers.oauth2.AuthorizationRequest;
import com.microsoft.identity.common.internal.providers.oauth2.PkceChallenge;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import java.io.UnsupportedEncodingException;

/**
 * Active Directory Federation Services Authorization Request.
 */
public class ActiveDirectoryFederationServicesAuthorizationRequest extends MicrosoftAuthorizationRequest {
    public ActiveDirectoryFederationServicesAuthorizationRequest(final String responseType,
                                                                 @NonNull final String clientId,
                                                                 final String redirectUri,
                                                                 final String state,
                                                                 final Set<String> scope,
                                                                 @NonNull final URL authority,
                                                                 @NonNull final String authorizationEndpoint,
                                                                 final String loginHint,
                                                                 final UUID correlationId,
                                                                 final PkceChallenge pkceChallenge,
                                                                 final String extraQueryParam,
                                                                 final String libraryVersion) {
        super(responseType, clientId, redirectUri, state, scope, authority, authorizationEndpoint,
                loginHint, correlationId, pkceChallenge, extraQueryParam, libraryVersion);

    }

    @Override
    public String getAuthorizationStartUrl() throws UnsupportedEncodingException, ClientException {
        throw new UnsupportedEncodingException("Not implemented.");
    }

    @Override
    public String getAuthorizationEndpoint() {
        return null;
    }
}

