const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'https://xnawjp24sv.apigw.ntruss.com',
            changeOrigin: true,
            pathRewrite: { '^/api': '' },
        })
    );
};
