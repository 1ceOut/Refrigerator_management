const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    // /api 경로를 https://xnawjp24sv.apigw.ntruss.com으로 프록시합니다.
    app.use(
        '/api',
        createProxyMiddleware({
            target: 'https://xnawjp24sv.apigw.ntruss.com',
            changeOrigin: true,
            pathRewrite: { '^/api': '' },
            headers: {
                // 필요한 경우 인증 헤더를 추가합니다.
                // 예: 'Authorization': 'Bearer your-token'
            }
        })
    );

    // /food 경로를 http://101.79.10.196:9200으로 프록시합니다.
    app.use(
        '/food',
        createProxyMiddleware({
            target: 'http://101.79.10.196:9200',
            changeOrigin: true,
            pathRewrite: { '^/food': '' },
            headers: {
                // Basic Authentication 설정
                'Authorization': 'Basic ' + Buffer.from('elastic:finalproject').toString('base64')
            }
        })
    );
    app.use(
        '/recipe',
        createProxyMiddleware({
            target: 'http://101.79.10.196:9200',
            changeOrigin: true,
            pathRewrite: { '^/recipe': '' },
            headers: {
                // Basic Authentication 설정
                'Authorization': 'Basic ' + Buffer.from('elastic:finalproject').toString('base64')
            }
        })
    );
};
