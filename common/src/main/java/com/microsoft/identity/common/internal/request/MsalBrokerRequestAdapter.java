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
package com.microsoft.identity.common.internal.request;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.identity.common.adal.internal.AuthenticationConstants;
import com.microsoft.identity.common.exception.ClientException;
import com.microsoft.identity.common.internal.authorities.Authority;
import com.microsoft.identity.common.internal.authorities.AzureActiveDirectoryAuthority;
import com.microsoft.identity.common.internal.authorities.Environment;
import com.microsoft.identity.common.internal.authscheme.AbstractAuthenticationScheme;
import com.microsoft.identity.common.internal.authscheme.BearerAuthenticationSchemeInternal;
import com.microsoft.identity.common.internal.authscheme.PopAuthenticationSchemeInternal;
import com.microsoft.identity.common.internal.broker.BrokerRequest;
import com.microsoft.identity.common.internal.broker.BrokerValidator;
import com.microsoft.identity.common.internal.commands.parameters.BrokerInteractiveTokenCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.BrokerSilentTokenCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.CommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.InteractiveTokenCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.RemoveAccountCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.SilentTokenCommandParameters;
import com.microsoft.identity.common.internal.commands.parameters.TokenCommandParameters;
import com.microsoft.identity.common.internal.logging.DiagnosticContext;
import com.microsoft.identity.common.internal.logging.Logger;
import com.microsoft.identity.common.internal.providers.microsoft.azureactivedirectory.AzureActiveDirectory;
import com.microsoft.identity.common.internal.providers.oauth2.OpenIdConnectPromptParameter;
import com.microsoft.identity.common.internal.ui.AuthorizationAgent;
import com.microsoft.identity.common.internal.ui.browser.Browser;
import com.microsoft.identity.common.internal.ui.browser.BrowserDescriptor;
import com.microsoft.identity.common.internal.ui.browser.BrowserSelector;
import com.microsoft.identity.common.internal.util.ClockSkewManager;
import com.microsoft.identity.common.internal.util.IClockSkewManager;
import com.microsoft.identity.common.internal.util.QueryParamsAdapter;
import com.microsoft.identity.common.internal.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.Broker.ACCOUNT_CLIENTID_KEY;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.Broker.ACCOUNT_HOME_ACCOUNT_ID;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.Broker.ACCOUNT_REDIRECT;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.Broker.DEFAULT_BROWSER_PACKAGE_NAME;
import static com.microsoft.identity.common.adal.internal.AuthenticationConstants.Broker.ENVIRONMENT;

public class MsalBrokerRequestAdapter implements IBrokerRequestAdapter {

    private static final String TAG = MsalBrokerRequestAdapter.class.getName();

    public static Gson sRequestAdapterGsonInstance;

    static {
        sRequestAdapterGsonInstance = new GsonBuilder()
                .registerTypeAdapter(
                        AbstractAuthenticationScheme.class,
                        new AuthenticationSchemeTypeAdapter()
                ).create();
    }

    @Override
    public BrokerRequest brokerRequestFromAcquireTokenParameters(@NonNull final InteractiveTokenCommandParameters parameters) {
        Logger.info(TAG, "Constructing result bundle from AcquireTokenOperationParameters.");

        final BrokerRequest brokerRequest = new BrokerRequest.Builder()
                .authority(parameters.getAuthority().getAuthorityURL().toString())
                .scope(TextUtils.join(" ", parameters.getScopes()))
                .redirect(getRedirectUri(parameters))
                .clientId(parameters.getClientId())
                .username(parameters.getLoginHint())
                .extraQueryStringParameter(
                        parameters.getExtraQueryStringParameters() != null ?
                                QueryParamsAdapter._toJson(parameters.getExtraQueryStringParameters())
                                : null
                ).prompt(parameters.getPrompt().name())
                .claims(parameters.getClaimsRequestJson())
                .forceRefresh(false)
                .correlationId(DiagnosticContext.getRequestContext().get(DiagnosticContext.CORRELATION_ID))
                .applicationName(parameters.getApplicationName())
                .applicationVersion(parameters.getApplicationVersion())
                .msalVersion(parameters.getSdkVersion())
                .environment(AzureActiveDirectory.getEnvironment().name())
                .multipleCloudsSupported(getMultipleCloudsSupported(parameters))
                .authorizationAgent(
                        parameters.isBrokerBrowserSupportEnabled() ?
                                AuthorizationAgent.BROWSER.name() :
                                AuthorizationAgent.WEBVIEW.name()
                ).authenticationScheme(parameters.getAuthenticationScheme()).build();

        return brokerRequest;
    }

    @Override
    public BrokerRequest brokerRequestFromSilentOperationParameters(@NonNull final SilentTokenCommandParameters parameters) {

        Logger.info(TAG, "Constructing result bundle from AcquireTokenSilentOperationParameters.");

        final BrokerRequest brokerRequest = new BrokerRequest.Builder()
                .authority(parameters.getAuthority().getAuthorityURL().toString())
                .scope(TextUtils.join(" ", parameters.getScopes()))
                .redirect(getRedirectUri(parameters))
                .clientId(parameters.getClientId())
                .homeAccountId(parameters.getAccount().getHomeAccountId())
                .localAccountId(parameters.getAccount().getLocalAccountId())
                .username(parameters.getAccount().getUsername())
                .claims(parameters.getClaimsRequestJson())
                .forceRefresh(parameters.isForceRefresh())
                .correlationId(DiagnosticContext.getRequestContext().get(DiagnosticContext.CORRELATION_ID))
                .applicationName(parameters.getApplicationName())
                .applicationVersion(parameters.getApplicationVersion())
                .msalVersion(parameters.getSdkVersion())
                .environment(AzureActiveDirectory.getEnvironment().name())
                .multipleCloudsSupported(getMultipleCloudsSupported(parameters))
                .authenticationScheme(parameters.getAuthenticationScheme())
                .build();

        return brokerRequest;
    }

    @NonNull
    private static AbstractAuthenticationScheme getAuthenticationScheme(
            @NonNull final Context context,
            @NonNull final BrokerRequest request) {
        final AbstractAuthenticationScheme requestScheme = request.getAuthenticationScheme();

        if (null == requestScheme) {
            // Default assumes the scheme is Bearer
            return new BearerAuthenticationSchemeInternal();
        } else {
            if (requestScheme instanceof PopAuthenticationSchemeInternal) {
                final IClockSkewManager clockSkewManager = new ClockSkewManager(context);
                ((PopAuthenticationSchemeInternal) requestScheme).setClockSkewManager(clockSkewManager);
            }

            return requestScheme;
        }
    }

    @Override
    public BrokerInteractiveTokenCommandParameters brokerInteractiveParametersFromActivity(
            @NonNull final Activity callingActivity) {

        Logger.info(TAG, "Constructing BrokerAcquireTokenOperationParameters from calling activity");

        final Intent intent = callingActivity.getIntent();

        final BrokerRequest brokerRequest = sRequestAdapterGsonInstance.fromJson(
                intent.getStringExtra(AuthenticationConstants.Broker.BROKER_REQUEST_V2),
                BrokerRequest.class
        );

        int callingAppUid = intent.getIntExtra(
                AuthenticationConstants.Broker.CALLER_INFO_UID, 0
        );

        List<Pair<String, String>> extraQP = new ArrayList<>();

        if (!TextUtils.isEmpty(brokerRequest.getExtraQueryStringParameter())) {
            extraQP = QueryParamsAdapter._fromJson(brokerRequest.getExtraQueryStringParameter());
        }

        final AzureActiveDirectoryAuthority authority = AdalBrokerRequestAdapter.getRequestAuthorityWithExtraQP(
                brokerRequest.getAuthority(),
                extraQP
        );

        if (authority != null) {
            authority.setMultipleCloudsSupported(brokerRequest.getMultipleCloudsSupported());
        }

        String correlationIdString = brokerRequest.getCorrelationId();

        if (TextUtils.isEmpty(correlationIdString)) {
            UUID correlationId = UUID.randomUUID();
            correlationIdString = correlationId.toString();
        }

        Logger.info(TAG, "Authorization agent passed in by MSAL: " + brokerRequest.getAuthorizationAgent());

        final BrokerInteractiveTokenCommandParameters.BrokerInteractiveTokenCommandParametersBuilder
                commandParametersBuilder = BrokerInteractiveTokenCommandParameters.builder()
                .authenticationScheme(getAuthenticationScheme(callingActivity, brokerRequest))
                .activity(callingActivity)
                .androidApplicationContext(callingActivity.getApplicationContext())
                .sdkType(SdkType.MSAL)
                .callerUid(callingAppUid)
                .callerPackageName(brokerRequest.getApplicationName())
                .callerAppVersion(brokerRequest.getApplicationVersion())
                .extraQueryStringParameters(extraQP)
                .authority(authority)
                .scopes(getScopesAsSet(brokerRequest.getScope()))
                .clientId(brokerRequest.getClientId())
                .redirectUri(brokerRequest.getRedirect())
                .loginHint(brokerRequest.getUserName())
                .correlationId(correlationIdString)
                .claimsRequestJson(brokerRequest.getClaims())
                .prompt(brokerRequest.getPrompt() != null ?
                        OpenIdConnectPromptParameter.valueOf(brokerRequest.getPrompt()) :
                        OpenIdConnectPromptParameter.NONE);

        if (brokerRequest.getAuthorizationAgent() != null
                && brokerRequest.getAuthorizationAgent().equalsIgnoreCase(AuthorizationAgent.BROWSER.name())
                && isCallingPackageIntune(brokerRequest.getApplicationName())) { // TODO : Remove this whenever we enable System Browser support in Broker for apps.
            Logger.info(TAG, "Setting Authorization Agent to Browser for Intune app");
            commandParametersBuilder
                    .authorizationAgent(AuthorizationAgent.BROWSER)
                    .brokerBrowserSupportEnabled(true)
                    .browserSafeList(getBrowserSafeListForBroker());
        } else {
            commandParametersBuilder.authorizationAgent(AuthorizationAgent.WEBVIEW);
        }

        // Set Global environment variable for instance discovery if present
        if (!TextUtils.isEmpty(brokerRequest.getEnvironment())) {
            AzureActiveDirectory.setEnvironment(
                    Environment.valueOf(brokerRequest.getEnvironment())
            );
        }

        return commandParametersBuilder.build();
    }

    @Override
    public BrokerSilentTokenCommandParameters brokerSilentParametersFromBundle(
            @NonNull final Bundle bundle,
            @NonNull final Context context,
            @NonNull final Account account) {

        Logger.info(TAG, "Constructing BrokerAcquireTokenSilentOperationParameters from result bundle");

        final BrokerRequest brokerRequest = sRequestAdapterGsonInstance.fromJson(
                bundle.getString(AuthenticationConstants.Broker.BROKER_REQUEST_V2),
                BrokerRequest.class
        );

        int callingAppUid = bundle.getInt(
                AuthenticationConstants.Broker.CALLER_INFO_UID
        );

        final Authority authority = Authority.getAuthorityFromAuthorityUrl(
                brokerRequest.getAuthority()
        );

        if (authority instanceof AzureActiveDirectoryAuthority) {
            ((AzureActiveDirectoryAuthority) authority).setMultipleCloudsSupported(
                    brokerRequest.getMultipleCloudsSupported()
            );
        }

        String correlationIdString = bundle.getString(
                brokerRequest.getCorrelationId()
        );
        if (TextUtils.isEmpty(correlationIdString)) {
            UUID correlationId = UUID.randomUUID();
            correlationIdString = correlationId.toString();
        }

        final BrokerSilentTokenCommandParameters commandParameters = BrokerSilentTokenCommandParameters
                .builder()
                .authenticationScheme(getAuthenticationScheme(context, brokerRequest))
                .androidApplicationContext(context)
                .accountManagerAccount(account)
                .sdkType(SdkType.MSAL)
                .callerUid(callingAppUid)
                .callerPackageName(brokerRequest.getApplicationName())
                .callerAppVersion(brokerRequest.getApplicationVersion())
                .authority(authority)
                .correlationId(correlationIdString)
                .scopes(getScopesAsSet(brokerRequest.getScope()))
                .redirectUri(brokerRequest.getRedirect())
                .clientId(brokerRequest.getClientId())
                .forceRefresh(brokerRequest.getForceRefresh())
                .claimsRequestJson(brokerRequest.getClaims())
                .loginHint(brokerRequest.getUserName())
                .homeAccountId(brokerRequest.getHomeAccountId())
                .localAccountId(brokerRequest.getLocalAccountId())
                .build();


        // Set Global environment variable for instance discovery if present
        if (!TextUtils.isEmpty(brokerRequest.getEnvironment())) {
            AzureActiveDirectory.setEnvironment(
                    Environment.valueOf(brokerRequest.getEnvironment())
            );
        }

        return commandParameters;
    }

    /**
     * Helper method to transforn scopes string to Set
     */
    private Set<String> getScopesAsSet(@Nullable final String scopeString) {
        if (TextUtils.isEmpty(scopeString)) {
            return new HashSet<>();
        }
        final String[] scopes = scopeString.split(" ");
        return new HashSet<>(Arrays.asList(scopes));
    }

    /**
     * Helper method to get redirect uri from parameters, calculates from package signature if not available.
     */
    private String getRedirectUri(@NonNull TokenCommandParameters parameters) {
        if (TextUtils.isEmpty(parameters.getRedirectUri())) {
            return BrokerValidator.getBrokerRedirectUri(
                    parameters.getAndroidApplicationContext(),
                    parameters.getApplicationName()
            );
        }
        return parameters.getRedirectUri();
    }

    public Bundle getRequestBundleForHello(@NonNull final CommandParameters parameters) {
        final Bundle requestBundle = new Bundle();
        requestBundle.putString(
                AuthenticationConstants.Broker.CLIENT_ADVERTISED_MAXIMUM_BP_VERSION_KEY,
                AuthenticationConstants.Broker.BROKER_PROTOCOL_VERSION_CODE
        );

        if (!StringUtil.isEmpty(parameters.getRequiredBrokerProtocolVersion())) {
            requestBundle.putString(
                    AuthenticationConstants.Broker.CLIENT_CONFIGURED_MINIMUM_BP_VERSION_KEY,
                    parameters.getRequiredBrokerProtocolVersion()
            );
        }

        return requestBundle;
    }

    public Bundle getRequestBundleForAcquireTokenSilent(final SilentTokenCommandParameters parameters) {
        final MsalBrokerRequestAdapter msalBrokerRequestAdapter = new MsalBrokerRequestAdapter();

        final Bundle requestBundle = new Bundle();
        final BrokerRequest brokerRequest = msalBrokerRequestAdapter.
                brokerRequestFromSilentOperationParameters(parameters);

        requestBundle.putString(
                AuthenticationConstants.Broker.BROKER_REQUEST_V2,
                sRequestAdapterGsonInstance.toJson(brokerRequest, BrokerRequest.class)
        );

        requestBundle.putInt(
                AuthenticationConstants.Broker.CALLER_INFO_UID,
                parameters.getAndroidApplicationContext().getApplicationInfo().uid
        );

        return requestBundle;
    }

    public Bundle getRequestBundleForGetAccounts(@NonNull final CommandParameters parameters) {
        final Bundle requestBundle = new Bundle();
        requestBundle.putString(ACCOUNT_CLIENTID_KEY, parameters.getClientId());
        requestBundle.putString(ACCOUNT_REDIRECT, parameters.getRedirectUri());
        //Disable the environment and tenantID. Just return all accounts belong to this clientID.
        return requestBundle;
    }

    public Bundle getRequestBundleForRemoveAccount(@NonNull final RemoveAccountCommandParameters parameters) {
        final Bundle requestBundle = new Bundle();
        if (null != parameters.getAccount()) {
            requestBundle.putString(ACCOUNT_CLIENTID_KEY, parameters.getClientId());
            requestBundle.putString(ENVIRONMENT, parameters.getAccount().getEnvironment());
            requestBundle.putString(ACCOUNT_HOME_ACCOUNT_ID, parameters.getAccount().getHomeAccountId());
        }

        return requestBundle;
    }

    public Bundle getRequestBundleForRemoveAccountFromSharedDevice(@NonNull final RemoveAccountCommandParameters parameters) {
        final Bundle requestBundle = new Bundle();

        try {
            Browser browser = BrowserSelector.select(parameters.getAndroidApplicationContext(), parameters.getBrowserSafeList());
            requestBundle.putString(DEFAULT_BROWSER_PACKAGE_NAME, browser.getPackageName());
        } catch (ClientException e) {
            // Best effort. If none is passed to broker, then it will let the OS decide.
            Logger.error(TAG, e.getErrorCode(), e);
        }

        return requestBundle;
    }

    private boolean getMultipleCloudsSupported(@NonNull final TokenCommandParameters parameters) {
        if (parameters.getAuthority() instanceof AzureActiveDirectoryAuthority) {
            final AzureActiveDirectoryAuthority authority = (AzureActiveDirectoryAuthority) parameters.getAuthority();
            return authority.getMultipleCloudsSupported();
        } else {
            return false;
        }
    }

    /**
     * List of System Browsers which can be used from broker, currently only Chrome is supported.
     * This information here is populated from the default browser safelist in MSAL.
     *
     * @return
     */
    public static List<BrowserDescriptor> getBrowserSafeListForBroker() {
        List<BrowserDescriptor> browserDescriptors = new ArrayList<>();
        final HashSet<String> signatureHashes = new HashSet();
        signatureHashes.add("7fmduHKTdHHrlMvldlEqAIlSfii1tl35bxj1OXN5Ve8c4lU6URVu4xtSHc3BVZxS6WWJnxMDhIfQN0N0K2NDJg==");
        final BrowserDescriptor chrome = new BrowserDescriptor(
                "com.android.chrome",
                signatureHashes,
                null,
                null
        );
        browserDescriptors.add(chrome);

        return browserDescriptors;
    }

    /**
     * Helper method to validate in Broker that the calling package in Microsoft Intune
     * to allow System Webview Support.
     */
    private boolean isCallingPackageIntune(@NonNull final String packageName) {
        final String methodName = ":isCallingPackageIntune";
        final String intunePackageName = "com.microsoft.intune";
        Logger.info(TAG + methodName, "Calling package name : " + packageName);
        return intunePackageName.equalsIgnoreCase(packageName);
    }
}
