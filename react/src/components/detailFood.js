import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

const DetailFood = () => {
    const { productName, scategory, productType } = useParams();
    const [nutritionInfo, setNutritionInfo] = useState(null);

    const fetchNutritionInfo = async () => {
        try {
            // productType 값 정규화
            const normalizedProductType = productType ? productType.trim().toLowerCase() : '';

            console.log('Normalized Product Type:', normalizedProductType); // 정규화된 값 확인

            let endpoint;
            let requestBody;

            // productType에 따라 적절한 엔드포인트 및 요청 본문 설정
            if (normalizedProductType === '가공식품') {
                endpoint = '/api/recipe/detail';
                requestBody = {
                    recipe_name: productName,
                    scategory: scategory
                };
            } else if (normalizedProductType === '원자재성 식품') {
                endpoint = '/api/food/detail';
                requestBody = {
                    mcategory: productName,
                    scategory: scategory
                };
            } else {
                throw new Error(`알 수 없는 제품 유형: ${productType}`); // 문제의 유형을 로그로 확인
            }

            // API 요청 보내기
            const response = await axios.post(endpoint, requestBody);

            const data = response.data;
            console.log('API 응답 데이터:', data);

            // 첫 번째 항목만 추출
            const hits = data.hits.hits;
            if (hits.length > 0) {
                setNutritionInfo(hits[0]._source);
            } else {
                setNutritionInfo(null);
            }
        } catch (error) {
            console.error('영양 정보 가져오기 오류', error);
        }
    };

    useEffect(() => {
        fetchNutritionInfo();
    }, [productName, scategory, productType]); // productType도 의존성 배열에 추가

    return (
        <div>
            <h2>영양정보</h2>
            {nutritionInfo ? (
                <div>
                    <h4>{nutritionInfo.personal}{nutritionInfo.personal_unit} 당 영양성분입니다.</h4>

                    <table>
                        <thead>
                        <tr>
                            <th>섬유질</th>
                            <th>칼슘</th>
                            <th>염분</th>
                            <th>칼로리</th>
                            <th>수분</th>
                            <th>탄수화물</th>
                            <th>단백질</th>
                            <th>지방</th>
                            <th>콜레스테롤</th>
                            <th>당분</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>{nutritionInfo.fiber}</td>
                            <td>{nutritionInfo.calcium}</td>
                            <td>{nutritionInfo.salt}</td>
                            <td>{nutritionInfo.kcal}</td>
                            <td>{nutritionInfo.water}</td>
                            <td>{nutritionInfo.carbon}</td>
                            <td>{nutritionInfo.protein}</td>
                            <td>{nutritionInfo.fat}</td>
                            <td>{nutritionInfo.cholesterol}</td>
                            <td>{nutritionInfo.sugar}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            ) : (
                <p>해당 영양정보를 찾을 수 없습니다.</p>
            )}
        </div>
    );
};

export default DetailFood;
