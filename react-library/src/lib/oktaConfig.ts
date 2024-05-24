export const oktaConfig = {
    clientId: '0oad1bp8nlyXO8wAq5d7',
    issuer: 'https://dev-61863993.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
};