// const express = require('express');
// const bodyParser = require('body-parser');
// const neo4j = require('neo4j-driver');
// const cors = require('cors');
//
// const app = express();
// app.use(bodyParser.json());
// app.use(cors());
//
// // Neo4j 데이터베이스 연결
// const driver = neo4j.driver('neo4j://175.196.97.148:7687', neo4j.auth.basic('neo4j', 'Pa$$word'));
//
// // 클라이언트에서 바코드 데이터를 받아서 Neo4j에 저장하는 라우트
// app.post('/save-barcode', async (req, res) => {
//     const { barcode, productName, expiryDate, companyName, address, productType, permissionDate } = req.body;
//
//     // 데이터 형식 검증 (예: 날짜 형식 검증)
//     if (!barcode || !productName || !expiryDate || !companyName || !address || !productType || !permissionDate) {
//         return res.status(400).send('Bad Request: Missing required fields');
//     }
//
//     // 날짜 형식 검증 (ISO 8601 형식으로 가정)
//     if (isNaN(new Date(expiryDate)) || isNaN(new Date(permissionDate))) {
//         return res.status(400).send('Bad Request: Invalid date format');
//     }
//
//     const session = driver.session();
//
//     try {
//         const result = await session.run(
//             'CREATE (p:Product {barcode: $barcode, productName: $productName, expiryDate: $expiryDate, companyName: $companyName, address: $address, productType: $productType, permissionDate: $permissionDate}) RETURN p',
//             { barcode, productName, expiryDate, companyName, address, productType, permissionDate }
//         );
//
//         res.status(200).send(result.records[0].get('p'));
//     } catch (error) {
//         console.error('Error saving barcode data:', error);
//         res.status(500).send('Internal Server Error');
//     } finally {
//         await session.close();
//     }
// });
//
// const PORT = process.env.PORT || 3000;
// app.listen(PORT, () => {
//     console.log(`Server is running on port ${PORT}`);
// });
