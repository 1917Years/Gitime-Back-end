package capstone.gitime.api.common.oauth2;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class Oauth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME = HttpSessionOAuth2AuthorizationRequestRepository.class
            .getName() + ".AUTHORIZATION_REQUEST";

    private final String sessionAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;

    private final String REDIRECT_PARAMETER = "redirect_uri";
    private boolean allowMultipleAuthorizationRequests;

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");
        if (this.allowMultipleAuthorizationRequests) {
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
            authorizationRequests.put(state, authorizationRequest);
            request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
            String redirectParam = request.getParameter(REDIRECT_PARAMETER);

            response.addCookie(new Cookie("redirect", redirectParam));
        }
        else {
            request.getSession().setAttribute(this.sessionAttributeName, authorizationRequest);
            String redirectParam = request.getParameter(REDIRECT_PARAMETER);

            response.addCookie(new Cookie("redirect", redirectParam));
        }


    }



        @Override
        public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
            Assert.notNull(request, "request cannot be null");
            String stateParameter = this.getStateParameter(request);
            if (stateParameter == null) {
                return null;
            }
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
            return authorizationRequests.get(stateParameter);
        }

//        @Override
//        public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
//                                             HttpServletResponse response) {
//            Assert.notNull(request, "request cannot be null");
//            Assert.notNull(response, "response cannot be null");
//            if (authorizationRequest == null) {
//                this.removeAuthorizationRequest(request, response);
//                return;
//            }
//            String state = authorizationRequest.getState();
//            Assert.hasText(state, "authorizationRequest.state cannot be empty");
//            if (this.allowMultipleAuthorizationRequests) {
//                Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
//                authorizationRequests.put(state, authorizationRequest);
//                request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
//            }
//            else {
//                request.getSession().setAttribute(this.sessionAttributeName, authorizationRequest);
//            }
//        }

        @Override
        public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
            Assert.notNull(request, "request cannot be null");
            String stateParameter = this.getStateParameter(request);
            if (stateParameter == null) {
                return null;
            }
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
            OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
            if (authorizationRequests.size() == 0) {
                request.getSession().removeAttribute(this.sessionAttributeName);
            }
            else if (authorizationRequests.size() == 1) {
                request.getSession().setAttribute(this.sessionAttributeName,
                        authorizationRequests.values().iterator().next());
            }
            else {
                request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
            }
            return originalRequest;
        }

        @Override
        public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
                                                                     HttpServletResponse response) {
            Assert.notNull(response, "response cannot be null");
            return this.removeAuthorizationRequest(request);
        }

        /**
         * Gets the state parameter from the {@link HttpServletRequest}
         * @param request the request to use
         * @return the state parameter or null if not found
         */
        private String getStateParameter(HttpServletRequest request) {
            return request.getParameter(OAuth2ParameterNames.STATE);
        }

        /**
         * Gets a non-null and mutable map of {@link OAuth2AuthorizationRequest#getState()} to
         * an {@link OAuth2AuthorizationRequest}
         * @param request
         * @return a non-null and mutable map of {@link OAuth2AuthorizationRequest#getState()}
         * to an {@link OAuth2AuthorizationRequest}.
         */
        private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            Object sessionAttributeValue = (session != null) ? session.getAttribute(this.sessionAttributeName) : null;
            if (sessionAttributeValue == null) {
                return new HashMap<>();
            }
            else if (sessionAttributeValue instanceof OAuth2AuthorizationRequest) {
                OAuth2AuthorizationRequest auth2AuthorizationRequest = (OAuth2AuthorizationRequest) sessionAttributeValue;
                Map<String, OAuth2AuthorizationRequest> authorizationRequests = new HashMap<>(1);
                authorizationRequests.put(auth2AuthorizationRequest.getState(), auth2AuthorizationRequest);
                return authorizationRequests;
            }
            else if (sessionAttributeValue instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, OAuth2AuthorizationRequest> authorizationRequests = (Map<String, OAuth2AuthorizationRequest>) sessionAttributeValue;
                return authorizationRequests;
            }
            else {
                throw new IllegalStateException(
                        "authorizationRequests is supposed to be a Map or OAuth2AuthorizationRequest but actually is a "
                                + sessionAttributeValue.getClass());
            }
        }

        /**
         * Configure if multiple {@link OAuth2AuthorizationRequest}s should be stored per
         * session. Default is false (not allow multiple {@link OAuth2AuthorizationRequest}
         * per session).
         * @param allowMultipleAuthorizationRequests true allows more than one
         * {@link OAuth2AuthorizationRequest} to be stored per session.
         * @since 5.5
         */
        @Deprecated
        public void setAllowMultipleAuthorizationRequests(boolean allowMultipleAuthorizationRequests) {
            this.allowMultipleAuthorizationRequests = allowMultipleAuthorizationRequests;
        }

    }

